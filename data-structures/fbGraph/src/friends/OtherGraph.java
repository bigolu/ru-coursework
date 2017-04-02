package friends;

import java.io.*;
import java.util.*;

class Neighbor {
	
	public int vertexNum;
	public Neighbor next;
	
	public Neighbor(int vnum, Neighbor nbr){
		
		this.vertexNum = vnum;
		next = nbr;
		
	}
	
}

class Vertex2 {
	
	String name;
	String school;
	Neighbor adjList;
	int vNum;
	
	Vertex2(String name, String school, Neighbor neighbors, int vNum){
		
		this.name = name;
		this.school = school;
		this.adjList = neighbors;
		this.vNum = vNum;
		
	}
}

class Path{
	int distance;
	String back;
	boolean[] visited;
	
	Path(int d, String b, int numVert){
		this.distance = d;
		this.back = b;
		this.visited = new boolean[numVert];
	}
}

class Connection{
	int dfsNum;
	int backNum;
	
	Connection(int d, int b){
		this.dfsNum = d;
		this.backNum = b;
	}
}

public class OtherGraph {
	
	HashMap<String, Vertex2> names; //for instant access to vertex given a persons name
	Vertex2[] numbers; //for instant access to vertex given a persons vertex Number
	ArrayList<HashMap<String, Vertex2>> islands; //subgraphs
	Set<String> connectors; // all connectors out of everyone in the entire graph
	
	
	
