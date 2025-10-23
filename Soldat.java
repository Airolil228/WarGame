package wargame;

public abstract class Soldat extends Element /*implements ISoldat*/{
	private final int POINTS_DE_VIE_MAX, PUISSANCE, TIR, PORTEE_VISUELLE;
	private int pointsDeVie;
	/*(…)*/
	Soldat(Carte carte, int pts, int portee, int puiss, int tir, Position pos) {
		POINTS_DE_VIE_MAX = pointsDeVie = pts;
		PORTEE_VISUELLE = portee; PUISSANCE = puiss; TIR = tir;
		this.carte = carte; this.pos = pos;
	}
	/*(…)*/
}