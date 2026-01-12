package wargame;

import wargame.Action. TypeAction;

public class Ordinateur implements IConfig {
	private int visionGeneral[][];
	private int nbMonstresVivants;
	private int nbHerosVivants;
	
	private int NBACTIONSMAX;
	
	private Carte map;
	
	// Le poid sert à priviliger certains actions que l'autre 
	// plus le poids est positif => préoritise cette action 
	// plus le poids est négatif => évite de le faire 
	private static final double POIDS_DISTANCE = -0.3;
	private static final double POIDS_PV_BAS = 2.0;
	private static final double POIDS_ATTAQUE = 5.0;
	private static final double POIDS_SAUVER_VIE = 3.0;
	
	public Ordinateur(int hauteur, int largeur, Carte map) {
		visionGeneral = new int[hauteur][largeur];
		nbMonstresVivants = map.getNbMonstreVivant();
		nbHerosVivants = map.getNbHerosVivant();
		this.map = map;
		this.NBACTIONSMAX = NB_HEROS + (hauteur * largeur) + 1;
	}
	
	public void majVision(){
		//initialisation de la visionGenerale de l'armée (Monstre)
		for (int i = 0; i < visionGeneral.length; i++) {
			for (int j = 0; j < visionGeneral[0].length; j++) {
				visionGeneral[i][j] = 0;
			}
		}
		
		Monstre[] armeeMonstre = map.getArmeeMonstre();
		
		//Pour chaque monstre vivant , on explore tous le case  dans le cadre 
		//2*portée+1 : Si la case est dans les limites de la carte ET à distance ≤ portée → marque 
		for (int k = 0; k < nbMonstresVivants; k++) {
			Monstre m = armeeMonstre[k];
			
			if (m != null && m.getPoints() > 0) {
				Position pos = m.getPos();
				int portee = m.getPortee();
				
				for (int i = -portee; i <= portee; i++) {
					for (int j = -portee; j <= portee; j++) {
						int y = pos.getY() + i;
						int x = pos. getX() + j;
						
						if (y >= 0 && y < HAUTEUR_CARTE && x >= 0 && x < LARGEUR_CARTE) {
							if (distance(pos.getX(), pos.getY(), x, y) <= portee) {
								visionGeneral[y][x] = 1;
							}
						}
					}
				}
			}
		}
	}
	
	public void jouerTour() {
		majVision(); //maj de la vision générale
		
		Monstre[] armeeMonstre = map.getArmeeMonstre(); 
		
		for (int i = 0; i < nbMonstresVivants; i++) {
			Monstre m = armeeMonstre[i];
			if (m != null && m.getPoints() > 0) {
				jouerMonstre(m);
			}
		}
	}
	
	/**
	 * Decide et exécute la meilleure action pour un monstre donné.
	 * Genere toute les actions possibles, calcule leur utilité, choisit la meilleure. 
	 * 
	 * @param monstre Le monstre qui doit agir
	 */
	
	private void jouerMonstre(Monstre monstre){
		Action[] actionsPossibles = genereActionPossibles(monstre); 
		
		if (actionsPossibles == null || actionsPossibles.length == 0) {
			return;
		}
		
		Action meilleureAction = null;
		double meilleureUtilite = Double.NEGATIVE_INFINITY; // infiniment pétit
		
		// Parcours tableau d'actions possibles,pour chaque action possible on obtiens son utilité 
		// On choisi la meilleures utilité parmes les actions obtenu et on force le monstre d'agir ainsi
		for (int i = 0; i < actionsPossibles.length; i++) {
			Action action = actionsPossibles[i];
			double utilite = calculerUtilite(monstre, action);
			
			if (utilite > meilleureUtilite) {
				meilleureUtilite = utilite;
				meilleureAction = action;
			}
		}
		
		if (meilleureAction != null) {
			executerAction(monstre, meilleureAction);
		}
	}
	/**
	 * Exécute une action choisie par l'IA.
	 * 
	 * @param monstre Le monstre qui exécute l'action
	 * @param action L'action à exécuter
	 */
	private void executerAction(Monstre monstre, Action action){
		Position posAvant = new Position(monstre.getPos().getX(), monstre.getPos().getY());
		
		switch(action.type){
			case ATTAQUER: 
				if (action.cible != null) {
					monstre.combat(action.cible);
				}
				break;
				
			case SE_RAPPROCHER:
				// Dure à trouver 
				
			case FUIR:
				Element destination = map.getElement(action. position);
				if (!(destination instanceof Plaine)) {
					return;
				}
				
				map.deplaceSoldat(action.position, monstre);
				monstre.seDeplace(action.position);
				break;
				
			case ATTENDRE:
				break;
		}
	}
	
	
	/**
	 * Calcule l'utilité (score) d'une action pour un monstre.
	 * Plus le score est élevé, plus l'action est intéressante.
	 * 
	 * @param monstre Le monstre qui envisage l'action
	 * @param action L'action à evaluer
	 * @return Le score d'utilite (plus élevé = meilleur)
	 */
	
