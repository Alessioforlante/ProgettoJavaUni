package it.univaq.disim.oop.croissantmanager.business.impl.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.UtenteNotFoundExc;
import it.univaq.disim.oop.croissantmanager.business.UtenteService;
import it.univaq.disim.oop.croissantmanager.domain.Azienda;
import it.univaq.disim.oop.croissantmanager.domain.Competenza;
import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
import it.univaq.disim.oop.croissantmanager.domain.Messaggio;
import it.univaq.disim.oop.croissantmanager.domain.Utente;

public class FileUtenteServiceImpl implements UtenteService {

	private String utentiFilename;
	private String messaggiFilename;

	public FileUtenteServiceImpl(String utentiFilename, String messaggiFilename) {
		this.utentiFilename = utentiFilename;
		this.messaggiFilename = messaggiFilename;
	}

	@Override
	public Utente authenticate(String username, String password) throws UtenteNotFoundExc, BusinessExc {
		try {
			FileData fileData = Utility.readAllRows(utentiFilename);
			for (String[] colonne : fileData.getRighe()) {
				if (colonne[4].equals(username) && colonne[5].equals(password)) {
					Utente utente = null;
					/*
					 * Switch per individuare se l'utente in questione sia un'azienda o un
					 * lavoratore
					 */
					switch (colonne[3]) {
					case "azienda":
						utente = new Azienda();
						break;
					case "lavoratore":
						utente = new Lavoratore();
						break;
					default:
						break;
					}
					if (utente != null) {
						utente.setId(Integer.parseInt(colonne[0]));
						utente.setUsername(username);
						utente.setPassword(password);
					} else {
						throw new BusinessExc("Si è verificato un errore nella lettura del file!");
					}
					if (utente instanceof Azienda) {
						((Azienda) utente).setRagione(colonne[1]);
						((Azienda) utente).setAmbito(colonne[2]);
						((Azienda) utente).setSede(colonne[6]);
						((Azienda) utente).setNumeroDipendenti(Integer.parseInt(colonne[8]));
					} else {
						((Lavoratore) utente).setNome(colonne[1]);
						((Lavoratore) utente).setCognome(colonne[2]);
						((Lavoratore) utente).setLuogoNascita(colonne[6]);
						((Lavoratore) utente).setDataNascita(LocalDate.parse(colonne[7]));

						/* Se leggo 'true' inserisco la Competenza relativa */

						Set<Competenza> competenzePossedute = new HashSet<>();
						for (int j = 1; j <= 10; j++) {
							if (colonne[j + 7].equals(String.valueOf(true))) {
								competenzePossedute.add(Competenza.values()[j - 1]);
							}
						}
						((Lavoratore) utente).setCompetenzePossedute(competenzePossedute);
					}
					return utente;
				}
			}

			/*
			 * Lancio l'eccezione UtenteNotFoundExc se l'utente che tenta di autenticarsi
			 * non è stato trovato
			 */

			throw new UtenteNotFoundExc();
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}
	}

