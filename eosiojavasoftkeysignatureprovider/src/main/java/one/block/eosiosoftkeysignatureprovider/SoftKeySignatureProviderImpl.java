package one.block.eosiosoftkeysignatureprovider;

import one.block.eosiojava.enums.AlgorithmEmployed;
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
import one.block.eosiosoftkeysignatureprovider.error.SoftKeySignatureErrorConstant;
import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.util.encoders.Hex;
import org.jetbrains.annotations.NotNull;
import sun.security.util.Pem;

import java.math.BigInteger;
import java.util.*;

/**
 * This file is a placeholder until development starts.
 */
public class SoftKeySignatureProviderImpl implements ISignatureProvider {

    /**
     * Keep a Set (Unique) of private key in PEM format
     */
    private Set<String> keys = new LinkedHashSet<>();

    /**
     * Whether getAvailableKeys return WIF legacy format for K1 key
     */
    private boolean returnLegacyFormatForK1;

    /**
     * Maximum re-try time should the signature provider try to create a canonical signature for K1 key
     */
    private static final int MAX_NOT_CANONICAL_RE_SIGN = 100;

    /**
     * Import private key into softkey signature provider
     *
     * @param privateKey - Eos format private key
     * @throws ImportKeyError
     */
    public void importKey(@NotNull String privateKey) throws ImportKeyError {
        if (privateKey.isEmpty()) {
            throw new ImportKeyError(SoftKeySignatureErrorConstant.IMPORT_KEY_INPUT_EMPTY_ERROR);
        }

        String privateKeyPem;

        try {
            privateKeyPem = EOSFormatter.convertEOSPrivateKeyToPEMFormat(privateKey);
        } catch (EOSFormatterError eosFormatterError) {
            throw new ImportKeyError(String.format(SoftKeySignatureErrorConstant.IMPORT_KEY_CONVERT_TO_PEM_ERROR, privateKey), eosFormatterError);
        }

        if (privateKeyPem.isEmpty()) {
            throw new ImportKeyError(SoftKeySignatureErrorConstant.IMPORT_KEY_CONVERT_TO_PEM_EMPTY_ERROR);
        }

        this.keys.add(privateKeyPem);
    }

