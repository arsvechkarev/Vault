package buisnesslogic

interface Cryptography {

    /**
     * Encrypts [plaintext] with [password] and returns encrypted data
     *
     * @see decryptData
     */
    fun encryptData(password: String, plaintext: String): ByteArray

    /**
     * Decrypts [ciphertext] using given [password] and returns decrypted plaintext. If decryption
     * was unsuccessful, throws exception
     */
    fun decryptData(password: String, ciphertext: ByteArray): String
}