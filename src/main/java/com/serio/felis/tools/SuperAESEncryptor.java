package com.serio.felis.tools;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import net.iharder.Base64;


public class SuperAESEncryptor {
	
	
	static String key				= "zx2ym8hu5iop3cj6rd7cvb==";
	
	
	/**
	 * AES 默认用key=zx2ym8hu5iop3cj6rd7cvb==做加密
	 * @param plainText
	 * @return
	 */
	public static String endecrypt(String plainText) {
		return endecrypt( plainText, key);
	}
	
	
	/**
	 * AES 加密
	 * @param plainText
	 * @param masterkey
	 * @return
	 */
    public static String endecrypt(String plainText,String masterkey) {
        return format(encryptToByteArray(plainText, masterkey));
    }
	
    
    /**
     * AES加密
     * @param plainText
     * @param masterkey
     * @return
     */
    public static byte[] encryptToByteArray(String plainText, String masterkey) {
        try{
        	
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(parse(masterkey), "AES"));

            return cipher.doFinal(plainText.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
		return null;
    }
    
    /**
     * AES 解密
     * @param cipherText
     * @return
     */
    public static String decrypt( String cipherText ) {
        return decryptFromByteArray(parse(cipherText), key);
    }
    
    /**
     * AES 解密
     * @param cipherText
     * @param masterkey
     * @return
     */
    public static String decrypt(String cipherText,String masterkey) {
        return decryptFromByteArray(parse(cipherText), masterkey);
    }
    
    
    /**
     * AES 解密
     * @param cipher
     * @param masterkey
     * @return
     */
    public static String decryptFromByteArray(byte[] cipher,String masterkey) {
        try{
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(parse(masterkey), "AES"));
            byte[] b = c.doFinal(cipher);
            return new String(b, UnicodeConstant.ENCODING_UTF_8);
        }catch (Exception e){
            
        }
        return null;
    }
    
    
    /**
     * Parse base64
     * @param strValue
     * @return
     */
	public static byte[] parse(String strValue) {
		try {
			return Base64.decode(strValue);
		} catch (IOException e) {		
			e.printStackTrace();
			throw new RuntimeException("Decode with Base64 error.");
		}
	}
	
	
	/**
	 * Format base64
	 * @param value
	 * @return
	 */
	public static String format(byte[] value) {
		return Base64.encodeBytes(value);
	}
}
