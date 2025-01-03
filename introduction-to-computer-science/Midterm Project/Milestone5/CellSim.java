import java.awt.Color;
public class CellSim{
	public static void main(String[] args){
	
		/*The following lines will prompt the user to indicate their parameters
	    * and read their input.
	    */
		
		System.out.print("Choose a dimension for the square tissue grid.");
		int n = IO.readInt();
		
		if (n<=1){ // the dimension of the grid cannot be lees than or equal to one
			System.out.print("Invalid Entry");
			System.exit(1);
		}
	
		System.out.print("Choose a threshold.");
		int threshold = IO.readInt();
		
		if (threshold < 0 || threshold > 100){
			System.out.println("Invalid threshold");
			System.exit(1);
		}
			
		System.out.print("Choose Maximum number of rounds to be iterated.");
		int maxRounds = IO.readInt();
		
		if (maxRounds <= 0){
			System.out.println("Invalid maxRounds");
			System.exit(1);
		}
		
		System.out.print("Choose a frequency at which the tissue is printed.");
		int frequency = IO.readInt();
		
		if (frequency < 0){
			System.out.println("Invalid frequency");
			System.exit(1);
		}
		
		System.out.print("What percentage of the tissue has blank spaces? (i.e. 50% = 50)");
		int percentBlank = IO.readInt();
		
		if (percentBlank <= 0 || percentBlank > 100){
			System.out.println("Invalid percentBlank");
			System.exit(1);
		}
		
		System.out.print("What percentage of the tissue has X's? (i.e. 50% = 50)");
		int percentX = IO.readInt();
		
		if (percentX < 0 || percentX > 100){
			System.out.println("Invalid percentX");
			System.exit(1);
		}
		
		System.out.println();
		
		char[][] initial = new char[n][n];
		int numRounds = 0;
		int totalNumMoved = 0;
		boolean satisfied = false;
		double numMoved = 0;
		
		char[][] tissue = new char[n][n]; //initializes the 2d array with the given dimensions
		assignCellTypes(tissue, percentBlank, percentX); //calls method to randomly distribute x's, o's, and blanks
		CellSimGUI visualTissue = new CellSimGUI(n, 0);
		initialVisual(visualTissue, tissue);
		visualTissue.delay = 100;
		
		for(int row = 0;row<tissue.length;row++){ // make copy of the initial array so I can print it at the end
    		for(int col = 0;col<tissue[0].length;col++){
    			initial[row][col] = tissue[row][col];
    		}
    	}
		
		for(int row = 0;row<initial.length;row++){ // make copy of the initial array so I can print it at the end
    		for(int col = 0;col<initial[0].length;col++){
    			initial[row][col] = tissue[row][col];
    		}
    	}
		
		
		
		while (true){
			if (boardSatisfied(tissue, threshold) || (numRounds >= maxRounds)){ //if board is satisfied or maxrounds is met, the simulation ends
				satisfied = boardSatisfied(tissue, threshold);
				endSimulation(initial, tissue, satisfied, numRounds, numMoved, totalNumMoved);
			}
			
			if (frequency != 0 && numRounds != 0){ //if numRounds%frequency = 0 then the frequency is a multiple of the the number of rounds
				if (numRounds%frequency == 0)      //so the board should be printed
					printTissue(tissue);
			}
			
			numMoved = moveAllUnsatisfied(tissue, visualTissue, threshold); //moves unsatisfied and adds the number moved to total movements
			totalNumMoved += numMoved;                        //so I can print total movements at the end
			numRounds++;
		}
		
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
		
		System.out.println();
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
	/**
	    * Given a tissue sample, and a (row,col) index into the array, determines if the agent at that location is satisfied.
	    * Note: Blank cells are always satisfied (as there is no agent)
	    *
	    * @param tissue a 2-D character array that has been initialized
	    * @param row the row index of the agent
	    * @param col the col index of the agent
	    * @param threshold the percentage of like agents that must surround the agent to be satisfied
	    * @return boolean indicating if given agent is satisfied
	    *
	    **/
	    public static boolean isSatisfied(char[][] tissue, int row, int col, int threshold){
	    	/* The if statement below check to see if there are any invalid entries such as a threshold below 0 or greater than
	    	 * 100 etc.... I consider an array with unequal dimensions to be invalid because it must be
	    	 * an n by n grid. I also consider a single square to be invalid despite the fact that its dimensions are equal
	    	 * because there would be no room to move an unsatisfied cell.
	    	 */
	    	
	    	if ((threshold<0)||(threshold>100)||(row<0)||(row>tissue.length)||(col<0)||(col>tissue[0].length)
	    	||(tissue.length != tissue[0].length)||(tissue.length<=1)||(tissue[0].length<=1)){
	    		System.out.print("Invalid Entry");
	    		System.exit(1);
	    	}
	    	
	    	/* Below, I have a series of if statements that do the following:
	    	 * - The first four check to see if the index is located at a corner of the array because if it
	    	 * is, I can only check certain surrounding areas without going out of bounds.
	    	 * 
	    	 * - After that, I check to see if the index is on the edges of the array because I would only be able
	    	 * to check certain surrounding areas without going out of bounds for these cases as well.
	    	 * 
	    	 * - Finally, once I have determined that is is not along the edges of the array or a corner of the array,
	    	 *  I will check all surrounding indexes.
	    	 *  
	    	 *  The way that I determine satisfaction is as follows:
	    	 *  - I look at the surrounding indexes and if they are equal to my initial index, then I add one to
	    	 *  the variable numSame which counts how many neighbors are equivalent to the initial.
	    	 *  
	    	 *  - If it is not equivalent then I check if it is not a space. I do so because if it is not a space
	    	 *  or the same cell type, then it must be the other cell type. If this is true then I add one to the
	    	 *  total counter which holds how many neighbors the initial cell has.
	    	 *  
	    	 *  - After that, I check to see if total = 0 because this means that all the cell's neighbors are all spaces
	    	 *  which means that it is satisfied so I return true if this case is true.
	    	 *  
	    	 *  - If total != 0, then I divide numSame by total to get the percentage of neighbors which are the same
	    	 *  and I compare that to the the threshold (I divide the threshold by 100 to get the decimal form
	    	 *  of the threshold percentage). If it is >= threshold then I return true. If not, I return false.
	    	 */
	    	
	    	
	    	
	    	if (tissue[row][col]==' ')
	    		return (true);
	    	
	    	if ((row==0)&&(col==0)){
	    		double numSame = 0;
	    		double total = 0;
	    		
	    		if (tissue[row][col]==tissue[row][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row][col+1] != ' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row+1][col]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][col]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row+1][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][col+1]!=' ')
	    			total++;
	    		if (total==0.0){
	    			return (true);
	    		}
	    		
	    		if ((numSame/total)>=( (double) threshold/100))
	    			return (true);
	    		
	    		else{
	    			return (false);
	    		}
	    	}
	    	
	    	else if ((row==0)&&(col==(tissue[0].length-1))){
	    		double numSame = 0;
	    		double total = 0;
	    		
	    		if (tissue[row][col]==tissue[row][tissue[0].length-2]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row][tissue[0].length-2]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row+1][tissue[0].length-1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][tissue[0].length-1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row+1][tissue[0].length-2]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][tissue[0].length-2]!=' ')
	    			total++;
	    		
	    		if (total==0)
	    			return (true);
	    		
	    		if ((numSame/total)>=( (double) threshold/100))
	    			return (true);
	    		
	    		else
	    			return (false);
	    	}
	    	
