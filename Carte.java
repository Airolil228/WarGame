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
		//tab[5][5] = new Heros(this,ISoldat.TypesH.HUMAIN,"BLOUP BLOUP",new Position(5,5));
		initArmeeHeros();
		initArmeeMonstre();
		initObstacleAlea();
		initRiviereAlea(7);
		initRiviereAlea(6);
		initRiviereAlea(5);
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
		Position p = new Position(x,y);
		return p;
	}
	public Position trouvePositionVideHeros() {
		// Trouve aléatoirement une position vide sur la carte
		boolean b = false;
		int x = -1;
		int y = -1;
		
		while (!b) {
			x = (int) (Math.random() * (LARGEUR_CARTE/2));
			y = (int) (Math.random() * HAUTEUR_CARTE);
			
			if (tab[y][x] instanceof Plaine) {
				b = true;
			}
		}
		Position p = new Position(x,y);
		return p;
	}
	public Position trouvePositionVideMonstre() {
		// Trouve aléatoirement une position vide sur la carte
		boolean b = false;
		int x = -1;
		int y = -1;
		
		while (!b) {
			x = (int) ((Math.random() * (LARGEUR_CARTE/2))+(LARGEUR_CARTE/2));
			y = (int) (Math.random() * HAUTEUR_CARTE);
			
			if (tab[y][x] instanceof Plaine) {
				b = true;
			}
		}
		Position p = new Position(x,y);
		return p;
	}
	
	private boolean verifRivierePossible(Position p) {
		int y,x;
		for (int i = -1;i<=1;i++) {
			y = p.getY() + i;
			for (int j = -1;j<=1;j++) {
				x = p.getX() + j;
				if ((x >= 0 && x < LARGEUR_CARTE) && (y >= 0 && y < HAUTEUR_CARTE) )  {
					if (tab[y][x] instanceof Plaine) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Position trouvePositionVide(Position pos) {
		// Trouve une position vide choisie
		// al�atoirement parmi les 8 positions adjacentes de pos
		boolean b = false;
		int x = -1;
		int y = -1;
		
		if (verifRivierePossible(pos)) { // Si on ne peut pas mettre de rivière on doit pas entrer dans la boucle
		
			while (!b) {
				x = ((int) (Math.random() * 3 - 1)) + pos.getX();  // entre -1 et 1
				y = ((int) (Math.random() * 3 - 1)) + pos.getY();
				
				if ((x >= 0 && x < LARGEUR_CARTE) && (y >= 0 && y < HAUTEUR_CARTE) )  {
					if (tab[y][x] instanceof Plaine) {
						b = true;
					}
				}
			}
			
		}
		
		
		Position p = new Position(x,y);
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
			Position p = trouvePositionVideHeros();
			int type = (int) (Math.random() * ISoldat.nbTypeHeros);
			ISoldat.TypesH th = ISoldat.TypesH.ELF;
			switch (type) {
			case(0):
				th = ISoldat.TypesH.ELF;
				break;
			case(1):
				th = ISoldat.TypesH.HOBBIT;
				break;
			case(2):
				th = ISoldat.TypesH.HUMAIN;
				break;
			case(3):
				th = ISoldat.TypesH.NAIN;
				break;
			default:
				;
			}
			
			Heros h = new Heros(this, th, "Robert", p);
			armeeHeros[i] = h;
			tab[p.getY()][p.getX()] = h;
		}
	}
	
	public void initArmeeMonstre() {
		for (int i = 0; i< NB_MONSTRES;i++) {
			Position p = trouvePositionVideMonstre();
			int type = (int) (Math.random() * ISoldat.nbTypeMonstre);
			ISoldat.TypesM th = ISoldat.TypesM.GOBELIN;
			switch (type) {
			case(0):
				th = ISoldat.TypesM.GOBELIN;
				break;
			case(1):
				th = ISoldat.TypesM.ORC;
				break;
			case(2):
				th = ISoldat.TypesM.TROLL;
				break;
			default:
				;
			}
			
			Monstre m = new Monstre(this, th, "Patrick", p);
			armeeMonstre[i] = m;
			tab[p.getY()][p.getX()] = m;
		}
	}
	
	public void initObstacleAlea() {
		for (int i = 0; i< NB_OBSTACLES;i++) {
			Position p = trouvePositionVide();
			
			int type = (int) (Math.random() * Obstacle.nbObstacle);
			Obstacle.TypeObstacle th = Obstacle.TypeObstacle.ROCHER;
			switch (type) {
			case(0):
				th = Obstacle.TypeObstacle.ROCHER;
				break;
			case(1):
				th = Obstacle.TypeObstacle.FORET;
				break;
			case(2):
				th = Obstacle.TypeObstacle.EAU;
				break;
			default:
				;
			}
			
			tab[p.getY()][p.getX()] = new Obstacle(th, p);
		}
	}
	
	public void initRiviereAlea(int tailleRiviere) {
		int nb_riv = tailleRiviere;
		Position p = trouvePositionVide();
		
		
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
            	case ("Obstacle"): // Obstacle: prendre en compte si c'est de l'eau, un rocher, une forêt (dans Obstacle.java)
            		Obstacle o = (Obstacle) getElement(x,y);
            		Obstacle.TypeObstacle to = o.getTYPE();
            		switch (to) {
					case EAU:
						g.setColor(COULEUR_EAU);
						break;
					case FORET:
						g.setColor(COULEUR_FORET);
						break;
					case ROCHER:
						g.setColor(COULEUR_ROCHER);
						break;
            		}
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
				
				System.out.println(" Case : " + tab[select.getX()][select.getY()].getClass().getSimpleName());
				
				if (tab[select.getX()][select.getY()] instanceof Heros) {  // Position getX getY
					Heros h = (Heros) tab[select.getX()][select.getY()];
					System.out.println(" Espèce : " + h.getTYPE() + ", nom :" + h.getNom());
					
				}
				
				if (tab[select.getX()][select.getY()] instanceof Monstre) {  // Position getX getY
					Monstre m = (Monstre) tab[select.getX()][select.getY()];
					System.out.println(" Espèce : " + m.getTYPE() + ", nom :" + m.getNom());
					
				}
				
				if (tab[select.getX()][select.getY()] instanceof Obstacle) {  // Position getX getY
					Obstacle o = (Obstacle) tab[select.getX()][select.getY()];
					System.out.println(" Type : " + o.getTYPE());
					
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