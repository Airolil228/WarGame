package wargame;

public class Heros extends Soldat{
	private final TypesH TYPE;
	
	public Heros(Carte carte, TypesH type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(),
		type.getPuissance(), type.getTir(), pos);
		this.setNom(nom); TYPE = type;
	}

	public TypesH getTYPE() {
		return TYPE;
	}
}