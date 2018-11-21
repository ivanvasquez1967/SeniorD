package aes;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.time.*;
import java.io.*;
public class Main {

	public static String Check(String m1, String m2){
		String c;
		if(m1.equals(m2)){
			 c="YES";
		}
		else c="NO";
		return c;
	}
	public static void main (String [] args) throws Exception
	{
		File file= new File("C:/Users/ivanvasquez/Desktop/SeniorDesign/Shared_Key.txt");
		file.getParentFile().mkdirs();
		/////////////////////////EC Diffie-Hellman//////////////////////////////
		Security.addProvider(new BouncyCastleProvider());
		KeyPairGenerator kpgen = KeyPairGenerator.getInstance("ECDH", "BC");
		kpgen.initialize(new ECGenParameterSpec("prime192v1"), new SecureRandom());
		KeyPair pairA = kpgen.generateKeyPair();
		KeyPair pairB = kpgen.generateKeyPair();
		/*
		System.out.println("Alice: " + pairA.getPrivate());
		System.out.println("Alice: " + pairA.getPublic());
		System.out.println("Bob:   " + pairB.getPrivate());
		System.out.println("Bob:   " + pairB.getPublic());
		*/
		//User A
		byte [] dataPrvA = ECDH.savePrivateKey(pairA.getPrivate());
		byte [] dataPubA = ECDH.savePublicKey(pairA.getPublic());
		
		//User B
		byte [] dataPrvB = ECDH.savePrivateKey(pairB.getPrivate());
		byte [] dataPubB = ECDH.savePublicKey(pairB.getPublic());
		System.out.println("Alice Prv: " + ECDH.bytesToHex(dataPrvA));
		System.out.println("Alice Pub: " + ECDH.bytesToHex(dataPubA));
		System.out.println("Bob Prv:   " + ECDH.bytesToHex(dataPrvB));
		System.out.println("Bob Pub:   " + ECDH.bytesToHex(dataPubB));

		String keyA;
		String keyB;
		keyA=ECDH.doECDH("UserA SharedKey: ", dataPrvA, dataPubB);
		keyB=ECDH.doECDH("UserB SharedKey: ", dataPrvB, dataPubA);
		//System.out.println("Secret for user A: "+ keyA);
		//System.out.println("Secret for user B: "+ keyB);
		
		//FileWriter writer= new FileWriter("");
		PrintWriter writer = new PrintWriter(file);
		writer.println(keyA);
		writer.close();
		
		/////////////////AES-CBC MAC///////////////////////////////////
		
		AES aes = new AES();			 // init AES encrypted class
		//ECDH ecdh= new ECDH();
		String data = "00112233445566778899aabbccddeeff";
		System.out.println("Original text : "+data);
		byte []in=new byte[data.length()];
		//User A
		aes.setKey(keyA);  // choose password
		String encrypted = aes.Encrypt(data); 
		String hex = Util.toHEX(encrypted.getBytes()).replace(" ", "");
		System.out.println("Encrypted text UserA: "+hex);
		String mac=aes.getMAC();
		System.out.println("MAC code UserA: "+mac);
		
		//User B
		aes.setKey(keyB);  // choose password
		String encryptedB = aes.Encrypt(data); 
		String hexB = Util.toHEX(encryptedB.getBytes()).replace(" ", "");
		System.out.println("Encrypted text UserB: "+hexB);
		String macB=aes.getMAC();
		System.out.println("MAC code UserB: "+macB);
		
		File file1= new File("C:/Users/ivanvasquez/Desktop/SeniorDesign/MAC_Code.txt");
		file.getParentFile().mkdirs();
		PrintWriter writer1 = new PrintWriter(file1);
		writer1.println(keyA);
		writer1.close();
		String test;
		test=Check(mac,macB);
		System.out.println("Do the MAC codes match? : "+ test);
		//System.out.println("MAC: "+mac);
		/*
		String unencrypted = aes.Decrypt(encrypted); 
		System.out.println("Decrypted text : "+unencrypted);
		*/
	}

}
