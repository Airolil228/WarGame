package wargame;

//import java.awt.Graphics;

import wargame.ISoldat.TypesH;

public class Carte implements ICarte, IConfig{
	private Element[][] tab;
	private Heros[] armeeHeros;
	private Monstre[] armeeMonstre;
	private int nbHerosVivant;
	private int nbMonstreVivant;
	private int hauteur;
	private int largeur;
	public Carte(int hauteur, int largeur) {
		int i,j;
		this.hauteur = hauteur;
		this.largeur = largeur;
		nbHerosVivant = NB_HEROS;
		nbMonstreVivant = NB_MONSTRES;
		tab = new Element[hauteur][largeur];
		armeeHeros = new Heros[NB_HEROS];  // Vide
		armeeMonstre = new Monstre[NB_MONSTRES];   // Vide
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
		boolean b = false;
		int x = -1;
		int y = -1;
		
		while (!b) {
			x = (int) (Math.random() * LARGEUR_CARTE);
			y = (int) (Math.random() * HAUTEUR_CARTE);
			
			if (tab[y][x] instanceof Plaine) {
				b = true;
			}
		}
		Position p = new Position(y,x);
		return p;
	}
	
	public Position trouvePositionVide(Position pos) {
		// Trouve une position vide choisie
		// al�atoirement parmi les 8 positions adjacentes de pos
		boolean b = false;
		int x = -1;
		int y = -1;
		
		while (!b) {
			x = (int) (Math.random() * 3 - 1);  // entre -1 et 1
			y = (int) (Math.random() * 3 - 1);
			
			if (tab[pos.getY()+y][pos.getX()+x] instanceof Plaine) {
				b = true;
			}
		}
		
		
		Position p = new Position(y,x);
		return p;
	}
	public Heros trouveHeros() {
		// Trouve al�atoirement un h�ros sur la carte
		Heros h;
		int x;
		
		x = (int) (Math.random() * nbHerosVivant);  // de 0 à nbHerosVivant-1 pour indice
		
		h = armeeHeros[x];
		
		return h;
	}
	public Heros trouveHeros(Position pos) {
		// Trouve un h�ros choisi al�atoirement
		// parmi les 8 positions adjacentes de pos
		Heros h = null;
		boolean b = true;
		int x = -1;
		int y = -1;
		
		// Verification qu'il y en a au moins 1
		for (int i=-1;i<=1;i++) {
			for (int j=-1;j<=1;j++) {
				if (tab[pos.getY()+i][pos.getX()+j] instanceof Heros) {
					b = false;
					i = 1;
					j=1;
				}
			}
		}
		
		while (!b) {
			x = (int) (Math.random() * 3 - 1);  // entre -1 et 1
			y = (int) (Math.random() * 3 - 1);
			
			if (tab[pos.getY()+y][pos.getX()+x] instanceof Heros) {
				b = true;
				h = (Heros) tab[y][x];
			}
		}
		
		
		return h;
	}
	
	public boolean deplaceSoldat(Position pos, Soldat soldat) {
		int dx=0;
		int dy=0;
		
		// Distance x entre le soldat et la pos
		if (pos.getX() > soldat.getPos().getX())
			dx = pos.getX() - soldat.getPos().getX();
		else// Distance x entre le soldat et la pos
			dx = soldat.getPos().getX() - pos.getX();
		
		// Distance y entre le soldat et la pos
		if (pos.getY() > soldat.getPos().getY())
			dy = pos.getY() - soldat.getPos().getY();
		else
			dy = soldat.getPos().getY() - pos.getY();
		
		// Possibilités de déplacement
		if ((dx > dy && dx < soldat.getPortee())||(dx < dy && dy < soldat.getPortee())) {
			// On peut se deplacer donc on se deplace
			tab[pos.getY()][pos.getX()] = soldat;
			tab[soldat.getPos().getY()][soldat.getPos().getX()] = new Plaine();
			soldat.setPos(pos);
			return true;
		}
		
		return false;
	}
	public void mort(Soldat perso) {
		
		if (perso instanceof Heros) {
		
			for (int i=0;i<nbHerosVivant;i++) {
				if (armeeHeros[i].equals(perso)) {
					
					// Si on le trouve on le déplace jusqu'a la fin en décalant les autres de une pos pour garder une liste d'armée héors vivante de 0 à nbHerosVivant
					
					for (int j=i;j<nbHerosVivant;j++) {
						armeeHeros[j] = armeeHeros[j+1];
					}
					armeeHeros[nbHerosVivant-1] = (Heros) perso; // On le garde au cas où mais plus accessible
					
					i = nbHerosVivant;	// Sortir de la boucle
				}
			}
			
			tab[perso.getPos().getY()][perso.getPos().getX()] = new Plaine(); // Joueur mort donc il n'est plus là
			nbHerosVivant --;
			
		}
		
		
		if (perso instanceof Monstre) {
			
			for (int i=0;i<nbMonstreVivant;i++) {
				if (armeeMonstre[i].equals(perso)) {
					
					// Si on le trouve on le déplace jusqu'a la fin en décalant les autres de une pos pour garder une liste d'armée monstres vivants de 0 à nbMonstreVivant
					
					for (int j=i;j<nbMonstreVivant;j++) {
						armeeMonstre[j] = armeeMonstre[j+1];
					}
					armeeMonstre[nbMonstreVivant-1] = (Monstre) perso; // On le garde au cas où mais plus accessible
					
					i = nbMonstreVivant;	// Sortir de la boucle
				}
			}
			
			tab[perso.getPos().getY()][perso.getPos().getX()] = new Plaine(); // Joueur mort donc il n'est plus là
			nbMonstreVivant --;
			
		}
		
	}
	
	public boolean actionHeros(Position pos, Position pos2) {
		return false;  // 
	}
	public void jouerSoldats(PanneauJeu pj) {
		
	}
/*	public void toutDessiner(Graphics g) {
		
	}*/
}