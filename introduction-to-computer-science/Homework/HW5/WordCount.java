
public class WordCount {
	
	public static void main(String[] args) {
		
		System.out.println("Input your sentence(s): ");
		String str = IO.readString();
		str = (' ' + str + ' ');
		System.out.println("Input your minimum length: ");
		int min = IO.readInt();
		int start = -1;
		int end = -1;
		int total = 0;
		
		if (min < 0)
			IO.reportBadInput();
		
		breakUp: while (str.length() >= 3){
			allSpaces: while(true){
				for (int i = 0;i<str.length();i++){
					if (str.charAt(i) != ' ')
						break allSpaces;
				}
				break breakUp;
			}
			
			
			
			for (int i = 0;i<str.length();i++){
				if (str.charAt(i) != ' '){
					start = i;
					break;
				}
			}
			
			
			for (int i = start+1;i<str.length();i++){
				if (str.charAt(i) == ' '){
					end = i;
					break;
				}
			}
			if (meetsLength(str.substring(start, end), min))
				total ++;
			str = str.substring(end);
		}
		
		IO.outputIntAnswer(total);
	}
	public static boolean meetsLength(String word, int min){
		int letters = 0;
			
		for (int i = 0;i<word.length();i++){
			if (word.charAt(i) >= 97 && word.charAt(i) <= 122){
				letters++;
			}
			else if (word.charAt(i) >= 65 && word.charAt(i) <= 90){
				letters++;
			}
		}
		
		if (letters >= min)
			return (true);
		else
			return (false);
	}
}