package nl.novi.breedsoft.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public final class KeyGeneratorUtils {
    /**
     * this class cannot be instantiated, only static methods.
     */
    private KeyGeneratorUtils() {
    }

    /**
     * Generate an RSA Keypair with keysize of 2048 bits
     * @return Generated RSA algorithm keypair
     */
    static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }
}
