package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file. The root of the 
	 * tree is stored in the root field.
	 */
	public void build() {
		this.root = buildHelper(sc);
	}
	
	private static TagNode buildHelper(Scanner s){
		String line = (s.hasNext()) ? s.nextLine() : null;
		
		return ((line == null) || (line.indexOf("</") != -1)) ? 
				null : 
				(line.charAt(0) == '<') ? 
					new TagNode(line.substring(1, line.length() - 1), buildHelper(s), buildHelper(s)) : 
					new TagNode(line, null, buildHelper(s));	
	}
	
	/**
	 
	private static TagNode addToTree(Stack<String> s){
		if(s.isEmpty()){
			return null;
		}
		else if(s.peek().length() == 0){
			String name = s.pop();
			return new TagNode(name, null, addToTree(s));
		}
		else if(s.peek().charAt(0) != '<'){
			String name = s.pop();
			return new TagNode(name, null, addToTree(s));
		}
		else if(s.peek().charAt(0) == '<'){
			String tag = s.pop();
			String tagName = tag.substring(1, tag.length() - 1);
			Stack<String> temp = new Stack<String>();
			Stack<String> lilStack = new Stack<String>();
			int nested = 0;
			
			while(true){
				if(s.peek().indexOf('<' + tagName) != -1){
					nested++;
					temp.push(s.pop());
				}
				else if(s.peek().indexOf("</" + tagName) != -1){
					if(nested > 0){
						nested--;
						temp.push(s.pop());
					}
					else{
						s.pop();
						break;
					}
				}
				else{
					temp.push(s.pop());
				}
			}
			
			while(!temp.isEmpty()){
				lilStack.push(temp.pop());
			}
			
			TagNode item = new TagNode(tagName, null,null);
			item.firstChild = addToTree(lilStack);
			item.sibling = addToTree(s);
			return item;
		}
		else{
			return null;
		}
	}
	*/

	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		if( oldTag.equals(newTag) ||
				oldTag.matches("html|body|table|tr|td") ||
				(oldTag.equals("li") && (!newTag.equals("p"))) ||
				(oldTag.matches("p|em|b") && (!newTag.matches("p|em|b"))) ||
				(oldTag.matches("ol|ul") && (!newTag.matches("ol|ul"))) )
		{
			System.out.println("Improper replacement");
			return;
		}
		
		switchTag(this.root, oldTag, newTag);
	}
	
	private static void switchTag(TagNode t, String oldTag, String newTag){
		if(t == null){
			return;
		}
		else if(t.tag.equals(oldTag) && t.firstChild != null){
			t.tag = newTag;
		}
		
		switchTag(t.firstChild, oldTag, newTag);
		switchTag(t.sibling, oldTag, newTag);
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		TagNode table = findTable(this.root);
		if(table == null){
			return;
		}
		
		TagNode ptr = table.firstChild;
		for(int i = 1; i < row && ptr != null; i++){
			if(ptr.tag.equals("tr") && ptr.firstChild != null){
				ptr = ptr.sibling;
			}
			else{
				ptr = ptr.sibling;
				i--;
			}
		}
		if(ptr == null){
			return;
		}
		else if(!ptr.tag.equals("tr") && ptr.firstChild != null){
			while(ptr != null ){
				if(ptr.tag.equals("tr") && ptr.firstChild != null){
					break;
				}
				else{
					ptr = ptr.sibling;
				}
			}
		}
		if(ptr == null){
			return;
		}
		
		ptr = ptr.firstChild;
		while(ptr != null){
			if(ptr.tag.equals("td") && ptr.firstChild != null){
				TagNode temp = ptr.firstChild;
				ptr.firstChild = new TagNode("b", temp, null);
				ptr = ptr.sibling;
			}
			else{
				ptr = ptr.sibling;
			}
		}
		System.out.println("boldRow breakpoint");
	}
	
	private static TagNode findTable(TagNode r){
		if(r == null){
			return null;
		}
		else if(r.tag.equals("table") && r.firstChild != null){
			return r;
		}
		
		TagNode maybeHere = findTable(r.firstChild);
		TagNode maybeThere = findTable(r.sibling);
		
		if(maybeHere != null){
			return maybeHere;
		}
		else if(maybeThere != null){
			return maybeThere;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		if(tag.matches("p|b|em")){
			case1Delete(root.firstChild, tag);
		}
		else if(tag.matches("ol|ul")){
			case2Delete(root, tag);
		}
		System.out.println("removeTag breakpoint");
	}
	
	/**
	 * If the tag to be removed is a p, b, or em tag, then it will get deleted with this method.
	 * 
	 * @param root
	 * @param tag
	 */
	private static void case1Delete(TagNode root, String tag){
		if(root == null){
			return;
		}
		
		TagNode ptr = root;
		
		if(ptr.tag.equals(tag) && ptr.firstChild != null){
			TagNode sibl = ptr.sibling;
			
			ptr.tag = ptr.firstChild.tag;
			ptr.sibling = ptr.firstChild.sibling;
			ptr.firstChild = ptr.firstChild.firstChild;
			
			while(ptr.sibling != null){
				if(ptr.tag.equals(tag) && ptr.firstChild != null){
					case1Delete(ptr, tag);
				}
				ptr = ptr.sibling;
			}
			if(ptr.tag.equals(tag) && ptr.firstChild != null){
				case1Delete(ptr, tag);
			}
			
			ptr.sibling = sibl;
		}
		
		case1Delete(root.firstChild, tag);
		case1Delete(root.sibling, tag);
	}
	
	/**
	 * If the tag to be removed is an ol or ul tag, then it will get deleted with this method.
	 * 
	 * @param root
	 * @param tag
	 */
	private static void case2Delete(TagNode root, String tag){
		if(root == null){
			return;
		}
		
		if(root.tag.equals(tag) && root.firstChild != null){
			TagNode sibl = root.sibling;
			
			root.tag = root.firstChild.tag;
			root.sibling = root.firstChild.sibling;
			root.firstChild = root.firstChild.firstChild;
			
			liToP(root, tag);
			
			TagNode ptr = root;
			
			while(ptr.sibling != null){
				if(ptr.tag.equals(tag) && ptr.firstChild != null){
					case2Delete(ptr, tag);
				}
				ptr = ptr.sibling;
			}
			if(ptr.tag.equals(tag) && ptr.firstChild != null){
				case2Delete(ptr, tag);
			}
			
			ptr.sibling = sibl;
		}
		
		case2Delete(root.firstChild, tag);
		case2Delete(root.sibling, tag);
	}
	
	/**
	 * In the case of a case2 deletion (ol or ul), if you pass this method the first child of the list,
	 * it will turn all li tags under it into p tags
	 * @param root
	 */
	private static void liToP(TagNode root, String tag){
		if(root == null){
			return;
		}
		else if(root.tag.matches("ul|ol") && (!root.tag.equals(tag))){
			liToP(root.sibling, tag);
		}
		else if(root.tag.equals("li") && root.firstChild != null){
			root.tag = "p";
			liToP(root.firstChild, tag);
			liToP(root.sibling, tag);
		}
		else{
			liToP(root.firstChild, tag);
			liToP(root.sibling, tag);
		}
	}
	
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		addTagR(this.root, word, tag);
		System.out.println("addTag breakpoint");
	}
	
	/**
	 * This method adds tags using recursion hence the 'R'. Thats about it.
	 * @param root
	 * @param word
	 * @param tag
	 */
	private static void addTagR(TagNode root, String word, String tag){
		if(root == null){
			return;
		}
		
		TagNode ptr = root;
		boolean firstItem = true;
		
		if(root.firstChild == null){
			StringTokenizer p = new StringTokenizer(root.tag, " ");
			while(p.hasMoreTokens()){
				String temp = p.nextToken();
				if(temp.equalsIgnoreCase(word) ||
				   temp.equalsIgnoreCase(word + '!') || 
				   temp.equalsIgnoreCase(word + '?') || 
				   temp.equalsIgnoreCase(word + '.') || 
				   temp.equalsIgnoreCase(word + ',') || 
				   temp.equalsIgnoreCase(word + ':') || 
				   temp.equalsIgnoreCase(word + ';'))
				{
					if(firstItem){
						if(ptr.tag.charAt(0) == ' '){
							ptr.tag = " ";
							TagNode sib = ptr.sibling;
							ptr.sibling = new TagNode(tag, new TagNode(temp, null, null), sib);
							ptr = ptr.sibling;
							firstItem = false;
						}
						else{
							ptr.tag = tag;
							ptr.firstChild = new TagNode(temp, null, null);
							firstItem = false;
						}
					}
					else{
						if(ptr.firstChild == null && ptr.tag.charAt(ptr.tag.length() - 1) != ' '){
							ptr.tag = ptr.tag + ' ';
							TagNode sib = ptr.sibling;
							ptr.sibling = new TagNode(tag, new TagNode(temp, null, null), sib);
							ptr = ptr.sibling;
							
						}
						else if(ptr.firstChild != null && ptr.firstChild.tag.charAt(ptr.firstChild.tag.length() - 1) != ' '){
							TagNode sib = ptr.sibling;
							ptr.sibling = new TagNode(" ", null, new TagNode(tag, new TagNode(temp, null, null), sib));
							ptr = ptr.sibling.sibling;
						}
						else{
							TagNode sib = ptr.sibling;
							ptr.sibling = new TagNode(tag, new TagNode(temp, null, null), sib);
							ptr = ptr.sibling;
						}
					}
				}
				else{
					if(firstItem){
						if(ptr.tag.charAt(0) == ' '){
							temp = ' ' + temp;
						}
						
						ptr.tag = temp;
						firstItem = false;
					}
					else{
						if(ptr.firstChild == null){
							ptr.tag = ptr.tag + ' ' + temp;
						}
						else{
							TagNode sib = ptr.sibling;
							ptr.sibling = new TagNode(" " + temp, null, sib);
							ptr = ptr.sibling;
						}
					}
				}
			}
			addTagR(ptr.sibling, word, tag);
		}
		else{
			addTagR(root.firstChild, word, tag);
			addTagR(root.sibling, word, tag);
		}
	}
	
	
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	
	
}
