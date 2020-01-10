package projet;

import java.util.ArrayList;
import java.util.List;
/**
 * Pemet de calculer le chemin le plus court 
 */
public class Dijkstra {
	
    final int V = 35;
    
    //  
    public int minDistance(int dist[], Boolean sptSet[]) { 
        int valeur= Integer.MAX_VALUE;
        int minimun_index = -1; 
        for (int i = 0; i < V; i++) 
            if (sptSet[i] == false && dist[i] <= valeur) { 
                valeur = dist[i]; 
                minimun_index = i; 
            } 
  
        return minimun_index; 
    } 
    
    
	// Calsule le plus court chemin partant de src jusqu'a destination
    public List<Integer> dijkstra(int graph[][], int src, int destination) { 
    	List<List<Integer>>  chemin= new ArrayList<List<Integer>>() ;
        int dist[] = new int[V]; // liste de poid minimun 
        // the shortest distance from src to i 
  
        // sptSet[i] will true if vertex i is included in shortest 
        // sptSet[i] est egal a true si on a trouver le chemin le plus court pour l'indice i
        Boolean sptSet[] = new Boolean[V]; 
  
        for (int i = 0; i < V; i++) { 
            dist[i] = Integer.MAX_VALUE; 
            sptSet[i] = false; 
            chemin.add(new ArrayList<Integer>());
        } 
  
        // la distance a la position inicial est toujour 0
        dist[src] = 0; 
        //on met en place algorithme de dijckstra
        for (int count = 0; count < V - 1; count++) { 
            int u = minDistance(dist, sptSet); 
            sptSet[u] = true;
            chemin.get(u).add(u); 
            for (int v = 0; v < V; v++) 
                if (!sptSet[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]){ 
                    dist[v] = dist[u] + graph[u][v]; 
                    chemin.get(v).clear();
                    chemin.get(v).addAll(chemin.get(u));
                }
        } 
        return chemin.get(destination);
    } 

	//fonction pour cree le graphique utilise par la fonction dijckstra a partir de la varialbe map
	public int[][] GrapheCreator(ArrayList<Integer> map) {
		int graph[][] = new int[35][35];
		
		for(int i=0; i<35 ; i++){
			for(int j=0; j<35 ; j++){
				if ((i==j+1 && i%5!=0) || (i==j-1 && i%5!=4) || i==j+5 || i==j-5){
					
					if (map.get(i)== 3){//case orange
						graph[i][j]= 5;
					}else if(map.get(i)==2){//case bleu
						graph[i][j]= 10;
					}else if(map.get(i)==5){//obstacle
						graph[i][j]=900;
					}else{
						graph[i][j]= 1;
					}
				
				}else {graph[i][j]=0;}//si il y a pas de 
			}
		}
		return graph;
	} 
}  	

