
/* Name: Olaolu Emmanuel
 * RUID: 159-00-3321
 */



public class IntNode {
	
         public int data;
         public IntNode next;
         
         public IntNode(int data, IntNode next) {
             this.data = data; this.next = next;
         }
         
         public String toString() {
             return data + "";
         }
         
         public static IntNode addBefore(IntNode front, int target, int newItem) {
             
        	 IntNode ptr = front; 
        	 IntNode prev = null;
        	 
        	 while (ptr != null){
        		 if (ptr.data == target){
        			 if (prev == null){
        				 return new IntNode(newItem, front);
        			 }
        			 else{
        				 prev.next = new IntNode(newItem, ptr);
        				 return front;
        			 }
        		 }
        		 
        		 prev = ptr;
        		 ptr = ptr.next;
        	 }
        	 
        	 return null;
       } 
         
       public static void deleteEveryOther(IntNode front) {
    	   
    	   IntNode ptr = front;
    	   IntNode prev = null;
    	   
    	   for(int i = 3;ptr != null;prev = ptr, ptr = ptr.next, i++){
    		   if(i%2 == 0){
    			   prev.next = ptr.next;
    		   }
    	   }
    	   
       }
       
       public static IntNode commonElements(IntNode frontL1, IntNode frontL2) {
    	   
    	   IntNode ptr1 = frontL1;
    	   IntNode ptr2 = frontL2;
    	   IntNode commonInts = null;
    	   IntNode ptr3 = null;
    	   
    	   while(ptr1 != null){
    		   while(ptr2 != null){
    			   if(ptr1.data == ptr2.data){
    				   if(commonInts != null){
    					   ptr3.next = new IntNode(ptr1.data, null);
    					   ptr3 = ptr3.next;
    					   break;
    				   }
    				   else{
    					   commonInts = new IntNode(ptr1.data, null);
    					   ptr3 = commonInts;
    				   }
    			   }
    			   else if(ptr2.data > ptr1.data){
    				   break;
    			   }
    			   
    			   ptr2 = ptr2.next;
    		   }
    		   ptr2 = frontL2;
    		   ptr1 = ptr1.next;
    	   }
    	   
    	   
    	   return commonInts;
        }
}