	public OtherGraph(String file) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(file));
		
		if(!sc.hasNext()){// empty input file
			return;
		}
		
		this.names = new HashMap<String, Vertex2>();
		this.numbers = new Vertex2[sc.nextInt()];
		sc.nextLine();//skip over line with number of friends
		this.islands = new ArrayList<HashMap<String, Vertex2>>();
		this.connectors = new HashSet<String>();
		
		
		for(int i =0; i < numbers.length; i++){//add people's info to hashmap and array
			String[] personInfo = sc.nextLine().split("[|]");
			
			if(personInfo.length == 2){//if line only has name and n for no
				names.put(personInfo[0].toLowerCase(), new Vertex2(personInfo[0].toLowerCase(), null, null, i));
				numbers[i] = names.get(personInfo[0].toLowerCase());
			}
			else{//line must have name, y for yes, and school name
				names.put(personInfo[0].toLowerCase(), new Vertex2(personInfo[0].toLowerCase(), personInfo[2].toLowerCase(), null, i));
				numbers[i] = names.get(personInfo[0].toLowerCase());
			}
		}
		
		while(sc.hasNextLine()){//add to adj linked list for each person
			String[] friendship = sc.nextLine().split("[|]");
			makeFriendship(friendship[0].toLowerCase(), friendship[1].toLowerCase(), names);
			makeFriendship(friendship[1].toLowerCase(), friendship[0].toLowerCase(), names);
		}
		
		this.islands = makeSubGraphs(this.names); //create islands
		
		for(HashMap<String, Vertex2> h : islands){
			//get connectors for each island
			String root = h.keySet().iterator().next();
			makeConnectors(new HashMap<String, Connection>(), h, root, root, new boolean[numbers.length], 1);
		}
		
	}
	
	
	/*
	 * input: graph
	 * It will break up a graph into subgraphs, if any exist, and return all the subGraphs
	 */
	public ArrayList<HashMap<String, Vertex2>> makeSubGraphs(HashMap<String, Vertex2> h){
		ArrayList<HashMap<String, Vertex2>> s = new ArrayList<HashMap<String, Vertex2>>();
		
		boolean[] visited = new boolean[numbers.length];
		
		ArrayList<String> keys = new ArrayList<String>();
		Iterator<String> i = h.keySet().iterator();
	    while (i.hasNext()) {
	    	keys.add(i.next());
	    }
		
		for(String person : keys){
			if(!visited[h.get(person).vNum]){
				ArrayList<String> people = allVertices(new ArrayList<String>(), h.get(person).name, new boolean[numbers.length], h);
				for(String kid : people){
					if(h.get(kid) != null){
						visited[h.get(kid).vNum] = true;
					}
				}
				
				HashMap<String, Vertex2> temp = new HashMap<String, Vertex2>();
				for(String person2 : people){
					if(h.get(person2) != null){
						temp.put(person2, h.get(person2));
					}
				}
				s.add(temp);
			}
		}
		
		return s;
	}
	
	/*
	 * not sure how to explain this, but pretty much it will give a start and target to a helper method which gets the path
	 * this method then backtracks that path and prints it
	 */
	public void shortestPath(String start, String target) {
		if(names.get(start) == null || names.get(target) == null){//if one of the people isnt in the entire graph
			System.out.println("There is no path from " + start + " to " + target);
			return;
		}
		
		HashMap<String, Path> paths = new HashMap<String, Path>();
		
		for(Vertex2 vert : numbers){
			paths.put(vert.name, new Path(0, null, numbers.length));
		}
		
		Vertex2 source = names.get(start);
		
		paths = shortHelper(paths, source);
		paths.get(start).back = null;
		
		
		if(paths.get(target) != null){
			Stack<String> allPaths = new Stack<String>();
			String place = target;
			
			while(paths.get(place).back != null){
				allPaths.push(place);
				place = paths.get(place).back;
			}
			
			String path = start;
			while(!allPaths.isEmpty()){
				path = path + "--" + allPaths.pop();
			}
			
			if(path.contains(target)){
				System.out.println("Path: " + path);
			}
			else{
				System.out.println("There is no path from " + start + " to " + target);
			}
		}
		
	}
	
	/*
	 * Does most of the work for shortest path because I needed to do it recursively
	 */
	public HashMap<String, Path> shortHelper(HashMap<String, Path> paths, Vertex2 curr){
		Neighbor nbr = curr.adjList;
		for(; nbr != null; nbr = nbr.next){
			if(paths.get(curr.name).visited[nbr.vertexNum] == false){
				break;
			}
		}
		
		if(nbr == null){
			return paths;
		}
		
		for(; nbr != null; nbr = nbr.next){
			if(paths.get(numbers[nbr.vertexNum].name).distance == 0){
				paths.get(numbers[nbr.vertexNum].name).back = curr.name;
				paths.get(numbers[nbr.vertexNum].name).distance = paths.get(curr.name).distance + 1;
			}
			else if(paths.get(numbers[nbr.vertexNum].name).distance > (paths.get(curr.name).distance + 1)){
				paths.get(numbers[nbr.vertexNum].name).back = curr.name;
				paths.get(numbers[nbr.vertexNum].name).distance = paths.get(curr.name).distance + 1;
				for(Neighbor nbr2 = numbers[nbr.vertexNum].adjList; nbr2 != null; nbr2 = nbr2.next){
					paths.get(numbers[nbr.vertexNum].name).visited[nbr2.vertexNum] = false;
				}
			}
			
			paths.get(curr.name).visited[nbr.vertexNum] = true;
		}
		
		for(nbr = curr.adjList; nbr != null; nbr = nbr.next){
			shortHelper(paths, names.get(numbers[nbr.vertexNum].name));
		}
		
		return paths;
	}

	/*
	 * for every island who has at least one kid who goes to the target school, 
	 * this method will hand that to a helper method which does all the work
	 */
	public void cliques(String school){
		boolean hasSchool = false;
		
		for(Vertex2 person : numbers){
			if(person.school != null){
				if(person.school.equals(school)){
					hasSchool = true;
				}
			}
		}
		
		if(hasSchool == false){
			System.out.println("Noone in this graph goes to " + school + ".");
			return;
		}
		
		for(int k = 0; k < islands.size(); k++){
			Iterator<String> i = islands.get(k).keySet().iterator();
		    while (i.hasNext()) {
		    	String key = i.next();
		    	
		    	if(islands.get(k).get(key).school != null){
		    		if(islands.get(k).get(key).school.equals(school)){
		    			//Cant change graph in field so I make a copy
		    			HashMap<String, Vertex2> islandCopy = islands.get(k);
		    			
		    			HashMap<String, Vertex2> temp = new HashMap<String, Vertex2>();
		    			ArrayList<String> keys = new ArrayList<String>();
						Iterator<String> it = islandCopy.keySet().iterator();
					    while (it.hasNext()) {
					    	keys.add(it.next());
					    }
						
					    for(String k2 : keys){
					    	temp.put(k2, new Vertex2(islandCopy.get(k2).name, islandCopy.get(k2).school, islandCopy.get(k2).adjList, islandCopy.get(k2).vNum));
					    }
						
					    cliquesHelper(temp, school);
		    			
		    			break;
		    		}
		    	}
		    }
		}
	}
	
	/*
	 * This method will take out all of the people who dont go to the target school, but are in an island with other kids
	 * from the target school since they are not technically in that school's clique
	 * Then it prints the resulting cliques
	 */
	public void cliquesHelper(HashMap<String, Vertex2> c, String school){
		//This block removes people who dont go to the targer school
		ArrayList<String> keys = new ArrayList<String>();
		Iterator<String> i = c.keySet().iterator();
	    while (i.hasNext()) {
	    	keys.add(i.next());
	    }
	    for(String k : keys){
	    	if(c.get(k).school == null){
	    		c.remove(k);
	    	}
	    	else if(!c.get(k).school.equals(school)){
	    		c.remove(k);
	    	}
	    }
	    
	    //this block removes relationships with people who dont go to the target school
	    keys = new ArrayList<String>();
	    Iterator<String> i2 = c.keySet().iterator();
	    while (i2.hasNext()) {
	    	keys.add(i2.next());
	    }
	    for(String k : keys){
    		Neighbor nbr = c.get(k).adjList;
	    	Neighbor prev = null;
	    	
	    	for(; nbr != null;){
	    		if(numbers[nbr.vertexNum].school == null){
	    			if(prev == null){
	    				c.get(k).adjList = c.get(k).adjList.next;
	    				nbr = c.get(k).adjList;
	    				prev = null;
	    			}
	    			else{
	    				prev.next = nbr.next;
	    				prev = nbr; nbr = nbr.next;
	    			}
	    		}
	    		else if(!numbers[nbr.vertexNum].school.equals(school)){
	    			if(prev == null){
	    				c.get(k).adjList = c.get(k).adjList.next;
	    				nbr = c.get(k).adjList;
	    				prev = null;
	    			}
	    			else{
	    				prev.next = nbr.next;
	    				prev = nbr; nbr = nbr.next;
	    			}
	    		}
	    		
	    		else{
	    			prev = nbr; nbr = nbr.next;
	    		}
	    	}
	    }
	    
	    //this block print the cliques
	    ArrayList<HashMap<String, Vertex2>> s = makeSubGraphs(c);
	    int k = 1;
	    for(HashMap<String, Vertex2> h : s){
	    	System.out.println("Clique " + k + ":");
			print(h);
			k++;
	    }
	    
	}
	
	
	/*
	 * given a person and their graph, this method will return all other people in that graph
	 * its useful for making sub graphs because you may not who who is in a graph with a given person
	 * you must also input an empty boolean[] and ArrayList<String> for it to work
	 */
	public ArrayList<String> allVertices(ArrayList<String> v, String name, boolean[] visited, HashMap<String, Vertex2> h){
		if(!visited[names.get(name).vNum]){
			visited[names.get(name).vNum] = true;
			v.add(name);
		}
		
		if(h.get(name) != null){
			Neighbor nbr = h.get(name).adjList;
			for(; nbr != null; nbr = nbr.next){
				if(!visited[nbr.vertexNum]){
					allVertices(v, numbers[nbr.vertexNum].name, visited, h);
				}
			}
		}
		
		return v;
	}
	
	/*
	 * This will make two people friends in a given graph
	 */
	public void makeFriendship(String f1, String f2, HashMap<String, Vertex2> h){
		h.get(f1).adjList = new Neighbor(names.get(f2).vNum, h.get(f1).adjList);
		return;
	}
	
	/*
	 * This just prints the connectors
	 */
	public void connectors(){
		
		String c = "";
		for(String person : this.connectors){
			c += person + ", ";
		}
		c = (c.length() == 0) ? "There are no connectors" : c.substring(0, c.length() - 2);
		
		System.out.println(c);
			
	}
	
	/*
	 * This method gets all connectors and adds them to the connectors field in the graph
	 */
	public int makeConnectors(HashMap<String, Connection> c, HashMap<String, Vertex2> h, String person, String root, boolean[] visited, int d){
		visited[names.get(person).vNum] = true;
		c.put(person, new Connection(d, d));
		
		Neighbor nbrs = names.get(person).adjList;
		for(Neighbor nbr = nbrs; nbr != null; nbr = nbr.next){
			if(!visited[nbr.vertexNum]){
				d = makeConnectors(c, h, numbers[nbr.vertexNum].name, root, visited, d + 1);
				
				if(c.get(person).dfsNum > c.get(numbers[nbr.vertexNum].name).backNum){
					c.get(person).backNum = Math.min(c.get(person).backNum, c.get(numbers[nbr.vertexNum].name).backNum);
				}
				else if(person.equals(root)){
					
					for(Neighbor nbrsCopy = names.get(root).adjList; nbrsCopy != null; nbrsCopy = nbrsCopy.next){
						if(c.get(numbers[nbrsCopy.vertexNum].name) == null){
							this.connectors.add(person);
							break;
						}
					}
					
				}
				else{
					this.connectors.add(person);
				}
			}
			else{
				c.get(person).backNum = Math.min(c.get(person).backNum, c.get(numbers[nbr.vertexNum].name).dfsNum);
			}
		}
		
		return d;
		
	}
	
	/*
	 * Prints a graph in the form that the input is in
	 */
	public void print(HashMap<String, Vertex2> graph){
		String num = "";
		String people = "";
		
		String relations = "";
		
		Iterator<String> iterator = graph.keySet().iterator();
		int i = 0;
	    while (iterator.hasNext()) {
	    	String key = iterator.next();
	    	Neighbor friends = graph.get(key).adjList;
	    	
	    	people += (graph.get(key).school != null) ? (key + "|y|" + graph.get(key).school + "\n") : (key + "|n" + "\n");
	    	i++;
	    	
	    	for(; friends != null; friends = friends.next){
	    		String reverse = numbers[friends.vertexNum].name + "|" + key;
	    		if(!relations.contains(reverse)){
	    			relations += key + "|" + numbers[friends.vertexNum].name + "\n";
	    		}
			}
	    }
		num += i + "\n";
		
		System.out.println(num + people + relations);
	}
}
