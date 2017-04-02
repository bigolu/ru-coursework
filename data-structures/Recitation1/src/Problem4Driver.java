
public class Problem4Driver {

	public static void main(String[] args) {
		
		StringNode llist = new StringNode("red", new StringNode("blue", new StringNode("blue", new StringNode("red", new StringNode("green", null)))));
		print(llist);
		
		System.out.println();
		
		//llist = new StringNode("red", null);
		
		print(StringNode.deleteAllOccurrences(null, "purple"));
	}
	
	public static void print(StringNode llist){
		StringNode ptr = llist;
		while(ptr != null){
			System.out.print(ptr + " --> ");
			ptr = ptr.next;
		}
		System.out.print("END");
	}

}
