package poly;

import java.io.*;
import java.util.StringTokenizer;

/**
 * This class implements a term of a polynomial.
 * 
 * @author runb-cs112
 *
 */
class Term {
	/**
	 * Coefficient of term.
	 */
	public float coeff;
	
	/**
	 * Degree of term.
	 */
	public int degree;
	
	/**
	 * Initializes an instance with given coefficient and degree.
	 * 
	 * @param coeff Coefficient
	 * @param degree Degree
	 */
	public Term(float coeff, int degree) {
		this.coeff = coeff;
		this.degree = degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		return other != null &&
		other instanceof Term &&
		coeff == ((Term)other).coeff &&
		degree == ((Term)other).degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (degree == 0) {
			return coeff + "";
		} else if (degree == 1) {
			return coeff + "x";
		} else {
			return coeff + "x^" + degree;
		}
	}
}

/**
 * This class implements a linked list node that contains a Term instance.
 * 
 * @author runb-cs112
 *
 */
class Node {
	
	/**
	 * Term instance. 
	 */
	Term term;
	
	/**
	 * Next node in linked list. 
	 */
	Node next;
	
	/**
	 * Initializes this node with a term with given coefficient and degree,
	 * pointing to the given next node.
	 * 
	 * @param coeff Coefficient of term
	 * @param degree Degree of term
	 * @param next Next node
	 */
	public Node(float coeff, int degree, Node next) {
		term = new Term(coeff, degree);
		this.next = next;
	}
}

/**
 * This class implements a polynomial.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Pointer to the front of the linked list that stores the polynomial. 
	 */ 
	Node poly;
	
	/** 
	 * Initializes this polynomial to empty, i.e. there are no terms.
	 *
	 */
	public Polynomial() {
		poly = null;
	}
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param br BufferedReader from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 */
	public Polynomial(BufferedReader br) throws IOException {
		String line;
		StringTokenizer tokenizer;
		float coeff;
		int degree;
		
		poly = null;
		
		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line);
			coeff = Float.parseFloat(tokenizer.nextToken());
			degree = Integer.parseInt(tokenizer.nextToken());
			poly = new Node(coeff, degree, poly);
		}
	}
	
	
	/**
	 * Returns the polynomial obtained by adding the given polynomial p
	 * to this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial to be added
	 * @return A new polynomial which is the sum of this polynomial and p.
	 */
	public Polynomial add(Polynomial p) {
		if((this.poly == null) && (p.poly == null)){
			return new Polynomial();
		}
		else if(this.poly == null){
			return p; 
		}
		else if(p.poly == null){
			return this;
		}
		
		Polynomial sum = new Polynomial(); sum.poly = new Node(-1, -1, null); //temp front
		Node ptr1 = this.poly, ptr2 = p.poly, ptr3 = sum.poly;
		
		while(ptr1 != null && ptr2 != null){
			
			if(ptr1.term.degree == ptr2.term.degree){
				if((ptr1.term.coeff + ptr2.term.coeff) != 0){
					ptr3.next = new Node((ptr1.term.coeff + ptr2.term.coeff), ptr1.term.degree, null);
					ptr3 = ptr3.next;
				}
			}
			else if(ptr1.term.degree < ptr2.term.degree){
				ptr3.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
				ptr3 = ptr3.next;
			}
			
			else{
				ptr3.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
				ptr3 = ptr3.next;
			}
			
			
			if(ptr1.term.degree == ptr2.term.degree){
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			}
			if(ptr1.term.degree < ptr2.term.degree){
				ptr2 = ptr2.next;
			}
			if(ptr1.term.degree > ptr2.term.degree){
				ptr1 = ptr1.next;
			}
		}
		sum.poly = sum.poly.next; //moved from temp front to actual front
		
		while(ptr1 != null){
			ptr3.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
			ptr3 = ptr3.next;
			ptr1 = ptr1.next;
		}
		while(ptr2 != null){
			ptr3.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
			ptr3 = ptr3.next;
			ptr2 = ptr2.next;
		}
		
		return sum;
	}
	
	/**
	 * Returns the polynomial obtained by multiplying the given polynomial p
	 * with this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial with which this polynomial is to be multiplied
	 * @return A new polynomial which is the product of this polynomial and p.
	 */
	public Polynomial multiply(Polynomial p) {
		if ((this.poly == null) || (p.poly == null)){
			return new Polynomial();
		}
		
		Polynomial part1 = new Polynomial();
		Polynomial part2 = new Polynomial();
		
		Node pplace = p.poly;
		
		while (pplace != null){
			part2 = distribute(pplace, this);
			part1 = part1.add(part2);
			pplace = pplace.next;
		}
		
		return part1;
	}
	
	/**
	 * distributes given term to given polynomial and returns that polynomial.
	 * 
	 * 
	 * @param term (get distributed)
	 * @param p (the polynomial)
	 * @return
	 */
	private static Polynomial distribute(Node  term, Polynomial p){
		if (term == null || p.poly == null){
			return new Polynomial();
		}
		
		Polynomial result = new Polynomial();
		result.poly = new Node(0, 0, null);
		Node resultplace = result.poly;
		Node pplace = p.poly;
		
		while (pplace != null){
			resultplace.term.degree = term.term.degree + pplace.term.degree;
			resultplace.term.coeff = term.term.coeff * pplace.term.coeff;
			
			pplace = pplace.next;
			resultplace.next = new Node(0, 0, null);
			resultplace = resultplace.next;
		}
		
		resultplace = result.poly;
		while (resultplace.next != null){
			if (resultplace.next.term.coeff == 0){
				resultplace.next = null;
				break;
			}
			resultplace = resultplace.next;
		}
		
		return result;
	}
	
	/**
	 * Evaluates this polynomial at the given value of x
	 * 
	 * @param x Value at which this polynomial is to be evaluated
	 * @return Value of this polynomial at x
	 */
	public float evaluate(float x) {
		float sum = 0;
		Node place = this.poly;
		
		while (place != null){
			sum += (Math.pow(x, place.term.degree))*(place.term.coeff);
			place = place.next;
		}
		
		return sum;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retval;
		
		if (poly == null) {
			return "0";
		} else {
			retval = poly.term.toString();
			for (Node current = poly.next ;
			current != null ;
			current = current.next) {
				retval = current.term.toString() + " + " + retval;
			}
			return retval;
		}
	}
}
