import java.util.NoSuchElementException;


public class Problem1Driver {

	public static void main(String[] args) {
		Stack<String> alphabet = new Stack<>();
		
		//alphabet.pop();
		
		for(char data = 'a'; data <= 'z'; data++){
			alphabet.push("" + data);
		}
		
		System.out.println("Stack before count: " + "\n" + alphabet);
		System.out.println("Size of the stack: " + size(alphabet));
		System.out.println("Stack after count: " + "\n" + alphabet);
	}
	
	public static <T> int size(Stack s){
		Stack<T> tempStack = new Stack();
		int counter = -1;
		
		while(true){
			counter++;
			try{
				tempStack.push((T) s.pop());
			}
			catch (NoSuchElementException e){
				break;
			}
		}
		
		while(true){
			try{
				s.push(tempStack.pop());
			}
			catch (NoSuchElementException e){
				break;
			}
		}
		
		return counter;
	}

}
