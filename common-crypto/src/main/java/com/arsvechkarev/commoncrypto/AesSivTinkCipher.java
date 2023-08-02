package com.arsvechkarev.commoncrypto;

import com.google.crypto.tink.subtle.AesSiv;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class AesSivTinkCipher {

  public static byte[] encrypt(String masterPassword, String plaintext)
      throws GeneralSecurityException {
    byte[] hashedPassword = MessageDigest.getInstance("SHA-512")
        .digest(masterPassword.getBytes(StandardCharsets.UTF_8));
    return new AesSiv(hashedPassword)
        .encryptDeterministically(plaintext.getBytes(StandardCharsets.UTF_8), null);
  }

  public static String decrypt(String masterPassword, byte[] ciphertext)
      throws GeneralSecurityException {
    byte[] hashedPassword = MessageDigest.getInstance("SHA-512")
        .digest(masterPassword.getBytes(StandardCharsets.UTF_8));
    return new String(new AesSiv(hashedPassword)
        .decryptDeterministically(ciphertext, null), StandardCharsets.UTF_8);
  }
}
