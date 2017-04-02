
public class Finals {

	public static void main(String[] args) {
		//bRounding
		double x = 5.45798455884;
		System.out.println("ANS: " + bRounding(x, 6));
		
		//reverseInsSort
		int[] test = {13,11,6,7,87,9,4};
		int[] test2 = reverseInsSort(test);
		
		for(int i = 0; i<test2.length;i++){
			System.out.print(test2[i] + " ");
		}
		System.out.println();
		
		//insString
		String[] stuff = new String[9];
		stuff[0] = "ajini";
		stuff[1] = "bjini";
		stuff[2] = "cjini";
		stuff[3] = "djini";
		stuff[4] = "ejini";
		stuff[5] = "fjini";
		stuff[6] = "gjini";
		stuff[7] = "hjini";
		
		insString(stuff, 8, "bruh");
		
		for(int i = 0; i<stuff.length;i++){
			System.out.print(stuff[i] + " ");
		}
		
		System.out.println();
		
		//code
		String code = "ajf /.,?<okr0196''[[";
		System.out.println(code);
		System.out.println(decipher(code));
		
		//reverseSentence
		String sentence = "In a perfect world this sentence would be reversed by my method.";
		System.out.println(sentence);
		System.out.println(reverseString2(sentence));
		
		//grammarcheck
		String watev = "   is  This thing I still work?";
		System.out.println(grammarCheck2(watev));
		
		//flipString
		String s = "flip this string";
		System.out.println(s);
		System.out.println(flipString(s));
		
		//round
		double number = 2.3579;
		System.out.println(round(number, 3));
		
		//tictactoe
		char[][] board = new char[5][4];
		board[1][3] = 'o';
		board[2][2] = 'o';
		board[3][1] = 'o';
		board[4][0] = ' ';
		
		if (ticTacToe(board, 3) == 'o'){
			System.out.println("It Works!!!");
		}
		else{
			System.out.println(ticTacToe(board, 3));
		}
		
		//sortingBackwards
		
		
		//revSort(stuff2);
		
		/*for (String letter : stuff2){
			System.out.print(letter);
		}
		System.out.println();*/
		
		//finalGambit
		String[] stuff2 = new String[7];
		stuff2[0] = "c";
		stuff2[1] = "e";
		stuff2[2] = "a";
		stuff2[3] = "g";
		stuff2[4] = "d";
		stuff2[5] = "f";
		stuff2[6] = "b";
		
		for (String letter : stuff2){
			System.out.print(letter);
		}
		System.out.println();
		
		hSort(stuff2);
		
	}
	
	public static double bRounding(double x, int n){
		
		if (n==0){
			double test = x*10.0;
			
			int newX = (int) x;
			
			if (test%10 >= 5){
				newX++;
			}
			
			return newX;
		}
		else{
			x = (x*10.0);
			return bRounding(x, n-1)/10.0;
		}
	}
	
	public static int[] reverseInsSort(int[] numbers){
		for(int numSorted = 1;numSorted < numbers.length;numSorted++){
			int temp = numbers[(numbers.length - numSorted) - 1];
			int spot = (numbers.length - numSorted) - 1;
			
			for (int list = numSorted; list >= 1; list--){
				if (temp > numbers[numbers.length - list]){
					spot = numbers.length - list;
					numbers[(numbers.length - list) - 1] = numbers[numbers.length - list];
				}
			}
			
			numbers[spot] = temp;
		}
		return numbers;
	}
	
	public static void insString(String[] container, int total, String str){
		int start = 0;
		int end = total - 1;
		int middle = (start + end)/2;
		
		for(;end >= start;){
			if (container[middle].compareToIgnoreCase(str) == 0){
				for(int i = total - 1;i >= middle;i--){
					container[i + 1] = container[i];
				}
				container[middle] = str;
			}
			
			else if (container[middle].compareToIgnoreCase(str) > 0){
				end = middle - 1;
				middle = (start + end)/2;
			}
			
			else{
				start = middle + 1;
				middle = (start + end)/2;
			}
		}
		
		if (container[middle].compareToIgnoreCase(str) > 0){
			for(int i = total - 1;i >= middle;i--){
				container[i + 1] = container[i];
			}
			container[middle] = str;
		}
		
		if (container[middle].compareToIgnoreCase(str) < 0){
			for(int i = total - 1;i > middle;i--){
				container[i + 1] = container[i];
			}
			container[middle + 1] = str;
		}
		return;
	}
	
	public static String decipher(String code){
		code = code.toLowerCase();
		String translation = "";
		
		for (int i = 0;i < code.length();i++){
			if ((Character.isDigit(code.charAt(i)) == false) && ((code.charAt(i) > 'z') || (code.charAt(i) < 'a'))){
				translation += code.charAt(i);
			}
			else if (code.charAt(i) == '0'){
				translation += '9';			
			}
			else if (code.charAt(i) == 'a'){
				translation += 'z';			
			}
			else{
				translation += (char) (code.charAt(i) - 1);
			}
		}
		
		return translation;
	}
	
	public static String reverseSentence(String str){
		String answer = "";
		str = str.toLowerCase();
		int spot = str.length()-1;
		
		for(int i = str.length() - 2; i >= 0; i--){
			if (str.charAt(i) == ' '){
				answer += str.substring(i+1, spot) + " ";
				spot = i;
			}
			else if (i == 0){
				answer+= str.substring(0, spot);
			}
		}
		
		
		answer += str.charAt(str.length() -1);
		
		String answer2 = "" + Character.toUpperCase(answer.charAt(0)) + answer.substring(1, answer.length());
		
		return answer2;
	}
	
