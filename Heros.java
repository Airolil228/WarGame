package wargame;

public class Heros extends Soldat implements ISoldat{
	private final String NOM;
	private final TypesH TYPE;
	
	public Heros(Carte carte, TypesH type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(),
		type.getPuissance(), type.getTir(), pos);
		NOM = nom; TYPE = type;
	}
	
	
	public int getPoints() {
		return 0; /* A remplacer */
	}
	
	public int getTour() {
		return 0; /* A remplacer */
	}
	
	public int getPortee() {
		return 0; /* A remplacer */
	}
	
	public void joueTour(int tour) {
		
	}
	
	public void combat(Soldat soldat) {
		
	}

	public void seDeplace(Position newPos) {
		
	}
	
}