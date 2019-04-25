package one.block.eosiosoftkeysignatureprovider.error;

import one.block.eosiojava.error.signatureProvider.SignatureProviderError;
import org.jetbrains.annotations.NotNull;

/**
 * Error class is used when there is an exception while attempting to import a key into the
 * signature provider.
 *
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