	private double calculerUtilite(Monstre monstre, Action action){
		double utilite = 0.0;
		Position posActuelle = monstre.getPos();
		double ratioVie = (double) monstre.getPoints() / monstre.getPointsMAX();
		
		switch(action.type) {
			//On si le point de vie de la cible est inferuire ou égale au puissance du monstre,on encourage à attaquer
			// sinon si la ratio entre le point de vie courant et le point vie max de la cible et que elle est voisine, on decourage d'attaquer 
			case ATTAQUER:
				utilite += POIDS_ATTAQUE;
				
				if (action.cible != null) {
					double ratioPVCible = (double) action.cible.getPoints() / action.cible.getPointsMAX();
					utilite += POIDS_PV_BAS * (1.0 - ratioPVCible);
					
					//On accord de la puissance de la frappe au different monstre
					int puissance = 0;
					switch (monstre.getTYPE()) {
						case TROLL:  puissance = 30; break;
						case ORC:  puissance = 10; break;
						case GOBELIN: puissance = 5; break;
					}
					
					// si on peut tuer d'un seule coup => encourage
					if (action.cible.getPoints() <= puissance) {
						utilite += 3.0;
					}
				}
				// si un monstre est trop faible ET que on se bat corp à corp 
				if (ratioVie < 0.3 && posActuelle.estVoisine(action.position)) {
					utilite -= 2.0;
				}
				break;
				
			case SE_RAPPROCHER:
				//
				//
				utilite += 1.5;
				if (action.cible != null) {
					double distApres = distance(action.position, action.cible.getPos());
					double distAvant = distance(posActuelle, action.cible.getPos());
					
					
					// si on est raproché trop à la cible
					if (distApres < distAvant) {
						utilite += POIDS_DISTANCE * distApres;
					} else {
						utilite -= 1.0;
					}
				}
				
				if (ratioVie < 0.3) {
					utilite -= POIDS_SAUVER_VIE;
				}
				break;
				
			case FUIR:
				// Si le monstre est trés éloigné de heros , on encourage à fuir  
				//
				if (ratioVie < 0.3) {
					utilite += POIDS_SAUVER_VIE;
					
					Heros herosPlusProche = trouverHerosPlusProcheDeTous(action.position);
					if (herosPlusProche != null) {
						double distApres = distance(action.position, herosPlusProche.getPos());
						double distAvant = distance(posActuelle, herosPlusProche.getPos());
						
						if (distApres > distAvant) {
							utilite += 2.0;
						}
					}
				} else {
					utilite -= 2.0;
				}
				break;
				
			case ATTENDRE: 
				// On décourage d'attendre
				utilite = -1.0;
				break;
		}
		
		// pour l'imprevisbilité 
		utilite += (Math.random() - 0.5) * 0.5;
		
		return utilite;
	}
	
	/**
	 * Genere toutes les actions possibles pour un monstre.
	 * @param monstre Le monstre pour lequel générer les actions
	 * @return Tableau d'actions possibles (attaques, déplacements, attente)
	 */
	public Action[] genereActionPossibles(Monstre monstre){
		Position pos = monstre.getPos();
		int nbaction = 0;
		
		Action[] actionsProvisoires = new Action[NBACTIONSMAX];
		
		Heros[] herosVisibles = trouverHerosVisibles(monstre);
		int nbHerosVisibles = herosVisibles.length;
		
		for(int i = 0; i < nbHerosVisibles; i++) {
			Heros heros = herosVisibles[i];
			
			if(heros != null && monstre.peutAttaquer(heros. getPos())){
				actionsProvisoires[nbaction] = new Action(TypeAction.ATTAQUER, heros.getPos(), heros);
				nbaction++;
			}
		}
		
		Action[] actionsDepl = genereCasesDepl(monstre, herosVisibles);
		
		int nbActionsDepl = 0;
		for (int i = 0; i < actionsDepl.length; i++) {
			if (actionsDepl[i] != null) {
				nbActionsDepl++;
			}
		}
		
		for (int i = 0; i < nbActionsDepl; i++) {
			actionsProvisoires[nbaction] = actionsDepl[i];
			nbaction++;
		}
		
		actionsProvisoires[nbaction] = new Action(TypeAction. ATTENDRE, pos, null);
		nbaction++;
		
		Action[] actionsFinal = new Action[nbaction];
		for (int i = 0; i < nbaction; i++) {
			actionsFinal[i] = actionsProvisoires[i];
		}
		
		return actionsFinal;
	}
	