	    	else if ((row==(tissue.length-1))&&(col==(tissue[0].length-1))){
	    		double numSame = 0;
	    		double total = 0;
	    		
	    		if (tissue[row][col]==tissue[row][tissue[0].length-2]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row][tissue[0].length-2]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row-1][tissue[0].length-1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row-1][tissue[0].length-1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row-1][tissue[0].length-2]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row-1][tissue[0].length-2]!=' ')
	    			total++;
	    		
	    		if (total==0)
	    			return (true);
	    		
	    		if ((numSame/total)>=( (double) threshold/100))
	    			return (true);
	    		
	    		else
	    			return (false);
	    	}
	    	
	    	else if ((row==(tissue.length-1))&&(col==0)){
	    		double numSame = 0;
	    		double total = 0;
	    		
	    		if (tissue[row][col]==tissue[tissue.length-1][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[tissue.length-1][col+1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[tissue.length-2][col]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[tissue.length-2][col]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[tissue.length-2][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[tissue.length-2][col+1]!=' ')
	    			total++;
	    		
	    		if (total==0)
	    			return (true);
	    		
	    		if ((numSame/total)>=( (double) threshold/100))
	    			return (true);
	    		
	    		else
	    			return (false);
	    	}
	    	
	    	else if (col==0){
	    		double numSame = 0;
	    		double total = 0;
	    		
	    		if (tissue[row][col]==tissue[row-1][col]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row-1][col]!=' ')
	    			total++;
	    	
	    		if (tissue[row][col]==tissue[row-1][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row-1][col+1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row][col+1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row+1][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][col+1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row+1][col]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][col]!=' ')
	    			total++;
	    	
	    		if (total==0)
	    			return (true);
	    		
	    		if ((numSame/total)>=( (double) threshold/100))
	    			return (true);
	    		
	    		else
	    			return (false);
	    	}
	    	
	    	else if (col==(tissue[0].length-1)){
	    		double numSame = 0;
	    		double total = 0;
	    		
	    		if (tissue[row][col]==tissue[row-1][col]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row-1][col]!=' ')
	    			total++;
	    	
	    		if (tissue[row][col]==tissue[row-1][tissue[0].length-2]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row-1][tissue[0].length-2]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row][col-1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row][col-1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row+1][col-1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][col-1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row+1][col]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][col]!=' ')
	    			total++;
	    	
	    		if (total==0)
	    			return (true);
	    		
	    		if ((numSame/total)>=( (double) threshold/100))
	    			return (true);
	    		
	    		else
	    			return (false);
	    	}
	    	
	    	else if (row==0){
	    		double numSame = 0;
	    		double total = 0;
	    		
	    		if (tissue[row][col]==tissue[row][col-1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row][col-1]!=' ')
	    			total++;
	    	
	    		if (tissue[row][col]==tissue[row+1][col-1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][col-1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row+1][col]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][col]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row+1][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][col+1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row][col+1]!=' ')
	    			total++;
	    	
	    		if (total==0)
	    			return (true);
	    		
	    		if ((numSame/total)>=( (double) threshold/100))
	    			return (true);
	    		
	    		else
	    			return (false);
	    	}
	    	
	    	else if (row==(tissue.length-1)){
	    		double numSame = 0;
	    		double total = 0;
	    		
	    		if (tissue[row][col]==tissue[row][col-1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row][col-1]!=' ')
	    			total++;
	    	
	    		if (tissue[row][col]==tissue[row-1][col-1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row-1][col-1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row-1][col]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row-1][col]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row-1][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row-1][col+1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row][col+1]!=' ')
	    			total++;
	    	
	    		if (total==0)
	    			return (true);
	    		
	    		if ((numSame/total)>=( (double) threshold/100))
	    			return (true);
	    		
	    		else
	    			return (false);
	    	}
	    	
	    	else {
	    		double numSame = 0;
	    		double total = 0;
	    		
	    		if (tissue[row][col]==tissue[row][col-1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row][col-1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row-1][col-1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row-1][col-1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row-1][col]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row-1][col]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row-1][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row-1][col+1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row][col+1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row+1][col+1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][col+1]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row+1][col]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][col]!=' ')
	    			total++;
	    		
	    		if (tissue[row][col]==tissue[row+1][col-1]){
	    			numSame++;
	    			total++;
	    		}
	    		
	    		else if (tissue[row+1][col-1]!=' ')
	    			total++;
	    		
	    		if (total==0)
	    			return (true);
	    		
	    		if ((numSame/total)>=( (double) threshold/100))
	    			return (true);
	    		
	    		else{
	    			return (false);
	    		}
	    	}
	    }
	    
	    /**
	    * Given a tissue sample, determines if all agents are satisfied.
	    * Note: Blank cells are always satisfied (as there is no agent)
	    *
	    * @param tissue a 2-D character array that has been initialized
	    * @return boolean indicating whether entire board has been satisfied (all agents)
	    **/
	    public static boolean boardSatisfied(char[][] tissue, int threshold){
	    	/* The if statement below checks if there are any invalid entries such as a threshold below 0 or greater than
	    	 * 100 etc.... 
	    	 */
	    	
	    	if((threshold<0)||(threshold>100)||(tissue.length != tissue[0].length)||(tissue.length<=1)||(tissue[0].length<=1)){
	    		System.out.print("Invalid Entry");
	    		System.exit(1);
	    	}      
	    		
	    	/*The nested for loop below checks each index of the array to see if it is satisfied or not. If the loop comes
	    	 * across one index that is not satisfied, then the method returns false. If not, then the method returns true.
	    	 */
	    	for(int row = 0;row<tissue.length;row++){
	    		for(int col = 0;col<tissue[0].length;col++){
	    			if (isSatisfied(tissue, row, col, threshold)==false)
	    				return (false);
	    		}
	    	}

	    	return (true);
	    }
	    
	    /**
	    * Given a tissue sample, move all unsatisfied agents to a vacant cell
	    *
	    * @param tissue a 2-D character array that has been initialized
	    * @param threshold the percentage of like agents that must surround the agent to be satisfied
	    *
	    **/
