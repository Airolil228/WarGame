package wargame;

import wargame.ISoldat.TypesH;

public class Monstre extends Soldat{
	private final String NOM;
	private final TypesM TYPE;
	
	public Monstre(Carte carte, TypesM type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(),
		type.getPuissance(), type.getTir(), pos);
		NOM = nom; TYPE = type;
	}
	
	public TypesM getTYPE() {
		return TYPE;
	}
}