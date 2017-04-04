import java.util.NoSuchElementException;


public class Problem2Driver {

	public static void main(String[] args) {
		Queue<String> line = new Queue<>();
		
		try{
			line.evenSplit();
		}
		catch(NoSuchElementException e){
			System.out.println("I messed up!");
		}
			
	}

}
