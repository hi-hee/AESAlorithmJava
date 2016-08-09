import java.util.ArrayList;


/* Main Class - This class executes en/decryption */

public class AES {
	
	public static void main(String args[]){
	
		String plaintext="F5371A4959FC9F202F6929D6243C26D2";
		String cipherKey="00000000000000000000000045";

		AES aes = new AES();

		label: for (int i = 0; i < 16777216; i++) {

			char text[][] = aes.InputToArray(plaintext);
			char key[][] = aes.InputToArray(cipherKey + String.format("%06X", i));
			
			/*Decryption*/
			aes.EnOrDecryption(text, key, 'D');

			for (int j = 0; j < 4; j++)
				for (int k = 0; k < 4; k++) {
					if (text[k][j]!=32 && text[k][j]!=46 && (text[k][j]<'a' | text[k][j]>'z') && (text[k][j]<'A' | text[k][j]>'Z')) {
						j = 4;k = 4;
						continue label;
					}
				}
			
			/* printing plaintext & key */
			System.out.println("Key= " + String.format("%06X", i));
			for (int j = 0; j < 4; j++)
				for (int k = 0; k < 4; k++)
					System.out.print(text[k][j]);
			System.out.println();
			break;

		}

	}

	/* en/decryption method
	 * type 'E' = encryption, 'D' = decryption */
	
	void EnOrDecryption(char text[][], char key[][], char type) {
		Round R = new Round();	
		
		/* To Copy Array */
		char copy[][]; copy=new char[4][4];
		for(int i=0; i<4; i++)
			for(int j=0; j<4; j++)
				copy[i][j]=key[i][j];
		
		/*KeySchedule*/
		ArrayList<char[][]> RoundKey = new ArrayList<char[][]>(); //All roundkey will be stored 
		RoundKey.add(0,copy);
	
		for(int i=1; i<11; i++){ 
			char tmp[][] = R.RoundkeyGenerator(key, i); 	
			if(type == 'D' && i<10)	R.MixColumns(tmp, type);	//Inverse mix cols
			RoundKey.add(i,tmp);
			}
		
		/*Initial transformation*/
		int RKeyNum = 0; if(type == 'D') RKeyNum=10;
		R.AddRoundkey(text, RoundKey.get(RKeyNum)); 
	
		/*Round 1 to 10*/
		for(int i=1; i<11; i++){
						R.SubBytes(text, type);			
						R.ShiftRows(text, type);						
			if(i<10) 	R.MixColumns(text, type);
			if(type == 'E')		R.AddRoundkey(text, RoundKey.get(i));
			else if(type == 'D') R.AddRoundkey(text, RoundKey.get(10-i));


		}
		
		
	}
	
	/* state array method */
	char[][] InputToArray(String text){
		char array[][]; array= new char[4][4];
		byte StringNum =0;
	
		for(int i=0; i<4; i++)
			for(int j=0; j<4; j++){
			array[j][i] = (char)Integer.parseInt(text.substring(StringNum,StringNum+2),16);
			StringNum+=2;
			}
		
		return array;
				
	}
	
	/*
	void print(char text[][]){
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				System.out.print(Integer.toHexString(text[i][j]&0xff)); 
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	*/
		
}
