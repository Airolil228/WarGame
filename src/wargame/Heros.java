package wargame;

import java.io.Serializable;

public class Heros extends Soldat implements Serializable{
	private final TypesH TYPE;
	private int tour = 1;
	private static final long serialVersionUID = 1L; // contrôle de la compatibilité
	
	public Heros(Carte carte, TypesH type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(),
		type.getPuissance(), type.getTir(), pos);
		this.setNom(nom); TYPE = type;
	}

	public TypesH getTYPE() {
		return TYPE;
	}
	
	public boolean peutJouer() {
		return (tour == 1);
	}
	
	public void aJouer() {
		tour = 0;
	}
	
	public void peutRejouer() {
		tour = 1;
	}
	
	public String toString() {
		String m = "";
		TypesH type = this.getTYPE();
		
		m += "Type : " + type + " | ";
		m += "Nom : " + this.getNom() + " | \n";
		m += "PV : " + this.getPoints() + "/" + this.getPointsMAX() + " | \n";
		m += "Portee : " + type.getPortee() + " | ";
		m += "Puissance : " + type.getPuissance() + " | ";
		m += "Tir : " + type.getTir() + "\n";
		
		return m;
	}
	
}