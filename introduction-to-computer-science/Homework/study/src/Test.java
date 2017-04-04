import java.util.HashMap;


public class Test{
	
	public static class Tree<T> {
	    private Node<T> root;

	    public Tree(T rootData) {
	        root = new Node<T>();
	        root.data = rootData;
	        //root.children = new ArrayList<Node<T>>();
	    }

	    public static class Node<T> {
	        private T data;
	        private Node<T> parent;
	        //private List<Node<T>> children;
	    }
	}
	
	public static void main(String[] args){
		HashMap<String, Integer> h = new HashMap<>();
		Tree<Integer> t = new Tree<Integer>(new Integer(5));
		System.out.println(hey());
	}
	
	public int hey(){
		return 5;
	}


    

}