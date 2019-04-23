package one.block.eosiosoftkeysignatureprovider;


import one.block.eosiojava.enums.AlgorithmEmployed;
import one.block.eosiojava.error.EosioError;
import one.block.eosiojava.error.signatureProvider.GetAvailableKeysError;
import one.block.eosiojava.error.signatureProvider.SignTransactionError;
import one.block.eosiojava.error.utilities.EOSFormatterError;
import one.block.eosiojava.error.utilities.EosFormatterSignatureIsNotCanonicalError;
import one.block.eosiojava.error.utilities.PEMProcessorError;
import one.block.eosiojava.interfaces.ISignatureProvider;
import one.block.eosiojava.models.signatureProvider.EosioTransactionSignatureRequest;
import one.block.eosiojava.models.signatureProvider.EosioTransactionSignatureResponse;
import one.block.eosiojava.utilities.EOSFormatter;
import one.block.eosiojava.utilities.PEMProcessor;
import one.block.eosiosoftkeysignatureprovider.error.ImportKeyError;
import one.block.eosiosoftkeysignatureprovider.error.SoftKeySignatureErrorConstants;
import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.util.encoders.Hex;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Example signature provider implementation for EOSIO-java SDK that signs transactions using
 * an in-memory private key generated with the secp256r1, prime256v1, or secp256k1 algorithms.  This
 * implementation is NOT secure and should only be used for educational purposes.  It is NOT
 * advisable to store private keys outside of secure devices like TEE's and SE's.
 *
 */
public class SoftKeySignatureProviderImpl implements ISignatureProvider {

    /**
     * Keep a Set (Unique) of private keys in PEM format
     */
    private Set<String> keys = new LinkedHashSet<>();

    /**
     * Whether getAvailableKeys should return WIF legacy format for key generated with secp256k1
     * algorithm.  This for of the key is prefaced with "EOS" instead of "PUB_K1_".
     */
    private boolean returnLegacyFormatForK1;

    /**
     * Maximum number of times the signature provider should try to create a canonical signature
     * for a secp256k1 generated key when the attempt fails.
     */
    private static final int MAX_NOT_CANONICAL_RE_SIGN = 100;

    /**
     * Index of R value in the signature result of softkey signing
     */
    private static final int R_INDEX = 0;

    /**
     * Index of S value in the signature result of softkey signing
     */
    private static final int S_INDEX = 1;

    /**
     * Import private key into softkey signature provider.  Private key is stored in memory.
     * NOT RECOMMENDED for production use!!!!
     *
     * @param privateKey - Eos format private key
     * @throws ImportKeyError Exception that occurs while trying to import a key
     */
    public void importKey(@NotNull String privateKey) throws ImportKeyError {
        if (privateKey.isEmpty()) {
            throw new ImportKeyError(SoftKeySignatureErrorConstants.IMPORT_KEY_INPUT_EMPTY_ERROR);
        }

        String privateKeyPem;

        try {
            privateKeyPem = EOSFormatter.convertEOSPrivateKeyToPEMFormat(privateKey);
        } catch (EOSFormatterError eosFormatterError) {
            throw new ImportKeyError(String.format(SoftKeySignatureErrorConstants.IMPORT_KEY_CONVERT_TO_PEM_ERROR, privateKey), eosFormatterError);
        }

        if (privateKeyPem.isEmpty()) {
            throw new ImportKeyError(SoftKeySignatureErrorConstants.CONVERT_TO_PEM_EMPTY_ERROR);
        }

        this.keys.add(privateKeyPem);
    }

