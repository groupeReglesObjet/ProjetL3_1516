package interfaceGraphique.view;

// TODO remove?
public enum TypeElement {
	PERSONNAGE("Personnage"),
	POTION("Potion");
	
	private String nom;
	
	private TypeElement(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}
	
	
}
