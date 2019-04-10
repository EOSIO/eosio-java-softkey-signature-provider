package one.block.eosiosoftkeysignatureprovider.error;

import one.block.eosiojava.error.signatureProvider.SignatureProviderError;
import org.jetbrains.annotations.NotNull;

/**
 * Error would be thrown by SoftKeySignatureProviderImpl#GenerateKey
 */
public class GenerateKeyError extends SignatureProviderError {
    public GenerateKeyError() {
    }

    public GenerateKeyError(@NotNull String message) {
        super(message);
    }

    public GenerateKeyError(@NotNull String message, @NotNull Exception exception) {
        super(message, exception);
    }

    public GenerateKeyError(@NotNull Exception exception) {
        super(exception);
    }
}
