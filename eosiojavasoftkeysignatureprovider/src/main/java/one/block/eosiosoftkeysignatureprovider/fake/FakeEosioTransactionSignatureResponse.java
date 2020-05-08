package one.block.eosiosoftkeysignatureprovider.fake;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import one.block.eosiojava.error.signatureProvider.SignatureProviderError;
import one.block.eosiojava.models.signatureProvider.EosioTransactionSignatureRequest;
import one.block.eosiojava.models.signatureProvider.EosioTransactionSignatureResponse;

public class FakeEosioTransactionSignatureResponse {
    /**
     * The serialized (Hex) version of {@link one.block.eosiojava.models.rpcProvider.Transaction}.
     * <br>
     * It is the result of {@link one.block.eosiojava.interfaces.ISerializationProvider#serializeTransaction(String)}
     * <br>
     * The transaction could have been modified by the signature provider.
     * <br>
     * If signature provider modifies the serialized transaction returned in the response {@link
     * EosioTransactionSignatureResponse#getSerializeTransaction()} but {@link
     * EosioTransactionSignatureRequest#isModifiable()} is false then {@link
     * one.block.eosiojava.error.session.TransactionGetSignatureNotAllowModifyTransactionError} will
     * be thrown
     */
    @NotNull
    private String serializeTransaction;

    @NotNull
    private String serializedContextFreeData;

    /**
     * The signatures that are signed by private keys of {@link EosioTransactionSignatureRequest#getSigningPublicKeys()}
     */
    @NotNull
    private List<String> signatures;

    /**
     * The error that occurred during signing.
     */
    @Nullable
    private SignatureProviderError error;

    public FakeEosioTransactionSignatureResponse(@NotNull String serializeTransaction, @NotNull String serializedContextFreeData,
                                             @NotNull List<String> signatures, @Nullable SignatureProviderError error) {
        this.serializeTransaction = serializeTransaction;
        this.serializedContextFreeData = serializedContextFreeData;
        this.signatures = signatures;
        this.error = error;
    }

    public FakeEosioTransactionSignatureResponse(@NotNull String serializeTransaction,
                                             @NotNull List<String> signatures, @Nullable SignatureProviderError error) {
        this(serializeTransaction, "", signatures, error);
    }

    /**
     * Gets the serialized transaction.
     *
     * @return the serialize transaction
     */
    @NotNull
    public String getSerializeTransaction() {
        return serializeTransaction;
    }

    /**
     * Gets signatures.
     *
     * @return the signatures
     */
    @NotNull
    public List<String> getSignatures() {
        return signatures;
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    @Nullable
    public SignatureProviderError getError() {
        return error;
    }

    /**
     * Gets serialized context free data.
     *
     * @return the serialized context free data
     */
    @NotNull
    public String getSerializedContextFreeData() { return serializedContextFreeData; }
}