	public static String grammarCheck2(String str){
		
		str = " " + str.toLowerCase();
		boolean hasPunctuation = false;
		char punctuation = ' ';
		int start = 0;
		String answer = "";
		int i = 0;
		
		if ((str.charAt(str.length() - 1) > 'z' || str.charAt(str.length() - 1) < 'a') && str.charAt(str.length() - 1) != ' '){
			hasPunctuation = true;
			punctuation = str.charAt(str.length() - 1);
			str = str.substring(0, str.length() -1 ) + ' ';
		}
		else{
			str = str.substring(0, str.length()) + ' ';
		}
		
		for (; i < str.length(); i++){
			if(str.charAt(i) != ' '){
				
				for(int j = i + 1; j < str.length(); j++){
					if(str.charAt(j) == ' '){
						
						if (str.substring(i,j).equals("i")){
							answer += 'I' + " ";
						}
						else{
							answer += str.substring(i, j) + ' ';
						}
						i = j;
						break;
					}
				}
			}
		}
		
		answer = answer.substring(0, answer.length() - 1);
		
		if(hasPunctuation){
			answer += punctuation;
		}
		else{
			answer += '.';
		}
		
		answer = Character.toUpperCase(answer.charAt(0)) + answer.substring(1, answer.length());
		
		return answer;
		
	}
	
	public static String reverseString2(String str){
		String answer = "";
		str = str.toLowerCase();
		str = ' ' + str.substring(0, str.length() - 1) + ' ';
		int start = 0;
		
		for (int end = 1; end < str.length(); end++){
			if (str.charAt(end) == ' '){
				for(int i = end -1; i > start ; i--){
					answer += str.charAt(i);
				}
				
				answer += ' ';
				start = end;
			}
		}
		
		answer = Character.toUpperCase(answer.charAt(0)) + answer.substring(1, answer.length() - 1) + '.';
		
		return answer;
	}
	
	public static String flipString(String str){
		if (str.length() == 0){
			return str;
		}
		else{
			return str.charAt(str.length() - 1) + flipString(str.substring(0, str.length() - 1));
		}
	}
	
	public static double round(double x, int n){
		if (n == 0){
			if((x*10)%10 >= 5){
				return x = (int) x + 1;
			}
			else{
				return x = (int) x;
			}
		}
		else{
			return (round(x*10, n-1))/10;
			
		}
	}
	
	public static char ticTacToe(char[][] board, int k){
		int numx = 0;
		int numo = 0;
		
		for (int i = 0; i < board.length; i++){
			numx = 0;
			numo = 0;
			for (int j =0;j < board[0].length; j++){
				if (board[i][j] == 'x'){
					numx++;
				}
				else if (board[i][j] == 'o'){
					numo++;
				}
				else if (board[i][j] == ' ' && j != board[0].length - 1){
					numo = 0;
					numx = 0;
				}
			}
			if (numx >= k){
				return 'x';
			}
			if (numo >= k){
				return 'o';
			}
		}
		
		for (int j = 0; j < board[0].length; j++){
			numx = 0;
			numo = 0;
			for (int i =0;i < board.length; i++){
				if (board[i][j] == 'x'){
					numx++;
				}
				else if (board[i][j] == 'o'){
					numo++;
				}
				else if (board[i][j] == ' ' && i != board.length - 1 ){
					numo = 0;
					numx = 0;
				}
			}
			if (numx >= k){
				return 'x';
			}
			else if (numo >= k){
				return 'o';
			}
		}
		
		for (int i = 0; i < board.length;i++){
			for (int j = 0; j <board[0].length; j++){
			
				numx = 0;
				numo = 0;
				
				for(int a = i, b = j;(a < board.length) && (b < board[0].length); a++, b++){
					if (board[a][b] == 'x'){
						numx++;
					}
					else if (board[a][b] == 'o'){
						numo++;
					}
					else if (board[a][b] == ' ' && (a != board.length - 1) || (b != board[0].length - 1)){
						numo = 0;
						numx = 0;
					}
				}
				if (numx >= k){
					return 'x';
				}
				else if (numo >= k){
					return 'o';
				}
			}
		}
		
		for (int i = 0; i < board.length;i++){
			for (int j = board[0].length - 1; j >= 0; j--){
			
				numx = 0;
				numo = 0;
				
				for(int a = i, b = j;(a < board.length - 1) && (b >= 0); a++, b--){
					if (board[a][b] == 'x'){
						numx++;
					}
					else if (board[a][b] == 'o'){
						numo++;
					}
					else if (board[a][b] == ' ' && (a != board.length - 1) || (b != 0)){
						numo = 0;
						numx = 0;
					}
				}
				if (numx >= k){
					return 'x';
				}
				if (numo >= k){
					return 'o';
				}
			}
		}
		
		return ' ';
	}
	
	public static void hSort(String[] stuff){
		int numSorted = 1;
		int spot = stuff.length - numSorted - 1;
		String temp = "";
		
		for (;numSorted < stuff.length;numSorted++){
			temp = stuff[stuff.length - numSorted - 1];
			int sorted = stuff.length - numSorted;
			spot = sorted - 1;
			
			
			for(;sorted < stuff.length;sorted++){
				
				if (temp.compareTo(stuff[sorted]) > 0){
					stuff[sorted - 1] = stuff[sorted];
					spot = sorted;
				}
			}
			stuff[spot] = temp;
			
			
		}
		
		for (String letter : stuff){
			System.out.print(letter);
		}
		System.out.println();
		
	}
}