public static int moveAllUnsatisfied(char[][] tissue, CellSimGUI visualTissue, int threshold){
	    	
	    	boolean[][] unsatisfied = new boolean[tissue.length][tissue[0].length];
	    	boolean[][] optimalX = new boolean[tissue.length][tissue[0].length];
	    	boolean[][] optimalO = new boolean[tissue.length][tissue[0].length];
	    	int numMoved = 0;
	    	
	    	reset(unsatisfied);
	    	for(int i = 0;i<tissue.length;i++){ //I made a boolean array of size nxn to tell if a cell is unsatisfied or not
	    		for(int j = 0;j<tissue[0].length;j++){
	    			if (isSatisfied(tissue, i, j, threshold) == (false)){
	    				unsatisfied[i][j] = true;
	    			}
	    		}
	    	}
	    	for(int i = 0;i<tissue.length;i++){ //If a cell is unsatisfied, I check first to see if there is a spot where it can move to and be satisfied. If not, then it moves to any blank space
	    		unsat: for(int j = 0;j<tissue[0].length;j++){
	    			reset(optimalX);
	    			reset(optimalO);
	    			optimizeXLocation(tissue, optimalX, threshold);
	    			optimizeOLocation(tissue, optimalO, threshold);
	    			
	    			if (unsatisfied[i][j]){
	    				if (tissue[i][j] == 'X'){
		    				for(int a = 0;a<tissue.length;a++){
		    					for(int b = 0;b<tissue[0].length;b++){
		    						if (optimalX[a][b] == (true)){
		    							tissue[a][b] = tissue[i][j];
		    							updateVisual(visualTissue, tissue, a, b);
		    							tissue[i][j] = ' ';
		    							updateVisual(visualTissue, tissue, i, j);
		    							numMoved++;
		    							continue unsat;
		    						}
		    					}
		    				}
	    				}
	    				else if (tissue[i][j] == 'O'){
		    				for(int a = 0;a<tissue.length;a++){
		    					for(int b = 0;b<tissue[0].length;b++){
		    						if (optimalO[a][b] == (true)){
		    							tissue[a][b] = tissue[i][j];
		    							updateVisual(visualTissue, tissue, a, b);
		    							tissue[i][j] = ' ';
		    							updateVisual(visualTissue, tissue, i, j);
		    							numMoved++;
		    							continue unsat;
		    						}
		    					}
		    				}
	    				}
	    				for(int a = 0;a<tissue.length;a++){
	    					for(int b = 0;b<tissue[0].length;b++){
	    						if (tissue[a][b] == ' '){
	    							tissue[a][b] = tissue[i][j];
	    							updateVisual(visualTissue, tissue, a, b);
	    							tissue[i][j] = ' ';
	    							updateVisual(visualTissue, tissue, i, j);
	    							numMoved++;
	    							continue unsat;
	    						}
	    					}
	    				}
	    			}
	    		}
	    	}
	    	
	    	return (numMoved);
	    }

	    
	    public static void optimizeXLocation(char[][] tissue, boolean[][] optimalX, int threshold){// sets index of boolean[][] optimalX to true to show that if you put an X in that index, then it will be satisfied
			double same = 0;
			double total = 0;
			
			for(int a = 0;a<tissue.length;a++){
				for(int b = 0;b<tissue[0].length;b++){
					if (tissue[a][b] == ' '){
						total = 0.0;
						same = 0.0;
						try{
							if (tissue[a-1][b-1] == 'X'){
								same++;
								total++;
							}
							else if (tissue[a-1][b-1] != ' ')
								total++;
						}
						catch (IndexOutOfBoundsException e){
						}
						
						try{
							if (tissue[a-1][b] == 'X'){
								same++;
								total++;
							}
							else if (tissue[a-1][b] != ' ')
								total++;
						}
						catch (IndexOutOfBoundsException e){
						}
						
						try{
							if (tissue[a-1][b+1] == 'X'){
								same++;
								total++;
							}
							else if (tissue[a-1][b+1] != ' ')
								total++;
						}
						catch (IndexOutOfBoundsException e){
						}
						
						try{
							if (tissue[a][b-1] == 'X'){
								same++;
								total++;
							}
							else if (tissue[a][b-1] != ' ')
								total++;
						}
						catch (IndexOutOfBoundsException e){
						}
						
						try{
							if (tissue[a][b+1] == 'X'){
								same++;
								total++;
							}
							else if (tissue[a][b+1] != ' ')
								total++;
						}
						catch (IndexOutOfBoundsException e){
						}
						
						try{
							if (tissue[a+1][b-1] == 'X'){
								same++;
								total++;
							}
							else if (tissue[a+1][b-1] != ' ')
								total++;
						}
						catch (IndexOutOfBoundsException e){
						}
						
						try{
							if (tissue[a+1][b] == 'X'){
								same++;
								total++;
							}
							else if (tissue[a+1][b] != ' ')
								total++;
						}
						catch (IndexOutOfBoundsException e){
						}
						
						try{
							if (tissue[a+1][b+1] == 'X'){
								same++;
								total++;
							}
							else if (tissue[a+1][b+1] != ' ')
								total++;
						}
						catch (IndexOutOfBoundsException e){
						}
						
						if (total == 0)
							optimalX[a][b] = true;
						else if ((double) threshold/100.0 <= (same/total))
							optimalX[a][b] = true;
						else
							optimalX[a][b] = false;
					}
				}
			}
		}
	    
