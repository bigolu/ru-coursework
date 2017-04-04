public class NthPrime {

    public static void main(String[] args) {
     
	System.out.print("Enter which prime number you want (i.e. 3 = third prime number): ");
	int nthPrimeNumber = IO.readInt();
	
	if(nthPrimeNumber < 1) {
			IO.reportBadInput();
			return;
		}
	int divisor, count;
	int num=1;
	for (count=0; count<nthPrimeNumber;)
		{
			
	num=num+1; 
	for (divisor = 2; divisor <= num; divisor++){
				
	if (num%divisor == 0) {
	break; 
		}
	}
	if (divisor == num){
				
	count = count+1; 
		}
	}
	
	IO.outputIntAnswer(num);
	
	}

	
}

		