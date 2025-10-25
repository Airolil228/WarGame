package wargame;


public class Monstre extends Soldat{
	private final String NOM;
	private final TypesM TYPE;
	
	public Monstre(Carte carte, TypesM type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(),
		type.getPuissance(), type.getTir(), pos);
		NOM = nom; TYPE = type;
	}
	
}