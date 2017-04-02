
public class Compress {

	public static void main(String[] args) {
		
		System.out.println("input your string");
		String str = IO.readString();
		String compressedString = "";
		
		if (str.length() <= 1){
			IO.outputStringAnswer(str);
			return;
		}
		
		char spot = str.charAt(0);
		int same = 1;
		
		for(int i = 1;i < str.length();i++){
			if (str.charAt(i) == spot)
				same++;
			else{
				if (same > 1){
					compressedString = compressedString + same + spot;
					spot = str.charAt(i);
					same = 1;
				}
				else{ 
					compressedString = compressedString + spot;
					spot = str.charAt(i);
					same = 1;
				}
			}
		}
		
		if (same > 1)
			compressedString = compressedString + same + spot;
		else{ 
			compressedString = compressedString + spot;
		}
		
		IO.outputStringAnswer(compressedString);
	}
}
