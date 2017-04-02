
public class Problem1Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DLLNode list = new DLLNode("a", null, null);
		DLLNode ptr = list;
		String data2= "";
		
		for(char data = 'b'; data <= 'z'; data++){
			ptr.next = new DLLNode(data2 + data, null, ptr);
			
			ptr = ptr.next;
		}
		ptr = list;
		
		while(ptr != null){
			if(ptr.data.equals("h")){
				break;
			}
			
			ptr = ptr.next;
		}
		
		printList(list);
		
		System.out.println();
		System.out.println("Target: " + ptr.data);
		
		printList(moveToFront(list, ptr));
		

	}
	
	public static DLLNode moveToFront(DLLNode front, DLLNode target) {
		DLLNode ptr = front;
		
		if(ptr == null){
			return ptr;
		}
		else if(ptr.next == null){
			return ptr;
		}
		else if(ptr == target){
			return ptr;
		}
		else if(target == null){
			return ptr;
		}
		
		while(ptr != null){
			if(ptr == target){
				break;
			}
			
			ptr = ptr.next;
		}
		
		if(ptr == null){
			return front;
		}
		
		if(ptr.next != null){
			ptr.next.prev = ptr.prev;
			ptr.prev.next = ptr.next;
			ptr.prev = null;
			ptr.next = front;
		}
		else{
			ptr.prev.next = ptr.next;
			ptr.prev = null;
			ptr.next = front;
		}
		
		return ptr;
    }
	
	public static void printList(DLLNode front){
		DLLNode ptr = front;
		
		while(ptr != null){
			System.out.print(ptr.data + "-->");
			
			ptr = ptr.next;
		}
		System.out.print("END");
		
	}
}
