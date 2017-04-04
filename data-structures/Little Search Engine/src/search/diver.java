package search;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class diver {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		LittleSearchEngine lse = new LittleSearchEngine();
		lse.makeIndex("docs.txt", "noisewords.txt");
		
		// getkeyWord
		System.out.println("getkeyWord");
		System.out.println("================================");
		String s[] = {"!distance.", "equi-distant", "Rabbit", "Between", "we're", 
				"World...", "World?!", "What,ever", "bruh:bruh.;;;;;"};
		
		for (int i = 0; i < s.length; i++) {
			System.out.println(lse.getKeyWord(s[i]));
		}
		System.out.println("================================");
		System.out.println("insertLastOccurrence");
		System.out.println("================================");
		// insertLastOccurrence
		int data[] = {12, 8, 7, 5, 3, 2, 6};
		ArrayList<Occurrence> occs = new ArrayList<Occurrence>();
		for (int i = 0; i < data.length; i++) {
			occs.add(new Occurrence("Doc", data[i]));
		}
		ArrayList<Integer> a = lse.insertLastOccurrence(occs);
		System.out.print("Sequence: ");
		for (int i = 0; i < a.size(); i ++) {
			System.out.print(a.get(i) + " ");
		}
		System.out.println();
		System.out.print("Result: ");
		for (int i = 0; i < occs.size(); i++) {
			System.out.print(occs.get(i).frequency + " ");
		}
		System.out.println();
		System.out.println("================================");
		System.out.println("top5search");
		System.out.println("================================");
		ArrayList<String> top = lse.top5search("deep", "world");
		System.out.println(top);
		
		
		
		System.out.println();
		System.out.println("================================");
		System.out.println("More Testing");
		System.out.println("================================");
		
		//getkeyWord
		String[] words = {"Word", "night,", "question??", "Could", "test-case"};
		String[] wordsAns = {"word", "night", "question", null, null};
		for(int i = 0; i < words.length; i++){
			if(lse.getKeyWord(words[i]) == null){
				if(lse.getKeyWord(words[i]) == wordsAns[i]){
					System.out.println(lse.getKeyWord(words[i]) + " is right");
				}
				else{
					System.out.println(lse.getKeyWord(words[i]) + " is WRONG");
				}
			}
			
			else if(lse.getKeyWord(words[i]).equals(wordsAns[i])){
				System.out.println(lse.getKeyWord(words[i]) + " is right");
			}
			else{
				System.out.println(lse.getKeyWord(words[i]) + " is WRONG");
			}
		}
		
		//insertLastOccurrence
		System.out.println();
		
		ArrayList<Occurrence> occLst1 = new ArrayList<Occurrence>();
		occLst1.add(new Occurrence("doc1.txt", 20));
		occLst1.add(new Occurrence("doc2.txt", 15));
		occLst1.add(new Occurrence("doc3.txt", 14));
		occLst1.add(new Occurrence("doc4.txt", 12));
		occLst1.add(new Occurrence("doc5.txt", 12));
		occLst1.add(new Occurrence("doc6.txt", 10));
		occLst1.add(new Occurrence("doc7.txt", 8));
		occLst1.add(new Occurrence("doc8.txt", 20));
		ArrayList<Integer> mids1 = lse.insertLastOccurrence(occLst1);
		
		ArrayList<Integer> occLst1Ans = new ArrayList<Integer>();
		occLst1Ans.add(3);
		occLst1Ans.add(1);
		occLst1Ans.add(0);
		
		for(int i = 0; i < mids1.size(); i++){
			if(mids1.get(i) == occLst1Ans.get(i)){
				System.out.println(mids1.get(i) + " is right");
			}
			else{
				System.out.println(mids1.get(i) + " is WRONG");
			}
		}
		
		//ins2

		occLst1 = new ArrayList<Occurrence>();
		occLst1.add(new Occurrence("doc1.txt", 20));
		occLst1.add(new Occurrence("doc2.txt", 15));
		occLst1.add(new Occurrence("doc3.txt", 14));
		occLst1.add(new Occurrence("doc4.txt", 12));
		occLst1.add(new Occurrence("doc5.txt", 12));
		occLst1.add(new Occurrence("doc6.txt", 10));
		occLst1.add(new Occurrence("doc7.txt", 8));
		occLst1.add(new Occurrence("doc8.txt", 1));
		mids1 = lse.insertLastOccurrence(occLst1);
		
		occLst1Ans = new ArrayList<Integer>();
		occLst1Ans.add(3);
		occLst1Ans.add(5);
		occLst1Ans.add(6);
		
		for(int i = 0; i < mids1.size(); i++){
			if(mids1.get(i) == occLst1Ans.get(i)){
				System.out.println(mids1.get(i) + " is right");
			}
			else{
				System.out.println(mids1.get(i) + " is WRONG");
			}
		}
		
		
		//ins3
		
		occLst1 = new ArrayList<Occurrence>();
		occLst1.add(new Occurrence("doc1.txt", 20));
		occLst1.add(new Occurrence("doc2.txt", 15));
		occLst1.add(new Occurrence("doc3.txt", 14));
		occLst1.add(new Occurrence("doc4.txt", 12));
		occLst1.add(new Occurrence("doc5.txt", 12));
		occLst1.add(new Occurrence("doc6.txt", 10));
		occLst1.add(new Occurrence("doc7.txt", 8));
		occLst1.add(new Occurrence("doc8.txt", 13));
		mids1 = lse.insertLastOccurrence(occLst1);
		
		occLst1Ans = new ArrayList<Integer>();
		occLst1Ans.add(3);
		occLst1Ans.add(1);
		occLst1Ans.add(2);
		
		for(int i = 0; i < mids1.size(); i++){
			if(mids1.get(i) == occLst1Ans.get(i)){
				System.out.println(mids1.get(i) + " is right");
			}
			else{
				System.out.println(mids1.get(i) + " is WRONG");
			}
		}
		
		//ins4
		occLst1 = new ArrayList<Occurrence>();
		occLst1.add(new Occurrence("doc1.txt", 20));
		occLst1.add(new Occurrence("doc2.txt", 15));
		occLst1.add(new Occurrence("doc3.txt", 14));
		occLst1.add(new Occurrence("doc4.txt", 12));
		occLst1.add(new Occurrence("doc5.txt", 12));
		occLst1.add(new Occurrence("doc6.txt", 10));
		occLst1.add(new Occurrence("doc7.txt", 8));
		occLst1.add(new Occurrence("doc8.txt", 10));
		mids1 = lse.insertLastOccurrence(occLst1);
		
		occLst1Ans = new ArrayList<Integer>();
		occLst1Ans.add(3);
		occLst1Ans.add(5);
		
		for(int i = 0; i < mids1.size(); i++){
			if(mids1.get(i) == occLst1Ans.get(i)){
				System.out.println(mids1.get(i) + " is right");
			}
			else{
				System.out.println(mids1.get(i) + " is WRONG");
			}
		}
		
		
		System.out.println();
		//loadkey words
		if(lse.loadKeyWords("pohlx.txt").size() == 61){
			System.out.println("pohlx.txt is right: " + lse.loadKeyWords("pohlx.txt").size());
		}
		else{
			System.out.println("pohlx.txt is WRONG: " + lse.loadKeyWords("pohlx.txt").size());
		}
		
		if(lse.loadKeyWords("Tyger.txt").size() == 53){
			System.out.println("Tyger.txt is right: " + lse.loadKeyWords("Tyger.txt").size());
		}
		else{
			System.out.println("Tyger.txt is WRONG: " + lse.loadKeyWords("Tyger.txt").size());
		}
		
		if(lse.loadKeyWords("jude.txt").size() == 25){
			System.out.println("jude.txt is right: " + lse.loadKeyWords("jude.txt").size());
		}
		else{
			System.out.println("jude.txt is WRONG: " + lse.loadKeyWords("jude.txt").size());
		}
		
		
		System.out.println();
		//mergeekeys
		lse.keywordsIndex = new HashMap<String,ArrayList<Occurrence>>();
		lse.makeIndex("2.txt", "noisewords.txt");
		
		if(lse.keywordsIndex.size() == 1059){
			System.out.println("keywordsMerge is right: " + lse.keywordsIndex.size());
		}
		else{
			System.out.println("keywordsMerge is WRONG: " + lse.keywordsIndex.size());
		}
		
		
		System.out.println();
		//top5
		lse.keywordsIndex = new HashMap<String,ArrayList<Occurrence>>();
		lse.makeIndex("t3.txt", "noisewords.txt");
		
		ArrayList<String> results = new ArrayList<String>();
		results.add("t2.txt"); results.add("t1.txt"); 
		
		for(int i =0; i < lse.top5search("bruhl", "bruh").size(); i++){
			if(lse.top5search("bruhl", "bruh").get(i).equals(results.get(i))){
				System.out.println("works for bruh and bruhl");
			}
			else{
				System.out.println("DOES NOT WORK for bruh and bruhl");
			}
		}
		System.out.println();
		
		if(lse.top5search("riggity", "rowscomin") == null){
			System.out.println("riggity rows comin!");
		}
		else{
			for(String doc : lse.top5search("riggity", "rowscomin")){
				System.out.println(doc);
			}
		}
		System.out.println();
		
		
		results = new ArrayList<String>();
		results.add("t2.txt"); results.add("t1.txt"); results.add("t5.txt");  results.add("t4.txt");
		for(int i =0; i < lse.top5search("yo", "nothere").size(); i++){
			if(lse.top5search("yo", "nothere").get(i).equals(results.get(i))){
				System.out.println("works for yo and nothere");
			}
			else{
				System.out.println("DOES NOT WORK for yo and yo nothere: ");
				System.out.println("should be: " + results.get(i));
				System.out.println("but is: " + lse.top5search("yo", "nothere").get(i));
			}
		}
		
		
	}
	
}


