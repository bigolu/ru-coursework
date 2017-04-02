
public class problem2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public class BTNode<T>{
		public BTNode(){
			int left;
			int right;
		}
	}
	
	public static <T> boolean isomorphic(BTNode<T> T1, BTNode<T> T2) {
		if(T1 == null && T2 == null){
			return true;
		}
		if(T1 == null || T2 == null){
			return false;
		}
		
		if(!isomorphic(T1.left, T2.left)){
			return false;
		}
		else{
			return isomorphic(T1.right, T2.right);
		}
    }

}
