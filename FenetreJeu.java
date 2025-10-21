package wargame;

public class FenetreJeu {
	public void static main() {
		Soldat s = new Soldat();
		Element e = new Element(s,0,0);
		
		System.out.println(""+e);
		return ;
	}
}