package wargame;

import wargame.Obstacle.TypeObstacle;

public class Plaine extends Element{
	private TypePlaine TYPE = TypePlaine.PLAINE;  // Ajout du champ TYPE
	public Plaine() {
		super();
	}
	public enum TypePlaine{
		PLAINE;
	}
	public TypePlaine getTYPE() {
		return TYPE;
	}
}
