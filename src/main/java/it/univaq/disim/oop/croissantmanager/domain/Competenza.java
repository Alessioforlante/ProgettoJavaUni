package it.univaq.disim.oop.croissantmanager.domain;

public enum Competenza {

	COMP1("Consapevolezza commerciale"), COMP2("Doti comunicative"), COMP3("Lavoro di squadra"), COMP4("Persuasione"),
	COMP5("Problem solving"), COMP6("Leadership"), COMP7("Doti organizzative"), COMP8("Perseveranza"), COMP9("Fiducia"),
	COMP10("Resilienza");

	private final String testo;

	Competenza(final String testo) {
		this.testo = testo;
	}

	@Override
	public String toString() {
		return testo;
	}

	// TODO: rimuovere il metodo seguente
	public static Competenza getCompetenzaByText(String string) throws IllegalArgumentException {
		for (Competenza competenza : Competenza.values()) {
			if (competenza.toString().equals(string))
				return competenza;
		}
		throw new IllegalArgumentException();
	}
}