    @Override
    public @NotNull EosioTransactionSignatureResponse signTransaction(@NotNull EosioTransactionSignatureRequest eosioTransactionSignatureRequest) throws SignTransactionError {
        if (eosioTransactionSignatureRequest.getSigningPublicKey().isEmpty()) {
            throw new SignTransactionError(SoftKeySignatureErrorConstant.SIGN_TRANS_EMPTY_KEY_LIST);
        }

        if (eosioTransactionSignatureRequest.getChainId().isEmpty()) {
            throw new SignTransactionError(SoftKeySignatureErrorConstant.SIGN_TRANS_EMPTY_CHAIN_ID);
        }

        if (eosioTransactionSignatureRequest.getSerializedTransaction().isEmpty()) {
            throw new SignTransactionError(SoftKeySignatureErrorConstant.SIGN_TRANS_EMPTY_TRANSACTION);
        }

        // Getting serializedTransaction and prepare signable transaction
        String serializedTransaction = eosioTransactionSignatureRequest.getSerializedTransaction();

        // This is the un-hashed message which is used for recover public key
        byte[] message;

        // This is the hashed message which is signed.
        byte[] hashedMessage;

        try {
            message = Hex.decode(EOSFormatter.prepareSerializedTransactionForSigning(serializedTransaction, eosioTransactionSignatureRequest.getChainId()).toUpperCase());
            hashedMessage = Sha256Hash.hash(message);
        } catch (EOSFormatterError eosFormatterError) {
            throw new SignTransactionError(String.format(SoftKeySignatureErrorConstant.SIGN_TRANS_PREPARE_SIGNABLE_TRANS_ERROR, serializedTransaction), eosFormatterError);
        }

        if (this.keys.isEmpty()) {
            throw new SignTransactionError(SoftKeySignatureErrorConstant.SIGN_TRANS_NO_KEY_AVAILABLE);
        }

        List<String> signatures = new ArrayList<>();

        // Getting public key and search for the corresponding private key
        for (String inputPublicKey : eosioTransactionSignatureRequest.getSigningPublicKey()) {
            BigInteger privateKeyBI = BigInteger.ZERO;
            AlgorithmEmployed curve = null;

            try {
                // Search for corresponding private key
                for (String key : keys) {
                    PEMProcessor availableKeyProcessor = new PEMProcessor(key);
                    //Extract public key in PEM format from inner private key
                    String innerPublicKeyPEM = availableKeyProcessor.extractPEMPublicKeyFromPrivateKey(this.returnLegacyFormatForK1);

                    // Convert input public key to PEM format for comparision
                    String inputPublicKeyPEM;

                    try {
                        inputPublicKeyPEM = EOSFormatter.convertEOSPublicKeyToPEMFormat(inputPublicKey);
                    } catch (EOSFormatterError eosFormatterError) {
                        throw new SignTransactionError(eosFormatterError);
                    }

                    if (innerPublicKeyPEM.equals(inputPublicKeyPEM)) {
                        privateKeyBI = new BigInteger(availableKeyProcessor.getKeyData());
                        curve = availableKeyProcessor.getAlgorithm();
                        break;
                    }
                }
            } catch (PEMProcessorError processorError) {
                throw new SignTransactionError(String.format(SoftKeySignatureErrorConstant.SIGN_TRANS_SEARCH_KEY_ERROR, inputPublicKey), processorError);
            }

            // Throw error if found no private key with input public key
            //noinspection ConstantConditions
            if (privateKeyBI.equals(BigInteger.ZERO) || curve == null) {
                throw new SignTransactionError(String.format(SoftKeySignatureErrorConstant.SIGN_TRANS_KEY_NOT_FOUND, inputPublicKey));
            }

            for (int i = 0; i < MAX_NOT_CANONICAL_RE_SIGN; i++) {
                try {
                    // Sign transaction
                    // Use default constructor to have signature generated with secureRandom, otherwise it would generate same signature for same key all the time
                    ECDSASigner signer = new ECDSASigner();

                    ECDomainParameters domainParameters;
                    try {
                        domainParameters = PEMProcessor.getCurveDomainParameters(curve);
                    } catch (PEMProcessorError processorError) {
                        throw new SignTransactionError(String.format(SoftKeySignatureErrorConstant.SIGN_TRANS_GET_CURVE_DOMAIN_ERROR, curve.getString()), processorError);
                    }

                    ECPrivateKeyParameters parameters = new ECPrivateKeyParameters(privateKeyBI, domainParameters);
                    signer.init(true, parameters);
                    BigInteger[] signatureComponents = signer.generateSignature(hashedMessage);
                    String signature = EOSFormatter.convertRawRandSofSignatureToEOSFormat(signatureComponents[0].toString(), signatureComponents[1].toString(), message, EOSFormatter.convertEOSPublicKeyToPEMFormat(inputPublicKey));
                    // Format Signature
                    signatures.add(signature);
                    break;
                }  catch (EOSFormatterError eosFormatterError) {
                    if (eosFormatterError.getCause() instanceof EosFormatterSignatureIsNotCanonicalError) {
                        // Try to sign again until MAX_NOT_CANONICAL_RE_SIGN is reached or get a canonical signature
                        continue;
                    }

                    throw new SignTransactionError(SoftKeySignatureErrorConstant.SIGN_TRANS_FORMAT_SIGNATURE_ERROR, eosFormatterError);
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
            throw new GetAvailableKeysError(SoftKeySignatureErrorConstant.GET_AVAILABLE_KEY_CONVERT_FROM_PEM_TO_EOS_ERROR, pemProcessorError);
        }

        return availableKeys;
    }

    /**
     * Whether getAvailableKeys return WIF legacy format for K1 key
     *
     * @return Whether getAvailableKeys return WIF legacy format for K1 key
     */
    public boolean isReturnLegacyFormatForK1() {
        return returnLegacyFormatForK1;
    }

    /**
     * Set returnLegacyFormatForK1 to true to get WIF Legacy format for K1 public key on getAvailableKey
     *
     * @param returnLegacyFormatForK1 true for getting WIF Legacy format of K1 public key on getAvailableKey
     */
    public void setReturnLegacyFormatForK1(boolean returnLegacyFormatForK1) {
        this.returnLegacyFormatForK1 = returnLegacyFormatForK1;
    }
}
