package wargame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;


public class MenuDemarrage extends JPanel implements IConfig{
	private JButton btnNouvellePartie;
	private JButton btnChargerPartie;
	private JButton btnQuitter;
	private JFrame fenetreJeu;
	private Runnable onNouvellePartie;
	
	public MenuDemarrage(JFrame fenetre){
		this.fenetreJeu = fenetre;
		setPreferredSize(new Dimension(LARGEUR_CARTE*NB_PIX_CASE,HAUTEUR_CARTE * NB_PIX_CASE ));
		setBackground(new Color(34,34,34)); //Fond sombre
		setLayout(null); // Layout absolu pour positionner les boutons
		
		creerComposants();
	}

	public void setOnNouvellePartie(Runnable r){ this.onNouvellePartie = r;}

	private void creerComposants() {
		JLabel  titre = new JLabel("⚔ WAR GAME ⚔", SwingConstants.CENTER);
		titre.setFont(new Font("Arial", Font.BOLD,48 ));
		titre.setForeground(Color.WHITE);
		titre.setBounds(0,100,LARGEUR_CARTE *NB_PIX_CASE,60);
		add(titre);
		
		//Bouton Nouvelle Partie
		btnNouvellePartie = creerBouton("Nouvelle partie",CENTREX,STARTY);
		btnNouvellePartie.addActionListener(e -> {
			if(onNouvellePartie != null)
				onNouvellePartie.run();
			});
		add(btnNouvellePartie);

		//Bouton Nouvelle Partie 
		btnChargerPartie = creerBouton("Charger une partie",CENTREX,STARTY+ESPACEMENT);
		add(btnChargerPartie);
		btnChargerPartie.addActionListener( e -> {
			Component parent = SwingUtilities.getWindowAncestor(this);
			String[] options = {"Slot 1", "Slot 2", "Slot 3", "Annuler"};
			
			int choix = javax.swing.JOptionPane.showOptionDialog(
				parent,
				"Choisissez un slot à charger", 
				"Charger une partie", 
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]
			); 
			
			if(choix == 0 || choix == 1 || choix == 2 ){
				
				int slotNumber = choix + 1;
				File fichier = new File("save/slot"+slotNumber+".wg");
				if(!fichier.exists()){
					JOptionPane.showMessageDialog(
						parent,
						"Aucun sauvegarde trouvée pout slot"+slotNumber+" .",
						"Slot vide",
						JOptionPane.WARNING_MESSAGE
					);
					return ;
				}
				
				try{
					JPanel tempPanel = new JPanel();
					
					Carte mapChargee = SauveCharge.charger(fichier,tempPanel);
					
					FenetreJeu.initialiserJeu(fenetreJeu, mapChargee);
					
				}catch(IOException ex){
					JOptionPane.showMessageDialog(
							parent, 
							"Erreur lors du chargement : " + ex.getMessage(),
							"Erreur de chargement",
							JOptionPane.ERROR_MESSAGE
							); 
					 ex.printStackTrace();
				}catch (ClassNotFoundException ex) {
					JOptionPane.showMessageDialog(
							parent, 
							"Fichier de sauvegadre corrompu ou incompatible.",
							"Erreur de chargement", 
							JOptionPane.ERROR_MESSAGE
						); 
					 ex.printStackTrace();
				}
				
				
			}			
		});
		
		
		//Bouton Quitter
		btnQuitter = creerBouton("Quitter",CENTREX,STARTY+ESPACEMENT*2); 
		//...
		add(btnQuitter); 
	}
	
	private JButton creerBouton(String texte, int x,int y){
		JButton bouton = new JButton(texte);
		bouton.setBounds(x,y,250,50);
		bouton.setFont(new Font("Arial",Font.BOLD,20));
		bouton.setFocusPainted(false);
		bouton.setBackground(new Color(70,130,180)); // Bleu acier
		bouton.setForeground(Color.WHITE);
		bouton.setBorder(BorderFactory.createRaisedBevelBorder());// bouton qui ressort
		
		bouton.addMouseListener( new MouseAdapter() {
			public void mouseEntered(MouseEvent e){
				bouton.setBackground(new Color(100,149,237));//Bleu plus clair 
			}
			public void mouseExited(MouseEvent e){
				bouton.setBackground(new Color(70,130,180)); // Bleu acier
			}
		});
		
		return bouton;
	}
	
	
	
	
}
