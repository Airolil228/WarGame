package wargame;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.*;

public class FenetreJeu implements IConfig{
    private static boolean running = true;
    private static int lastClickX = -1;
    private static int lastClickY = -1;
    private static JButton boutonFinDeTour;
    
    
    private static void creationBoutonsHeros(JMenuBar panelBoutons, JPanel panelJeu, Carte map) {

        boutonFinDeTour = new JButton("Fin Tour");
       
        // Ajout au panel
        panelBoutons.add(boutonFinDeTour);
        
        
        boutonFinDeTour.addActionListener(e -> actionFinDeTour(panelJeu, map));

        panelBoutons.getParent().revalidate();
        panelBoutons.getParent().repaint();
    }
    
    private static void actionFinDeTour(JPanel panelBoutons, Carte map) {
    	map.finDeTour();
    	
    	panelBoutons.getParent().revalidate();
        panelBoutons.getParent().repaint();
    }
    
	public static void main(String[] args) {
		Carte map = new Carte(HAUTEUR_CARTE,LARGEUR_CARTE);
		
        JFrame jeu = new JFrame("Jeu");
        jeu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jeu.setPreferredSize(new java.awt.Dimension(((LARGEUR_CARTE+1) * NB_PIX_CASE), (HAUTEUR_CARTE * NB_PIX_CASE) + 100 + HAUTEUR_BARRE_MENU));
        
        
        JPanel main = new JPanel();
        main.setPreferredSize(new java.awt.Dimension((LARGEUR_CARTE * NB_PIX_CASE), (HAUTEUR_CARTE * NB_PIX_CASE) + 100));
        
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        menuBar.setBackground(Color.gray);
        menuBar.setPreferredSize(new Dimension(LARGEUR_CARTE*NB_PIX_CASE,HAUTEUR_BARRE_MENU+10));
        
        jeu.setJMenuBar(menuBar);
        
        JPanel panel = new PanneauJeu(map);
        
        jeu.add(main);
        main.add(panel);
        creationBoutonsHeros(menuBar,panel,map);
        jeu.pack();
        jeu.setLocationRelativeTo(null);
        jeu.setVisible(true);
        
        
        
     // Listener des clics
        jeu.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                lastClickX = (e.getX()-5) / NB_PIX_CASE;
                lastClickY = (e.getY()-45- HAUTEUR_BARRE_MENU) / NB_PIX_CASE;
                System.out.println("Clic détecté: " + lastClickY + ", " + lastClickX);
                
                if ( lastClickY>=0 && lastClickY<HAUTEUR_CARTE && lastClickX>=0 && lastClickX<LARGEUR_CARTE ) {
                	map.marquerCase(lastClickY, lastClickX);
                	panel.repaint();
                }
                lastClickX = -1;
            }
        });

        jeu.setVisible(true);

        // Thread du jeu (boucle infinie tant que la fenêtre est ouverte)
        Thread gameLoop = new Thread(() -> {
            while (running) {

                // Exemple : si un clic a eu lieu
                if (lastClickX != -1) {
                    System.out.println("Traitement du clic...");
                    lastClickX = -1;
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
