package org.saartako.common.encrypt;

public class Encryptions {

    private static final Encryption encryption = new SecureEncryption("PBKDF2WithHmacSHA256", 65536, 256);

    public static Encryption getDefaultEncryption() {
        return encryption;
    }
}
