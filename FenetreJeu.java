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
    
    
    private static void creationBoutonsHeros(JPanel panelBoutons, Carte map) {

        boutonAttaquer = new JButton("Attaquer");
        boutonDeplacer = new JButton("Déplacer");
        
        panelBoutons.setLayout(new BoxLayout(panelBoutons, BoxLayout.LINE_AXIS));
        panelBoutons.setPreferredSize(new Dimension(150, 200)); // largeur fixe

        panelBoutons.setLayout(new FlowLayout(FlowLayout.LEFT));
       
        // Ajout au panel
        panelBoutons.add(boutonAttaquer);
        panelBoutons.add(boutonDeplacer);
        
        
        boutonAttaquer.addActionListener(e -> actionAttaquer(map));
        boutonDeplacer.addActionListener(e -> actionDeplacer(map));

        panelBoutons.getParent().revalidate();
        panelBoutons.getParent().repaint();
    }
    
    private static void actionAttaquer(Carte map) {
    	Position p = map.getSelect();
    	Element e = map.getElement(p);
    	if (e instanceof Heros) {
    		System.out.println("→ Action : ATTAQUER");// ici tu mets ton code pour attaquer
    	}
    	
    }

    private static void actionDeplacer(Carte map) {
    	Position p = map.getSelect();
    	Element e = map.getElement(p);
    	if (e instanceof Heros) {
    		System.out.println("→ Action : DEPLACER");// ici code pour déplacer un héros
    	}
    }
    
    
	public static void main(String[] args) {
		Carte map = new Carte(HAUTEUR_CARTE,LARGEUR_CARTE);
		
        JFrame jeu = new JFrame("Jeu");
        jeu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jeu.setPreferredSize(new java.awt.Dimension(((LARGEUR_CARTE+1) * NB_PIX_CASE), (HAUTEUR_CARTE * NB_PIX_CASE) + 100));
        
        
        JPanel main = new JPanel();
        main.setPreferredSize(new java.awt.Dimension((LARGEUR_CARTE * NB_PIX_CASE), (HAUTEUR_CARTE * NB_PIX_CASE) + 100));
        
        
        JPanel panel = new PanneauJeu(map);
        
        
        jeu.add(main);
        main.add(panel);
        creationBoutonsHeros(main,map);
        jeu.pack();
        jeu.setLocationRelativeTo(null);
        jeu.setVisible(true);
        
        
        
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

