package one.block.eosiojavasoftkeysignatureprovider;

import one.block.eosiojava.enums.AlgorithmEmployed;
import one.block.eosiojava.error.EosioError;
import one.block.eosiojava.error.signatureProvider.GetAvailableKeysError;
import one.block.eosiojava.error.signatureProvider.SignTransactionError;
import one.block.eosiojava.error.utilities.Base58ManipulationError;
import one.block.eosiojava.error.utilities.EOSFormatterError;
import one.block.eosiojava.error.utilities.PEMProcessorError;
import one.block.eosiojava.models.signatureProvider.EosioTransactionSignatureRequest;
import one.block.eosiojava.models.signatureProvider.EosioTransactionSignatureResponse;
import one.block.eosiojava.utilities.EOSFormatter;
import one.block.eosiosoftkeysignatureprovider.SoftKeySignatureProviderImpl;
import one.block.eosiosoftkeysignatureprovider.error.ImportKeyError;
import one.block.eosiosoftkeysignatureprovider.error.SoftKeySignatureErrorConstants;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * This is a placeholder test class until development starts.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(EOSFormatter.class)
public class SoftKeySignatureProviderImplTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    //region positive test
    @Test
    public void importKeyR1Test() {
        String privateKeyEOS = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String publicKeyEOS = "PUB_R1_71AYFp3Aasa2od6bwmXEQ13MMfqv4wuJwCRx1Z1dbRifrQEqZt";

        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKeyEOS);
            List<String> keys = provider.getAvailableKeys();
            assertEquals(1, keys.size());
            assertEquals(publicKeyEOS, keys.get(0));
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not throw error!!!!");
        } catch (GetAvailableKeysError getAvailableKeysError) {
            getAvailableKeysError.printStackTrace();
            fail("Should not throw error!!!");
        }
    }

    @Test
    public void importKeyK1LegacyTest() {
        String privateKeyEOS = "5JKVeYzRs42DpnHU1rUeJHPZyXb1pCdhyayx7FD2qKHV63F71zU";
        String publicKeyEOS = "EOS8CbY5PhQZGF2gzPKRBaNG4YzB4AwpmfnDcVZMSPZTqQMiGxPbF";
        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKeyEOS);
            provider.setReturnLegacyFormatForK1(true);
            List<String> keys = provider.getAvailableKeys();
            assertEquals(1, keys.size());
            assertEquals(publicKeyEOS, keys.get(0));
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not throw error!!!!");
        } catch (GetAvailableKeysError getAvailableKeysError) {
            getAvailableKeysError.printStackTrace();
            fail("Should not throw error!!!");
        }
    }

    @Test
    public void importKeyK1Test() {
        String privateKeyEOS = "5JKVeYzRs42DpnHU1rUeJHPZyXb1pCdhyayx7FD2qKHV63F71zU";
        String publicKeyEOS = "PUB_K1_8CbY5PhQZGF2gzPKRBaNG4YzB4AwpmfnDcVZMSPZTqQMn1uFhB";
        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKeyEOS);
            List<String> keys = provider.getAvailableKeys();
            assertEquals(1, keys.size());
            assertEquals(publicKeyEOS, keys.get(0));
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not throw error!!!!");
        } catch (GetAvailableKeysError getAvailableKeysError) {
            getAvailableKeysError.printStackTrace();
            fail("Should not throw error!!!");
        }
    }

    @Test
    public void getAvailableKeyTest() {
        String privateKeyK1EOS = "5JKVeYzRs42DpnHU1rUeJHPZyXb1pCdhyayx7FD2qKHV63F71zU";
        String privateKeyR1EOS = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String publicKeyR1EOS = "PUB_R1_71AYFp3Aasa2od6bwmXEQ13MMfqv4wuJwCRx1Z1dbRifrQEqZt";
        String publicKeyK1EOS = "PUB_K1_8CbY5PhQZGF2gzPKRBaNG4YzB4AwpmfnDcVZMSPZTqQMn1uFhB";

        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKeyK1EOS);
            provider.importKey(privateKeyR1EOS);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not throw error!!!");
        }

        try {
            List<String> keys = provider.getAvailableKeys();
            assertEquals(2, keys.size());
            assertEquals(publicKeyK1EOS, keys.get(0));
            assertEquals(publicKeyR1EOS, keys.get(1));
        } catch (GetAvailableKeysError getAvailableKeysError) {
            getAvailableKeysError.printStackTrace();
            fail("Should not throw error!!!");
        }
    }

    @Test
    public void signTransactionTest() {
        String privateKeyR1EOS = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String publicKeyR1EOS = "PUB_R1_71AYFp3Aasa2od6bwmXEQ13MMfqv4wuJwCRx1Z1dbRifrQEqZt";

        String serializedTransaction = "8BC2A35CF56E6CC25F7F000000000100A6823403EA3055000000572D3CCDCD01000000000000C03400000000A8ED32322A000000000000C034000000000000A682A08601000000000004454F530000000009536F6D657468696E6700";
        List<String> publicKeys = Collections.singletonList(publicKeyR1EOS);
        String chainId = "687fa513e18843ad3e820744f4ffcf93b1354036d80737db8dc444fe4b15ad17";
        EosioTransactionSignatureRequest request = new EosioTransactionSignatureRequest(serializedTransaction, publicKeys, chainId, null, false);
        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKeyR1EOS);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not fail here!!!");
        }

        try {
            EosioTransactionSignatureResponse response = provider.signTransaction(request);
            assertNotNull(response);
            assertEquals(serializedTransaction, response.getSerializeTransaction());
            assertEquals(1, request.getSigningPublicKeys().size());
            assertTrue(response.getSignatures().get(0).contains("SIG_R1_"));
        } catch (SignTransactionError signTransactionError) {
            signTransactionError.printStackTrace();
            fail("Should not fail here!!!");
        }
    }

    @Test
    public void signTransactionWithMultipleKeyExpectMultiSignatures() {
        String privateKeyK1EOS = "5JKVeYzRs42DpnHU1rUeJHPZyXb1pCdhyayx7FD2qKHV63F71zU";
        String publicKeyK1EOS = "PUB_K1_8CbY5PhQZGF2gzPKRBaNG4YzB4AwpmfnDcVZMSPZTqQMn1uFhB";
        String privateKeyR1EOS = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String publicKeyR1EOS = "PUB_R1_71AYFp3Aasa2od6bwmXEQ13MMfqv4wuJwCRx1Z1dbRifrQEqZt";

        String serializedTransaction = "8BC2A35CF56E6CC25F7F000000000100A6823403EA3055000000572D3CCDCD01000000000000C03400000000A8ED32322A000000000000C034000000000000A682A08601000000000004454F530000000009536F6D657468696E6700";
        List<String> publicKeys = Arrays.asList(publicKeyR1EOS, publicKeyK1EOS);
        String chainId = "687fa513e18843ad3e820744f4ffcf93b1354036d80737db8dc444fe4b15ad17";
        EosioTransactionSignatureRequest request = new EosioTransactionSignatureRequest(serializedTransaction, publicKeys, chainId, null, false);
        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKeyR1EOS);
            provider.importKey(privateKeyK1EOS);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not fail here!!!");
        }

        try {
            EosioTransactionSignatureResponse response = provider.signTransaction(request);
            assertNotNull(response);
            assertEquals(serializedTransaction, response.getSerializeTransaction());
            assertEquals(2, request.getSigningPublicKeys().size());
            assertEquals(request.getSigningPublicKeys().size(), response.getSignatures().size());

            for (String signature : response.getSignatures()) {
                assertTrue(signature.contains("SIG_R1_") || signature.contains("SIG_K1_"));
            }
        } catch (SignTransactionError signTransactionError) {
            signTransactionError.printStackTrace();
            fail("Should not fail here!!!");
        }
    }

    //endregion

    //region Negative tests

    @Test
    public void importKey_thenFailEmptyError() throws ImportKeyError {
        exceptionRule.expect(ImportKeyError.class);
        exceptionRule.expectMessage(SoftKeySignatureErrorConstants.IMPORT_KEY_INPUT_EMPTY_ERROR);
        new SoftKeySignatureProviderImpl().importKey("");
    }

    @Test
    public void importKey_thenFailConvertToPemError() throws ImportKeyError {
        String invalidPrivateKey = "trash data";
        exceptionRule.expect(ImportKeyError.class);
        exceptionRule.expectMessage(String.format(SoftKeySignatureErrorConstants.IMPORT_KEY_CONVERT_TO_PEM_ERROR, invalidPrivateKey));
        exceptionRule.expectCause(IsInstanceOf.<EosioError>instanceOf(EOSFormatterError.class));

        new SoftKeySignatureProviderImpl().importKey(invalidPrivateKey);
    }

    @Test
    public void getAvailableKey_thenFailConvertFromPemToEosError() throws GetAvailableKeysError {
        String privateKeyK1EOS = "5JKVeYzRs42DpnHU1rUeJHPZyXb1pCdhyayx7FD2qKHV63F71zU";

        exceptionRule.expect(GetAvailableKeysError.class);
        exceptionRule.expectMessage(SoftKeySignatureErrorConstants.CONVERT_TO_PEM_EMPTY_ERROR);
        exceptionRule.expectCause(IsInstanceOf.<EosioError>instanceOf(PEMProcessorError.class));

        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();
        try {
            provider.importKey(privateKeyK1EOS);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not throw exception here");
        }

        try {
            PowerMockito.mockStatic(EOSFormatter.class);
            when(EOSFormatter.encodePublicKey(any(byte[].class), any(AlgorithmEmployed.class), anyBoolean())).thenThrow(new Base58ManipulationError());
        } catch (Base58ManipulationError base58ManipulationError) {
            base58ManipulationError.printStackTrace();
            fail("Should not throw exception on mocking");
        }

        provider.getAvailableKeys();
    }

    @Test
    public void signTransactionTest_thenFailEmptyPublicKey() throws SignTransactionError {
        exceptionRule.expect(SignTransactionError.class);
        exceptionRule.expectMessage(SoftKeySignatureErrorConstants.SIGN_TRANS_EMPTY_KEY_LIST);

        String privateKeyR1EOS = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String serializedTransaction = "8BC2A35CF56E6CC25F7F000000000100A6823403EA3055000000572D3CCDCD01000000000000C03400000000A8ED32322A000000000000C034000000000000A682A08601000000000004454F530000000009536F6D657468696E6700";
        List<String> publicKeys = new ArrayList<>(); // Set to empty
        String chainId = "687fa513e18843ad3e820744f4ffcf93b1354036d80737db8dc444fe4b15ad17";
        EosioTransactionSignatureRequest request = new EosioTransactionSignatureRequest(serializedTransaction, publicKeys, chainId, null, false);
        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKeyR1EOS);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not fail here!!!");
        }

        provider.signTransaction(request);
    }

    @Test
    public void signTransactionTest_thenFailWithEmptyChainId() throws SignTransactionError {
        exceptionRule.expect(SignTransactionError.class);
        exceptionRule.expectMessage(SoftKeySignatureErrorConstants.SIGN_TRANS_EMPTY_CHAIN_ID);

        String privateKeyR1EOS = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String publicKeyR1EOS = "PUB_R1_71AYFp3Aasa2od6bwmXEQ13MMfqv4wuJwCRx1Z1dbRifrQEqZt";

        String serializedTransaction = "8BC2A35CF56E6CC25F7F000000000100A6823403EA3055000000572D3CCDCD01000000000000C03400000000A8ED32322A000000000000C034000000000000A682A08601000000000004454F530000000009536F6D657468696E6700";
        List<String> publicKeys = Collections.singletonList(publicKeyR1EOS);
        String chainId = "";
        EosioTransactionSignatureRequest request = new EosioTransactionSignatureRequest(serializedTransaction, publicKeys, chainId, null, false);
        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKeyR1EOS);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not fail here!!!");
        }

        provider.signTransaction(request);
    }

    @Test
    public void signTransactionTest_thenFailWithEmptySerializedTransaction() throws SignTransactionError {
        exceptionRule.expect(SignTransactionError.class);
        exceptionRule.expectMessage(SoftKeySignatureErrorConstants.SIGN_TRANS_EMPTY_TRANSACTION);

        String privateKeyR1EOS = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String publicKeyR1EOS = "PUB_R1_71AYFp3Aasa2od6bwmXEQ13MMfqv4wuJwCRx1Z1dbRifrQEqZt";

        String serializedTransaction = "";
        List<String> publicKeys = Collections.singletonList(publicKeyR1EOS);
        String chainId = "687fa513e18843ad3e820744f4ffcf93b1354036d80737db8dc444fe4b15ad17";
        EosioTransactionSignatureRequest request = new EosioTransactionSignatureRequest(serializedTransaction, publicKeys, chainId, null, false);
        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKeyR1EOS);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not fail here!!!");
        }

        provider.signTransaction(request);
    }

    @Test
    public void signTransactionTest_thenFailWithPrepareSignableTransaction() throws SignTransactionError {
        String serializedTransaction = "8BC2A35CF56E6CC25F7F000000000100A6823403EA3055000000572D3CCDCD01000000000000C03400000000A8ED32322A000000000000C034000000000000A682A08601000000000004454F530000000009536F6D657468696E6700";
        exceptionRule.expect(SignTransactionError.class);
        exceptionRule.expectMessage(String.format(SoftKeySignatureErrorConstants.SIGN_TRANS_PREPARE_SIGNABLE_TRANS_ERROR, serializedTransaction));
        exceptionRule.expectCause(IsInstanceOf.<EosioError>instanceOf(EOSFormatterError.class));

        String privateKeyR1EOS = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String publicKeyR1EOS = "PUB_R1_71AYFp3Aasa2od6bwmXEQ13MMfqv4wuJwCRx1Z1dbRifrQEqZt";

        List<String> publicKeys = Collections.singletonList(publicKeyR1EOS);
        String chainId = "687fa513e18843ad3e820744f4ffcf93b1354036d80737db8dc444fe4b15ad17";
        EosioTransactionSignatureRequest request = new EosioTransactionSignatureRequest(serializedTransaction, publicKeys, chainId, null, false);
        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKeyR1EOS);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not fail here!!!");
        }

        try {
            PowerMockito.mockStatic(EOSFormatter.class);
            when(EOSFormatter.prepareSerializedTransactionForSigning(any(String.class), any(String.class))).thenThrow(new EOSFormatterError());
        } catch (EOSFormatterError eosFormatterError) {
            eosFormatterError.printStackTrace();
            fail("Should not fail here!!!");
        }
        provider.signTransaction(request);
    }

    @Test
    public void signTransactionTest_thenFailNoKeyAvailable() throws SignTransactionError {
        exceptionRule.expect(SignTransactionError.class);
        exceptionRule.expectMessage(SoftKeySignatureErrorConstants.SIGN_TRANS_NO_KEY_AVAILABLE);

        String publicKeyR1EOS = "PUB_R1_71AYFp3Aasa2od6bwmXEQ13MMfqv4wuJwCRx1Z1dbRifrQEqZt";

        String serializedTransaction = "8BC2A35CF56E6CC25F7F000000000100A6823403EA3055000000572D3CCDCD01000000000000C03400000000A8ED32322A000000000000C034000000000000A682A08601000000000004454F530000000009536F6D657468696E6700";
        List<String> publicKeys = Collections.singletonList(publicKeyR1EOS);
        String chainId = "687fa513e18843ad3e820744f4ffcf93b1354036d80737db8dc444fe4b15ad17";
        EosioTransactionSignatureRequest request = new EosioTransactionSignatureRequest(serializedTransaction, publicKeys, chainId, null, false);
        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        provider.signTransaction(request);
    }

    @Test
    public void signTransactionTest_thenFailKeyNotFound() throws SignTransactionError {
        String publicKeyR1EOS = "PUB_R1_71AYFp3Aasa2od6bwmXEQ13MMfqv4wuJwCRx1Z1dbRifrQEqZt";

        exceptionRule.expect(SignTransactionError.class);
        exceptionRule.expectMessage(String.format(SoftKeySignatureErrorConstants.SIGN_TRANS_KEY_NOT_FOUND, publicKeyR1EOS));

        String privateKeyK1EOS = "5JKVeYzRs42DpnHU1rUeJHPZyXb1pCdhyayx7FD2qKHV63F71zU";

        String serializedTransaction = "8BC2A35CF56E6CC25F7F000000000100A6823403EA3055000000572D3CCDCD01000000000000C03400000000A8ED32322A000000000000C034000000000000A682A08601000000000004454F530000000009536F6D657468696E6700";
        List<String> publicKeys = Collections.singletonList(publicKeyR1EOS);
        String chainId = "687fa513e18843ad3e820744f4ffcf93b1354036d80737db8dc444fe4b15ad17";
        EosioTransactionSignatureRequest request = new EosioTransactionSignatureRequest(serializedTransaction, publicKeys, chainId, null, false);
        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKeyK1EOS);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not fail here!!!");
        }

        provider.signTransaction(request);
    }

    //endregion
}
