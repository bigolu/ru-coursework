public class CellSim{
	public static void main(String[] args){
	
		/* The following two lines will prompt the user to indicate the dimensions for the square tissue grid
	    * and reads their input.
	    */
		
		System.out.print("Choose a dimension for the square tissue grid.");
		int n = IO.readInt();
		
		if (n<=1){ // the dimension of the grid cannot be lees than or equal to one
			System.out.print("Invalid Entry");
			System.exit(1);
		}
	
		char[][] tissue = new char[n][n]; //initializes the 2d array with the given dimensions
		
		System.out.print("What percentage of the tissue has blank spaces? (i.e. 50% = 50)");
		int percentBlank = IO.readInt();
		
		System.out.print("What percentage of the tissue has X's? (i.e. 50% = 50)");
		int percentX = IO.readInt();
		
		
		assignCellTypes(tissue, percentBlank, percentX); //calls method to randomly distribute x's, o's, and blanks
		printTissue(tissue); //calls method to print the 2d array
	}
	
	/**
	* Given a tissue sample, prints the cell make up in grid form
	*
	* @param tissue a 2-D character array representing a tissue sample
	* 
	***/
	public static void printTissue(char[][] tissue){

		/* I initialize row and col to keep track of the row and column index that I am currently 
		 * printing to in my nested for loop. They also serve as the indices for the row and column of my array.
		 */
		
		int row = 0;
		int col = 0;
		
		// Below I have a nested for loop which will print the 2d array tissue. 
		//Both loops will run until the last row and column index respectively. 
		//After each iteration, I add one to the control variable to move to the next row and column respectively.
		
		/* The outer for loop uses the variable row to control which row I am printing to */
		
		for (;row<tissue.length;row++){
			
			/* The inner for loop uses the variable col to control which column I am printing to.
			 * I initialize col to 0 in this loop so that the column index resets to 0 when I go to the next row.
			 */
			
			for (col = 0;col<tissue[0].length;col++){
				System.out.print(tissue[row][col]); // Prints that index of the array
			}
			System.out.println(); // Once one row has been printed, this allows me to go to the next line
		}
	}
	
	/**
	* Given a blank tissue sample, populate it with the correct cell makeup given the parameters. 
	* Cell type 'X' will be represented by the character 'X'
	* Cell type 'O' will be represented by the character 'O'
	* Vacant spaces will be represented by the character ' '
	*
	* Phase I: alternate X and O cells throughout, vacant cells at the "end" (50% credit)
	*		e.g.:	'X' 'O' 'X' 'O' 'X'
	*				'O' 'X' 'O' 'X' 'O'
	*				'X' 'O' 'X' 'O' 'X'
	*				' ' ' ' ' ' ' ' ' '
	*				' ' ' ' ' ' ' ' ' '
	*
	* Phase II: Random assignment of all cells (100% credit)
	*
	* @param tissue a 2-D character array that has been initialized
	* @param percentBlank the percentage of blank cells that should appear in the tissue
	* @param percentX Of the remaining cells, not blank, the percentage of X cells that should appear in the tissue. Round up if not a whole number
	*
	**/
	public static void assignCellTypes(char[][] tissue, int percentBlank, int percentX){
		
		/* numOfX - amount of x's in the grid, numOfO - amount of o's in the grid, numOfBlank - amount of blanks in the grid
		 * r - a random number used to assign x's , o's, and spaces to indices in the array
		 * row and col - indices of the 2d array tissue
		 */
		
		double numOfX, numOfO, numOfBlank, r;  
		int row, col;
		
		/* The two following if statements check for error conditions.
		 * first one - checks if total percent is over 100 or if the percentage of blanks is is <= 0
		 * or if the percentage of x's is a negative. (I only check if percentX is negative because
		 * technically there doesn't have to be x's so it could equal 0.)
		 * 
		 * second one - checks if the dimensions of the grid are equal because it needs to be an n by n square
		 */
		
		if ((percentBlank<=0)||(percentX<0)||(percentBlank>100)||(percentX>100)){
			System.out.print("invalid percentages");
			System.exit(1);
		}
		
		if (tissue.length != tissue[0].length){
			System.out.println("invalid dimensions");
			System.exit(1);
		}
			
		/* the following three assignments calculate the amount of x's o's and spaces by doing the following:
		 * first, it takes the percentage and divides it by 100 to make it a decimals. Then it multiplies it by the amount
		 * of tiles. Since there is no percentage for o's, the amount of o's is equal to the total amount of tiles
		 * minus the amount of x's and spaces.
		 */
		
		numOfBlank = Math.ceil(((double) percentBlank/100.0)*( (double) tissue.length* (double) tissue.length));
		numOfX = Math.ceil(((double) percentX/100.0)*( ((double) tissue.length* (double) tissue.length)-(numOfBlank)));
		numOfO = (((double) tissue.length* (double) tissue.length)-(numOfBlank + numOfX));
		
		/* The following assignments set up choices for my randomizer to choose from depending on which elements
		 * I still have at least one of
		 */
		
		String xoblank = "XO ";
		String xo = "XO";
		String xblank = "X ";
		String oblank = "O ";
		
		/* How the while loop works:
		 * First it checks how which elements I have at least one of.
		 * 
		 * Then it starts a for loop which will check if the value at tissue[row][col] (row and col have been assigned to 
		 * random numbers between 0 and the last index of the array) equals '\u0000' which
		 * is the character form of null.(I check for this because when the array is first made, the default value for
		 * all indices is '\u0000'. This way I can tell if I already put something in that spot. Once a free spot
		 * is found, it breaks out of the for loop.
		 * 
		 * Next, r is assigned to a random integer between 0 and 2 or 0 and 3 depending on how many types of elements I
		 * have next. I do this because r represents an index in a string that contains my choices of x's, o's, or spaces
		 * that I have to choose from. Once, I have the random index, I get the character at that index and assign it
		 * to the free spot that I found earlier.
		 * 
		 * After that, I check to see which character I put in that free spot and depending on the character
		 * I subtract one from the amount of that character that I have left to assign to cells.
		 * 
		 * Finally, I check to see if the amount of all of my remaining characters is equal to zero because
		 * if that is true then that means I have assigned all of the cells and spaces. If true, the program breaks from the 
		 * main while loop and ends the method.
		 * 
		 * note - I subtract .0000000001 from the random numbers that I generate because if the number ever equals its upper
		 * bound then I would get an ArrayIndexOutOfBounds error
		 * 		for example: When picking a random spot in the grid, I have the row and col set equal to a random
		 * 		number between 0 and n and then cast it as an integer to remove the decimal. n is not in the
		 * 		bounds of the array so I subtract a very small number so that the most the random number can equal is
		 * 		6.999999999 which will become 6 when I cast is an integer.
		 */
		
		main: while (true){
		
		if ((numOfX>0)&&(numOfO>0)&&(numOfBlank>0)){
			for (;;){
				row = (int) (Math.random()*(tissue.length-.00000000000000000001));
				col = (int) (Math.random()*(tissue.length-.00000000000000000001));
				if (tissue[row][col] == '\u0000')
					break;
			}
			r = (int) (((Math.random()*(3-1)) + 1)-.00000000000000000001);
			tissue[row][col] = xoblank.charAt((int) r);
			if(tissue[row][col] == 'X')
				numOfX--;
			else if(tissue[row][col] == 'O')
				numOfO--;
			else
				numOfBlank--;
		}
		else if ((numOfX>0)&&(numOfO>0)){
			for (;;){
				row = (int) (Math.random()*(tissue.length-.00000000000000000001));
				col = (int) (Math.random()*(tissue.length-.00000000000000000001));
				if (tissue[row][col] == '\u0000')
					break;
			}
			r = (int) (((Math.random()*(2-1)) + 1)-.00000000000000000001);
			tissue[row][col] = xo.charAt((int) r);
			if(tissue[row][col] == 'X')
				numOfX--;
			else
				numOfO--;
		}
		else if ((numOfX>0)&&(numOfBlank>0)){
			for (;;){
				row = (int) (Math.random()*(tissue.length-.00000000000000000001));
				col = (int) (Math.random()*(tissue.length-.00000000000000000001));
				if (tissue[row][col] == '\u0000')
					break;
			}
			r = (int) (((Math.random()*(2-1)) + 1)-.00000000000000000001);
			tissue[row][col] = xblank.charAt((int) r);
			if(tissue[row][col] == 'X')
				numOfX--;
			else
				numOfBlank--;
		}
		else if ((numOfO>0)&&(numOfBlank>0)){
			for (;;){
				row = (int) (Math.random()*(tissue.length-.00000000000000000001));
				col = (int) (Math.random()*(tissue.length-.00000000000000000001));
				if (tissue[row][col] == '\u0000')
					break;
			}
			r = (int) (((Math.random()*(2-1)) + 1)-.00000000000000000001);
			tissue[row][col] = oblank.charAt((int) r);
			if(tissue[row][col] == 'O')
				numOfO--;
			else
				numOfBlank--;
		}
		else if (numOfX>0){
			for (;;){
				row = (int) (Math.random()*(tissue.length-.00000000000000000001));
				col = (int) (Math.random()*(tissue.length-.00000000000000000001));
				if (tissue[row][col] == '\u0000')
					break;
			}
			tissue[row][col] = 'X';
			numOfX--;
		}
		else if (numOfO>0){
			for (;;){
				row = (int) (Math.random()*(tissue.length-.00000000000000000001));
				col = (int) (Math.random()*(tissue.length-.00000000000000000001));
				if (tissue[row][col] == '\u0000')
					break;
			}
			tissue[row][col] = 'O';
			numOfO--;
		}
		else if (numOfBlank>0){
			for (;;){
				row = (int) (Math.random()*(tissue.length-.00000000000000000001));
				col = (int) (Math.random()*(tissue.length-.00000000000000000001));
				if (tissue[row][col] == '\u0000')
					break;
			}
			tissue[row][col] = ' ';
			numOfBlank--;
		}
		if ((numOfX<=0)&&(numOfO<=0)&&(numOfBlank<=0))
			break main;
		}
	}
}