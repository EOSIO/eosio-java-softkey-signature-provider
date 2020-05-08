package one.block.eosiosoftkeysignatureprovider.error;

import one.block.eosiosoftkeysignatureprovider.SoftKeySignatureProviderImpl;

/**
 * Error constants that pertain to the signing of transactions using the softkey signature provider
 * implementation {@link one.block.eosiosoftkeysignatureprovider.SoftKeySignatureProviderImpl}
 */
public class SoftKeySignatureErrorConstants {
    /**
     * Unable to convert the provided key to PEM format.  This probably indicates that the provided
     * key is not recognized or invalid.
     */
    public static final String IMPORT_KEY_CONVERT_TO_PEM_ERROR = "Can't convert %s to Pem format.";
    /**
     * No key information was provided.
     */
    public static final String IMPORT_KEY_INPUT_EMPTY_ERROR = "Input can't be empty!";
    /**
     * PEM conversion produced an empty string.
     */
    public static final String CONVERT_TO_PEM_EMPTY_ERROR = "Converting to pem was success but pem result is empty.";
    /**
     * The signable transaction preparation failed.  There may have been a problem with the
     * provided serialized transaction.
     */
    public static final String SIGN_TRANS_PREPARE_SIGNABLE_TRANS_ERROR = "Error when trying to prepare signable transaction from serialized transaction %s";
    /**
     * The signable transaction preparation failed when including context free data.  There may have been a problem with the
     * provided serialized transaction or serialized context free data.
     */
    public static final String SIGN_TRANS_PREPARE_SIGNABLE_TRANS_OR_CONTEXT_FREE_DATA_ERROR = "Error when trying to prepare signable transaction from serialized transaction %s and serialized context free data %s";
    /**
     * The list of keys to be used for signing was empty.
     */
    public static final String SIGN_TRANS_EMPTY_KEY_LIST = "List of public keys to sign can't be empty!";
    /**
     * The provided chain id was empty.
     */
    public static final String SIGN_TRANS_EMPTY_CHAIN_ID = "Chain id can't be empty!";
    /**
     * The provided serialized transaction was empty.
     */
    public static final String SIGN_TRANS_EMPTY_TRANSACTION = "Serialized Transaction can't be empty.";
    /**
     * No keys have been imported into the signature provider for signing.  A key is necessary to
     * sign a transaction.
     */
    public static final String SIGN_TRANS_NO_KEY_AVAILABLE = "No key available in signature provider! Make sure to call import key.";
    /**
     * A public key is derived from the private key imported for signing.  The public keys needed to verify the transaction that come from
     * the chain are compared to the derived public key.  If an unexpected exception happens during the matching process, this error message is thrown.
     */
    public static final String SIGN_TRANS_SEARCH_KEY_ERROR = "Error when trying to search for corresponding private key from input public key %s";
    /**
     * A public key is derived from the private key imported for signing.  The public keys needed to verify the transaction that come from
     * the chain are compared to the derived public key.  If there is not a match this error message is thrown.
     */
    public static final String SIGN_TRANS_KEY_NOT_FOUND = "Found no corresponding private key with input public key %s";
    /**
     * Encountered when the domain parameters for the key being used to sign the transaction are
     * unobtainable.
     */
    public static final String SIGN_TRANS_GET_CURVE_DOMAIN_ERROR = "Error when trying to get EC Curve domain of %s";
    /**
     * Describes a failure to format the signature that must accompany a signed transaction.
     */
    public static final String SIGN_TRANS_FORMAT_SIGNATURE_ERROR = "Error when trying to format signature.";
    /**
     * Key curve is not supported in key transformation on {@link SoftKeySignatureProviderImpl#getAvailableKeys()}
     */
    public static final String GET_KEYS_KEY_FORMAT_NOT_SUPPORTED = "Error on trying to transform key in getAvailableKey(): Algorithm is not supported!";
}
