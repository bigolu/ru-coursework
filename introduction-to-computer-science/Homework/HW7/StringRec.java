public class StringRec{
	public static void main(String[] args) {
		System.out.println("Gimme a string: ");
		String str = IO.readString();
		String test = str;
		
		System.out.println("Input: " + str);
		
		//str = Compress.shorten(str);
		//System.out.println("Compressed Version: " + str);
		
		str = decompress(str);
		System.out.println("Decompressed Version: " + decompress(str));
		
		if (test.equals(str))
			System.out.println("It worked!");
	}

	public static String decompress(String compressedText){
		if (compressedText.length() == 0)
			return "";
		else{
			if(Character.isDigit(compressedText.charAt(0))){
				int num = compressedText.charAt(0) - '0';
				return (expand(compressedText.charAt(1), num) + decompress(compressedText.substring(2)));
			}
			else
				return (compressedText.charAt(0) + decompress(compressedText.substring(1)));
		}
		
		
	}
	
	public static String expand(char c, int n){
		if (n==0)
			return "";
		else{
			return (c + expand(c, n-1));
		}
			
	}
}
