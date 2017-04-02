public class Taxes {

    public static void main(String[] args) {
	
	System.out.print ("Enter your taxable income: $");
	int income = IO.readInt ();
	double total = 0;
		if (income < 0) {
			IO.reportBadInput ();
			return;
			}
		else if (income == 0){
			IO.outputIntAnswer (0);
			}
		else if (income >= 82000)
				total += ((8000*.10)+(26000*.15)+(48000*.25)+((income-82000)*.35));
			else if ((income <= 82000) && (income >= 34000))
				total += ((8000*.10)+(26000*.15)+((income-34000)*.25));
			else if ((income <= 34000) && (income >= 8000))
				total += ((8000*.10)+((income - 8000)*.15));
			else
				total += (income*.10);
				
		IO.outputDoubleAnswer (total);
				

	

		}

	
	}
