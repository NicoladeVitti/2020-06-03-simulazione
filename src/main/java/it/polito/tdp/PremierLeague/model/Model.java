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
	List<Player> dreamTeam;
	Double gradoDiTitolaritaMAX;
	
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

	private Double getGradoTitolaritaPlayer(Player p) {
		
		Double archiEntranti = 0.0;
		Double archiUscenti = 0.0;
		
		for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(p)) {
			archiUscenti += grafo.getEdgeWeight(e);
		}
		
		for(DefaultWeightedEdge e : grafo.incomingEdgesOf(p)) {
			archiEntranti += grafo.getEdgeWeight(e);
		}
		
		return (archiUscenti - archiEntranti);
		
	}
	
	private Double getGradoDiTitolaritaTotale(List<Player> par) {
		
		Double tot = 0.0;
		
		for(Player p : par) {
			tot += this.getGradoTitolaritaPlayer(p);
		}
		
		return tot;
	}
	
	public List<Player> dreamTeam(Integer giocatori) {
		
		this.dreamTeam = new LinkedList<Player>();
		this.gradoDiTitolaritaMAX = 0.0;
		List<Player> parziale = new LinkedList<Player>();
		List<Player> esclusi = new LinkedList<Player>();
		
		ricorsione(parziale, giocatori, esclusi);
		
		return dreamTeam;
		
		
	}

	private void ricorsione(List<Player> parziale, Integer giocatori, List<Player> esclusi) {
		
		if(parziale.size() == giocatori) {
			if(this.gradoDiTitolaritaMAX < this.getGradoDiTitolaritaTotale(parziale)) {
				this.gradoDiTitolaritaMAX = this.getGradoDiTitolaritaTotale(parziale);
				this.dreamTeam = new LinkedList<>(parziale);
			}
			
			return;
		}
		
		else {
			
			for(Player p : grafo.vertexSet()) {
				
				if(!parziale.contains(p) && !esclusi.contains(p)) {
					
					parziale.add(p);
					
					for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(p)) {
						esclusi.add(Graphs.getOppositeVertex(this.grafo, e, p));
					}
					
					ricorsione(parziale, giocatori, esclusi);
					
					parziale.remove(parziale.size()-1);
					esclusi.remove(esclusi.size()-1);
					
					
				}	
				
			}
		}
	}
}
