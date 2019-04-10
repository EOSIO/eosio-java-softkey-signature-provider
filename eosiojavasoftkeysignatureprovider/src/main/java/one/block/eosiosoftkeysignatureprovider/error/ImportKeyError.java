package one.block.eosiosoftkeysignatureprovider.error;

import one.block.eosiojava.error.signatureProvider.SignatureProviderError;
import org.jetbrains.annotations.NotNull;

/**
 * Error would be thrown by SoftKeySignatureProviderImpl#ImportKeyError
 */
public class ImportKeyError extends SignatureProviderError {
    public ImportKeyError() {
    }

    public ImportKeyError(@NotNull String message) {
        super(message);
    }

    public ImportKeyError(@NotNull String message, @NotNull Exception exception) {
        super(message, exception);
    }

    public ImportKeyError(@NotNull Exception exception) {
        super(exception);
    }
}
