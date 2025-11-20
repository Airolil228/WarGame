package wargame;
import java.awt.Color;
import java.awt.Graphics;

import wargame.ISoldat.TypesH;


public class Obstacle extends Element {
	public static int nbObstacle = 2;
	public enum TypeObstacle {
		ROCHER (IConfig.COULEUR_ROCHER), FORET (IConfig.COULEUR_FORET), EAU (IConfig.COULEUR_EAU);
		private final Color COULEUR;
		TypeObstacle(Color couleur) { COULEUR = couleur; }
		public static TypeObstacle getObstacleAlea() {
			return values()[(int)(Math.random()*values().length)];
		}
	}
	private TypeObstacle TYPE;
	Obstacle(TypeObstacle type, Position pos) { TYPE = type; this.setPos(pos);; }
	public String toString() { return ""+TYPE; }
	
	public TypeObstacle getTYPE() {
		return TYPE;
	}
}