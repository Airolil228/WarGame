package wargame;

import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class FenetreJeu implements IConfig{
    private static boolean running = true;
    private static int lastClickX = -1;
    private static int lastClickY = -1;
    private static JButton boutonFinDeTour;
    private static JButton boutonSauveGarde;
    private static JButton boutonRetourMenu;
    private static Boolean dragging = false;
    
    private static int dragDebutX,dragDebutY;
    private static int currentMouseX,currentMouseY;
    
    private static Element draggedElement =  null; 
    private static Element elementSurvole = null; 
    
    private static void creationBoutonsHeros(JMenuBar panelBoutons, JPanel panelJeu, Carte map){
        boutonFinDeTour = new JButton("Fin Tour");
        boutonSauveGarde = new JButton("Sauvegarder"); 
        boutonRetourMenu = new JButton("Retour menu"); 
        
        
        // Ajout au panel
        panelBoutons.add(boutonFinDeTour);
        panelBoutons.add(boutonSauveGarde);
        panelBoutons.add(boutonRetourMenu); 
        
        boutonFinDeTour.addActionListener(e -> actionFinDeTour(panelJeu, map));
        
        boutonSauveGarde.addActionListener(e -> {
        	java.awt.Component parent = SwingUtilities.getWindowAncestor(panelJeu); //Récupère la fenêtre (JFrame) qui contient panelJeu
        	String[] options = {"Slot 1","Slot 2","Slot 3","Annuler"}; 
            
        	//Affichier le menu des slots 
        	int choix  = javax.swing.JOptionPane.showOptionDialog(
        			parent,//
        			"Choisissez un slot de sauvegarde",//
        			"Sauvegarde",
        			JOptionPane.DEFAULT_OPTION,//Type d’options par défaut
        			JOptionPane.QUESTION_MESSAGE,//Icône question affichée dans la boîte
        			null,//icônne personalisée
        			options,//les boutons affichés
        			options[0]); //sélectionné par défaut
        	
        	//Traitement du choix 
        	if(choix == 0 || choix == 1 || choix == 2){
        		int SlotNumber = choix + 1;
        		File dir = new File("save");
        		
        		if(!dir.exists()){
        			dir.mkdirs();
        		}
        		
        		//Chemin du fichier
        		File fichier = new File(dir,"slot" + SlotNumber + ".wg");
        		
        		//Dans le cas si le chimin vers le fichier exists : demander de la confiramtions d'écraisement ce slot
        		if(fichier.exists()){
        			int confirm = JOptionPane.showConfirmDialog(
        					parent,
        					"Le slot" + SlotNumber + "contient déjâ une sauvegarde.\n Voulez-vous l'écraser ?", 
        					"Confirmation d'écrasement", 
        					JOptionPane.YES_NO_OPTION,
        					JOptionPane.WARNING_MESSAGE
        			);
        			if(confirm != JOptionPane.YES_OPTION){
        				return ;
        			}
        		}
        	
        	
        	try{
        		SauveCharge.sauvegarder(map,fichier);
        		JOptionPane.showMessageDialog(parent,
        				"Sauvegarde efectuée dans le slot " + SlotNumber + " ("+ fichier.getPath() + ")",
        				"Sauvegarde reussie", 
        				JOptionPane.INFORMATION_MESSAGE
        				);
        	}catch (Exception ex){
        		JOptionPane.showMessageDialog(
        		parent,
        		"Erreur de la sauvegarde: "+ ex.getMessage(),
        		"Erreur",
        		JOptionPane.INFORMATION_MESSAGE
        		);
        		ex.printStackTrace();
        	}
          }
        }); 
        
        
        panelBoutons.getParent().revalidate();
        panelBoutons.getParent().repaint();
    }
    
    private static void actionFinDeTour(JPanel panelBoutons, Carte map) {
    	map.finDeTour();
    	
    	panelBoutons.getParent().revalidate();
        panelBoutons.getParent().repaint();
    }
    
    public static int getCurrentMouseX(){
    	 return currentMouseX;  
    }
    
    public static int getCurrentMouseY(){
    	return currentMouseY;
    }
    
	public static void main(String[] args) {
		
		
        JFrame jeu = new JFrame("Jeu");
        jeu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jeu.setPreferredSize(new java.awt.Dimension(((LARGEUR_CARTE+1) * NB_PIX_CASE), (HAUTEUR_CARTE * NB_PIX_CASE) + 100 + HAUTEUR_BARRE_MENU));
        MenuDemarrage MenDem = new MenuDemarrage(jeu);
       
        //Nouvelle partie 
        MenDem.setOnNouvellePartie(() -> {
        Carte map = new Carte(HAUTEUR_CARTE,LARGEUR_CARTE);
        JPanel main = new JPanel();	
        main.setPreferredSize(new java.awt.Dimension((LARGEUR_CARTE * NB_PIX_CASE), (HAUTEUR_CARTE * NB_PIX_CASE) + 100));
       
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        menuBar.setBackground(Color.gray);
        menuBar.setPreferredSize(new Dimension(LARGEUR_CARTE*NB_PIX_CASE,HAUTEUR_BARRE_MENU+10));
        
        jeu.setJMenuBar(menuBar);
        
        JPanel panel = new PanneauJeu(map);
        map.setPanneauJeu(panel);
        
        jeu.setContentPane(main);
        main.add(panel);
        creationBoutonsHeros(menuBar,panel,map);
        jeu.pack();
        jeu.setLocationRelativeTo(null);
        jeu.revalidate();
        jeu.repaint();
        
        //Listener des clics
        jeu.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                lastClickX = (e.getX()-5) / NB_PIX_CASE;
                lastClickY = (e.getY()-45- HAUTEUR_BARRE_MENU) / NB_PIX_CASE;
                System.out.println("Clic détecté: " + lastClickY + ", " + lastClickX);
                
               
                
                if ( lastClickY>=0 && lastClickY<HAUTEUR_CARTE && lastClickX>=0 && lastClickX<LARGEUR_CARTE ) {
                	map.marquerCase(lastClickY, lastClickX);
                	Element element = map.getElement(lastClickX,lastClickY);
                	if(element instanceof Heros){
	                	dragging = true;
	                	dragDebutX = lastClickX;
	                    dragDebutY = lastClickY;
	                    draggedElement = element;
	                    System.out.println("Debut X: "+ dragDebutX + " Debut Y"+ dragDebutY );
	                    panel.repaint();
                	}
                }
                
            }
            
            public void mouseReleased(MouseEvent e){
            	if(dragging){
            		int dropX = (e.getX()-5) / NB_PIX_CASE;    
            		int dropY = (e.getY()-45- HAUTEUR_BARRE_MENU) / NB_PIX_CASE;
            		
            		map.marquerCase(dragDebutY, dragDebutX);
            		map.marquerCase(dropY, dropX);
            		System.out.println("Drop sur: " + dropY + ", " + dropX);
            		panel.repaint();
            	}
            	dragging = false;
            	draggedElement = null;
            }
            
        });
        
        jeu.addMouseMotionListener(new MouseMotionListener() {
        	public void mouseDragged(MouseEvent e) {
        		if(dragging) {
        		currentMouseX = e.getX()-5;
        		currentMouseY = e.getY()-45- HAUTEUR_BARRE_MENU;
        		
        		int currentCaseX = currentMouseX / NB_PIX_CASE; 
        		int currentCaseY = currentMouseY / NB_PIX_CASE;
        		
        		System.out.println("Drag en cours vers: " + currentMouseY + ", " + currentMouseX);
        		panel.repaint(); 
        		}
        	}

			
			public void mouseMoved(MouseEvent e){
				currentMouseX = e.getX() - 5;
				currentMouseY = e.getY() - 45 - HAUTEUR_BARRE_MENU; 
				
				if(dragging){
					panel.repaint();
				}
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
	    });
        
        //Afficher le menu en premier 
        jeu.setContentPane(MenDem);
        jeu.pack();
        jeu.setLocationRelativeTo(null);
        jeu.setVisible(true);
	}
	
	//FIN DU MAIN
	public static Element getDraggedElement() {
		return draggedElement;
	}
	
	public static Boolean isDragging() {
		return dragging;
	}
}
