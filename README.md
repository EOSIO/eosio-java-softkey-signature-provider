# EOSIO SDK for Java Soft Key Signature Provider ![EOSIO Alpha](https://img.shields.io/badge/EOSIO-Alpha-blue.svg)
[![Software License](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://github.com/EOSIO/eosio-java-android-abieos-serialization-provider/blob/master/LICENSE)
![Lagnuage Java](https://img.shields.io/badge/Language-Java-yellow.svg)

## Overview

Using this framework, you can sign an `EosioTransactionSignatureRequest` using `K1` and `R1` type private keys.

## Basic Usage

To Import private key

```java
String privateKeyEOS = "Your eos format private key in K1 or R1 type"
SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();
try {
  provider.importKey(privateKeyEOS);
} catch (ImportKeyError importKeyError) {
  importKeyError.printStackTrace();
}
```

To sign a `EosioTransactionSignatureRequest`, you should first create it with your serialized transaction and list of public keys. Eosio-java handle the creation of the object for you.
Finally, Call signTransaction to sign.

```java
try {
  String serializedTransaction = "Your serialized transaction";
  List<String> publicKeys = Arrays.asList("Your eos format public key in K1 or R1 type");
  EosioTransactionSignatureRequest request = new EosioTransactionSignatureRequest(serializedTransaction, publicKeys, chainId, null, false);
  EosioTransactionSignatureResponse response = provider.signTransaction(request);
} catch (SignTransactionError signTransactionError) {
  signTransactionError.printStackTrace();
}
```

## Contents of the library

This library is an example implementation of `ISignatureProvider`. It implements the following methods:

* `EosioTransactionSignatureResponse signTransaction(EosioTransactionSignatureRequest eosioTransactionSignatureRequest)` signs an `Transaction`.
* `List<String> getAvailableKeys()` returns an list, containing the public keys associated with the private keys that is added to the object.

Importing key by calling

* `importKey(String privateKey)`

## Installation

This provider is intended to be used in conjunction with [EOSIO SDK for Java](https://github.com/EOSIO/eosio-java) as a simple sample for signature provider provider.

To use Softkey signature Provider with EOSIO SDK for Java in your app, add the following modules to your build.gradle:

**TODO** This needs to be updated when the distribution strategy is finalized.

```java
implementation 'one.block:eosio-java:0.1-alpha'
implementation 'one.block:eosio-java-android-serialization-provider:0.1-alpha'
implementation 'one.block:eosio-java-softkey-signature-provider:0.1-alpha'
```

Then refresh your gradle project.

Now Softkey signature provider is ready for use within EOSIO SDK for Java according to the [EOSIO SDK for Java Basic Usage instructions](https://github.com/EOSIO/eosio-java/tree/develop#basic-usage).

## Contributing

[Contributing Guide](./CONTRIBUTING.md)

[Code of Conduct](./CONTRIBUTING.md#conduct)

## License

[MIT](./LICENSE)

## Important

See LICENSE for copyright and license terms.  Block.one makes its contribution on a voluntary basis as a member of the EOSIO community and is not responsible for ensuring the overall performance of the software or any related applications.  We make no representation, warranty, guarantee or undertaking in respect of the software or any related documentation, whether expressed or implied, including but not limited to the warranties or merchantability, fitness for a particular purpose and noninfringement. In no event shall we be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or documentation or the use or other dealings in the software or documentation.  Any test results or performance figures are indicative and will not reflect performance under all conditions.  Any reference to any third party or third-party product, service or other resource is not an endorsement or recommendation by Block.one.  We are not responsible, and disclaim any and all responsibility and liability, for your use of or reliance on any of these resources. Third-party resources may be updated, changed or terminated at any time, so the information here may be out of date or inaccurate.

Wallets and related components are complex software that require the highest levels of security.  If incorrectly built or used, they may compromise users’ private keys and digital assets. Wallet applications and related components should undergo thorough security evaluations before being used.  Only experienced developers should work with this software.
a