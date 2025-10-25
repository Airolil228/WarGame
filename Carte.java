package wargame;

import java.awt.Graphics;

import wargame.ISoldat.TypesH;

public class Carte implements ICarte{
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
		tab[5][5] = new Heros(this,ISoldat.TypesH.HUMAIN,"BLOUP BLOUP",new Position(5,5));
		
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
	public Element getElement(int x,int y) {
		return tab[x][y];
	}
	
	public Position trouvePositionVide() {
		// Trouve aléatoirement une position vide sur la carte
		Position p = new Position(0,0);
		return p;
	}
	public Position trouvePositionVide(Position pos) {
		// Trouve une position vide choisie
		// al�atoirement parmi les 8 positions adjacentes de pos
		Position p = new Position(0,0);
		return p;
	}
	public Heros trouveHeros() {
		// Trouve al�atoirement un h�ros sur la carte
		Heros h = new Heros(this,ISoldat.TypesH.HUMAIN,"BLOUP BLOUP",new Position(5,5));
		return h;
	}
	public Heros trouveHeros(Position pos) {
		// Trouve un h�ros choisi al�atoirement
		Heros h = new Heros(this,ISoldat.TypesH.HUMAIN,"BLOUP BLOUP",new Position(5,5));
		return h;
	}
									 // parmi les 8 positions adjacentes de pos
	public boolean deplaceSoldat(Position pos, Soldat soldat) {
		return false;
	}
	public void mort(Soldat perso) {
		
	}
	public boolean actionHeros(Position pos, Position pos2) {
		return false;
	}
	public void jouerSoldats(PanneauJeu pj) {
		
	}
	public void toutDessiner(Graphics g) {
		
	}
}