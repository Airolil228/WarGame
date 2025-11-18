package wargame;

import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PanneauJeu extends JPanel implements IConfig{
	private static final long serialVersionUID = -5386655355068292584L;
	private Carte map;
    
	/**
	 * 
	 */
	public PanneauJeu(Carte map) {
		this.map = map;
		
		setPreferredSize(new java.awt.Dimension((LARGEUR_CARTE * NB_PIX_CASE) + 200, HAUTEUR_CARTE * NB_PIX_CASE));
	}

	protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessin du tableau
        map.toutDessiner(g);
    }
	
    
    private static void actionAttaquer() {
        System.out.println("→ Action : ATTAQUER");// ici tu mets ton code pour attaquer
    }

    private static void actionDeplacer() {
        System.out.println("→ Action : DEPLACER");// ici code pour déplacer un héros
    }
}
