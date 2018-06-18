//package com.baozun.tools;
//
//import com.baozun.nebula.utilities.common.EncryptUtil;
//import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
//
//
///**
// * 加密
// * @author zl.shi
// *
// */
//public class Encryptor {
//	
//	
//	/**
//	 * RSA 加密
//	 * @param plainText
//	 * @param key
//	 * @return
//	 */
//	public static String endecryptRSA( String plainText, String key ){
//        try{
//            return EncryptUtil.getInstance().getEncryptor("RSA").encrypt(plainText, key);
//        }catch (EncryptionException e1){
//            return plainText;
//        }
//    }
//	
//	
//	/**
//	 * AES 加密
//	 * @param plainText
//	 * @return
//	 */
//	public static String endecryptAES( String plainText ){
//
//		try{
//			return EncryptUtil.getInstance().encrypt(plainText);
//		}catch (EncryptionException e1){
//			return plainText;
//		}
//		
//    }
//	
//	
//	/**
//	 * AES 解密
//	 * @param plainText
//	 * @return
//	 */
//	public static String decryptAES( String plainText ){
//
//		try{
//			return EncryptUtil.getInstance().decrypt(plainText);
//		}catch (EncryptionException e1){
//			return plainText;
//		}
//		
//    }
//	
//	
//	public static void main(String[] args) {
//		
////		System.out.println(Encryptor.endecryptAES("赤坎区南桥街道  文明宾馆 往麻章方向前一路口（有个泉记鸡饭店）右转，在东北小吃店的往上两栋楼(红墙绿门)。"));
////		System.out.println(decryptAES("RVUQ08Lxcan+aTiceEsKoA=="));
//		
////		String mi = SuperAESEncryptor.endecrypt("黑子");
//		String mi = SuperAESEncryptor.decrypt("RVUQ08Lxcan+aTiceEsKoA==");
//		
//		System.out.println(mi);
////		System.out.println(decryptAES(mi));
//	}
//
//}
