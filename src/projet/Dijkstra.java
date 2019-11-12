package projet;

import java.util.ArrayList;
import java.util.List;

public class Dijkstra {
	// A utility function to find the vertex with minimum distance value, 
    // from the set of vertices not yet included in shortest path tree 
    static final int V = 35; 
    int minDistance(int dist[], Boolean sptSet[]) 
    { 
        // Initialize min value 
        int min = Integer.MAX_VALUE;
        int min_index = -1; 
  
        for (int v = 0; v < V; v++) 
            if (sptSet[v] == false && dist[v] <= min) { 
                min = dist[v]; 
                min_index = v; 
            } 
  
        return min_index; 
    } 
  
    // A utility function to print the constructed distance array 
    void printSolution(int dist[], List<List<Integer>> chemin) 
    { 
        System.out.println("Vertex \t\t Distance \t\t chemin"); 
        for (int i = 0; i < V; i++) 
            System.out.println(i + " \t\t " + dist[i] + " \t\t\t " + see(chemin.get(i))); 
    } 
  
   

	// Function that implements Dijkstra's single source shortest path 
    // algorithm for a graph represented using adjacency matrix 
    // representation 
    public List<Integer> dijkstra(int graph[][], int src, int destination) 
    { 
    	List<List<Integer>>  chemin= new ArrayList<List<Integer>>() ;
        int dist[] = new int[V]; // The output array. dist[i] will hold 
        // the shortest distance from src to i 
  
        // sptSet[i] will true if vertex i is included in shortest 
        // path tree or shortest distance from src to i is finalized 
        Boolean sptSet[] = new Boolean[V]; 
  
        // Initialize all distances as INFINITE and stpSet[] as false 
        for (int i = 0; i < V; i++) { 
            dist[i] = Integer.MAX_VALUE; 
            sptSet[i] = false; 
            chemin.add(new ArrayList<Integer>());
        } 
  
        // Distance of source vertex from itself is always 0 
        dist[src] = 0; 
  
        // Find shortest path for all vertices 
        for (int count = 0; count < V - 1; count++) { 
            // Pick the minimum distance vertex from the set of vertices 
            // not yet processed. u is always equal to src in first 
            // iteration. 
            int u = minDistance(dist, sptSet); 
  
            // Mark the picked vertex as processed 
            sptSet[u] = true;
            chemin.get(u).add(u);
  
            // Update dist value of the adjacent vertices of the 
            // picked vertex. 
            for (int v = 0; v < V; v++) 
  
                // Update dist[v] only if is not in sptSet, there is an 
                // edge from u to v, and total weight of path from src to 
                // v through u is smaller than current value of dist[v] 
                if (!sptSet[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]){ 
                    dist[v] = dist[u] + graph[u][v]; 
                    chemin.get(v).clear();
                    chemin.get(v).addAll(chemin.get(u));
                }
        } 
  
        // print the constructed distance array 
        //printSolution(dist, chemin); 
        return chemin.get(destination);
    } 
  
    // Driver method 
    /*public static void main(String[] args) 
    { 
        
    	ArrayList<Integer> map = new ArrayList<Integer>(35);
    	map.add(4);map.add(2);map.add(1);map.add(1);map.add(0);
		map.add(0);map.add(2);map.add(1);map.add(1);map.add(1);
		map.add(0);map.add(2);map.add(2);map.add(1);map.add(3);
		map.add(0);map.add(0);map.add(2);map.add(1);map.add(1);
		map.add(0);map.add(3);map.add(3);map.add(3);map.add(1);
		map.add(0);map.add(0);map.add(0);map.add(0);map.add(2);
		map.add(0);map.add(0);map.add(0);map.add(0);map.add(2);
    	int graph[][] = GrapheCreator(map);
        Dijkstra t = new Dijkstra (); 
        t.dijkstra(graph, 4,0); 
        //see(graph);
       // see(map);
    *///}
    
    
    private String see(List list) {
    	String chemin="";
    	for(int i=0; i<list.size() ; i++){
    		chemin+=list.get(i);
    	}
		return chemin;
	}

    private static void see(ArrayList<Integer> map) {
    	for(int i=0; i<35 ; i++){
    		System.out.print(map.get(i));
    	}
	}

	private static void see(int[][] graph) {
		for(int i=0; i<35 ; i++){
			for(int j=0; j<34 ; j++){
				System.out.print(graph[i][j]);			
			}
			System.out.println(graph[i][34]);
		}
		
	}

	//fonction pour cree le graphe a partir du MAP
	public int[][] GrapheCreator(ArrayList<Integer> map) {
		int graph[][] = new int[35][35];
		
		for(int i=0; i<35 ; i++){
			for(int j=0; j<35 ; j++){
				if ((i==j+1 && i%5!=0) || (i==j-1 && i%5!=4) || i==j+5 || i==j-5){
					
					if (map.get(i)== 3){
						graph[i][j]= 5;
					}else if(map.get(i)==2){
						graph[i][j]= 10;
					}else if(map.get(i)==5){
						graph[i][j]=900;
					}else{
						graph[i][j]= 1;
					}
				
				}else {graph[i][j]=0;}
			}
		}
		return graph;
	} 
}  	

