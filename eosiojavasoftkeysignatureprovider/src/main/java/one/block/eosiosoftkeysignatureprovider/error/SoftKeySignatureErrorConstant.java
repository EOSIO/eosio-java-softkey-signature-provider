package one.block.eosiosoftkeysignatureprovider.error;

public class SoftKeySignatureErrorConstant {

    public static final String IMPORT_KEY_CONVERT_TO_PEM_ERROR = "Can't convert %s to Pem format.";
    public static final String IMPORT_KEY_INPUT_EMPTY_ERROR = "Input can't be empty!";
    public static final String IMPORT_KEY_CONVERT_TO_PEM_EMPTY_ERROR = "Converting to pem was success but pem result is empty.";
    public static final String GET_AVAILABLE_KEY_CONVERT_FROM_PEM_TO_EOS_ERROR = "Converting to pem was success but pem result is empty!";
    public static final String SIGN_TRANS_PREPARE_SIGNABLE_TRANS_ERROR = "Error when trying to prepare signable transaction from serialized transaction %s";
    public static final String SIGN_TRANS_EMPTY_KEY_LIST = "List of public keys to sign can;t be empty!";
    public static final String SIGN_TRANS_EMPTY_CHAIN_ID = "Chain id can't be empty!";
    public static final String SIGN_TRANS_EMPTY_TRANSACTION = "Serialized Transaction can't be empty.";
    public static final String SIGN_TRANS_NO_KEY_AVAILABLE = "No key available in signature provider! Make sure to call import key.";
    public static final String SIGN_TRANS_SEARCH_KEY_ERROR = "Error when trying to search for corresponding private key from input public key %s";
    public static final String SIGN_TRANS_KEY_NOT_FOUND = "Found no corresponding private key with input public key %s";
    public static final String SIGN_TRANS_GET_CURVE_DOMAIN_ERROR = "Error when trying to get EC Curve domain of %s";
    public static final String SIGN_TRANS_FORMAT_SIGNATURE_ERROR = "Error when trying to format signature.";
}
