
public class Problem1Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		IntNode linkedList = new IntNode(1, null);
		IntNode ptr = linkedList;
		
		for(int i = 2; i < 6; i++){
			ptr.next = new IntNode(i, null);
			ptr = ptr.next;
		}
		
		/*while (ptr != null){
			System.out.println(ptr);
			ptr = ptr.next;
		}*/
		
		linkedList = IntNode.addBefore(linkedList, 1, 0);
		ptr = linkedList;
		
		while (ptr != null){
			System.out.println(ptr);
			ptr = ptr.next;
		}
		
		/*if (IntNode.addBefore(linkedList, 6, 8) == null){
			System.out.println("success!");
		}*/
		
	}

}