public static void optimizeOLocation(char[][] tissue, boolean[][] optimalO, int threshold){// sets index of boolean[][] optimalO to true to show that if you put an O in that index, then it will be satisfied
	double same = 0.0;
	double total = 0.0;
	for(int a = 0;a<tissue.length;a++){
		for(int b = 0;b<tissue[0].length;b++){
			if (tissue[a][b] == ' '){
				total = 0.0;
				same = 0.0;
				try{
					if (tissue[a-1][b-1] == 'O'){
						same++;
						total++;
					}
					else if (tissue[a-1][b-1] != ' ')
						total++;
				}
				catch (IndexOutOfBoundsException e){
				}
				
				try{
					if (tissue[a-1][b] == 'O'){
						same++;
						total++;
					}
					else if (tissue[a-1][b] != ' ')
						total++;
				}
				catch (IndexOutOfBoundsException e){
				}
				
				try{
					if (tissue[a-1][b+1] == 'O'){
						same++;
						total++;
					}
					else if (tissue[a-1][b+1] != ' ')
						total++;
				}
				catch (IndexOutOfBoundsException e){
				}
				
				try{
					if (tissue[a][b-1] == 'O'){
						same++;
						total++;
					}
					else if (tissue[a][b-1] != ' ')
						total++;
				}
				catch (IndexOutOfBoundsException e){
				}
				
				try{
					if (tissue[a][b+1] == 'O'){
						same++;
						total++;
					}
					else if (tissue[a][b+1] != ' ')
						total++;
				}
				catch (IndexOutOfBoundsException e){
				}
				
				try{
					if (tissue[a+1][b-1] == 'O'){
						same++;
						total++;
					}
					else if (tissue[a+1][b-1] != ' ')
						total++;
				}
				catch (IndexOutOfBoundsException e){
				}
				
				try{
					if (tissue[a+1][b] == 'O'){
						same++;
						total++;
					}
					else if (tissue[a+1][b] != ' ')
						total++;
				}
				catch (IndexOutOfBoundsException e){
				}
				
				try{
					if (tissue[a+1][b+1] == 'O'){
						same++;
						total++;
					}
					else if (tissue[a+1][b+1] != ' ')
						total++;
				}
				catch (IndexOutOfBoundsException e){
				}
				if (total == 0.0){
					optimalO[a][b] = true;
				}
				else if ((double) threshold/100.0 <= (same/total)){
					optimalO[a][b] = true;
				}
				else{
					optimalO[a][b] = false;
				}
			}
		}
	}
		}

		public static void reset(boolean[][] b){// changes all values of a boolean array to its default value
			for(int i = 0; i < b.length; i++){
				for(int j = 0; j < b[0].length; j++){
					b[i][j] = false;
				}
			}
				
		}
	    
	    public static void endSimulation(char[][] initial, char[][] tissue, boolean satisfied, int numRounds, 
	    double numMoved, int totalNumMoved){
	    	
	    	double temp = numMoved;
	    	/* to find out how many cells are satisfied, I take the number of unsatisfied and add the spaces to it
	    	 * then I subtract that sum from the size of the array
	    	 */
	    	
	    	for(int row = 0;row<tissue.length;row++){//add blank spaces to dissatisfied cells
	    		for(int col = 0;col<tissue[0].length;col++){
	    			if (tissue[row][col] == ' ')
	    				numMoved++;
	    		}
	    	}
	    	
	    	
	    	double percentSatisfied = ((((double)tissue.length*(double)tissue[0].length)-numMoved)/(temp + (((double)tissue.length*(double)tissue[0].length)-numMoved))*100);
	    	
	    	//Depending on whether or not the board is satisfied, the method prints the appropriate information
	    	
	    	
	    	if (satisfied){
	    		System.out.println("The initial tissue: ");
	    		System.out.println();
	    		printTissue(initial);
	    		System.out.println("The final tissue: ");
	    		System.out.println();
	    		printTissue(tissue);
	    		System.out.println("The board is satisfied.");
	    		System.out.println("Number of Rounds: " + numRounds); 
	    		System.out.println("Total Movements: " + totalNumMoved);
	    	}
	    	else{
	    		System.out.println("The initial tissue: ");
	    		System.out.println();
	    		printTissue(initial);
	    		System.out.println("The final tissue: ");
	    		System.out.println();
	    		printTissue(tissue);
	    		System.out.println("The board is not satisfied.");
	    		System.out.println("Percentage of Satisfied cells: " + percentSatisfied);
	    		System.out.println("Total Movements: " + totalNumMoved);
	    	}
	    	System.exit(0); //ends the program, thus ending the simulation
	    }
	    
	    public static void initialVisual(CellSimGUI visualTissue, char[][] tissue){// gives visual representation of the intial tissue
	    	for(int i = 0; i < tissue.length; i++){
	    		for(int j = 0; j < tissue[0].length; j++){
	    			if (tissue[i][j] == 'X')
	    				visualTissue.setCell(i, j, Color.red);
	    			else if (tissue[i][j] == 'O')
	    				visualTissue.setCell(i, j, Color.blue);
	    			else
	    				visualTissue.setCell(i, j, Color.white);
	    		}
	    	}
	    }
	    
	    public static void updateVisual(CellSimGUI visualTissue, char[][] tissue, int i, int j){//reflects a change made in the tissue array in my visual representation
			if (tissue[i][j] == 'X')
				visualTissue.setCell(i, j, Color.red);
			else if (tissue[i][j] == 'O')
				visualTissue.setCell(i, j, Color.blue);
			else if (tissue[i][j] == ' ')
				visualTissue.setCell(i, j, Color.white);
	    }
	    
	    public static String countCellTypes(char[][] initial, char[][] tissue){//I used this to make sure I had the same number of cells and blanks at the end as I did in the beginning
	    	int b = 0;
	    	int x = 0;
	    	int o = 0;
	    	
	    	int b2 = 0;
	    	int x2 = 0;
	    	int o2 = 0;
	    	
	    	for(int i = 0; i < tissue.length; i++){
	    		for(int j = 0; j < tissue[0].length; j++){
	    			if (tissue[i][j] == 'X')
	    				x++;
	    			else if (tissue[i][j] == 'O')
	    				o++;
	    			else
	    				b++;
	    		}
	    	}
	    	
	    	for(int i = 0; i < tissue.length; i++){
	    		for(int j = 0; j < tissue[0].length; j++){
	    			if (initial[i][j] == 'X')
	    				x2++;
	    			else if (initial[i][j] == 'O')
	    				o2++;
	    			else
	    				b2++;
	    		}
	    	}
	    	
	    	return "num x in final: " + x + "\n" + "num o in final: " + o + "\n" + "num b in final: " + b + "\n" + "num x in orig: " + x2 + "\n" + "num o in orig: " + o2 + "\n" + "num b in orig: " + b2 + "\n";
	    }
}