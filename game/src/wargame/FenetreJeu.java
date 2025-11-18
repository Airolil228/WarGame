package wargame;


import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.*;

public class FenetreJeu implements IConfig{
    private static boolean running = true;
    private static int lastClickX = -1;
    private static int lastClickY = -1;
    private static JButton boutonAttaquer;
    private static JButton boutonDeplacer;
    
    
    private static void creationBoutonsHeros(JPanel panelBoutons) {

        boutonAttaquer = new JButton("Attaquer");
        boutonDeplacer = new JButton("Déplacer");
        
        panelBoutons.setLayout(new BoxLayout(panelBoutons, BoxLayout.Y_AXIS));
        panelBoutons.setPreferredSize(new Dimension(150, 200)); // largeur fixe

        panelBoutons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        

        // Ajout au panel
        panelBoutons.add(boutonAttaquer);
        panelBoutons.add(boutonDeplacer);

        panelBoutons.getParent().revalidate();
        panelBoutons.getParent().repaint();
    }
    
    
	public static void main(String[] args) {
		Carte map = new Carte(HAUTEUR_CARTE,LARGEUR_CARTE);
		
		
		
        JFrame jeu = new JFrame("Jeu");
        jeu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        JPanel panel = new PanneauJeu(map);
        

        jeu.add(panel);
        jeu.pack();
        jeu.setLocationRelativeTo(null);
        jeu.setVisible(true);
        
        creationBoutonsHeros(panel);
        
        
     // Listener des clics
        jeu.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                lastClickX = e.getX() / NB_PIX_CASE;
                lastClickY = (e.getY() / NB_PIX_CASE) - 2;
                System.out.println("Clic détecté: " + lastClickX + ", " + lastClickY);
                
                map.marquerCase(lastClickY, lastClickX);
                panel.repaint();
            }
        });

        jeu.setVisible(true);

        // Thread du jeu (boucle infinie tant que la fenêtre est ouverte)
        Thread gameLoop = new Thread(() -> {
            while (running) {

                // Exemple : si un clic a eu lieu
                if (lastClickX != -1) {
                    System.out.println("Traitement du clic...");
                    lastClickX = -1; // On "consomme" le clic
                }

                // Ton code de mise à jour du jeu ici
                // ...

                try { Thread.sleep(16); } catch (InterruptedException ignored) {}
            }
            System.out.println("Boucle de jeu arrêtée.");
        });

        gameLoop.start();

        // Quand la fenêtre se ferme → arrêter la boucle
        jeu.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
                running = false;   // ARRÊTE LA BOUCLE
                try {
                    gameLoop.join();  // attend que le thread s'arrête proprement
                } catch (InterruptedException ex) {}
            }
        });
    }
}
