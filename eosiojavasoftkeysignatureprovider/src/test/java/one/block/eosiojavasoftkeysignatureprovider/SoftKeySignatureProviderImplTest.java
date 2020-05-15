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
import one.block.eosiosoftkeysignatureprovider.fake.FakeEosioTransactionSignatureRequest;
import one.block.eosiosoftkeysignatureprovider.fake.FakeEosioTransactionSignatureResponse;
import one.block.eosiosoftkeysignatureprovider.fake.FakeSoftKeySignatureProviderImpl;

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
    public void importKeyK1Test() {
        String privateKeyEOS = "5JKVeYzRs42DpnHU1rUeJHPZyXb1pCdhyayx7FD2qKHV63F71zU";
        String publicKeyEOSLegacy = "EOS8CbY5PhQZGF2gzPKRBaNG4YzB4AwpmfnDcVZMSPZTqQMiGxPbF";
        String publicKeyEOS = "PUB_K1_8CbY5PhQZGF2gzPKRBaNG4YzB4AwpmfnDcVZMSPZTqQMn1uFhB";
        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKeyEOS);
            List<String> keys = provider.getAvailableKeys();
            assertEquals(2, keys.size());
            assertTrue(keys.contains(publicKeyEOS));
            assertTrue(keys.contains(publicKeyEOSLegacy));
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
        String publicKeyK1EOSLegacy = "EOS8CbY5PhQZGF2gzPKRBaNG4YzB4AwpmfnDcVZMSPZTqQMiGxPbF";

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
            assertEquals(3, keys.size());
            assertTrue(keys.contains(publicKeyK1EOS));
            assertTrue(keys.contains(publicKeyK1EOSLegacy));
            assertTrue(keys.contains(publicKeyR1EOS));
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
    public void signTransactionWithContextFreeDataTest() {
        String privateKeyR1EOS = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String publicKeyR1EOS = "PUB_R1_71AYFp3Aasa2od6bwmXEQ13MMfqv4wuJwCRx1Z1dbRifrQEqZt";

        String serializedTransaction = "8BC2A35CF56E6CC25F7F000000000100A6823403EA3055000000572D3CCDCD01000000000000C03400000000A8ED32322A000000000000C034000000000000A682A08601000000000004454F530000000009536F6D657468696E6700";
        String serializedContextFreeData = "c21bfb5ad4b64bfd04838b3b14f0ce0c7b92136cac69bfed41bef92f95a9bb20";
        List<String> publicKeys = Collections.singletonList(publicKeyR1EOS);
        String chainId = "687fa513e18843ad3e820744f4ffcf93b1354036d80737db8dc444fe4b15ad17";
        EosioTransactionSignatureRequest request = new EosioTransactionSignatureRequest(serializedTransaction, publicKeys, chainId, null, false, serializedContextFreeData);
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
            assertEquals(serializedContextFreeData, response.getSerializedContextFreeData());
            assertEquals(1, request.getSigningPublicKeys().size());
            assertTrue(response.getSignatures().get(0).contains("SIG_R1_"));
        } catch (SignTransactionError signTransactionError) {
            signTransactionError.printStackTrace();
            fail("Should not fail here!!!");
        }
    }

    @Test
    public void signTransactionTestMultipleKeys() {
        // R1 key test
        List<String[]> keyPairs = new ArrayList<>();
        keyPairs.add(new String[]{"PVT_R1_27XFnmZj3gYEeeSy4fy5PoqoP63n94nhTAyFMcuJGAV9b5cqyC", "PUB_R1_5AvUuRssyb7Z2HgNHVofX5heUV5dk8Gni1BGNMzMRCGbhdhBbu"});
        keyPairs.add(new String[]{"PVT_R1_GrfEfbv5at9kbeHcGagQmvbFLdm6jqEpgE1wsGbrfbZNjpVgT", "PUB_R1_4ztaVy8L9zbmzTdpfq5GcaFYwGwXTNmN3qW7qcgHMmfUZhpzQQ"});
        keyPairs.add(new String[]{"PVT_R1_wCpPsaY9o8NU9ZsuwaYVQUDkCfj1aWJZGVcmMM6XyYHJVqvqp", "PUB_R1_5xawnnr3mWayv2wkiqBGWqu4RQLNJffLSXHiL3BofdY7ortMy4"});
        keyPairs.add(new String[]{"PVT_R1_2sXhBwN8hCLSWRxxfZg6hqwGymKSudtQ7Qa5wUWyuW54E1Gd7P", "PUB_R1_6UYnNnXv2CutCtTLgCQxJbHBeWDG3JZaSQJK9tQ7K3JUdzXw9p"});
        keyPairs.add(new String[]{"PVT_R1_2fJmPgaik4rUeU1NDchQjnSPkQkga4iKzdK5hhdbKf2PQFJ57t", "PUB_R1_5MVdX3uzs6qDHUYpdSksZFc5rAu5P4ba6MDaySuYyzQqmCw96Q"});
        keyPairs.add(new String[]{"PVT_R1_2FBMJryipxmAeiwFYXvBTRhX1y5tdepDYBjCm4VqBWcsmdy1xD", "PUB_R1_5qjeAbU6mUM4PLRQBw8V4kxuc5pAjnJFpcMrdZmHF6L6uH57dk"});
        keyPairs.add(new String[]{"PVT_R1_2tjkXAnQPi5Jte8H5SihUQDRnJDPTny5hoiWxxeKm7uC1osiet", "PUB_R1_5BpFt4f1PXzvU2SVmwZdtCiFWbwDRHPzh8Fiao8PCd1R17pH5S"});
        keyPairs.add(new String[]{"PVT_R1_onDM2GMv8D9E7tXuZtGtyEGdLr5TWBuE6weLBwB9hC3NNao6", "PUB_R1_85V3FDScTPvPKhQLQMhrqrQE4xnrqWbZVor5B2LC1qwuJaL15p"});
        keyPairs.add(new String[]{"PVT_R1_jW6MUBQWWmy8Zj1nhtGuMSZaPMH2FXwyJYBkJKndPBRCFPAiH", "PUB_R1_8H8Gwa6rwETdZsKWV6r8Ec9MCa4eVU6PKjDWxShB4EjxRtw5mb"});
        keyPairs.add(new String[]{"PVT_R1_2fX6RwwREC8mVCvX31C5ivCsVAGbQ4waX3wEmiRQoamsGQm2cV", "PUB_R1_5tTLGveJ7TndwDEk8mbw6wEYxNoRmUSrSLjqRdahZWmF1r93QP"});
        keyPairs.add(new String[]{"PVT_R1_2bmerckTyLpK5Z9gaoAHbHWhRBHEC3b8uxdqnFciK9tgx7RnL1", "PUB_R1_84ra3upayTtazvWHJQUgfcJ6hTKYtyUJfckx8qXeRi2kRoxbSJ"});
        keyPairs.add(new String[]{"PVT_R1_2i3AZiJEQxUejHL5c8TheCXdB4yuRacJkVkdR5PxLqDwcax6mT", "PUB_R1_8V7dPunExH1bWnD88gduac71tbjW1CSPuaaUVhJwZG6yF544xE"});
        keyPairs.add(new String[]{"PVT_R1_2HXMYeSqWGqPhUbK8FLzfxJzMV9dPT7ZXFJi9Q2DLkSN1dNwzp", "PUB_R1_7QKfB2nBJJPsUDoBBJVRYV9ak1egBH6dDDGPgvT83zUs1AvQfU"});

        for (String[] keyPair : keyPairs) {
            this.signTransactionTestWithArgs(keyPair[0], keyPair[1]);
        }

        // K1 test in WIF test
        keyPairs = new ArrayList<>();
        keyPairs.add(new String[]{"5KaUJmXMVc2FF1XMRAjCxmh5or2w6awq4SaGft7HfApJLGDroFd", "EOS5aYo7EthRA5XPG72ekWdbkjYPkk8o7ufLdDzoaj8QR9oshZAXW"});
        keyPairs.add(new String[]{"5KaqhmASMEa6NeV6shfhq8AuYQa5r3xrxWvXB4SiSHwxaQeN2m6", "EOS65d2eTiu7TTdJrQ75JXdMxH657zBiT1Mqz4KhX3eyQAgQtpgf1"});
        keyPairs.add(new String[]{"5K1GuwAFjjvFJsiQu2NHYoWWCV16xAxZ1LgUKw8WrF3T5Tg4556", "EOS8K6Jq3CFSgk3zBNAAsvY7a5p4vzyfxDQDprTwzQnUXnLEZBZAt"});
        keyPairs.add(new String[]{"5KRdi3aCwQWSmX4nqNRbhYe6LLJz3xcxvEcWX141qarZGKqKKXQ", "EOS7ABgtG3CDJEPFfxqbvgLy77pxi2Ai3QJtQvrdhnduxEjjNzKVz"});
        keyPairs.add(new String[]{"5KdTC3SHPDC3orkhvrDbDGvnT2NzR5kexkCnu5RkNbv9vaujSfp", "EOS6pr3Mz91u1Yv3Tkt7T7oqrT4w5nCmSuZCoPHwJCwGipoqaHBQ8"});
        keyPairs.add(new String[]{"5K4VhWHSUJnwTaBhEx8LTECS4DewzRnizb2micRHfwwtnWmSMVx", "EOS8ajgEKL7eba36WpAhAiWp9jWxkP7ySzReFPVLkV7vNXKK6WhqA"});
        keyPairs.add(new String[]{"5KFjmNrL2cx2SysfMhdFzGH9F7ERVfc85TogKeF55jS18VErhiA", "EOS7z5Co6Ynggq2ygsLWrn8sQ7kDvYiBTs5mFxWN8HvcxB35wyXUN"});

        for (String[] keyPair : keyPairs) {
            this.signTransactionTestWithArgs(keyPair[0], keyPair[1]);
        }

        // K1 in new format test
        keyPairs = new ArrayList<>();
        keyPairs.add(new String[]{"5JKVeYzRs42DpnHU1rUeJHPZyXb1pCdhyayx7FD2qKHV63F71zU", "PUB_K1_8CbY5PhQZGF2gzPKRBaNG4YzB4AwpmfnDcVZMSPZTqQMn1uFhB"});
        keyPairs.add(new String[]{"5KW9dCerdNkGKa5Eis3tqmomFaK3FSwGyzShcFHFCxhSg3cYcda", "PUB_K1_6Lqa1KEfyCtMkf4aTUKsiFiRQ2QsfV6s7MrNe4AHL3xqtMYboc"});
        keyPairs.add(new String[]{"5JfxDRNHhbG9buaYWM9BT2taCckeQo7CzCxBoCrm2GmGSzUegXJ", "PUB_K1_4zLmUratR7P1Bm8ofu1QywPYn9hYTdE4xK5J23n3kwA8jYyqnq"});
        keyPairs.add(new String[]{"5JvqRMk6P7vHoScC3TKzEFBRYGdk1bej6pGGaToEjs9oaZUXm1a", "PUB_K1_5CfaiaeiyWWFXBYnAuYQXfLoTWxsxmycdG7gShvJoQCLRcGTqE"});
        keyPairs.add(new String[]{"5JNMHnmTzKJTDHT5a7JCVU7TRz7As3GdeFdwgWRRgPBNW2buSLY", "PUB_K1_5RGwHXDwKugv8GZEFR1enxNEsoEKGGChwPUtEcEKKQbFmr5jgF"});
        keyPairs.add(new String[]{"5J49cDDhbnwprVysd93yoogQXLh3Q37ge6iJPynjqHbzm9nozGy", "PUB_K1_8SpQXDt2ULK4v3Yx2u7qUp6zNaNT1gRwnrU5AJhM67Lk25tom8"});
        keyPairs.add(new String[]{"5KXUHV94KCG2evdGQ1cNpUJ6PigS99mJBv2mvKNcjxQvXEee3c1", "PUB_K1_7BtR7o48FqfHhXgWtxifXGUPrg94HDHFPgTtgVG1vCA8KaxVgt"});
        keyPairs.add(new String[]{"5K3otXd2RRQTZao5CYQXo2nHu2rC4VRN6LYQwyShxcfwNtsSgLo", "PUB_K1_8mNC41aAcXmZeoMZcRW6DUNSxs7AfZdfLnQ5WMbLPuzKPrk3wL"});
        keyPairs.add(new String[]{"5JNtXpnqYQ1Fe3y7S3D7eVetprBuuf8Xzo89roo1QMPTB24kHkm", "PUB_K1_7FopBLaVh1uUQJ7qxFGCZgMttsBJKhXM9hbpFp83gmrUxYJLLE"});
        keyPairs.add(new String[]{"5JM4fskPtJSwCqSb8Ax6vSYYRr7HwyrpnBxwfG3qr5mUFpMwgkA", "PUB_K1_77DjPHQtPRwXZKDtVvV93PsytUgevbqdPQcZpdPt2MwR541nX2"});
        keyPairs.add(new String[]{"5K8EvWwTbQj7anhLWfr1K1mSMi78CD8YqHquei1gntJDStV8Zdh", "PUB_K1_8B89xwYNkcEEcwyC66LPptRAspCZ7SXy59BqsyoFeNyyq1sKfc"});
        keyPairs.add(new String[]{"5J5DhKFZZqkNkTunWWbgrJNLDwZiK1vJk8Rd8nNL2fKPzLScn1x", "PUB_K1_58aKDkGmkHfswooj8nwhtrc42QrHVzUurnrhDR4Jr1rEk5uXRs"});

        for (String[] keyPair : keyPairs) {
            this.signTransactionTestWithArgs(keyPair[0], keyPair[1]);
        }
    }

    private void signTransactionTestWithArgs(String privateKey, String publicKey) {
        String serializedTransaction = "8BC2A35CF56E6CC25F7F000000000100A6823403EA3055000000572D3CCDCD01000000000000C03400000000A8ED32322A000000000000C034000000000000A682A08601000000000004454F530000000009536F6D657468696E6700";
        List<String> publicKeys = Collections.singletonList(publicKey);
        String chainId = "687fa513e18843ad3e820744f4ffcf93b1354036d80737db8dc444fe4b15ad17";
        EosioTransactionSignatureRequest request = new EosioTransactionSignatureRequest(serializedTransaction, publicKeys, chainId, null, false);
        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKey);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not fail here!!!");
        }

        try {
            EosioTransactionSignatureResponse response = provider.signTransaction(request);
            assertNotNull(response);
            assertEquals(serializedTransaction, response.getSerializeTransaction());
            assertEquals(1, request.getSigningPublicKeys().size());
            assertTrue(response.getSignatures().get(0).contains("SIG_"));
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

    @Test
    public void getAvailableKeyK1_thenFailKeyAmountIsNot1() {
        String privateKeyEOS = "5JKVeYzRs42DpnHU1rUeJHPZyXb1pCdhyayx7FD2qKHV63F71zU";
        SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();

        try {
            provider.importKey(privateKeyEOS);
            List<String> keys = provider.getAvailableKeys();
            assertNotEquals(1, keys.size());
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not throw error!!!!");
        } catch (GetAvailableKeysError getAvailableKeysError) {
            getAvailableKeysError.printStackTrace();
            fail("Should not throw error!!!");
        }
    }

    //endregion
}
