package it.polito.tdp.PremierLeague.model;

public class Opposite implements Comparable<Opposite>{

	private Player opposite;
	private Double pesoArco;
	
	public Opposite(Player opposite, Double pesoArco) {
		super();
		this.opposite = opposite;
		this.pesoArco = pesoArco;
	}

	public Player getOpposite() {
		return opposite;
	}

	public void setOpposite(Player opposite) {
		this.opposite = opposite;
	}

	public Double getPesoArco() {
		return pesoArco;
	}

	public void setPesoArco(Double pesoArco) {
		this.pesoArco = pesoArco;
	}

	@Override
	public int compareTo(Opposite o) {
		return Double.compare(pesoArco, o.pesoArco);
	}

	@Override
	public String toString() {
		return opposite.playerID+" "+opposite.name+" "+pesoArco;
	}
	
	
}
