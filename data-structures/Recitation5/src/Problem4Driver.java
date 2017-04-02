
public class Problem4Driver {

	public static void main(String[] args) {
		LinkedList<Integer> nums = new LinkedList<Integer>();
		
		nums.front = new Node<Integer>(1, new Node<Integer>(2, new Node<Integer>(3, new Node<Integer>(4, new Node<Integer>(5, null)))));
		System.out.println(nums);
		nums.moveTowardFront(4);
		System.out.println(nums);

	}

}
