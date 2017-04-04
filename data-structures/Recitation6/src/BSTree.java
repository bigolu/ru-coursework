
public class BSTree<T extends Comparable<T>> {
	BSTNode<T> root;
	int size;
	
	public BSTree(){
		root = null;
		size = 0;
	}
	
	public void insert(BSTNode<T> root, T data) throws IllegalArgumentException{
		if(root == null){
			this.root = new BSTNode<T>(data);
			return;
		}
		
		int c = data.compareTo(root.data);
		
		if(c == 0){
			throw new IllegalArgumentException("Item already exists in tree!");
		}
		else if(c < 0){
			if(root.left == null){
				root.left = new BSTNode<T>(data);
				size++;
				return;
			}
			else{
				insert(root.left, data); 
			}
		}
		else{
			if(root.right == null){
				root.right = new BSTNode<T>(data);
				size++;
				return;
			}
			else{
				insert(root.right, data);
			}
		}
	}
	
}
