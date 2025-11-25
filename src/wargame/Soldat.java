package wargame;

public abstract class Soldat extends Element implements ISoldat{
	private final int POINTS_DE_VIE_MAX, PUISSANCE, TIR, PORTEE_VISUELLE;
	private int pointsDeVie;
	private Carte carte;
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
	
	public void combat_bis(Soldat soldat){
		int puissance_coup; 
		if(getPos().estVoisine(soldat.getPos())){//corps à corps 
			puissance_coup = (int)(Math.random() * (PUISSANCE + 1)); 
			pointsDeVie -= puissance_coup; 
		}else{ // combat à distance
			puissance_coup = (int)(Math.random() * (TIR + 1)); 
			pointsDeVie -= puissance_coup;
		}
	}
	
	public boolean est_mort(Soldat soldat){	
		return (soldat.pointsDeVie == 0); 
	}
	
	public void combat(Soldat soldat) {
		combat_bis(soldat);
		
		if(!est_mort(soldat)){
			combat_bis(soldat);
		}
		
	}

	public void seDeplace(Position newPos) {
		Position pos = getPos();
		System.out.println("Ancienne pos: " + pos.getY() + "," + pos.getX());
		System.out.println("Nouvelle pos: " + newPos.getY() + "," + newPos.getX());
		 
		 // mettre à jour la position d'un héros 
		 setPos(newPos);
	}
	
	
	
	public boolean peutAttaquer(Position pos) { // Pos : position de l'adversaire
		// Calcul si l'on peut ou non attaquer l'ennemie
		Position p = getPos();
		if (p.estVoisine(pos)) {
			return true;
		}
		return false;
	}
}