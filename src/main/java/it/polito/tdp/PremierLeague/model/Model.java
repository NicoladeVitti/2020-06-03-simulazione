package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	Graph<Player, DefaultWeightedEdge> grafo;
	PremierLeagueDAO dao;
	Map<Integer, Player> idMap;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
	}

	public void creaGrafo(Double golFatti) {
		
		dao.listAllPlayers(idMap);
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//AGGIUNGO I VERTICI 
		Graphs.addAllVertices(this.grafo, dao.getVertici(golFatti, idMap));
		
		//AGGIUNGO GLI ARCHI
		List<Adiacenza> adiacenze = dao.getAdiacenze(idMap);
		for(Adiacenza a : adiacenze) {
			if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
				Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
			}
		}
		
		
		
	}
	
	public Integer getNumArchi() {
		return grafo.edgeSet().size();
	}
	
	public Integer getNumVertici() {
		return grafo.vertexSet().size();
	}
	

	public Player topPlayer() {
		
		Player top = null;
		Integer maxGiocatoriBattuti = 0;
		
		for(Player p : grafo.vertexSet()){
			Integer archiUscenti = grafo.outDegreeOf(p);
			
			if(archiUscenti > maxGiocatoriBattuti) {
				maxGiocatoriBattuti = archiUscenti;
				top = p;
			}
		}
		
		return top;
	}
	
	public List<Opposite> giocatoriBattuti(){
		
		List<Opposite> result = new LinkedList<>();
		Player top = this.topPlayer();
		
		for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(top)) {
			Opposite o = new Opposite(Graphs.getOppositeVertex(this.grafo, e, top), grafo.getEdgeWeight(e));
			result.add(o);
		}
		
		Collections.sort(result);
		return result;
		
			
	}
}
