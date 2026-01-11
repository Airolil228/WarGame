package wargame;

import java.util.ArrayList;
import java.util.List;
import wargame.Action.TypeAction;

// Pas encore implémenté, pour plus tard ^^
public class Ordinateur implements IConfig {
	private int visionGeneral[][]; // cases visibles par l'ordinateur : case visible 1, case non visible 0
	private int nbMonstresVivants;
	private int nbHerosVivants;
	
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
	}
	
	
	/**
	 * Met à jour la vision générale de l'ordinateur, Parcourt tous les monstres vivants et marque les cases visibles
	 */
	public void majVision() {
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
	 * MÉTHODE PRINCIPALE : Joue le tour de tous les monstres avec l'IA
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
	
	/**
	 * Décide et exécute la meilleure action pour un monstre donné
	 * Utilise l'IA basée sur l'utilité
	 */
	 private void jouerMonstre(Monstre monstre) {
		//Jouer monstre 
	}
	
	private List<Action> genererActionsPossibles(Monstre monstre) {
			List<Action> actions = new ArrayList<>();
			Position pos = monstre.getPos();
			int porteeDeplacement = monstre. getPorteeDeplacement();
			
			// 1. Actions d'attaque (héros à portée)
			List<Heros> herosVisibles = trouverHerosVisibles(monstre);
			for (Heros heros : herosVisibles) {
				if (monstre.peutAttaquer(heros. getPos())) {
					actions.add(new Action(TypeAction.ATTAQUER, heros.getPos(), heros));
				}
			}
			
			// 2. Actions de déplacement
			for (int dy = -porteeDeplacement; dy <= porteeDeplacement; dy++) {
				for (int dx = -porteeDeplacement; dx <= porteeDeplacement; dx++) {
					int newX = pos.getX() + dx;
					int newY = pos.getY() + dy;
					Position newPos = new Position(newX, newY);
					
					if (newPos.estValide() && distance(pos.getX(), pos.getY(), newX, newY) <= porteeDeplacement) {
						Element element = map.getElement(newPos);
						
						// Peut se déplacer sur une plaine
						if (element instanceof Plaine) {
							// Déplacement vers un héros (rapprochement)
							Heros herosPlusProche = trouverHerosPlusProche(newPos, herosVisibles);
							if (herosPlusProche != null) {
								actions.add(new Action(TypeAction.SE_RAPPROCHER, newPos, herosPlusProche));
							} else {
								// Déplacement de fuite
								actions.add(new Action(TypeAction.FUIR, newPos, null));
							}
						}
					}
				}
			}
			
			// 3. Action d'attente (ne rien faire)
			actions.add(new Action(TypeAction.ATTENDRE, pos, null));
			
			return actions;
	}
	
	
	
	// ==================== MÉTHODES UTILITAIRES ====================
	/**
	 * Trouve tous les héros visibles par un monstre
	 */
	private List<Heros> trouverHerosVisibles(Monstre monstre) {
		List<Heros> heros = new ArrayList<>();
		Position pos = monstre.getPos();
		int portee = monstre.getPortee();
		
		Heros[] armeeHeros = map.getArmeeHeros();
		
		for (int i = 0; i < nbHerosVivants; i++) {
			Heros h = armeeHeros[i];
			if (h != null && h. getPoints() > 0){
				Position posHeros = h.getPos();
				
				if (distance(pos, posHeros) <= portee) {
					heros.add(h);
				}
			}
		}
		
		return heros;
	}
	
	/**
	 * Trouve le héros le plus proche d'une position donnée
	 */
	private Heros trouverHerosPlusProche(Position pos, List<Heros> herosVisibles) {
		if (herosVisibles.isEmpty()) return null;
		
		Heros plusProche = herosVisibles.get(0);
		double distMin = distance(pos, plusProche.getPos());
		
		for (Heros h : herosVisibles) {
			double dist = distance(pos, h.getPos());
			if (dist < distMin) {
				distMin = dist;
				plusProche = h;
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
				double dist = distance(pos, h.getPos());
				if (dist < distMin){
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
