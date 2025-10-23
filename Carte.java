package wargame;

public class Carte {
	private Element[][] tab;
	private int hauteur;
	private int largeur;
	public Carte(int hauteur, int largeur) {
		int i,j;
		this.hauteur = hauteur;
		this.largeur = largeur;
		tab = new Element[hauteur][largeur];
		for (i=0;i<hauteur;i++) {
			for (j=0;j<largeur;j++) {
				tab[i][j] = new Plaine();
			}
		}
	}
	public Element[][] getJeu() {
		return tab;
	}
	public void setJeu(Element[][] jeu) {
		tab = jeu;
	}
	public void afficherCarteConsole() {
		int i,j;
		System.out.println("Affichage de la carte");
		for (j=0;j<largeur;j++) {
			System.out.print("-------");
		}
		System.out.println();
		for (i=0;i<hauteur;i++) {
			System.out.print("|");
			for (j=0;j<largeur;j++) {
				if (tab[i][j].EstVisible()) {
					System.out.print(" "+ tab[i][j].getClass().getSimpleName() +" ");
				}else {
					System.out.print(" Vide ");
				}
				System.out.print("|");
			}
			System.out.println("");
		}
		for (j=0;j<largeur;j++) {
			System.out.print("-------");
		}
	}
	public Element getElement(Position pos) {
		return tab[pos.getX()][pos.getY()];
	}
}