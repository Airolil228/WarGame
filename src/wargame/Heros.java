package wargame;

public class Heros extends Soldat{
	private final TypesH TYPE;
	private int tour = 1;
	
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
	
}