package wargame;

import java.util.ArrayList;
import java.util.List;
import wargame.Action.TypeAction;

// Pas encore implémenté, pour plus tard ^^
public class Ordinateur implements IConfig {
	private int visionGeneral[][]; // cases visibles par l'ordinateur : case visible 1, case non visible 0
	private int nbMonstresVivants;
	private int nbHerosVivants;
	
	final int NBACTIONSMAX;
	
	private Carte map;
	
	private static final double POIDS_DISTANCE = -0.3;      // Préfère les cibles proches
	private static final double POIDS_PV_BAS = 2.0;         // Préfère cibler les héros avec peu de PV
	private static final double POIDS_ATTAQUE = 5.0;        // Forte préférence pour attaquer
	private static final double POIDS_SAUVER_VIE = 3.0;     // Préfère fuir quand peu de PV
	
	
	
	public Ordinateur(int hauteur, int largeur, Carte map) {
		visionGeneral = new int[hauteur][largeur];
		nbMonstresVivants = NB_MONSTRES;
		nbHerosVivants = NB_HEROS;
		this.map = map;
		this.NBACTIONSMAX = NB_HEROS + (hauteur * largeur) + 1;
	}
	
	/**
	 * Met à jour la vision générale de l'ordinateur, Parcourt tous les monstres vivants et marque les cases visibles
	 */
	
