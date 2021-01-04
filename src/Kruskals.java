import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Kruskals {

	// default constructor
	public Kruskals()
	{
	}
	
	// kurksal's algorithm using book's pseudocode
	ArrayList<Edge> kruskal(ArrayList<Edge> edges, int numVertices)
	{
		// disjoint set that corresponds to each vertex
		DisjSets disjointSet = new DisjSets(numVertices);
		// priority queue to use to remove the smallest edges
		PriorityQueue<Edge> smallestWeightEdges = new PriorityQueue<>(edges);
		// array list to hold the minimum spanning tree
		ArrayList<Edge> minSpanTree = new ArrayList<>();
		
		// while the minimum spanning tree has less than the number of vertices minus 1 edges, continue adding smallest weight edges
		while(minSpanTree.size()<(numVertices-1))
		{
			// remove the smallest weight edge
			Edge currentEdge = smallestWeightEdges.remove();
			
			// if the vertices of the edge aren't connected, connect them in the disjoint set and add the vertices to the minimum spanning tree
			if(disjointSet.find(currentEdge.getU())!=disjointSet.find(currentEdge.getV()))
			{
				minSpanTree.add(currentEdge);
				disjointSet.union(disjointSet.find(currentEdge.getU()), disjointSet.find(currentEdge.getV()));
			}
		}
		
		// returning the minimum spanning tree
		return minSpanTree;
	}
	
	// main method
	public static void main(String args[]) throws IOException
	{
		// creating a Kruskal object
		Kruskals outputGraph = new Kruskals();
		// making a new Scanner to read in the names of the vertices
		Scanner inputGraph = new Scanner(new File("assn9_data.csv"));
		ArrayList<String> correspondingVertices = new ArrayList<>(); // array list to hold the names of the vertices that correspond to the ints in the disjoint set
		ArrayList<Edge> edges = new ArrayList<>(); // array list to hold the edges in the graph
		
		// loop to read in all the corresponding vertices 
		while(inputGraph.hasNextLine())
		{
			String line = inputGraph.nextLine(); // gets a whole line
			// gets the current vertex that we are on
			String currentVertex = line.substring(0, line.indexOf(","));
			correspondingVertices.add(currentVertex); // adds the current vertex to the array list of vertices that correspond to the ints in the disjoint set
		}
		
		// closes the scanner
		inputGraph.close();

		// make a new scanner to read in the edges and weights
		inputGraph = new Scanner(new File("assn9_data.csv"));

		// while there is data in the adjacency list(.csv file), keep reading
		while(inputGraph.hasNextLine())
		{
			String line = inputGraph.nextLine(); // gets a whole line
			// gets the current vertex that we are on
			String currentVertex = line.substring(0, line.indexOf(","));
			// removes the current vertex from the current line we are on
			line = line.substring(line.indexOf(",")+1);
			
			// while there is still more data to be read on the current line, keep reading in data
			while(line.indexOf(",")>=0)
			{
				// read in each vertex connected to the current vertex
				String connectedVertex = line.substring(0, line.indexOf(","));
				line = line.substring(line.indexOf(",")+1); // remove the connected vertex from the current line we are on
				int weight; // int variable to hold weight of the edge
				if(line.indexOf(",")>=0) // if there are more items on the line only do up to the comma
				{
					// take in the weight of the edge
					weight = Integer.parseInt(line.substring(0, line.indexOf(",")));
					line = line.substring(line.indexOf(",")+1); // remove the int from the current line we are on
				}
				else // if there are no more items on the line, you can do the rest of the line
				{
					weight = Integer.parseInt(line); // take in the weight of the edge
				}
				
				// set the current and connected vertex numbers to negative to detect errors
				int currentVertexNumber = -1;
				int connectedVertexNumber = -1;
				
				// loop to find out what the corresponding vertex number is for the current and connected vertex
				for(int i = 0; i < correspondingVertices.size(); i++)
				{
					// set current vertex number to i if found
					if(correspondingVertices.get(i).contentEquals(currentVertex))
					{
						currentVertexNumber = i;
					}
					// set connected vertex number to i if found
					if(correspondingVertices.get(i).contentEquals(connectedVertex))
					{
						connectedVertexNumber = i;
					}
				}
				
				// if connected and current vertex numbers are above 0, that is, there is no error, add the edge with the vertices and weight to the array list of edges
				if(connectedVertexNumber>=0 && connectedVertexNumber >=0)
					edges.add(new Edge(currentVertexNumber, connectedVertexNumber, weight));
				else // else print an error message saying that there was an error determining the corresponding vertices
					System.out.println("Error reading in corresponding vertices");
			}
		}
		// closing the scanner
		inputGraph.close();
		
		// call the kruskal's algorithm method
		ArrayList<Edge> minSpanTree = outputGraph.kruskal(edges, correspondingVertices.size());
		
		// int accumulating variable to hold the sum of the distances in the tree
		int sumDistances = 0;
		
		// loop through and print the minimum spanning tree
		for(int i = 0; i < minSpanTree.size(); i++)
		{
			// print the vertices corresponding to the number in the disjoint set for each edge in the minimum spanning tree along with the weight
			System.out.println(correspondingVertices.get(minSpanTree.get(i).getU()) + ", " + correspondingVertices.get(minSpanTree.get(i).getV()) + ", " + minSpanTree.get(i).getWeight());
			sumDistances += minSpanTree.get(i).getWeight(); // add the current edge's distance to the sum of the distances
		}
		
		// print out the sum of the distances
		System.out.println("\nSum of distances: " + sumDistances);
	}
}

// class to hold data about each edge in the graph
class Edge implements Comparable<Edge>
{
	// edge = {u, v}
	private int u; // represents one vertex connected by the edge
	private int v; // represents the other vertex connected by the edge
	private int weight; // represents the weight of the edge
	
	// default contructor
	public Edge()
	{
		u = 0;
		v = 0;
		weight = 0;
	}
	
	// constructor with parameters
	public Edge(int u, int v, int weight)
	{
		this.u = u;
		this.v = v;
		this.weight = weight;
	}
	
	// gets the first vertex that's a part of the connection
	public int getU()
	{
		return u;
	}
	
	// gets the second vertex that's a part of the connection
	public int getV()
	{
		return v;
	}
	
	// gets the weight of the current edge
	public int getWeight()
	{
		return weight;
	}
	
	// compareTo method which is required when implementing Comparable
	public int compareTo(Edge otherEdge)
	{
		// if the current weight is smaller than the other edge's weight, return -1
		if(this.getWeight()<otherEdge.getWeight())
		{
			return -1;
		}
		// if the current weight is the same return 0
		else if(this.getWeight()<otherEdge.getWeight())
		{
			return 0;
		}
		// if the current weight is bigger than the other edge's weight, return 1
		else
		{
			return 1;
		}
	}
}