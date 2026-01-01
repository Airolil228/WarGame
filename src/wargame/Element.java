package wargame;

import java.io.Serializable;

public abstract class Element implements Serializable{
	private boolean estVisible;
	private String nom;
	private Position pos;
	
	private static final long serialVersionUID = 1L; // contrôle de la compatibilité
	
	public Element() {
		this.estVisible = true;
		this.nom = "vide";
		this.pos = new Position(-1,-1);
	}
	
	public boolean EstVisible() {
		return estVisible;
	}
	public void setEstVisible(boolean estVisible) {
		this.estVisible = estVisible;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Position getPos() {
		return pos;
	}
	public void setPos(Position pos) {
		this.pos.setY(pos.getY());
		this.pos.setX(pos.getX());
	}
	public void setPos(int x, int y) {
		this.pos.setY(y);
		this.pos.setX(x);
	}
}
