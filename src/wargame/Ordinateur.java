package wargame;

// Pas encore implémenté, pour plus tard ^^
public class Ordinateur implements IConfig{
	private int visionGeneral[][]; // cases visibles par l'ordinateur : case visible 1, case non visible 0
	private int nbMonstresVivants;
	private int nbHerosVivants;
	
	private Carte map;
	
	public Ordinateur(int hauteur, int largeur, Carte map) {
		
		visionGeneral = new int[hauteur][largeur];
		
		nbMonstresVivants = NB_MONSTRES;
		nbHerosVivants = NB_HEROS;
		
		this.map = map;
	}
	
	
	public void majVision() {
		
	}
}
