public class Poly {

    public static void main(String[] args) {
		 int a,b,c ;
		 System.out.print ("Enter the first root") ;
		 a = IO.readInt () ;
		 System.out.print ("Enter the second root") ;
		 b = IO.readInt () ;
		 System.out.print ("Enter the third root") ;
		 c = IO.readInt () ;
		 
		 if (a==0 && b==0 && c==0){
			System.out.println ("The Polynomial is:" + "\n" + "x^3");
		 }
		 else {
			System.out.println ("The Polynomial is:" + "\n" + "x^3 " + "+ " + ((-1*c)-b-a) + "x^2 " + "+ " + ((b*c)+(c*a)+(a*b)) + "x " + "- " + (a*b*c)) ;
		}
    }

}
