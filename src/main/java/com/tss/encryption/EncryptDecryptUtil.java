/**
 * 
 */
package com.tss.encryption;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.tss.service.GnMap;

/**
 * @author NARESH GUMMAGUTTA
 *@since 9 Sep, 2021
 */
public class EncryptDecryptUtil {
	
	public static String RSA="RSA";
	public static String AES="AES"; 
	private static Cipher rsaCipher;
	private static Cipher aesCipher;
	public static final String ADMIN_APP_ENCRYPT = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMUuk6G4zx1whyaeGc9XpRVeyreNxC2NRGPVGTmZ7KkP88qSMCAGMGKwUOXfOl8BBxwLSwF02tVhQcS23bt3CK0CAwEAAQ==";
	public static final String CLIENT_APP_DECRYPT = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAxS6TobjPHXCHJp4Zz1elFV7Kt43ELY1EY9UZOZnsqQ/zypIwIAYwYrBQ5d86XwEHHAtLAXTa1WFBxLbdu3cIrQIDAQABAkB9Q8Oh/6gECdKyI8o9wG4tj1S1Gyi/z3jc+ynVQAXquAnu0RmIgsCw6fMIXj2T8XKUcG4WHPL92bp842o8RjwBAiEA/rr/LH4VloBS1/YvVEEMrUDNO8YSECu3nm46xS05bC0CIQDGKifJ9YNK0yL/WPjAHb03q29uaZHJVGYPd5pXWWtegQIgNoA/9H8Nk2PlVbugqMA6PB9vSei5GKih4s3m2SUx8gkCIEKjtIO3G3LTM+a47dX1akdJUIzJ1avlxPiYmP+c432BAiEAjRATrKw+hiKms0ZmW9ItZKCJO5ot0Wf90rweLdONMqo=";
	public static final String ADMIN_APP_DECRYPT = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAnGdWf9F7Dbp4ufRj6+D9s0S+J3SZWUDN5YFy6/7ALs/FLFp9kAKyi1vB5FJiYFVZo9f4+NQpqDoMQ5P8a9apYwIDAQABAkBM22chRoKOu3yJ/LOBfT2Oc5Bx+RVGS9ciGdusZrJwDbmRixQ8DwVm7OuGICmIhQy17wDxQImp+DgOw7CqcAOxAiEA4SvwiYHmO1p9EQQ/X21j/VLWBJhWWT9w0bkCqBCoR9sCIQCx0SS+FG29wVmISXUpVm3HGLTjqHFTpwlEJt9Uk+Z/GQIgRfoQy7sZ4NIzUel/BFoLm/t1VYI+O4gyuIqFn9NlaMcCIDTZ29jfS4rl6A2Y/8jMlo5hqoor65sYf6mRADosR4spAiA+zQ2BmeZTxFX+H9jVDfn2KS8yZzgDSZxpUKG+6qaH+Q==";
	
	public static SecretKey genAesKey()throws Exception {
		return KeyGenerator.getInstance(AES).generateKey();
	}
	
	public static PublicKey getPublicKey(String data) throws Exception {
		X509EncodedKeySpec spec = new X509EncodedKeySpec(DatatypeConverter.parseBase64Binary(new String(data)));
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}
	
	@SuppressWarnings("serial")
	public static GnMap encrypt(String dataJson)throws Exception{
		String publicKey = ADMIN_APP_ENCRYPT;
		Map<String, Object> res=new LinkedHashMap<String, Object>();
		String aesKey=DatatypeConverter.printBase64Binary(EncryptDecryptUtil.genAesKey().getEncoded());
		Key key=new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), EncryptDecryptUtil.AES);
		res.put("key", EncryptDecryptUtil.encryptText(aesKey,  EncryptDecryptUtil.getPublicKey(publicKey),EncryptDecryptUtil.RSA));
		res.put("data",EncryptDecryptUtil.encryptText(dataJson, key,EncryptDecryptUtil.AES));
		return new GnMap()	{{ put("data",res);}};
	}
//	public static Map<String, Object> encryptAsClient(Map<String, Object> data,Map<String, Object> clientDetails)throws Exception{
//		if(data==null)return null;
//		boolean encryption=clientDetails==null?false:clientDetails.getB("encryption.enable");
//		String secretKey=clientDetails==null?null:clientDetails.getS("encryption.clientEncrypt");
//		if(ZcUtil.isBlank(secretKey)) encryption=false;
//		if (encryption) { 
//			return encrypt(data, secretKey);
//		}
//		return data;
//	}
	public static String decrypt(GnMap data)throws Exception{
		String privateKey = CLIENT_APP_DECRYPT;
		String aesKey=EncryptDecryptUtil.decryptText(data.getS("data.key"), EncryptDecryptUtil.getPrivateKey(privateKey),EncryptDecryptUtil.RSA);
		Key keys=new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), EncryptDecryptUtil.AES);
		return EncryptDecryptUtil.decryptText(data.getS("data.data"), keys, EncryptDecryptUtil.AES);
	}
	
//	public static Map<String, Object> decrypt(Map<String, Object> data,Map<String, Object> clientDetails)throws Exception{
//		if(data==null)return null;
//		boolean encryption=clientDetails==null?false:clientDetails.getB("encryption.enable");
//		String secretKey=clientDetails==null?null:clientDetails.getS("encryption.serverDecrypt");
//		if(Utility.isBlank(secretKey)) encryption=false;
////		if(Map<String, Object>.IS_CLIENT_ADMIN || Map<String, Object>.IS_ZEROCODE_ADMIN) encryption=false;
//		if (encryption) { 
//			return decrypt(data, secretKey);
//		}
//		return data;
//	}
	
//	public static Map<String, Object> decryptAsClient(Map<String, Object> data,Map<String, Object> clientDetails)throws Exception{
//		if(data==null)return null;
//		boolean encryption=clientDetails==null?false:clientDetails.getB("encryption.enable");
//		String secretKey=clientDetails==null?null:clientDetails.getS("encryption.clientDecrypt");
//		if(ZcUtil.isBlank(secretKey)) encryption=false;
//		if (encryption) { 
//			return decrypt(data, secretKey);
//		}
//		return data;
//	}
	
	public static String encryptText(String msg, Key key,String algorithm) 
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			UnsupportedEncodingException, IllegalBlockSizeException, 
			BadPaddingException, InvalidKeyException {
		Cipher cipher="AES".equalsIgnoreCase(algorithm)?aesCipher():rsaCipher();
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return DatatypeConverter.printBase64Binary(cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8)));
	}	
	public static String decryptText(String msg, Key key,String algorithm)
			throws InvalidKeyException, UnsupportedEncodingException, 
			IllegalBlockSizeException, BadPaddingException,NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher="AES".equalsIgnoreCase(algorithm)?aesCipher():rsaCipher();
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(DatatypeConverter.parseBase64Binary(msg)),StandardCharsets.UTF_8);
	}	 
	public static PrivateKey getPrivateKey(String data) throws Exception {
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(DatatypeConverter.parseBase64Binary(new String(data)));
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}
	
	private static Cipher rsaCipher()throws NoSuchAlgorithmException, NoSuchPaddingException {
		if(rsaCipher==null) rsaCipher=Cipher.getInstance(RSA);
		return rsaCipher;
	}
	private static Cipher aesCipher()throws NoSuchAlgorithmException, NoSuchPaddingException {
		if(aesCipher==null) aesCipher=Cipher.getInstance(AES);
		return aesCipher;
	}	 
}
