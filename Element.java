package wargame;

public abstract class Element {
	private boolean estVisible;
	private Position pos;
	private Carte carte;
	public Element(Carte carte, int pts/* ??? */, Position pos) {
		this.carte = carte;
		this.pos.setX(pos.getX());
		this.pos.setY(pos.getY());
	}
	public Position getP() {
		return pos;
	}
	public void setP(Position p2) {
		this.pos.setX(p2.getX());
		this.pos.setY(p2.getY());
	}
	public boolean isEstVisible() {
		return estVisible;
	}
	public void setEstVisible(boolean estVisible) {
		this.estVisible = estVisible;
	}
	public Position getPos() {
		return pos;
	}
	public void setPos(Position pos) {
		this.pos = pos;
	}
	public Carte getCarte() {
		return carte;
	}
	public void setCarte(Carte carte) {
		this.carte = carte;
	}
	
}
