package wargame;

public class Carte {
	private Element[][] Jeu;
	public Carte(int hauteur, int largeur) {
		Jeu = new Element[hauteur][largeur];
	}
	public Element[][] getJeu() {
		return Jeu;
	}
	public void setJeu(Element[][] jeu) {
		Jeu = jeu;
	}
}