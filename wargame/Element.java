package wargame;

public abstract class Element {
	private boolean estVisible;
	private String nom;
	private Position pos;

	public Element() {
		this.estVisible = true;
		this.nom = "vide";
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
		this.pos = pos;
	}
}
