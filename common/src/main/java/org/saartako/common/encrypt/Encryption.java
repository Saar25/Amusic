package org.saartako.common.encrypt;

public interface Encryption {

    String encrypt(String message, String salt);

}