	public void majVision(){
		// Réinitialiser la vision
		for (int i = 0; i < visionGeneral.length; i++) {
			for (int j = 0; j < visionGeneral[0].length; j++) {
				visionGeneral[i][j] = 0;
			}
		}
		Monstre[] ArmeeMonstre = map.getArmeeMonstre();
		
		// Pour chaque monstre vivant, marquer sa zone de vision
		for (int k = 0; k < nbMonstresVivants; k++) {
			Monstre m = ArmeeMonstre[k];
			
			if (m != null && m.getPoints() > 0) {
				Position pos = m.getPos();
				int portee = m.getPortee();
				
				for (int i = -portee; i <= portee; i++) {
					for (int j = -portee; j <= portee; j++) {
						int y = pos.getY() + i;
						int x = pos. getX() + j;
						
						if (y >= 0 && y < HAUTEUR_CARTE && x >= 0 && x < LARGEUR_CARTE) {
							if (distance(pos. getX(), pos.getY(), x, y) <= portee) {
								visionGeneral[y][x] = 1;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * méthode principale: joue le tour de tous les monstres avec l'IA
	 */
	public void jouerTour() {
		majVision();
		Monstre[] armeeMonstre = map.getArmeeMonstre();
		// Pour chaque monstre vivant
		for (int i = 0; i < nbMonstresVivants; i++) {
			Monstre m = armeeMonstre[i];
			if (m != null && m.getPoints() > 0) {
				jouerMonstre(m);
			}
		}
	}
	
	
	private void executerAction(Monstre monstre, Action action){
		switch(action.type){
		case ATTAQUER:
			if (action.cible != null) {
				System.out.println(monstre.getNom() + " attaque " + action.cible.getNom());
				monstre.combat(action.cible);
			}
			break;
			
		case SE_RAPPROCHER:
		case FUIR:
			System.out.println(monstre. getNom() + " se déplace vers (" + action.position.getX() + "," + action.position.getY() + ")");
			map.deplaceSoldat(action.position, monstre);
			monstre.seDeplace(action.position);
			break;
			
		case ATTENDRE: 
			System.out.println(monstre.getNom() + " attend");
			break;
		
		}
		
	}
	
	private void jouerMonstre(Monstre monstre){
		Action[] actionsPossibles = genereActionPossibles(monstre);
		if (actionsPossibles == null || actionsPossibles.length == 0) {
			return;
		}
		
		Action meilleureAction = null;
		double meilleureUtilite = 0.0;
		
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
	
	
	
	
	private double calculerUtilite(Monstre monstre, Action action){
	    double utilite = 0.0;
	    Position posActuelle = monstre.getPos();
	    double ratioVie = (double) monstre.getPoints() / monstre.getPointsMAX();
	    
	    switch(action.type) {
	        case ATTAQUER:
	            utilite += POIDS_ATTAQUE;
	            
	            // Bonus si cible faible
	            if (action.cible != null) {
	                double ratioPVCible = (double) action.cible.getPoints() / action.cible.getPointsMAX();
	                utilite += POIDS_PV_BAS * (1.0 - ratioPVCible);
	            }
	            
	            // Malus si monstre faible en corps à corps
	            if (ratioVie < 0.3 && posActuelle.estVoisine(action.position)) {
	                utilite -= 2.0;
	            }
	            break;
	            
	        case SE_RAPPROCHER:
	            utilite += 1.5;
	            
	            // Bonus si on se rapproche vraiment
	            if (action.cible != null) {
	                double distApres = distance(action.position, action.cible. getPos());
	                double distAvant = distance(posActuelle, action.cible.getPos());
	                
	                if (distApres < distAvant) {
	                    utilite += POIDS_DISTANCE * distApres;
	                } else {
	                    utilite -= 1.0;
	                }
	            }
	            
	            // Si faible, préfère fuir
	            if (ratioVie < 0.3) {
	                utilite -= POIDS_SAUVER_VIE;
	            }
	            break;
	            
	        case FUIR:  
	            if (ratioVie < 0.3) {
	                utilite += POIDS_SAUVER_VIE;
	                
	                Heros herosPlusProche = trouverHerosPlusProcheDeTous(action.position);
	                if (herosPlusProche != null) {
	                    double distApres = distance(action. position, herosPlusProche. getPos());
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
	            utilite = -1.0;
	            break;
	    }
	    
	    // Ajouter aléatoire
	    utilite += (Math.random() - 0.5) * 0.5;
	    
	    return utilite;
	}
	
	public Action[] genereActionPossibles(Monstre monstre){
		Position pos = monstre.getPos();
		Action[] action = new Action[NBACTIONSMAX];
		
		int nbaction = 0; 
		
		Action[] actionsProvisoires = new Action[NBACTIONSMAX];
		Action[] actionsDepl  = new Action[NBACTIONSMAX];
		
		Heros[] herosVisibles = trouverHerosVisibles(monstre);
		int nbHerosVisibles = herosVisibles.length;
		
		//On trouve les actions qu'on peut attaquer 
		for(int i = 0;  i < nbHerosVisibles; i++) {
			Heros heros = herosVisibles[i]; 
			
			if(heros != null && monstre.peutAttaquer(heros.getPos())){
				actionsProvisoires[nbaction] = new Action(TypeAction.ATTAQUER,heros.getPos(),heros);
				nbaction++;
			}
			
		}
		 
		actionsDepl = genereCasesDepl(monstre,herosVisibles);
		
		int nbActionsDepl = 0;
		for (int i = 0; i < actionsDepl.length; i++) {
			if (actionsDepl[i] != null) {
				nbActionsDepl++;
			}
		}
		
		// Ajouter les actions de déplacement
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
	
	public Action[] genereCasesDepl(Monstre monstre,Heros[] herosVisibles){
		Position pos = monstre.getPos();
		int porteeDeplacement = monstre.getPorteeDeplacement();
		
		Action[] actionsDepl  = new Action[NBACTIONSMAX];
		int nbdepl = 0; 
 		
		for(int dy = -porteeDeplacement; dy <= porteeDeplacement; dy++){
			for(int dx = -porteeDeplacement; dx <= porteeDeplacement; dx++){
				
				int newX = pos.getX()+dx;
				int newY = pos.getY()+dy;
				Position newPos = new Position(newX, newY);
				
				if(newPos.estValide() && distance(pos. getX(), pos.getY(), newX, newY) <= porteeDeplacement){
					Element element = map.getElement(newPos);
				
					if(element instanceof Plaine){
						Heros herosPlusProche = trouverHerosPlusProche(newPos, herosVisibles,herosVisibles.length);
						if(herosPlusProche != null) {
							actionsDepl[nbdepl] = new Action(TypeAction.SE_RAPPROCHER,newPos,herosPlusProche );//n indique vers quel héros on se rapproche    
						}else {
							actionsDepl[nbdepl] = new Action(TypeAction.FUIR, newPos, null);
						}
						nbdepl++; 
					}
				}
			}
		}
		
		 Action[] resultat = new Action[nbdepl];
		 
		 for (int i = 0; i < nbdepl; i++){
		        resultat[i] = actionsDepl[i];
		  }
		return resultat;
	} 
	
	
	// ================================== Fonctions utilitaires  ====================
	/**
	 * Trouve tous les héros visibles par un monstre
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
		
		// Créer un tableau de la taille exacte
		Heros[] resultat = new Heros[nbTrouves];
		for (int i = 0; i < nbTrouves; i++) {
			resultat[i] = herosVisibles[i];
		}
		
		return resultat;
	}
	
	/**
	 * Trouve le héros le plus proche d'une position donnée
	 */
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
		double distMin = Double. MAX_VALUE;
		
		Heros[] armeeHeros = map.getArmeeHeros();
		
		for (int i = 0; i < nbHerosVivants; i++) {
			Heros h = armeeHeros[i];
			if (h != null && h.getPoints() > 0) {
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
