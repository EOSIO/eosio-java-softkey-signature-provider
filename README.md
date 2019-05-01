![Java Logo](img/java-logo.png)
# EOSIO SDK for Java: Softkey Signature Provider ![EOSIO Alpha](https://img.shields.io/badge/EOSIO-Alpha-blue.svg)

[![Software License](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://github.com/EOSIO/eosio-java-softkey-signature-provider/blob/master/LICENSE)
![Language Java](https://img.shields.io/badge/Language-Java-yellow.svg)
![](https://img.shields.io/badge/Deployment%20Target-JVM-blue.svg)

Softkey Signature Provider is an example pluggable signature provider for [EOSIO SDK for Java](https://github.com/EOSIO/eosio-java). It allows for signing transactions using in-memory SECP256K1 and SECP256R1 keys.

**Important:** Softkey Signature Provider stores keys in memory and is therefore not secure. It should only be used for development purposes. In production, we strongly recommend using a signature provider that interfaces with a KeyStore, authenticator or wallet.

*All product and company names are trademarks™ or registered® trademarks of their respective holders. Use of them does not imply any affiliation with or endorsement by them.*

## Contents

- [About Signature Providers](#about-signature-providers)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Basic Usage](#basic-usage)
- [Android Example App](#android-example-app)
- [Library Methods](#library-methods)
- [Want to Help?](#want-to-help)
- [License & Legal](#license)

## About Signature Providers

The Signature Provider abstraction is arguably the most useful of all of the [EOSIO SDK for Java](https://github.com/EOSIO/eosio-java) providers. It is responsible for:

* finding out what keys are available for signing (`getAvailableKeys`), and
* requesting and obtaining transaction signatures with a subset of the available keys (`signTransaction`).

By simply switching out the signature provider on a transaction, signature requests can be routed any number of ways. Need a signature from keys in the platform's Keychain or KeyStore? Configure the [TransactionSession](https://github.com/EOSIO/eosio-java/blob/master/eosiojava/src/main/java/one/block/eosiojava/session/TransactionSession.java) with a conforming signature provider that exposes that functionality. Need signatures from a wallet on the user's device? A signature provider can do that too!

All signature providers must conform to the [ISignatureProvider](https://github.com/EOSIO/eosio-java/blob/master/eosiojava/src/main/java/one/block/eosiojava/interfaces/ISignatureProvider.java) Protocol.

### Prerequisites

* Java JDK 1.8+ (1.7 source compatibility is targeted)
* Gradle 4.10.1+

## Installation

This provider is intended to be used in conjunction with [EOSIO SDK for Java](https://github.com/EOSIO/eosio-java) as a provider plugin.

To use Softkey Signature Provider with EOSIO SDK for Java in your app, add the following modules to your `build.gradle`:

```java
implementation 'one.block:eosiojava:0.1.0'
implementation 'one.block:eosiojavasoftkeysignatureprovider:0.1.0'
```

If you are using Softkey Signature Provider, or any library that depends on it, in an Android application you must also add the following to your application's `build.gradle` file in the `android` section:

```groovy
// Needed to get bitcoin-j to produce a valid apk for android.
packagingOptions {
    exclude 'lib/x86_64/darwin/libscrypt.dylib'
    exclude 'lib/x86_64/freebsd/libscrypt.so'
    exclude 'lib/x86_64/linux/libscrypt.so'
}
```

Then refresh your gradle project. Then you're all set for the [Basic Usage](#basic-usage) example!

## Basic Usage

Generally, signature providers are called by the [TransactionProcessor](https://github.com/EOSIO/eosio-java/blob/master/eosiojava/src/main/java/one/block/eosiojava/session/TransactionProcessor.java) during signing. (See an [example here](https://github.com/EOSIO/eosio-java#basic-usage).) If you find, however, that you need to get available keys or request signing directly, this library can be invoked as follows:

```java
SoftKeySignatureProviderImpl provider = new SoftKeySignatureProviderImpl();
try {
    List<String> availableKeys = provider.getAvailableKeys();
} catch (GetAvailableKeysError getAvailableKeysError) {
    getAvailableKeysError.printStackTrace();
}
```

And to import a private key:

```java
try {
  provider.importKey("Your eos format private key in SECP256K1 or SECP256R1 type");
} catch (ImportKeyError importKeyError) {
  importKeyError.printStackTrace();
}
```

To sign an `EosioTransactionSignatureRequest`, you should first create it with your serialized transaction and list of public keys. EOSIO SDK for Java handles the creation of the object for you.

Finally, call `signTransaction` to sign.

```java
try {
  String serializedTransaction = "Your serialized transaction";
  List<String> publicKeys = Arrays.asList("Your eos format public key in SECP256K1 or SECP256R1 type");
  EosioTransactionSignatureRequest request = new EosioTransactionSignatureRequest(serializedTransaction, publicKeys, chainId, null, false);
  EosioTransactionSignatureResponse response = provider.signTransaction(request);
} catch (SignTransactionError signTransactionError) {
  signTransactionError.printStackTrace();
}
```

## Android Example App

If you'd like to see EOSIO SDK for Java: Softkey Signature Provider in action, check out our open source [Android Example App](https://github.com/EOSIO/eosio-java-android-example-app)--a working application that fetches an account's token balance and pushes a transfer action.

## Library Methods

This library is an example implementation of [ISignatureProvider](https://github.com/EOSIO/eosio-java/blob/master/eosiojava/src/main/java/one/block/eosiojava/interfaces/ISignatureProvider.java). It implements the following protocol methods:

* `signTransaction(EosioTransactionSignatureRequest eosioTransactionSignatureRequest)` signs a `Transaction`
* `getAvailableKeys()` returns an array containing the public keys associated with the private keys that the object is initialized with

Import a key by calling:

* `importKey(String privateKey)`

## Want to help?

Interested in contributing? That's awesome! Here are some [Contribution Guidelines](./CONTRIBUTING.md) and the [Code of Conduct](./CONTRIBUTING.md#conduct).

## License

[MIT](./LICENSE)

## Important

See LICENSE for copyright and license terms.  Block.one makes its contribution on a voluntary basis as a member of the EOSIO community and is not responsible for ensuring the overall performance of the software or any related applications.  We make no representation, warranty, guarantee or undertaking in respect of the software or any related documentation, whether expressed or implied, including but not limited to the warranties or merchantability, fitness for a particular purpose and noninfringement. In no event shall we be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or documentation or the use or other dealings in the software or documentation.  Any test results or performance figures are indicative and will not reflect performance under all conditions.  Any reference to any third party or third-party product, service or other resource is not an endorsement or recommendation by Block.one.  We are not responsible, and disclaim any and all responsibility and liability, for your use of or reliance on any of these resources. Third-party resources may be updated, changed or terminated at any time, so the information here may be out of date or inaccurate.

Wallets and related components are complex software that require the highest levels of security.  If incorrectly built or used, they may compromise users’ private keys and digital assets. Wallet applications and related components should undergo thorough security evaluations before being used.  Only experienced developers should work with this software.
