
package interfaceGraphique.uiSimple;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;

import interfaceGraphique.IHM;
import interfaceGraphique.tableModel.PotionTableModel;
import interfaceGraphique.tableModel.PersonnageTableModel;
import interfaceGraphique.tableRenderer.HeaderRenderer;
import interfaceGraphique.tableRenderer.NormalRenderer;
import interfaceGraphique.view.VueElement;
import interfaceGraphique.view.VuePersonnage;
import interfaceGraphique.view.VuePersonnageDeconnecte;
import interfaceGraphique.view.VuePotion;

/**
 * Panneau contenant les tableaux des elements de la partie.
 */
public class ElementsJPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Hauteur du header (premiere ligne) des tableaux.
	 */
	private static final int HEADER_HEIGHT = 35;
    	
    /**
     * IHM a laquelle appartient ce panneau.
     */
	private IHM ihm;
	
	/**
	 * Modele de la table des personnages.
	 */
	private PersonnageTableModel modelTablePersonnages;
	
	/**
	 * Modele de la table des potions.
	 */
	private PotionTableModel modelTablePotions;
	
	/**
	 * ScrollPane contenant les personnages.
	 */
	private JScrollPane jScrollPanePersonnages;

	/**
	 * ScrollPane contenant les potions.
	 */
	private JScrollPane jScrollPanePotions;
	
	/**
	 * SplitPane separant les personnages et les potions.
	 */
    private JSplitPane jSplitPane;
    
    /**
	 * Tableau des personnages.
	 */
	private JTable jTablePersonnages;

	/**
     * Tableau des potions.
     */
    private JTable jTablePotions;
    
    /**
     * Menu contextuel (clic droit). 
     */
    private JPopupMenu menuContextuel;
    
    /**
     * Permet d'afficher la vue detaillee d'un element.
     */
	private JMenuItem detail;
    
    /**
     * Cree un panel qui affiche les elements de la partie sous forme de 
     * tableaux.
     * @param ihm IHM
     */
    public ElementsJPanel(IHM ihm) {    
    	this.ihm = ihm;    	
    	initComposants();    	
    }
    
    /**
     * Initialise les composants.
     */
    private void initComposants() {
    	setPreferredSize(new Dimension(800,600));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        // initialisation des tables
        initTablePersonnages();        
        initTablePotions();
        
        // initialisation du menu de clic droit
        initMenuClickDroit();

        // creation et ajout du listener de clic sur les tables
        MouseListener listener = new MouseAdapter() {			
			@Override
			public void mouseClicked(MouseEvent e) {
				clickOnTable(e);
			}		
			
		};
		
        jTablePersonnages.addMouseListener(listener); 
        jTablePotions.addMouseListener(listener);
		
		// ajout des composants
        jSplitPane = new JSplitPane();
        jSplitPane.setDividerLocation(350);
        jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        
        jSplitPane.setTopComponent(jScrollPanePersonnages);
        jSplitPane.setBottomComponent(jScrollPanePotions);        
        
        add(jSplitPane);
        
	}

    /**
     * Initialise le menu contextuel.
     */
    private void initMenuClickDroit() {
        detail = new JMenuItem();
        menuContextuel = new JPopupMenu();
        
        detail.setText("Afficher la vue detaillee");
        menuContextuel.add(detail);
        
        detail.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ihm.getElementSelectionne() != null) {
					ihm.detailleSelectionne(MouseInfo.getPointerInfo().getLocation()); 
				}
			}
		});
	}

	/**
     * Initialise la table des personnages.
     */
	private void initTablePersonnages() {
		jTablePersonnages = new JTable();

        // mise en place du modele
        modelTablePersonnages = new PersonnageTableModel();         
        jTablePersonnages.setModel(modelTablePersonnages);
        
        // ajustement de la taille des colonnes
        for (int i = 0; i < modelTablePersonnages.getColumnCount(); i++) {
        	int width = modelTablePersonnages.getColumnWidth(i);
        	if (width != 0) {
        		jTablePersonnages.getColumnModel().getColumn(i).setMaxWidth(width);
        		jTablePersonnages.getColumnModel().getColumn(i).setPreferredWidth(width);
        	}
        }
        
        jTablePersonnages.setDefaultRenderer(Object.class, new NormalRenderer(IHM.grisClair, IHM.noir));
        jTablePersonnages.setDefaultRenderer(Integer.class, new NormalRenderer(IHM.grisClair, IHM.noir));              
        
        jTablePersonnages.setIntercellSpacing(new Dimension(0, 0));
        jTablePersonnages.setRowHeight(35);
		
        jTablePersonnages.setTableHeader(new JTableHeader(jTablePersonnages.getColumnModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = HEADER_HEIGHT;
				return d;
			}
		});
		
        jTablePersonnages.getTableHeader().setDefaultRenderer(new HeaderRenderer());
        
        jScrollPanePersonnages = new JScrollPane();
        jScrollPanePersonnages.getViewport().setBackground(new Color (115,115,115));
        jScrollPanePersonnages.setBorder(BorderFactory.createTitledBorder(
        		null, 
        		"Personnages", 
        		TitledBorder.CENTER, 
        		TitledBorder.DEFAULT_POSITION, 
        		new Font("Helvetica Neue", 0, 14), 
        		new Color(0, 0, 0)));
        
        jScrollPanePersonnages.setViewportView(jTablePersonnages);
	}

    /**
     * Initialise la table des potions.
     */
	private void initTablePotions() {
		jTablePotions = new JTable();
        
        // mise en place du modele
        modelTablePotions = new PotionTableModel();         
        jTablePotions.setModel(modelTablePotions);
        
        // ajustement de la taille des colonnes
        for (int i = 0; i < modelTablePotions.getColumnCount(); i++) {
        	int width = modelTablePotions.getColumnWidth(i);
        	if (width != 0) {
        		jTablePotions.getColumnModel().getColumn(i).setMaxWidth(width);
        		jTablePotions.getColumnModel().getColumn(i).setPreferredWidth(width);
        	}
        }
        
        jTablePotions.setDefaultRenderer(Object.class, new NormalRenderer(IHM.noir, IHM.grisClair));
        jTablePotions.setDefaultRenderer(Integer.class, new NormalRenderer(IHM.noir, IHM.grisClair));              
        
        jTablePotions.setIntercellSpacing(new Dimension(0, 0));
        jTablePotions.setRowHeight(35);
		
        jTablePotions.setTableHeader(new JTableHeader(jTablePotions.getColumnModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = HEADER_HEIGHT;
				return d;
			}
		});
		
        jTablePotions.getTableHeader().setDefaultRenderer(new HeaderRenderer());

        jScrollPanePotions = new JScrollPane();
        jScrollPanePotions.getViewport().setBackground(IHM.grisFonce);
        jScrollPanePotions.setBorder(BorderFactory.createTitledBorder(
        		null, 
        		"Potions", 
        		TitledBorder.CENTER, 
        		TitledBorder.DEFAULT_POSITION, 
        		new Font("Helvetica Neue", 0, 14), 
        		new Color(0, 0, 0)));
        
        jScrollPanePotions.setViewportView(jTablePotions);
	}


	/**
	 * Traitement a realiser lors du clic sur un tableau.
	 * @param ev evenement de la souris
	 */
	private void clickOnTable(MouseEvent ev) {
		// vue clique
		VueElement newSelect = null;
		
		// vue deja selectionne
		VueElement prevSelect = ihm.getElementSelectionne();
		
		// recuperation de la vue clique
		JTable table = ((JTable) ev.getSource());
		int selectedLine = table.rowAtPoint(ev.getPoint());
		
		if (selectedLine != -1) {
			if (table.getModel() instanceof PersonnageTableModel) {
				newSelect = ((PersonnageTableModel) table.getModel()).getVue(selectedLine);
			} else {
				newSelect = ((PotionTableModel) table.getModel()).getVue(selectedLine);
			}
		}
		// selection dans l'ihm de la vue clique 
		ihm.setElementSelectionne(newSelect);
		
		int buttonDown = ev.getButton();
		
		// clic gauche
		if (buttonDown == MouseEvent.BUTTON1) {
			// si selection de l'element deja selectionne,
			// on le deselectionne dans l'ihm
			if(prevSelect != null && newSelect != null && newSelect.getRefRMI() == prevSelect.getRefRMI()) {
				ihm.setElementSelectionne(null);
			}
			
		} else if(buttonDown == MouseEvent.BUTTON3) {
			// si clic droit, on affiche le menu contextuel
	    	menuContextuel.show(table, ev.getX(), ev.getY());
	    }
	}

	/**
	 * Definit les elements de la partie.
	 * @param personnages personnage present dans l'arene
	 * @param potions potions presentes dans l'arene
	 * @param personnagesDeconnectes personnages deconnectes de l'arene
	 * @param potionsEnAttente potions en attente de rentrer sur l'arene
	 */
	public void setElements(List<VuePersonnage> personnages, List<VuePotion> potions, 
			List<VuePersonnageDeconnecte> personnagesDeconnectes, List<VuePotion> potionsEnAttente) {
				
		// comparateur triant des vueElement selon leur refRMI
		Comparator<VueElement> vueComparator = new Comparator<VueElement>() {
			@Override
			public int compare(VueElement v1, VueElement v2) {
				return v1.getRefRMI() - v2.getRefRMI();
			}
		};
		
		// tri des potions et des personnages
		Collections.sort(personnages, vueComparator);
		Collections.sort(potions, vueComparator);
		
		// personnages deconnectes
		Comparator<VuePersonnageDeconnecte> compDeconnectes = new Comparator<VuePersonnageDeconnecte>() {
			@Override
			public int compare(VuePersonnageDeconnecte v1, VuePersonnageDeconnecte v2) {
				Integer tour1, tour2;
				tour1 = v1.getTourDeconnexion();
				tour2 = v2.getTourDeconnexion();
				return tour2.compareTo(tour1);
			}
		};
		
		Collections.sort(personnagesDeconnectes, compDeconnectes);
		
		if (ihm.getElementSelectionne() != null) {
			// recherche de l'element selectionne
			for (VuePersonnage vp : personnages) {
				if (vp.getRefRMI() == ihm.getElementSelectionne().getRefRMI()) {
					vp.setSelected(true);					
				}
			}
			
			for (VuePersonnageDeconnecte vpd : personnagesDeconnectes) {
				if (vpd.getRefRMI() == ihm.getElementSelectionne().getRefRMI()) {
					vpd.setSelected(true);
				}
			}
			
			for (VuePotion vp : potions) {
				if (vp.getRefRMI() == ihm.getElementSelectionne().getRefRMI()) {
					vp.setSelected(true);					
				}
			}
			
			for (VuePotion vp : potionsEnAttente) {
				if (vp.getRefRMI() == ihm.getElementSelectionne().getRefRMI()) {
					vp.setSelected(true);					
				}
			}
		}		
		
    	modelTablePersonnages.setVues(personnages);
    	modelTablePersonnages.setDeconnectes(personnagesDeconnectes);
    	modelTablePersonnages.fireTableDataChanged();
    	
    	modelTablePotions.setVues(potions);
    	modelTablePotions.setEnAttente(potionsEnAttente);
    	modelTablePotions.fireTableDataChanged();    	
    }              
}
