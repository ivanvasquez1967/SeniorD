package aes;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.Security;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.ECGenParameterSpec;
import javax.crypto.KeyAgreement;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.crypto.generators.BaseKDFBytesGenerator;
public class ECDH
{
	final protected static char[] hexArray = "0123456789abcdef".toCharArray();
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte [] savePublicKey (PublicKey key) throws Exception
	{
		//return key.getEncoded();

		ECPublicKey eckey = (ECPublicKey)key;
		return eckey.getQ().getEncoded(true);
	}

	public static PublicKey loadPublicKey (byte [] data) throws Exception
	{
		/*KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
		return kf.generatePublic(new X509EncodedKeySpec(data));*/

		ECParameterSpec params = ECNamedCurveTable.getParameterSpec("prime192v1");//Change to 128 currently 192
		ECPublicKeySpec pubKey = new ECPublicKeySpec(
				params.getCurve().decodePoint(data), params);
		KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
		return kf.generatePublic(pubKey);
	}

	public static byte [] savePrivateKey (PrivateKey key) throws Exception
	{
		//return key.getEncoded();

		ECPrivateKey eckey = (ECPrivateKey)key;
		return eckey.getD().toByteArray();
	}

	public static PrivateKey loadPrivateKey (byte [] data) throws Exception
	{
		//KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
		//return kf.generatePrivate(new PKCS8EncodedKeySpec(data));

		ECParameterSpec params = ECNamedCurveTable.getParameterSpec("prime192v1");
		ECPrivateKeySpec prvkey = new ECPrivateKeySpec(new BigInteger(data), params);
		KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
		return kf.generatePrivate(prvkey);
	}

	public static String doECDH (String name, byte[] dataPrv, byte[] dataPub) throws Exception
	{
		KeyAgreement ka = KeyAgreement.getInstance("ECDH", "BC");
		ka.init(loadPrivateKey(dataPrv));
		ka.doPhase(loadPublicKey(dataPub), true);
		byte [] secret = ka.generateSecret();
		String a=bytesToHex(secret);
		System.out.println(name + bytesToHex(secret));
		return a;
	}

	
}