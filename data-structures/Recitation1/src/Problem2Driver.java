
public class Problem2Driver {
	
	public static void main(String[] args) {

			StringNode llist = new StringNode("red", new StringNode("blue", new StringNode("blue", new StringNode("red", new StringNode("green", null)))));
			print(llist);
			
			System.out.println(StringNode.numberOfOccurrences(null, "green"));
	}
	
	public static void print(StringNode llist){
		StringNode ptr = llist;
		while(ptr != null){
			System.out.println(ptr);
			ptr = ptr.next;
		}
	}

}
