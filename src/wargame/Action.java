package wargame;

public class Action implements IConfig{
		private enum TypeAction { ATTAQUER,SE_RAPPROCHER,FUIR,ATTENDRE }
		
		TypeAction type;
		Position position;
		Heros cible;
		
		Action(TypeAction type, Position position, Heros cible) {
			this.type = type;
	        this.position = position;
			this.cible = cible;
		}
	}