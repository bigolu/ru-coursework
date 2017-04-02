
public class Problem3Driver {

	public static void main(String[] args) {

		System.out.println("Test Case 1");
		IntNode madNumbers = new IntNode(1, new IntNode(2, new IntNode(3, new IntNode(4, new IntNode(5, new IntNode(6, null))))));
		print(madNumbers);
		
		System.out.println();
		
		IntNode.deleteEveryOther(madNumbers);
		print(madNumbers);
		
		System.out.println();
		
		
		System.out.println("Test Case 2");
		madNumbers = new IntNode(1, new IntNode(2, null));
		print(madNumbers);
		
		System.out.println();
		
		IntNode.deleteEveryOther(madNumbers);
		print(madNumbers);
		
		System.out.println();
		
		
		System.out.println("Test Case 3");
		madNumbers = new IntNode(1, null);
		print(madNumbers);
		
		System.out.println();
		
		IntNode.deleteEveryOther(madNumbers);
		print(madNumbers);
		
		System.out.println();
		
		
		System.out.println("Test Case 4");
		madNumbers = null;
		
		IntNode.deleteEveryOther(madNumbers);
		
		if (madNumbers == null){
			System.out.println("success!");
		}
		
	}

	public static void print(IntNode llist){
		IntNode ptr = llist;
		while(ptr != null){
			System.out.print(ptr + " --> ");
			ptr = ptr.next;
		}
		System.out.print("END");
	}
}
