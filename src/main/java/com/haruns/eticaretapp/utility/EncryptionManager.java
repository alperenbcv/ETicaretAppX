package com.haruns.eticaretapp.utility;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionManager {
	private static final String ALGORITHM="AES";
	private static final String KEY = "qAz1+!rUt7w$eS5#";
	//MEHMETERTOP99 -> qAz1+!rUt7w$eS5#SDLKGAGJI3881334£>$>£½1TWEQT1£$>
	public static String encrypt(String data)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,
			       BadPaddingException {
		Cipher cipher=Cipher.getInstance(ALGORITHM);
		SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		byte[] encryptedData= cipher.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(encryptedData);
	}
	//qAz1+!rUt7w$eS5#SDLKGAGJI3881334£>$>£½1TWEQT1£$ -> MEHMETERTOP99
	public static String decrypt(String data)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,
			       BadPaddingException {
		Cipher cipher=Cipher.getInstance(ALGORITHM);
		SecretKeySpec keySpec=new SecretKeySpec(KEY.getBytes(), ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		byte[] decodedData=Base64.getDecoder().decode(data);
		byte[] originalData=cipher.doFinal(decodedData);
		return new String(originalData);
	}
	
}