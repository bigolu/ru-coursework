
public class Problem2Driver {

	public static void main(String[] args) {

		BSTree<Integer> tree = new BSTree();
		tree.root = new BSTNode(2);
		tree.root.left = new BSTNode(1);
		tree.root.right = new BSTNode(3);
		
		tree.insert(tree.root, 5);
		
		System.out.println(tree.root.right.right);
	}

}