	@Override
	public void sendMessaggio(Messaggio messaggio, Lavoratore lavoratore) throws BusinessExc {
		try {
			FileData fileData = Utility.readAllRows(messaggiFilename);
			try (PrintWriter writer = new PrintWriter(new File(messaggiFilename))) {
				long contatore = fileData.getContatore();
				writer.println((contatore + 1));
				for (String[] righe : fileData.getRighe()) {
					writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
				}
				StringBuilder row = new StringBuilder();
				row.append(contatore);
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(messaggio.getAzienda().getId());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(lavoratore.getId());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(messaggio.getTesto());
				writer.println(row.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}

	}

	@Override
	public Set<StringBuilder> findAllMessaggi(Lavoratore lavoratore) throws BusinessExc {
		Set<StringBuilder> result = new HashSet<>();
		try {
			FileData fileData = Utility.readAllRows(messaggiFilename);
			for (int i = 0; i < fileData.getRighe().size(); i++) {
				if (Integer.parseInt(fileData.getRighe().get(i)[2]) == lavoratore.getId()) {
					StringBuilder message = new StringBuilder();

					/*
					 * Evito che eventuali virgole non facciano parte del messaggio ricevute dal
					 * lavoratore
					 */

					for (int j = 3; j < fileData.getRighe().get(i).length - 1; j++) {
						message.append(fileData.getRighe().get(i)[j]);
						message.append(",");
					}
					message.append(fileData.getRighe().get(i)[fileData.getRighe().get(i).length - 1]);
					result.add(message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}

		return result;
	}

	@Override
	public void registerAsWorker(Utente lavoratore) throws BusinessExc {
		try {
			FileData fileData = Utility.readAllRows(utentiFilename);
			try (PrintWriter writer = new PrintWriter(new File(utentiFilename))) {
				long contatore = fileData.getContatore();
				writer.println((contatore + 1));
				for (String[] righe : fileData.getRighe()) {
					writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
				}
				StringBuilder row = new StringBuilder();
				row.append(contatore);
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(((Lavoratore) lavoratore).getNome());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(((Lavoratore) lavoratore).getCognome());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append("lavoratore");
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(lavoratore.getUsername());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(lavoratore.getPassword());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(((Lavoratore) lavoratore).getLuogoNascita());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(((Lavoratore) lavoratore).getDataNascita().toString());
				for (Competenza competenza : Competenza.values()) {
					row.append(Utility.SEPARATORE_COLONNA);
					/*
					 * Inserisco "true" se il lavoratore possiede la competenza corrispondente,
					 * "false" altrimenti
					 */
					if (((Lavoratore) lavoratore).getCompetenzePossedute().contains(competenza)) {
						row.append("true");
					} else {
						row.append("false");
					}
				}
				writer.println(row.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();

		}
	}

	@Override
	public void registerAsCompany(Utente azienda) throws BusinessExc {
		try {
			FileData fileData = Utility.readAllRows(utentiFilename);
			try (PrintWriter writer = new PrintWriter(new File(utentiFilename))) {
				long contatore = fileData.getContatore();
				writer.println((contatore + 1));
				for (String[] righe : fileData.getRighe()) {
					writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
				}
				StringBuilder row = new StringBuilder();
				row.append(contatore);
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(((Azienda) azienda).getRagione());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(((Azienda) azienda).getAmbito());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append("azienda");
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(azienda.getUsername());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(azienda.getPassword());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(((Azienda) azienda).getSede());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append("null");
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(((Azienda) azienda).getNumeroDipendenti());
				writer.println(row.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}
	}

	/*
	 * Il metodo isAlreadyExisting controlla l'esistenza di un utente (azienda o
	 * lavoratore) con un username corrispondente alla stringa passata come
	 * parametro
	 */
	@Override
	public boolean isAlreadyExisting(String username) throws BusinessExc {
		try {
			FileData fileData = Utility.readAllRows(utentiFilename);
			for (int i = 0; i < fileData.getRighe().size(); i++) {
				if (fileData.getRighe().get(i)[4].equals(username)) {
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}
		return false;
	}

	public Lavoratore findLavoratoreById(int indice) throws BusinessExc {
		Lavoratore lavoratore = new Lavoratore();
		try {
			FileData fileData = Utility.readAllRows(utentiFilename);
			for (String[] colonne : fileData.getRighe()) {
				if (Integer.parseInt(colonne[0]) == Integer.valueOf(indice)) {
					lavoratore.setId(Integer.parseInt(colonne[0]));
					lavoratore.setNome(colonne[1]);
					lavoratore.setCognome(colonne[2]);
					lavoratore.setUsername(colonne[4]);
					lavoratore.setPassword(colonne[5]);
					lavoratore.setLuogoNascita(colonne[6]);
					lavoratore.setDataNascita(LocalDate.parse(colonne[7]));
					Set<Competenza> competenzePossedute = new HashSet<>();

					/* Se leggo 'true' inserisco la Competenza relativa */

					for (int j = 8; j <= 17; j++) {
						if (colonne[j].equals(String.valueOf(true))) {
							competenzePossedute.add(Competenza.values()[j - 8]);
						}
					}
					lavoratore.setCompetenzePossedute(competenzePossedute);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}

		return lavoratore;
	}

	public Azienda findAziendaById(int indice) throws BusinessExc {
		Azienda azienda = new Azienda();
		try {
			FileData fileData = Utility.readAllRows(utentiFilename);
			for (String[] colonne : fileData.getRighe()) {
				if (Integer.parseInt(colonne[0]) == Integer.valueOf(indice)) {
					azienda.setId(Integer.parseInt(colonne[0]));
					azienda.setRagione(colonne[1]);
					azienda.setAmbito(colonne[2]);
					azienda.setUsername(colonne[4]);
					azienda.setPassword(colonne[5]);
					azienda.setSede(colonne[6]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}

		return azienda;
	}

}
