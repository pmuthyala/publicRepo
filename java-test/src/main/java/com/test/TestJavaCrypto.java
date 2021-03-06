package com.test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TestJavaCrypto {

	private static final String CIPHER_TRANSFORMATION_DESede = "DESede/CBC/PKCS5Padding";
	private static final String CIPHER_DESede_ALGO = "DESede";
	private static final String ENCRYPTION_KEY = "shouldbesomebitslongsojustaddingmorenumbers1234567890inthekey";
	private static final String MESSAGE = "Test Message which is a multiple of 16 to encrypt and decrypt ";

	public static void main(String[] a) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException,
			InvalidAlgorithmParameterException, UnsupportedEncodingException {
		byte plainText[] = MESSAGE.getBytes();
		byte desKeyData[] = ENCRYPTION_KEY.getBytes();
		System.out.println("key length " + desKeyData.length);
		encrypt(plainText, desKeyData);
	}

	private static void encrypt(byte[] plainText, byte[] desKeyData)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException {
		Cipher c = Cipher.getInstance(CIPHER_TRANSFORMATION_DESede);
		SecretKeySpec sKey = new SecretKeySpec(desKeyData, CIPHER_DESede_ALGO);
		c.init(Cipher.ENCRYPT_MODE, sKey,
				new IvParameterSpec(c));

		byte[] encryptedString = c.doFinal(plainText);

		System.out.println("Before DES Encryption " + plainText);
		System.out.println("After DES Encryption " + encryptedString);
	}
}
