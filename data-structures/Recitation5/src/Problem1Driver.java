
public class Problem1Driver {

	public static void main(String[] args) {
		Queue<Integer> nums = new Queue<Integer>();
		nums.enqueue(1);
		nums.enqueue(2);
		nums.enqueue(3);
		nums.enqueue(4);
		System.out.println(peek(nums));
		System.out.println(nums);
	}
	
	
	
	public static <T> T peek(Queue<T> q){
		if(q.size() == 0){
			return null;
		}
		
		T result = q.dequeue();
		q.enqueue(result);
		
		int size = q.size() - 1;	
		
		for(int i = 0; i < size; i++){
			q.enqueue(q.dequeue());
		}
		
		return result;
	}

}
