import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;


public class Search 
{
	private HashMap<String, Node> cities;
	private BinaryHeap queue;
	private int numNodes;
	
	public static void main(String[] args) 
	{
		new Search(args[0]);
	}
	
	public Search(String fileName)
	{
		cities = new HashMap<String, Node>();
		numNodes = 0;
		try
		{
			File target = new File("./inputData/" + fileName);
			if (target.exists())
			{	
				System.out.print("Reading city data... ");
				BufferedReader in = new BufferedReader(new FileReader(target));
				String line = in.readLine();
				while (line != null)
				{
					if (line.length() < 5)
					{
						System.out.println("Sketchh: " + line);
					}
					parseLine(line);
					line = in.readLine();
				}
				in.close();
				System.out.println("Done. \n");
				Scanner scan = new Scanner(System.in);
				System.out.println("Number of cities: " + cities.size());
				String c1 = "";
				String c2 = "";
				while (!c1.equals("0") && !c2.equals("0"))
				{
					if(!cities.containsKey(c1))
					{
						System.out.print("Please enter name of start city (0 to quit) : ");
						c1 = scan.nextLine();
						if (!cities.containsKey(c1) && !c1.equals("0"))
						{
							System.out.println(c1 + " not in data-set.");
							c1 = "";
						}
					}
					else if(!cities.containsKey(c2))
					{
						System.out.print("Please enter name of end city (0 to quit) : ");
						c2 = scan.nextLine();
						if (!cities.containsKey(c2) && !c2.equals("0"))
						{
							System.out.println(c2 + " not in data-set.");
							c2 = "";
						}
					}
					else
					{
						System.out.println("Searching for path from " + c1 + " to " + c2);
						aStar(c1, c2);
						System.out.println("------------------------------------");
						System.out.println("Enter 0 to quit, or any other keys to search again: ");
						c1 = scan.nextLine();
						c2 = "";
						System.out.println("-------------------------------------");
					}
				}
				
				System.out.println("Goodbye.");
			}
			else
			{
				System.out.println("Bad File Name.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void aStar(String source, String destination) 
	{
		queue = new BinaryHeap(numNodes*100);
		Node src = cities.get(source);
		Node dest = cities.get(destination);
		double totalCost;
		src.pathCost = 0.0;
		src.parent = null; //No parent, base case
		totalCost = src.pathCost + arcLength(src, dest);
		queue.insert(totalCost, new Node(new String(src.key), src.lat, src.lon, src.pathCost, src.parent, src.edges));
		
		while (!queue.empty() && !src.key.equals(destination))
		{
			src = (Node) queue.getMinData();
			queue.removeMin();
			//System.out.print("Popped Off: " + src.toString());
			//System.out.println("True Cost for Node " + src.key + ": " + (src.pathCost + arcLength(src, dest)));
			if (!src.key.equals(destination))
			{
				Iterator<String> iter = src.edges.keySet().iterator();
				while(iter.hasNext())
				{
					String adjacentNodeKey = iter.next();
					Node adjacentNode = cities.get(adjacentNodeKey);
				
					adjacentNode = new Node(new String(adjacentNode.key), adjacentNode.lat, 
						adjacentNode.lon, (src.pathCost + src.edges.get(adjacentNodeKey)), src, adjacentNode.edges);
				
					totalCost = adjacentNode.pathCost + arcLength(adjacentNode, dest);
					queue.insert(totalCost, adjacentNode);
				}
			}
		}
		if (src.key.equals(destination))
		{
			System.out.println("Target Found: " + src.key + " " + src.lat + " " + src.lon + "\n");
			System.out.print("Route Found: ");
			if (destination.equalsIgnoreCase(source))
			{
				System.out.println("Started at goal; no travel necessary!");
			}
			else
			{
				System.out.println(buildPath(src));
			}
			
			System.out.println("Distance: " + src.pathCost + " miles \n");
			
		}
		else
		{
			System.out.println("Route Found: NO PATH");
			System.out.println("Distance: -1 \n");
		}
		System.out.println();
		System.out.print("Total nodes generated	: ");
		System.out.println(queue.getNumEntries());
		System.out.print("Nodes left in open list	: ");
		System.out.println(queue.getSize());
		System.out.println();
	}

	private String buildPath(Node node) 
	{
		if (node.parent == null)
		{
			return new String(node.key);
		}
		else
		{
			return buildPath(node.parent) + " -> " + node.key;
		}
	}

	private double arcLength(Node src, Node dest) 
	{
		double result = 0.0;
		double latR1 = (src.lat * (Math.PI / 180));
		double latR2 = (dest.lat * (Math.PI / 180));
		double lonR1 = (src.lon * (Math.PI / 180));
		double lonR2 = (dest.lon * (Math.PI / 180));
		double distLat = latR2 - latR1;
		double distLon = lonR2 - lonR1;
		
		double a = (Math.sin((distLat / 2)) * Math.sin((distLat / 2))) 
				+ (Math.cos(latR1) * Math.cos(latR2) * Math.sin(distLon / 2) * Math.sin(distLon / 2));
		
		double c = 2 * (Math.atan2(Math.sqrt(a), Math.sqrt(1-a)));
		
		result = 3959.0 * c;

		return result;
	}

	private void parseLine(String line) 
	{
		if (line.charAt(0) == '#' || line.length() <= 1)
		{
			return; //Comments and empty lines aren't evaluated.
		}
		else
		{
			try
			{
				Reader toParse = new StringReader(line);
				StreamTokenizer parser = new StreamTokenizer(toParse);
				parser.eolIsSignificant(true);
				parser.lowerCaseMode(false);
				parser.parseNumbers();
				parser.ordinaryChar(',');
				parser.ordinaryChar(':');
				parser.wordChars(39, ')');
				parser.nextToken();
				if (parser.ttype == parser.TT_WORD)
				{
					StringBuilder sb = new StringBuilder();
					sb.append(parser.sval);
					parser.nextToken();
					while(parser.ttype == parser.TT_WORD)
					{
						sb.append(" " + parser.sval);
						//System.out.println(parser.sval);
						parser.nextToken();
					}
					if (parser.ttype == parser.TT_NUMBER)
					{
						double lat = parser.nval;
						parser.nextToken();
						double lon = parser.nval;
						Node node = new Node(sb.toString(), lat, lon);
						cities.put(sb.toString(), node);
						numNodes++;
					}
					else if (parser.ttype == ',')
					{
						StringBuilder sb2 = new StringBuilder();
						parser.nextToken();
						sb2.append(parser.sval);
						parser.nextToken();
						while (parser.ttype == parser.TT_WORD)
						{
							sb2.append(" " + parser.sval);
							parser.nextToken();
						}
						parser.nextToken(); //Skip ":"
						addEdges(sb.toString(), sb2.toString(), parser.nval);
						numNodes++;
						
					}
					else
					{
						System.out.println(parser.toString());
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		
	}

	private void addEdges(String node1, String node2, double nval) 
	{
		Node n1 = cities.get(node1);
		Node n2 = cities.get(node2);
		/*
		System.out.println("----------------------------Edge Start------------------");
		System.out.print(node1 + ": "); 
		System.out.println(n1.toString());
		System.out.print(node2 + ": "); 
		System.out.println(n2.toString());
		System.out.println("PAIR VALUE OF: " + nval);
		System.out.println("-------------------------------Edge End------------------");
		*/
		
		n1.edges.put(n2.getKey(), nval);
		n2.edges.put(n1.getKey(), nval);
		
	}

	public class Node
	{
		double lat;
		double lon;
		double pathCost;
		boolean isKnown;
		protected HashMap<String, Double> edges = new HashMap<String, Double>();
		protected Node parent;
		private String key = "";
		
		public Node(String name, double la, double lo)
		{
			lat = la;
			lon = lo;
			pathCost = 0;
			parent = null;
			key = name;
			isKnown = false;
			edges = new HashMap<String, Double>();
		}
		
		public Node(String name, double la, double lo, double dist, Node parent, 
				HashMap<String, Double> map)
		{
			lat = la;
			lon = lo;
			pathCost = dist;
			this.parent = parent;
			key = name;
			isKnown = false;
			edges = map;
		}
		
		public String toString()
		{
			return new String("  Key: " + key + " || Lat: " + lat + " || Lon: " + lon + " || PC: " + pathCost + " || \nEdges: " + edges.toString());
		}
		public String getKey()
		{
			return key;
		}
	}
}
