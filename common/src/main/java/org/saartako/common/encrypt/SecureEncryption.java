package org.saartako.common.encrypt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class SecureEncryption implements Encryption {

    private final String algorithm;
    private final int iterations;
    private final int keyLength;

    public SecureEncryption(String algorithm, int iterations, int keyLength) {
        this.algorithm = algorithm;
        this.iterations = iterations;
        this.keyLength = keyLength;
    }

    @Override
    public String encrypt(String message, String salt) {
        try {
            final char[] passwordChars = message.toCharArray();
            final byte[] saltBytes = Base64.getDecoder().decode(salt);
            final PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, this.iterations, this.keyLength);
            final SecretKeyFactory factory = SecretKeyFactory.getInstance(this.algorithm);
            final byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
