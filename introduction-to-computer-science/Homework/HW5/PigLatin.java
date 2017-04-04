
public class PigLatin {

	public static void main(String[] args) {
		
		System.out.println("Input your word: ");
		String word = IO.readString();
		word = (word.toLowerCase());
		
		if(word.length() <= 0){
			IO.outputStringAnswer(word);
			return;
		}
		
		if (vowelFirst(word))
			word = (word + "way");
		else
			word = (word.substring(1, word.length()) + word.charAt(0) + "ay");
		
		IO.outputStringAnswer(word);
	}
	public static boolean vowelFirst(String word){
		
		word = word.toLowerCase();
		System.out.println(word);
		
		if ((word.charAt(0) == 'a') || (word.charAt(0) == 'e') || (word.charAt(0) == 'i') || 
		(word.charAt(0) == 'o') || (word.charAt(0) == 'u'))
			return (true);
		else
			return (false);
	}
}
