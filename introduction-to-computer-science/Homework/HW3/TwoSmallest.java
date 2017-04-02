public class TwoSmallest {

    public static void main(String[] args) {
	
	int end=-1, lowestValue = -1, secondLowest=-1, n=-1, count=-1, temp;
	temp = 0;
	
	System.out.print ("Enter a terminating value: ");
	end = IO.readInt ();
	System.out.print ("Enter a number: ");
	n = IO.readInt ();
	
	if (n == end)
	{
		IO.reportBadInput();
		System.out.print ("(You must give at least two values to have a lowest and second lowest value)");
		return;
	}
	for (count=0; n!=end; count++)
	{
		if (count==0)
		{ 	
			lowestValue = n;
		}
		 
		if (count ==1)
		{
			System.out.print ("Enter a number: ");
			secondLowest = IO.readInt ();
				if (secondLowest == end)
				{
					IO.reportBadInput();
					System.out.print ("(You must give at least two values to have a lowest and second lowest value)");
					return;
				}
				else 
					if (secondLowest < lowestValue)
					{
						temp = lowestValue;
						lowestValue = secondLowest;
						secondLowest = temp;
					}
		}	
		if(count >1)
		{		
			if (n < lowestValue)
			{
				secondLowest = lowestValue;
				lowestValue = n;
			}
			 else
				if (n < secondLowest)
					secondLowest = n;	
			System.out.print ("Enter a number: ");
			n = IO.readInt ();
			}
		}
	IO.outputIntAnswer (lowestValue);
	IO.outputIntAnswer (secondLowest);
	
	}

	
	}
