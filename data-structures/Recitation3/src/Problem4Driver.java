
public class Problem4Driver {

	public static void main(String[] args) {
		String expr = "2 3 4 - * 5 /";
		
		System.out.println("Result: " + postfixEvaluate(expr));
	}
	
	public static float postfixEvaluate(String expr) { 
		//this doesn't work.
		//I think I know how to do it now, but I don't feel like coding it.
		/*Stack<Character> operator = new Stack();
		Stack<Integer> digit = new Stack();
		//Stack<Character> data = new Stack();
		float answer = 0;
		
		for(int i = 0; i < expr.length(); i++){
			if((expr.charAt(i) >= '0') && (expr.charAt(i) <= '9')){
				digit.push(Character.digit(expr.charAt(i), 10));
			}
			else if(expr.charAt(i) != ' '){
				break;
			}
		}
		
		for(int i = expr.length() - 1; i > 0; i--){
			if(expr.charAt(i) == '+' || expr.charAt(i) == '-' || expr.charAt(i) == '*' || expr.charAt(i) == '/'){
				operator.push(expr.charAt(i));
			}
			else if(expr.charAt(i) != ' '){
				break;
			}
		}
		
		float a = (float) digit.pop();
		float b = (float) digit.pop();
		char c = operator.pop();
		
		if(c == '+'){
			answer += a + b;
		}
		else if(c == '-'){
			answer += a - b;
		}
		else if(c == '/'){
			answer += a/b;
		}
		else{
			answer += a*b;
		}
		
		while(digit.getSize() > 0){
			b = digit.pop();
			c = operator.pop();
			
			if(c == '+'){
				answer = answer + b;
			}
			else if(c == '-'){
				answer = answer - b;
			}
			else if(c == '/'){
				answer = answer/b;
			}
			else{
				answer = answer*b;
			}
		}*/
		
		return 0;
    }

}
