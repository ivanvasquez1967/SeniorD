package aes;

public class MAC {

	private static byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int Roundone=0;
	public byte[] Initalize( byte[] mess,byte[] cipher ,int numRounds){
	byte[] input= new byte[mess.length];
	byte[] mac =new byte[mess.length];
		for (int i=0;i<numRounds;i++){
		if (Roundone==0){
			input[i]=(byte)(mess[i]^iv[i]);
			mac[i]= encrypt(input[i]);
		}
		else{
			input[i]=(byte)(mess[i]^cipher[i]);
			mac[i]=encrypt(input[i]);
		}
		Roundone=1;
	}
		
	
		return input;
	}
/*	private static byte[] Mac(byte[]key,byte[]mess,int length){
		byte[] mac=new byte[length];
		
		
		return mac;
	}
	*/
	
}
