package solitaire;


import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		CardNode ptr = this.deckRear;
		
		while(true){
			if(ptr.cardValue == 27){//swap value with card in front
				int temp = ptr.next.cardValue;
				ptr.next.cardValue = ptr.cardValue;
				ptr.cardValue = temp;
				return;
			}
			ptr = ptr.next;
		}
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
		CardNode ptr = this.deckRear;
		
		while(true){
			if(ptr.cardValue == 28){//swap place with the two cards in front
				int temp1 = ptr.next.cardValue;
				int temp2 = ptr.next.next.cardValue;
				ptr.next.next.cardValue = ptr.cardValue;
				ptr.next.cardValue = temp2;
				ptr.cardValue = temp1;
				return;
			}
			ptr = ptr.next;
		}
		
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		CardNode prevJokerA = this.deckRear;
		CardNode prevJokerB = this.deckRear;
		CardNode jokerA = this.deckRear.next;
		CardNode jokerB = this.deckRear.next;
		int placeB = 1;
		int placeA = 1;
		
		while(true){
			if(jokerA.cardValue == 27){
				break;
			}
			placeA++;
			prevJokerA = jokerA;
			jokerA = jokerA.next;
		}
		
		while(true){
			if(jokerB.cardValue == 28){
				break;
			}
			placeB++;
			prevJokerB = jokerB;
			jokerB = jokerB.next;
		}
		
		if(((this.deckRear.cardValue == 27) || (this.deckRear.cardValue == 28)) && ((this.deckRear.next.cardValue == 27) || (this.deckRear.next.cardValue == 28))){
			//if they the deck looks like: 27.....28 or 28...27
			return;
		}
		else if(this.deckRear.cardValue ==27){
			//if they the deck looks like: .....28....27
			this.deckRear = prevJokerB;
			return;
		}
		else if(this.deckRear.next.cardValue == 27){
			//if they the deck looks like: 2728...... or 27...28......
			this.deckRear = jokerB;
			return;
		}
		else if(this.deckRear.next.cardValue == 28){
			//if they the deck looks like: 2827...... or 28...27......
			this.deckRear = jokerA;
			return;
		}
		else if(this.deckRear.cardValue == 28){
			//if they the deck looks like: …....27...28 or ….....2728
			this.deckRear = prevJokerA;
			return;
		}
		else if(placeA < placeB){
			//if deck looks like: …..2728..... or ….27....28....  
			prevJokerA.next = jokerB.next;
			jokerB.next = this.deckRear.next;
			this.deckRear.next = jokerA;
			this.deckRear = prevJokerA;
		}
		else{
			//deck must look like: ...2827....  or  ...28....27..
			prevJokerB.next = jokerA.next;
			jokerA.next = this.deckRear.next;
			this.deckRear.next = jokerB;
			this.deckRear = prevJokerB;
		}
	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {		
		if(this.deckRear.cardValue == 27 || this.deckRear.cardValue == 28){
			return;
		}
		
		CardNode ptr = this.deckRear;
		CardNode prevDeckRear = this.deckRear;
		
		while(prevDeckRear.next != this.deckRear){
			prevDeckRear = prevDeckRear.next;
		}
		
		for(int i = 0;i < this.deckRear.cardValue; i++){
			ptr = ptr.next;
		}
		
		CardNode front = this.deckRear.next;
		
		this.deckRear.next = ptr.next;
		ptr.next = this.deckRear;
		prevDeckRear.next = front;
		return;
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		while(true){
			jokerA();
			jokerB();
			tripleCut();
			countCut();
			
			if(isKey()){
				return keyValue();
			}
		}
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		String newMessage = "";
		int temp = 0;
		
		for(int i= 0; i < message.length(); i++){
			if(message.charAt(i) >= 'A' && message.charAt(i) <= 'Z'){
				temp = (message.charAt(i) - 'A' + 1) + getKey();
				if(temp >26){
					temp -= 26;
				}
				newMessage = newMessage + (char) (temp + 64);
			}
		}
		
	    return newMessage;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		String newMessage = "";
		int temp = 0;
		
		for(int i= 0; i < message.length(); i++){
			temp = (message.charAt(i) - 'A' + 1) - getKey();
			if(temp <= 0){
				temp += 26;
			}
			newMessage = newMessage + (char) (temp + 64);
		}
		
	    return newMessage;
	}
	
	private boolean isKey(){
		int card = this.deckRear.next.cardValue;
		if(card == 28){
			card = 27;
		}
		
		CardNode ptr = this.deckRear;
		
		for(int i = 0; i < card; i++){
			ptr = ptr.next;
		}
		
		if(ptr.next.cardValue != 27 && ptr.next.cardValue != 28){
			return true;
		}
		
		return false;
	}
	
	private int keyValue(){
		int card = this.deckRear.next.cardValue;
		if(card == 28){
			card = 27;
		}
		
		CardNode ptr = this.deckRear;
		
		for(int i = 0; i < card; i++){
			ptr = ptr.next;
		}
		
		return ptr.next.cardValue;
	}
	
	/*public static void main(String[] args){
		Solitaire test = new Solitaire();
		test.makeDeck();
		
		while(test.deckRear.cardValue != 12){
			test.makeDeck();
		}
		
		printList(test.deckRear);
		test.countCut();
		printList(test.deckRear);
	}*/
}
