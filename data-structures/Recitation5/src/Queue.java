import java.util.NoSuchElementException;


public class Queue<T> {    
	Node<T> rear;
	private int size;

	public Queue() { 
		rear = null; 
		size = 0;
	}
	
	public void enqueue(T obj) { 
		if(rear == null){
			rear = new Node<T>(obj, null);
			rear.next = rear;
			size++;
		}
		else{
			Node<T> ptr = rear.next;
			
			while(ptr != rear){
				ptr = ptr.next;
			}
			
			ptr.next = new Node<T>(obj, rear.next);
			rear = ptr.next;
			size++;
		}
	}
	
	public T dequeue() throws NoSuchElementException { 
		if(rear == null){
			throw new NoSuchElementException("This Queue is already empty!");
		}
		
		Node<T> ptr = rear;
		
		if(ptr.next == rear){
			T temp = ptr.data;
			rear = null;
			size--;
			return temp;
		}
		else{
			T temp = ptr.next.data;
			ptr.next = ptr.next.next;
			size--;
			return temp;
		}
		
		
	}
	
	public boolean isEmpty() { 
		if(size == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public int size() { 
		return size;
	}
	
	// extract the even position items from this queue into
    // the result queue, and delete them from this queue
    public Queue<T> evenSplit() {
    	Queue<T> even = new Queue<T>();
    	int size = this.size();
    	
    	while(size != 0){
			this.enqueue(this.dequeue());
			size--;
			
			if(size != 0){
				even.enqueue(this.dequeue());
				size--;
    		}
    	}
    	
    	return even;
    }
    
    public String toString(){
    	if(this.rear == null){
    		return "";
    	}
    	
    	String ret = "";
    	Node<T> ptr = rear.next;
    	
    	while(ptr != rear){
    		ret += ptr.data;
    		
    		ptr = ptr.next;
    	}
    	
    	ret += this.rear.data;
    	
    	return ret;
    }
}
