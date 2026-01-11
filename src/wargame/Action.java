package wargame;

public class Action implements IConfig{
		public enum TypeAction { ATTAQUER,SE_RAPPROCHER,FUIR,ATTENDRE }
		
		public TypeAction type;
		Position position;
		Heros cible;
		
		Action(TypeAction type, Position position, Heros cible) {
			this.type = type;
	        this.position = position;
			this.cible = cible;
		}
		
		public TypeAction getType(){
			return type;
		}
		
		public Position getPosition(){
			return position;
		}
		
		public Heros getCible(){ 
			return cible;
		}
		
	}