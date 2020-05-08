package one.block.eosiosoftkeysignatureprovider.fake;

import java.util.List;

import one.block.eosiojava.models.signatureProvider.BinaryAbi;
import one.block.eosiojava.models.signatureProvider.EosioTransactionSignatureRequest;

public class FakeEosioTransactionSignatureRequest extends EosioTransactionSignatureRequest {
    public String serializedContextFreeData;
    /**
     * Instantiates a new Eosio transaction signature request.
     *
     * @param serializedTransaction the serialized transaction
     * @param signingPublicKeys     the signing public keys
     * @param chainId               the chain id
     * @param abis                  the ABIs
     * @param isModifiable          boolean to indicate whether the signature provider is able to modify the
     */
    public FakeEosioTransactionSignatureRequest(String serializedTransaction, List<String> signingPublicKeys, String chainId, List<BinaryAbi> abis, boolean isModifiable, String serializedContextFreeData) {
        super(serializedTransaction, signingPublicKeys, chainId, abis, isModifiable);
        this.serializedContextFreeData = serializedContextFreeData;
    }

    public String getSerializedContextFreeData() {
        return this.serializedContextFreeData;
    }
}
