package wargame;

import java.io.Serializable;

public abstract class Soldat extends Element implements ISoldat,Serializable{
	private final int POINTS_DE_VIE_MAX, PUISSANCE, TIR, PORTEE_VISUELLE;
	private int pointsDeVie;
	private Carte carte;
	private static final long serialVersionUID = 1L; // contrôle de la compatibilité
	
	/*(…)*/
	Soldat(Carte carte, int pts, int portee, int puiss, int tir, Position pos) {
		POINTS_DE_VIE_MAX = pointsDeVie = pts;
		PORTEE_VISUELLE = portee; PUISSANCE = puiss; TIR = tir;
		this.carte = carte; setPos(pos);
	}
	
	public int getPoints() {
		return pointsDeVie; 
	}
	
	public int getTour() {
		return 0; /* A remplacer */
	}
	
	public int getPortee() {
		return PORTEE_VISUELLE; 
	}
	
	public void joueTour(int tour) {
		return ;
	}
	
	public boolean peutAttaquer(Position pos) { // Pos : position de l'adversaire
		// Calcul si l'on peut ou non attaquer l'ennemie
		Position p = getPos();
		if (p.estVoisine(pos)) {
			return true;
		}
		int portee = this.getPortee();
		if ((pos.getY() <= p.getY()+portee) && (pos.getY() >= p.getY()-portee) && (pos.getX() <= p.getX()+portee) && (pos.getX() >= p.getX()-portee)){
			if (this.TIR > 0) {
				return true;
			}
		}
		return false;
	}
	
	
	public void combat_bis(Soldat soldat){
		int puissance_coup; 
		if(getPos().estVoisine(soldat.getPos())){//corps à corps 
			puissance_coup = (int)(Math.random() * (this.PUISSANCE + 1)); 
			soldat.pointsDeVie -= puissance_coup;
			System.out.println("Attaque : " + puissance_coup + ", Il reste :" + soldat.pointsDeVie);
		}else{ // combat à distance
			puissance_coup = (int)(Math.random() * (this.TIR + 1)); 
			soldat.pointsDeVie -= puissance_coup;
			System.out.println("Attaque : " + puissance_coup + ", Il reste :" + soldat.pointsDeVie);
		}
	}
	
	public boolean est_mort(){	
		return (this.pointsDeVie <= 0);
	}
	
	public void combat(Soldat soldat) {
		combat_bis(soldat);
		
		if(soldat.est_mort()){
			soldat.carte.mort(soldat);
		}else {
			if (soldat.peutAttaquer(this.getPos())) {
				soldat.combat_bis(this);
				
				if (est_mort()) {
					this.carte.mort(this);
				}
			}
			
		}
	}

	public void seDeplace(Position newPos) {
		Position pos = getPos();
		System.out.println("Ancienne pos: " + pos.getY() + "," + pos.getX());
		System.out.println("Nouvelle pos: " + newPos.getY() + "," + newPos.getX());
		 
		 // mettre à jour la position d'un héros 
		 setPos(newPos);
	}
	
}