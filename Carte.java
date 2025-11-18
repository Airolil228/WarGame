package wargame;

import java.awt.Graphics;

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
	private Position select;
	public Carte(int hauteur, int largeur) {
		select = new Position(-1,-1);
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
	
	public void initArmeeHeros() {
		for (int i = 0; i< NB_HEROS;i++) {
			Position p = trouvePositionVide();
			Heros h = new Heros(this, ISoldat.TypesH.HUMAIN, "Robert", p);
			armeeHeros[i] = h;
			tab[p.getY()][p.getX()] = h;
		}
	}
	
	public void initArmeeMonstre() {
		for (int i = 0; i< NB_MONSTRES;i++) {
			Position p = trouvePositionVide();
			Monstre m = new Monstre(this, ISoldat.TypesM.GOBELIN, "Patrick", p);
			armeeMonstre[i] = m;
			tab[p.getY()][p.getX()] = m;
		}
	}
	
	public void initObstacleAlea(int nb_ob) {
		for (int i = 0; i< nb_ob;i++) {
			Position p = trouvePositionVide();
			tab[p.getY()][p.getX()] = new Obstacle(Obstacle.TypeObstacle.ROCHER, p);
		}
	}
	
	public void initRiviereAlea(int nb_riv) {
		Position p = trouvePositionVide();
		tab[p.getY()][p.getX()] = new Obstacle(Obstacle.TypeObstacle.EAU, p);
		nb_riv --;
		
		while (nb_riv > 0) {
			p = trouvePositionVide(p);
			tab[p.getY()][p.getX()] = new Obstacle(Obstacle.TypeObstacle.EAU, p);
			nb_riv --;
		}
	}
	public void toutDessiner(Graphics g) {
		// TODO Stub de la méthode généré automatiquement
		for (int x = 0; x < HAUTEUR_CARTE; x++) {
            for (int y = 0; y < LARGEUR_CARTE; y++) {
            	switch (getElement(x,y).getClass().getSimpleName()) {
            	case ("Plaine"):
                    g.setColor(COULEUR_PLAINE);
                    g.fillRect(y * NB_PIX_CASE, x * NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
                    break;
            	case ("Obstacle"):
                    g.setColor(COULEUR_ROCHER); // Obstacle: prendre en compte si c'est de l'eau, un rocher, une forêt (dans Obstacle.java)
                    g.fillRect(y * NB_PIX_CASE, x * NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
                    break;
            	case ("Heros"):
                    g.setColor(COULEUR_HEROS);  // Prendre en compte les héros déjà joués également
                    g.fillRect(y * NB_PIX_CASE, x * NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
                    break;
            	case ("Monstre"):
                    g.setColor(COULEUR_MONSTRES);
                    g.fillRect(y * NB_PIX_CASE, x * NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
                    break;
            	}
            	

                g.setColor(COULEUR_TEXTE); // contour
                g.drawRect(y * NB_PIX_CASE, x * NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
            }
        }
		g.setColor(COULEUR_SELECTION);
		if (select.getY() != -1) { // Si pas de selection x = -1 et y = -1
			if (select.getX() >= 0 && select.getX() < hauteur && select.getY() >= 0 && select.getY() < largeur ) {
				g.drawRect(select.getY() * NB_PIX_CASE, select.getX() * NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE); // Draw : Y puis X
				if (tab[select.getX()][select.getY()] instanceof Heros) {  // Position getX getY
					Heros h = (Heros) tab[select.getX()][select.getY()];
					System.out.println(" Espèce : " + h.getTYPE() + ", nom :" + h.getNom());
					
				}
			}
		}
	}
	public Position getSelect() {
		Position p = new Position(select.getX(),select.getY());
		return p;
	}
	public void marquerCase(int y, int x) {
	    select = new Position(y,x);
	}
	// A faire
	public boolean actionHeros(Position pos, Position pos2) {
		return false;  // 
	}
	public void jouerSoldats(PanneauJeu pj) {
		
	}
}