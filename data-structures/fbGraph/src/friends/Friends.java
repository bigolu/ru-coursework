package friends;

import java.io.*;
import java.util.*;
/**
 * Authors:
 * Olaolu Emmanuel
 * and
 * Sten Knutsen
 * 
 */

public class Friends {

	public static void main(String[] args) throws IOException {
		
		System.out.print("Enter file name: ");
		String file = "";
		
		//wont end until the input file is found
		while(true){
			try{
				Scanner sc = new Scanner(System.in);
				file = sc.nextLine();
				Scanner sc2 = new Scanner(new File(file));
				break;
			}
			catch(FileNotFoundException f){
				System.out.println("Invalid input, try again.");
			}
		}
		
		OtherGraph friends = new OtherGraph(file);
		
		while(true){
			System.out.println();
			System.out.println("1. Shortest intro chain");
			System.out.println("2. Cliques at school");
			System.out.println("3. Connectors");
			System.out.println("4. Quit");
			System.out.println();
			System.out.print("Select 1 - 4: ");
			Scanner sn = new Scanner(System.in);
			String sl = sn.nextLine();
			
			char selection = sl.charAt(0);
			
			if(selection == '1'){
				//shortest path
				System.out.print("Enter name of first person: ");
				Scanner s1 = new Scanner(System.in);
				String start = s1.nextLine();
				System.out.print("Enter name of second person: ");
				Scanner s2 = new Scanner(System.in);
				String target = s1.nextLine();
				System.out.println();
				friends.shortestPath(start.toLowerCase(), target.toLowerCase());
				continue;
			}
			if(selection == '2'){
				System.out.println("Enter the name of the school: ");
				Scanner s1 = new Scanner(System.in);
				String school = s1.nextLine();
				System.out.println();
				friends.cliques(school.toLowerCase());
				continue;
			}
			if(selection == '3'){
				System.out.println();
				friends.connectors();
				continue;
			}
			if(selection == '4'){
				System.out.println("Session ended.");
				break;
			}
			
			System.out.println("Invalid entry.");
			System.out.println();
			
		}//end while
		

	}

}
