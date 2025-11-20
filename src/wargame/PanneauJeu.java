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
	
}
