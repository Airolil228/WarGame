package wargame;

public abstract class Soldat extends Element implements ISoldat{
	private final int POINTS_DE_VIE_MAX, PUISSANCE, TIR, PORTEE_VISUELLE;
	private int pointsDeVie;
	private Carte carte;
	private Position pos;
	/*(…)*/
	Soldat(Carte carte, int pts, int portee, int puiss, int tir, Position pos) {
		POINTS_DE_VIE_MAX = pointsDeVie = pts;
		PORTEE_VISUELLE = portee; PUISSANCE = puiss; TIR = tir;
		this.carte = carte; this.pos = pos;
	}
	
	public int getPoints() {
		return 0; /* A remplacer */
	}
	
	public int getTour() {
		return 0; /* A remplacer */
	}
	
	public int getPortee() {
		return 0; /* A remplacer */
	}
	
	public void joueTour(int tour) {
		
	}
	
	public void combat(Soldat soldat) {
		
	}

	public void seDeplace(Position newPos) {
		
	}
}