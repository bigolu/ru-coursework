
public class DLLNode {
         public String data;
         public DLLNode prev, next;
         
         public DLLNode(String data, DLLNode next, DLLNode prev) {
             this.data = data; this.next = next; this.prev = prev;
         }
}