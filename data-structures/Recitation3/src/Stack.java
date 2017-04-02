import java.util.NoSuchElementException;


public class Stack<T> {
       
       private Node<T> first;
       private int size;
       
       public Stack() {
    	   this.first = null;
    	   size = 0;
       }
       
       public void push(T item) {
    	   if(this.first == null){
    		   this.first = new Node<T>(item, null);
    		   size++;
    	   }
    	   else{
    		   Node<T> ptr = first;
    		   
    		   while(ptr.next != null){
    			   ptr = ptr.next;
    		   }
    		   
    		   ptr.next = new Node<T>(item, null);
    		   size++;
    	   }
       }
       
       public T pop() throws NoSuchElementException { 
    	   try{
	    	   if(this.first == null){
	    		   throw new NoSuchElementException();
	    	   }
    	   }
    	   catch(NoSuchElementException e){
    		   throw new NoSuchElementException("There is nothing in this stack!");
    	   }
    	   
    	   size--;
		   Node<T> ptr = first;
		   Node<T> prev = null;
		   
		   while(ptr.next != null){
			   prev = ptr;
			   ptr = ptr.next;
		   }
		   
		   if(prev == null){
			   T data = ptr.data;
			   this.first = null;
			   return data;
		   }
		   else{
			   T data = ptr.data;
			   prev.next = null;
			   return data;
		   }
       }
       
       public boolean isEmpty() {
    	   if(this.first == null){
    		   return true;
    	   }
    	   else{
    		   return false;
    	   }
	   }    
       
       public String toString() {
    	   String data = "";
    	   Node<T> ptr = this.first;
    	   
    	   while(ptr != null){
    		   data += ptr.data;
    		   ptr = ptr.next;
    	   }
    	   
    	   return data;
       }
       
       public T peek(){
    	   if(this.first == null){
    		   return null;
    	   }
    	   
    	   Node<T> ptr = this.first;
    	   
    	   while(ptr.next != null){
    		   ptr = ptr.next;
    	   }
    	   return ptr.data;
       }
       
       public void clear(){
    	   this.first = null;
    	   size = 0;
       }
       
       public int getSize(){
    	   return this.size;
       }
}
