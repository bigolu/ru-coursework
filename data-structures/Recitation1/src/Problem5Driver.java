
public class Problem5Driver {

	public static void main(String[] args) {

		IntNode one =  new IntNode(1, new IntNode(5, new IntNode(7, new IntNode(9, null))));
		print(one);
		System.out.println();
		
		IntNode two =  new IntNode(2, new IntNode(5, new IntNode(7, new IntNode(19, null))));
		print(two);
		System.out.println();
		
		print(IntNode.commonElements(one, two));
		
		
	}
	
	public static void print(IntNode llist){
		IntNode ptr = llist;
		while(ptr != null){
			System.out.print(ptr + " --> ");
			ptr = ptr.next;
		}
		if (ptr == null)
		System.out.print("END");
	}

}
