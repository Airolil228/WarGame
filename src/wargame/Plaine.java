package wargame;

import java.io.Serializable;

import wargame.Obstacle.TypeObstacle;

public class Plaine extends Element implements Serializable{
	private TypePlaine TYPE = TypePlaine.PLAINE;  // Ajout du champ TYPE
	private static final long serialVersionUID = 1L; // contrôle de la compatibilité
	
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
