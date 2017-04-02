
public class StringNode {
	
         public String data;
         public StringNode next;
         
         public StringNode(String data, StringNode next) {
             this.data = data; this.next = next;
         }
         
         public String toString() {
             return data;
         }
         
         public static int numberOfOccurrences(StringNode front, String target) {
        	 
        	 int count = 0;
        	 StringNode ptr = front;
        	 
        	 while(ptr != null){
        		 if(ptr.data.equals(target)){
        			 count++;
        		 }
        		 
        		 ptr = ptr.next;
        	 }
        	 
        	 return  count;
         } 
         
         public static StringNode deleteAllOccurrences(StringNode front, String target) {
        	 
        	 /*StringNode ptr = front;
        	 StringNode prev = null;
        	 boolean wasFirstElement = false; // the first element was the target
        	 
        	 while(ptr != null){
        		 wasFirstElement = false;
        		 
        		 if(ptr.data.equals(target)){
        			 if(prev == null){
        				 front = front.next;
        				 wasFirstElement = true;
        			 }
        			 else{
        				 prev.next = ptr.next;
        			 }
        		 }
        		 
        		 if(wasFirstElement){
        			 ptr = ptr.next;
        		 }
        		 else{
        			 if(!ptr.data.equals(target)){
        				 prev = ptr;
        			 }
        			 ptr = ptr.next;
        		 }
        	 }*/
        	 
        	 StringNode ptr = front;
        	 StringNode lag = null;
        	 if(ptr == null){
        		 return front;
        	 }
        	 
        	 while(true){
        		 if(ptr != null){
        			 if(ptr.data.equals(target)){
        				 front = front.next;
        				 ptr = front;
        			 }
        			 else{
        				 break;
        			 }
        		 }
        		 
        	 }
        	 
        	 while(ptr != null){
        		 if(ptr.data.equals(target)){
        			 lag.next = ptr.next;
        			 ptr = ptr.next;
        		 }
        		 else{
        			 lag = ptr;
        			 ptr = ptr.next;
        		 }
        	 }
        	 
        	 
        	 
        	 
        	 return front;
         } 
}
