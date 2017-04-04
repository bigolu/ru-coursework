
public class LinkedList<T> {
         public Node<T> front;
         int size;
         
         
         
         public boolean moveTowardFront(T target) {
        	Node<T> ptr = front;
        	Node<T> lag = null;
         	
        	while(ptr != null){
        		if(ptr.data == target){
        			break;
        		}
        		else{
        			lag = ptr;
        			ptr = ptr.next;
        		}
        	}
        	
        	if(ptr == null){
        		return false;
        	}
        	
        	if(lag == null){
        		return true;
        	}
        	
        	T temp = lag.data;
        	lag.data = ptr.data;
        	ptr.data = temp;
        	 
        	return true;
         }
         
         public String toString(){
        	 String result = "";
        	 
        	 Node<T> ptr = this.front;
        	 
        	 while(ptr != null){
        		 result = result + ptr.data;
        		 ptr = ptr.next;
        	 }
        	 
        	 return result;
         }
}
