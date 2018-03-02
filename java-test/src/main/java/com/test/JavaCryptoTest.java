package com.test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class JavaCryptoTest {

	/*
	 * private static final String CIPHER_TRANSFORMATION =
	 * "DESede/CBC/PKCS5Padding"; private static final String CIPHER_ALGO =
	 * "DESede"; private static final String ENCRYPTION_KEY =
	 * "keyshouldbesomebitslongsojustaddingmorenumbers1234567890inthekey";
	 * private static final String MESSAGE =
	 * "Test Message which is a multiple of 16 to encrypt and decrypt ";
	 */
	private static final String CIPHER_TRANSFORMATION = "AES/ECB/PKCS5Padding";
	private static final String CIPHER_ALGO = "AES";
	private static final String ENCRYPTION_KEY = "keyshouldbesomeb";
	private static final String MESSAGE = "Test Message which is a multiple of 16 to encrypt and decrypt ";

	public static void main(String[] a)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
		byte plainText[] = MESSAGE.getBytes();
		byte desKeyData[] = ENCRYPTION_KEY.getBytes();
		System.out.println("key length " + desKeyData.length);
		String encryptedData = encrypt(plainText, desKeyData);
		String decryptedData = decrypt(encryptedData, desKeyData);
		System.out.println("\n After decrypt result is same as plain text :: "+decryptedData);
	}

	private static String decrypt(String encryptedData, byte[] desKeyData) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher c = Cipher.getInstance(CIPHER_TRANSFORMATION);
		SecretKeySpec sKey = new SecretKeySpec(desKeyData, CIPHER_ALGO);
		c.init(Cipher.DECRYPT_MODE, sKey);
		byte [] decryptedData = c.doFinal(Base64.getDecoder().decode(encryptedData));
		System.out.println("\n after decrypt ");
		for (int i = 0; i < decryptedData.length; i++)
			System.out.print((char) decryptedData[i] + " ");
		return new String(decryptedData);
	}

	private static String encrypt(byte[] plainText, byte[] desKeyData)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance(CIPHER_TRANSFORMATION);
		SecretKeySpec sKey = new SecretKeySpec(desKeyData, CIPHER_ALGO);
		c.init(Cipher.ENCRYPT_MODE, sKey);

		byte[] encryptedByteArray = c.doFinal(plainText);

		System.out.println("Before DES Encryption ");
		for (int i = 0; i < plainText.length; i++)
			System.out.print((char) plainText[i] + " ");

		String encryptedString = Base64.getEncoder().encodeToString(encryptedByteArray);
		System.out.println("\n After DES Encryption :: " + encryptedString);

		return encryptedString;
	}

}