	/**
	 * Genere toutes les actions de déplacement possibles pour un monstre.
	 * Si héros visible :  se rapproche de lui. 
	 * Si aucun héros visible : se rapproche vers la gauche (où sont les héros).
	 * 
	 * @param monstre Le monstre qui veut se déplacer
	 * @param herosVisibles Tableau des héros visibles par ce monstre
	 * @return Tableau d'actions de déplacement (SE_RAPPROCHER ou FUIR)
	 */
	
	public Action[] genereCasesDepl(Monstre monstre, Heros[] herosVisibles){
		
	    Position pos = monstre.getPos();
	    int porteeDeplacement = monstre.getPorteeDeplacement();
	    
	    Action[] actionsDepl = new Action[NBACTIONSMAX];
	    int nbdepl = 0;
	    
	    for(int dy = -porteeDeplacement; dy <= porteeDeplacement; dy++){
	        for(int dx = -porteeDeplacement; dx <= porteeDeplacement; dx++){
	            
	            if (dx == 0 && dy == 0) continue;
	            
	            int newX = pos.getX() + dx;
	            int newY = pos. getY() + dy;
	            Position newPos = new Position(newX, newY);
	            
	            if(newPos.estValide() && distance(pos. getX(), pos.getY(), newX, newY) <= porteeDeplacement){
	                Element element = map.getElement(newPos);
	            
	                if(element instanceof Plaine){
	                    Heros herosPlusProche = trouverHerosPlusProche(newPos, herosVisibles, herosVisibles.length);
	                    
	                    if(herosPlusProche != null) {
	                        actionsDepl[nbdepl] = new Action(TypeAction.SE_RAPPROCHER, newPos, herosPlusProche);
	                    } else {
	                        if (newX < pos.getX()) {
	                            actionsDepl[nbdepl] = new Action(TypeAction.SE_RAPPROCHER, newPos, null);
	                        } else {
	                            actionsDepl[nbdepl] = new Action(TypeAction.FUIR, newPos, null);
	                        }
	                    }
	                    nbdepl++;
	                }
	            }
	        }
	    }
	    
	    Action[] resultat = new Action[nbdepl];
	    for (int i = 0; i < nbdepl; i++) {
	        resultat[i] = actionsDepl[i];
	    }
	    
	    return resultat;
	}
	
	/**
	 * Trouve tous les héros visibles par un monstre (dans sa portée de vision).
	 * 
	 * @param monstre Le monstre qui regarde
	 * @return Tableau des héros visibles (taille exacte, sans null)
	 */
	public Heros[] trouverHerosVisibles(Monstre monstre){
		Heros[] herosVisibles = new Heros[NB_HEROS];
		int nbTrouves = 0;
		
		Position pos = monstre.getPos();
		int portee = monstre.getPortee();
		
		Heros[] armeeHeros = map.getArmeeHeros();
		
		for (int i = 0; i < nbHerosVivants; i++) {
			Heros h = armeeHeros[i];
			if (h != null && h.getPoints() > 0) {
				Position posHeros = h.getPos();
				
				if (distance(pos, posHeros) <= portee) {
					herosVisibles[nbTrouves] = h;
					nbTrouves++;
				}
			}
		}
		
		Heros[] resultat = new Heros[nbTrouves];
		for (int i = 0; i < nbTrouves; i++) {
			resultat[i] = herosVisibles[i];
		}
		
		return resultat;
	}
	
	public Heros trouverHerosPlusProche(Position pos, Heros[] herosVisibles, int nbHerosVisibles) {
		if (herosVisibles == null || nbHerosVisibles == 0) {
			return null;
		}
		
		Heros plusProche = herosVisibles[0];
		double distMin = distance(pos, plusProche.getPos());
		
		for (int i = 1; i < nbHerosVisibles; i++) {
			Heros h = herosVisibles[i];
			if (h != null) {
				double dist = distance(pos, h.getPos());
				if (dist < distMin) {
					distMin = dist;
					plusProche = h;
				}
			}
		}
		
		return plusProche;
	}
	
	private Heros trouverHerosPlusProcheDeTous(Position pos) {
		Heros plusProche = null;
		double distMin = Double.MAX_VALUE;
		
		Heros[] armeeHeros = map.getArmeeHeros();
		
		for (int i = 0; i < nbHerosVivants; i++) {
			Heros h = armeeHeros[i];
			if (h != null && h. getPoints() > 0) {
				double dist = distance(pos, h. getPos());
				if (dist < distMin) {
					distMin = dist;
					plusProche = h;
				}
			}
		}
		
		return plusProche;
	}
	
	private double distance(Position p1, Position p2) {
		return distance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	
	private double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
}