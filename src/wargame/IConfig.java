package wargame;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JMenuBar;

public interface IConfig {
	int LARGEUR_CARTE = 25; int HAUTEUR_CARTE = 15; // en nombre de cases
	int NB_PIX_CASE = 50;
	int POSITION_X = 100; int POSITION_Y = 50; // Position de la fen�tre
	int NB_HEROS = 6; int NB_MONSTRES = 15; int NB_OBSTACLES = 20;
	
	int HAUTEUR_BARRE_MENU = 30;
	
	int LARGEUR_TITRE = 500; 
	int HAUTEUR_TITRE = 250;
	
	int LARGEUR_BOUTON = 400;
	int HAUTEUR_BOUTON = 100;
	
	int STARTY = 250;
	int ESPACEMENT = 150;
	
	int LARGEUR_JEU = (LARGEUR_CARTE * NB_PIX_CASE);
	int HAUTEUR_JEU = (HAUTEUR_CARTE * NB_PIX_CASE) + 100 + HAUTEUR_BARRE_MENU+10;
	
	
	Color COULEUR_VIDE = Color.white, COULEUR_INCONNU = Color.lightGray;
	Color COULEUR_TEXTE = Color.black, COULEUR_MONSTRES = Color.black;
	Color COULEUR_HEROS = Color.red, COULEUR_HEROS_DEJA_JOUE = Color.pink;
	Color COULEUR_EAU = Color.blue, COULEUR_FORET = Color.getHSBColor((float) 0.35,(float) 0.95,(float) 0.55), COULEUR_ROCHER = Color.gray;
	Color COULEUR_PLAINE = Color.green; Color COULEUR_SELECTION = Color.red; Color COULEUR_CHAMP_ACTION = Color.pink;
	
	ImageIcon ECLAIR = new ImageIcon("images/units/eclair.png");
	ImageIcon COEUR = new ImageIcon("images/units/coeur.png");
	ImageIcon ORC = new ImageIcon("images/units/Orc.png");
	ImageIcon TROLL = new ImageIcon("images/units/Troll.png");
	ImageIcon GOBELIN = new ImageIcon("images/units/Gobelin.png");
	ImageIcon ELF = new ImageIcon("images/units/Elf.png");
	ImageIcon NAIN = new ImageIcon("images/units/Nain.png");
	ImageIcon HUMAIN = new ImageIcon("images/units/Humain.png");
	ImageIcon HOBBIT = new ImageIcon("images/units/Hobbit.png");
	ImageIcon PLAINE = new ImageIcon("images/terrain/Plaine.png");
	ImageIcon EAU = new ImageIcon("images/terrain/Eau.png");
	ImageIcon FORET = new ImageIcon("images/terrain/Foret.png");
	ImageIcon ROCHER = new ImageIcon("images/terrain/Rocher.png");
	ImageIcon BROUILLARD = new ImageIcon("images/terrain/Brouillard.png");
	
	ImageIcon FOND = new ImageIcon("images/menu/fond.png");
	
	ImageIcon TITRE = new ImageIcon("images/menu/titre.png");
	ImageIcon BOUTON = new ImageIcon("images/menu/bouton.png");
}