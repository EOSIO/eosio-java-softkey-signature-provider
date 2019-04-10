package one.block.eosiosoftkeysignatureprovider;

import one.block.eosiojava.error.signatureProvider.GetAvailableKeysError;
import one.block.eosiojava.error.signatureProvider.SignTransactionError;
import one.block.eosiojava.error.utilities.EOSFormatterError;
import one.block.eosiojava.error.utilities.PEMProcessorError;
import one.block.eosiojava.interfaces.ISignatureProvider;
import one.block.eosiojava.models.signatureProvider.EosioTransactionSignatureRequest;
import one.block.eosiojava.models.signatureProvider.EosioTransactionSignatureResponse;
import one.block.eosiojava.utilities.EOSFormatter;
import one.block.eosiojava.utilities.PEMProcessor;
import one.block.eosiosoftkeysignatureprovider.error.ImportKeyError;
import one.block.eosiosoftkeysignatureprovider.error.SoftKeySignatureErrorConstant;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This file is a placeholder until development starts.
 */
public class SoftKeySignatureProviderImpl implements ISignatureProvider {

    /**
     * Keep a Set (Unique) of private key in PEM format
     */
    private Set<String> keys = new LinkedHashSet<>();

    /**
     * Import private key into softkey signature provider
     *
     * @param privateKey - Eos format private key
     * @throws ImportKeyError
     */
    public void importKey(String privateKey) throws ImportKeyError {
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
    public @NotNull EosioTransactionSignatureResponse signTransaction(EosioTransactionSignatureRequest eosioTransactionSignatureRequest) throws SignTransactionError {
        return null;
    }

    @Override
    public @NotNull List<String> getAvailableKeys() throws GetAvailableKeysError {
//        List<String> availableKeys = new ArrayList<>();
//        if (this.keys.isEmpty()) {
//            return availableKeys;
//        }
//
//        try {
//            for (String key : this.keys) {
//                PEMProcessor processor = new PEMProcessor(key);
//                processor.getKeyData();
//            }
//        } catch (PEMProcessorError pemProcessorError) {
//            pemProcessorError.printStackTrace();
//        }
        throw new NotImplementedException();
    }
}
