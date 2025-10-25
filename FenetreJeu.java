package wargame;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FenetreJeu implements IConfig{
	public static void main(String[] args) {
		Carte map = new Carte(HAUTEUR_CARTE,LARGEUR_CARTE);
		
		
		
        JFrame jeu = new JFrame("Jeu");
        jeu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Dessin du tableau
                for (int x = 0; x < HAUTEUR_CARTE; x++) {
                    for (int y = 0; y < LARGEUR_CARTE; y++) {
                    	switch (map.getElement(x,y).getClass().getSimpleName()) {
                    	case ("Plaine"):
	                        g.setColor(COULEUR_PLAINE);
	                        g.fillRect(y * NB_PIX_CASE, x * NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
	                        break;
                    	case ("Obstacle"):
	                        g.setColor(COULEUR_ROCHER); // Obstacle: prendre en compte si c'est de l'eau, un rocher, une forêt (dans Obstacle.java)
	                        g.fillRect(y * NB_PIX_CASE, x * NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
	                        break;
                    	case ("Heros"):
	                        g.setColor(COULEUR_HEROS);  // Prendre en compte les héros déjà joués également
	                        g.fillRect(y * NB_PIX_CASE, x * NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
	                        break;
                    	case ("Monstre"):
	                        g.setColor(COULEUR_MONSTRES);
	                        g.fillRect(y * NB_PIX_CASE, x * NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
	                        break;
                    	}
                    	

                        g.setColor(COULEUR_TEXTE); // contour
                        g.drawRect(y * NB_PIX_CASE, x * NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
                    }
                }
            }
        };

        panel.setPreferredSize(new java.awt.Dimension(LARGEUR_CARTE * NB_PIX_CASE, HAUTEUR_CARTE * NB_PIX_CASE));
        jeu.add(panel);
        jeu.pack();
        jeu.setLocationRelativeTo(null);
        jeu.setVisible(true);
    }
}
