package wargame;

import java.io.Serializable;

import wargame.ISoldat.TypesH;

public class Monstre extends Soldat implements Serializable{
	private final TypesM TYPE;
	private static final long serialVersionUID = 1L; // contrôle de la compatibilité

	
	public Monstre(Carte carte, TypesM type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(),
		type.getPuissance(), type.getTir(), pos);
		this.setNom(nom); TYPE = type;
	}
	
	public TypesM getTYPE() {
		return TYPE;
	}
}