    @Override
    public @NotNull EosioTransactionSignatureResponse signTransaction(@NotNull EosioTransactionSignatureRequest eosioTransactionSignatureRequest) throws SignTransactionError {
        if (eosioTransactionSignatureRequest.getSigningPublicKeys().isEmpty()) {
            throw new SignTransactionError(SoftKeySignatureErrorConstants.SIGN_TRANS_EMPTY_KEY_LIST);
        }

        if (eosioTransactionSignatureRequest.getChainId().isEmpty()) {
            throw new SignTransactionError(SoftKeySignatureErrorConstants.SIGN_TRANS_EMPTY_CHAIN_ID);
        }

        if (eosioTransactionSignatureRequest.getSerializedTransaction().isEmpty()) {
            throw new SignTransactionError(SoftKeySignatureErrorConstants.SIGN_TRANS_EMPTY_TRANSACTION);
        }

        // Getting serializedTransaction and preparing signable transaction
        String serializedTransaction = eosioTransactionSignatureRequest.getSerializedTransaction();

        // This is the un-hashed message which is used to recover public key
        byte[] message;

        // This is the hashed message which is signed.
        byte[] hashedMessage;

        try {
            message = Hex.decode(EOSFormatter.prepareSerializedTransactionForSigning(serializedTransaction, eosioTransactionSignatureRequest.getChainId()).toUpperCase());
            hashedMessage = Sha256Hash.hash(message);
        } catch (EOSFormatterError eosFormatterError) {
            throw new SignTransactionError(String.format(SoftKeySignatureErrorConstants.SIGN_TRANS_PREPARE_SIGNABLE_TRANS_ERROR, serializedTransaction), eosFormatterError);
        }

        if (this.keys.isEmpty()) {
            throw new SignTransactionError(SoftKeySignatureErrorConstants.SIGN_TRANS_NO_KEY_AVAILABLE);
        }

        List<String> signatures = new ArrayList<>();

        // Getting public key and searching for the corresponding private key
        for (String inputPublicKey : eosioTransactionSignatureRequest.getSigningPublicKeys()) {
            BigInteger privateKeyBI = BigInteger.ZERO;
            AlgorithmEmployed curve = null;

            try {
                // Search for corresponding private key
                for (String key : keys) {
                    PEMProcessor availableKeyProcessor = new PEMProcessor(key);
                    //Extract public key in PEM format from inner private key
                    String innerPublicKeyPEM = availableKeyProcessor.extractPEMPublicKeyFromPrivateKey(this.returnLegacyFormatForK1);

                    // Convert input public key to PEM format for comparision
                    String inputPublicKeyPEM = EOSFormatter.convertEOSPublicKeyToPEMFormat(inputPublicKey);

                    if (innerPublicKeyPEM.equals(inputPublicKeyPEM)) {
                        privateKeyBI = new BigInteger(availableKeyProcessor.getKeyData());
                        curve = availableKeyProcessor.getAlgorithm();
                        break;
                    }
                }
            } catch (EosioError error) {
                throw new SignTransactionError(String.format(SoftKeySignatureErrorConstants.SIGN_TRANS_SEARCH_KEY_ERROR, inputPublicKey), error);
            }

            // Throw error if found no private key with input public key
            //noinspection ConstantConditions
            if (privateKeyBI.equals(BigInteger.ZERO) || curve == null) {
                throw new SignTransactionError(String.format(SoftKeySignatureErrorConstants.SIGN_TRANS_KEY_NOT_FOUND, inputPublicKey));
            }

            for (int i = 0; i < MAX_NOT_CANONICAL_RE_SIGN; i++) {
                // Sign transaction
                // Use default constructor to have signature generated with secureRandom, otherwise it would generate same signature for same key all the time
                ECDSASigner signer = new ECDSASigner();

                ECDomainParameters domainParameters;
                try {
                    domainParameters = PEMProcessor.getCurveDomainParameters(curve);
                } catch (PEMProcessorError processorError) {
                    throw new SignTransactionError(String.format(SoftKeySignatureErrorConstants.SIGN_TRANS_GET_CURVE_DOMAIN_ERROR, curve.getString()), processorError);
                }

                ECPrivateKeyParameters parameters = new ECPrivateKeyParameters(privateKeyBI, domainParameters);
                signer.init(true, parameters);
                BigInteger[] signatureComponents = signer.generateSignature(hashedMessage);

                try {
                    String signature = EOSFormatter.convertRawRandSofSignatureToEOSFormat(signatureComponents[R_INDEX].toString(), signatureComponents[S_INDEX].toString(), message, EOSFormatter.convertEOSPublicKeyToPEMFormat(inputPublicKey));
                    // Format Signature
                    signatures.add(signature);
                    break;
                }  catch (EOSFormatterError eosFormatterError) {
                    // In theory, Non-canonical error only happened with K1 key
                    if (eosFormatterError.getCause() instanceof EosFormatterSignatureIsNotCanonicalError && curve == AlgorithmEmployed.SECP256K1) {
                        // Try to sign again until MAX_NOT_CANONICAL_RE_SIGN is reached or get a canonical signature
                        continue;
                    }

                    throw new SignTransactionError(SoftKeySignatureErrorConstants.SIGN_TRANS_FORMAT_SIGNATURE_ERROR, eosFormatterError);
                }
            }
        }

        return new EosioTransactionSignatureResponse(serializedTransaction, signatures, null);
    }

    @Override
    public @NotNull List<String> getAvailableKeys() throws GetAvailableKeysError {
        List<String> availableKeys = new ArrayList<>();
        if (this.keys.isEmpty()) {
            return availableKeys;
        }

        try {
            for (String key : this.keys) {
                PEMProcessor processor = new PEMProcessor(key);
                availableKeys.add(processor.extractEOSPublicKeyFromPrivateKey(this.returnLegacyFormatForK1));
            }
        } catch (PEMProcessorError pemProcessorError) {
            throw new GetAvailableKeysError(SoftKeySignatureErrorConstants.CONVERT_TO_PEM_EMPTY_ERROR, pemProcessorError);
        }

        return availableKeys;
    }

    /**
     * Whether getAvailableKeys should return WIF legacy format for key generated with secp256k1
     * algorithm.  This for of the key is prefaced with "EOS" instead of "PUB_K1_".     *
     * @return Whether getAvailableKeys return WIF legacy format for K1 key
     */
    public boolean isReturnLegacyFormatForK1() {
        return returnLegacyFormatForK1;
    }

    /**
     * Set returnLegacyFormatForK1 to true to get WIF Legacy format for secp256k1 generated public
     * key returned from method getAvailableKeys().
     *
     * @param returnLegacyFormatForK1 true for getting WIF Legacy format of K1 public key on getAvailableKey
     */
    public void setReturnLegacyFormatForK1(boolean returnLegacyFormatForK1) {
        this.returnLegacyFormatForK1 = returnLegacyFormatForK1;
    }
}
