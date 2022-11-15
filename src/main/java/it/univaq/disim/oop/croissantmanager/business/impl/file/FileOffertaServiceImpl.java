package it.univaq.disim.oop.croissantmanager.business.impl.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.NoOffersFoundExc;
import it.univaq.disim.oop.croissantmanager.business.OffertaService;
import it.univaq.disim.oop.croissantmanager.business.UtenteService;
import it.univaq.disim.oop.croissantmanager.domain.Azienda;
import it.univaq.disim.oop.croissantmanager.domain.Candidatura;
import it.univaq.disim.oop.croissantmanager.domain.Competenza;
import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
import it.univaq.disim.oop.croissantmanager.domain.OffertaLavoro;

public class FileOffertaServiceImpl implements OffertaService {

	private String repositoryBase;
	private String offerteFilename;
	private String candidatureFilename;
	private UtenteService utenteService;

	public FileOffertaServiceImpl(String repositoryBase, String offerteFilename, String candidatureFilename,
			UtenteService utenteService) {
		this.repositoryBase = repositoryBase;
		this.offerteFilename = offerteFilename;
		this.candidatureFilename = candidatureFilename;
		this.utenteService = utenteService;
	}

	/* Metodi relativi alle offerte */

	@Override
	public Set<OffertaLavoro> findAllOfferte() throws BusinessExc {
		Set<OffertaLavoro> result = new HashSet<>();
		try {
			FileData fileData = Utility.readAllRows(offerteFilename);
			for (int i = 0; i < fileData.getRighe().size(); i++) {
				OffertaLavoro offerta = createOffertaFromRow(i);
				result.add(offerta);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}
		return result;
	}

	@Override
	public OffertaLavoro findOffertaDiLavoroById(int id) throws BusinessExc {
		try {
			FileData fileData = Utility.readAllRows(offerteFilename);
			for (int i = 0; i < fileData.getRighe().size(); i++) {
				if (Integer.parseInt(fileData.getRighe().get(i)[0]) == Integer.valueOf(id)) {
					return createOffertaFromRow(i);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}

		throw new NoOffersFoundExc();
	}

	@Override
	public Set<OffertaLavoro> findOfferteByAzienda(Azienda azienda) throws BusinessExc {
		Set<OffertaLavoro> result = new HashSet<>();
		try {
			FileData fileData = Utility.readAllRows(offerteFilename);
			for (int i = 0; i < fileData.getRighe().size(); i++) {
				/*
				 * Se l'id corrisponde a quello di azienda, aggiungo l'offerta alla collection
				 * result
				 */
				if (Integer.parseInt(fileData.getRighe().get(i)[3]) == azienda.getId()) {
					OffertaLavoro offerta = createOffertaFromRow(i);
					result.add(offerta);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}
		return result;
	}

	@Override
	public Set<OffertaLavoro> findBestOfferte(Lavoratore lavoratore, int indice) throws BusinessExc {
		Set<OffertaLavoro> result = new HashSet<>();

		try {
			FileData fileData = Utility.readAllRows(offerteFilename);
			for (int i = 0; i < fileData.getRighe().size(); i++) {
				OffertaLavoro offerta = createOffertaFromRow(i);
				Set<Competenza> competenze = new HashSet<>(offerta.getCompetenzeRichieste());
				/*
				 * Il metodo retainAll rimuove dalla collection tutte le competenze non
				 * possedute anche dal lavoratore
				 */
				competenze.retainAll(lavoratore.getCompetenzePossedute());
				/*
				 * Verifico se ho trovato almeno (indice) competenze comuni
				 */
				if ((competenze.size()) >= indice) {
					result.add(offerta);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}

		return result;
	}

	/*
	 * Il metodo getMaximumAffinity quantifica l'affinità tra il lavoratore e
	 * l'offerta più affine alle sue competenze disponibile al momento. Il valore
	 * restituito è utilizzato in BestOffersController al fine di impostare il
	 * valore massimo e di partenza dello spinner con il quale l'utente può
	 * "filtrare" la sua ricerca
	 */
	@Override
	public int getMaximumAffinity(Lavoratore lavoratore) throws BusinessExc {
		int max = 0;

		try {
			FileData fileData = Utility.readAllRows(offerteFilename);
			for (int i = 0; i < fileData.getRighe().size(); i++) {
				OffertaLavoro offerta = createOffertaFromRow(i);
				Set<Competenza> competenze = new HashSet<>(offerta.getCompetenzeRichieste());
				competenze.retainAll(lavoratore.getCompetenzePossedute());
				if (competenze.size() > max) {
					max = competenze.size();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}

		return max;
	}

	@Override
	public Set<OffertaLavoro> findOffertaByLocalitaOrKeyword(String localitaOrKeyword) throws BusinessExc {
		Set<OffertaLavoro> result = new HashSet<>();
		try {
			FileData fileData = Utility.readAllRows(offerteFilename);
			for (int i = 0; i < fileData.getRighe().size(); i++) {
				/*
				 * "Cerco" la stringa passata come parametro tra la ragione dell'azienda che ha
				 * inserito l'offerta, la località e il settore
				 */
				if (fileData.getRighe().get(i)[1].equals(localitaOrKeyword)
						|| fileData.getRighe().get(i)[4].equals(localitaOrKeyword)
						|| ((FileUtenteServiceImpl) utenteService)
								.findAziendaById(Integer.parseInt(fileData.getRighe().get(i)[4])).getRagione()
								.equals(localitaOrKeyword)) {
					OffertaLavoro offerta = createOffertaFromRow(i);
					result.add(offerta);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}
		return result;
	}

	@Override
	public void addOfferta(OffertaLavoro offerta) throws BusinessExc {
		try {
			FileData fileData = Utility.readAllRows(offerteFilename);
			try (PrintWriter writer = new PrintWriter(new File(offerteFilename))) {
				long contatore = fileData.getContatore();
				offerta.setId(Integer.valueOf((int) contatore));
				writer.println((contatore + 1));
				for (String[] righe : fileData.getRighe()) {
					writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
				}
				StringBuilder row = new StringBuilder();
				row.append(contatore);
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(offerta.getLocalita());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(offerta.getDataInserimento().toString());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(offerta.getAzienda().getId());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(offerta.getSettore());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(offerta.getSalario());
				for (int i = 0; i < Competenza.values().length; i++) {
					row.append(Utility.SEPARATORE_COLONNA);
					/*
					 * Inserisco "true" se l'offerta richiede la competenza corrispondente, "false"
					 * altrimenti
					 */
					if (offerta.getCompetenzeRichieste().contains(Competenza.values()[i])) {
						row.append("true");
					} else {
						row.append("false");
					}
				}
				writer.println(row.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc(e);
		}
	}

	@Override
	public void removeOfferta(OffertaLavoro offerta) throws BusinessExc {
		try {
			/*
			 * Sfrutto un file temporaneo da rinominare al termine dell'operazione di
			 * rimozione
			 */
			Path tempFilePath = Paths.get(repositoryBase + File.separator + "offerte_temp.txt");
			Files.createFile(tempFilePath);
			FileData fileData = Utility.readAllRows(offerteFilename);
			try (PrintWriter writer = new PrintWriter(tempFilePath.toFile())) {
				long contatore = fileData.getContatore();
				writer.println((contatore - 1));
				for (String[] righe : fileData.getRighe()) {
					if (!(Integer.parseInt(righe[0]) == (offerta.getId()))) {
						writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
					}
				}
			}
			/* Rimuovo il file "vecchio" e rinomino quello temporaneo */

			Files.delete(Paths.get(repositoryBase + File.separator + "offerte.txt"));
			Files.move(tempFilePath, Paths.get(repositoryBase + File.separator + "offerte.txt"),
					StandardCopyOption.REPLACE_EXISTING);

			/* Rimuovo eventuali candidature legate all'offerta appena rimossa */

			FileData fileData2 = Utility.readAllRows(candidatureFilename);
			for (int i = 0; i < fileData2.getRighe().size(); i++) {
				if (Integer.parseInt(fileData2.getRighe().get(i)[3]) == offerta.getId()) {
					Candidatura candidatura = new Candidatura();
					candidatura.setId(Integer.parseInt(fileData2.getRighe().get(i)[0]));
					removeCandidatura(candidatura);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc(e);
		}

	}

	@Override
	public void updateOfferta(OffertaLavoro offerta) throws BusinessExc {
		try {
			/*
			 * Sfrutto un file temporaneo da rinominare al termine dell'operazione di
			 * aggiornamento
			 */
			Path tempFilePath = Paths.get(repositoryBase + File.separator + "offerte_temp.txt");
			Files.createFile(tempFilePath);
			FileData fileData = Utility.readAllRows(offerteFilename);
			try (PrintWriter writer = new PrintWriter(tempFilePath.toFile())) {
				long contatore = fileData.getContatore();
				writer.println((contatore));
				for (String[] righe : fileData.getRighe()) {
					if (!(Integer.parseInt(righe[0]) == (offerta.getId()))) {
						writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
					} else {
						StringBuilder row = new StringBuilder();
						row.append(offerta.getId());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(offerta.getLocalita());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(offerta.getDataInserimento().toString());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(offerta.getAzienda().getId());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(offerta.getSettore());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(offerta.getSalario());
						for (int i = 0; i < Competenza.values().length; i++) {
							row.append(Utility.SEPARATORE_COLONNA);
							/*
							 * Inserisco "true" se l'offerta richiede la competenza corrispondente, "false"
							 * altrimenti
							 */
							if (offerta.getCompetenzeRichieste().contains(Competenza.values()[i])) {
								row.append("true");
							} else {
								row.append("false");
							}
						}
						writer.println(row.toString());
					}
				}
			}

			/* Rimuovo il file "vecchio" e rinomino quello temporaneo */

			Files.delete(Paths.get(repositoryBase + File.separator + "offerte.txt"));
			Files.move(tempFilePath, Paths.get(repositoryBase + File.separator + "offerte.txt"),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc(e);
		}
	}

	/* Metodi relativi alle candidature */

	@Override
	public List<Candidatura> findAllCandidature() throws BusinessExc {
		List<Candidatura> result = new ArrayList<>();
		try {
			FileData fileData = Utility.readAllRows(candidatureFilename);
			for (int i = 0; i < fileData.getRighe().size(); i++) {
				Candidatura candidatura = createCandidaturaFromRow(i);
				result.add(candidatura);
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}

		return result;
	}

	@Override
	public List<Candidatura> findCandidatureByLavoratore(Lavoratore lavoratore) throws BusinessExc {
		List<Candidatura> result = new ArrayList<>();
		try {
			FileData fileData = Utility.readAllRows(candidatureFilename);
			for (int i = 0; i < fileData.getRighe().size(); i++) {
				if (Integer.parseInt(fileData.getRighe().get(i)[2]) == lavoratore.getId()) {
					Candidatura candidatura = createCandidaturaFromRow(i);
					result.add(candidatura);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}

		return result;
	}

	@Override
	public void addCandidatura(Candidatura candidatura) throws BusinessExc {
		try {
			FileData fileData = Utility.readAllRows(candidatureFilename);
			try (PrintWriter writer = new PrintWriter(new File(candidatureFilename))) {
				long contatore = fileData.getContatore();
				candidatura.setId(Integer.valueOf((int) contatore));
				writer.println((contatore + 1));
				for (String[] righe : fileData.getRighe()) {
					writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
				}
				StringBuilder row = new StringBuilder();
				row.append(contatore);
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(candidatura.getDataCandidatura().toString());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(candidatura.getLavoratore().getId());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(candidatura.getOfferta().getId());
				writer.println(row.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc(e);
		}
	}

	@Override
	public void removeCandidatura(Candidatura candidatura) throws BusinessExc {
		try {
			/*
			 * Sfrutto un file temporaneo da rinominare al termine dell'operazione di
			 * rimozione
			 */
			Path tempFilePath = Paths.get(repositoryBase + File.separator + "candidature_temp.txt");
			Files.createFile(tempFilePath);
			FileData fileData = Utility.readAllRows(candidatureFilename);
			try (PrintWriter writer = new PrintWriter(tempFilePath.toFile())) {
				long contatore = fileData.getContatore();
				writer.println((contatore - 1));
				for (String[] righe : fileData.getRighe()) {
					if (!(Integer.parseInt(righe[0]) == (candidatura.getId()))) {
						writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
					}
				}
			}
			Files.delete(Paths.get(repositoryBase + File.separator + "candidature.txt"));
			Files.move(tempFilePath, Paths.get(repositoryBase + File.separator + "candidature.txt"),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc(e);
		}
	}

	/* Metodi relativi alla creazione di oggetti da file */

	/* Crea un'offerta a partire da una riga */
	private OffertaLavoro createOffertaFromRow(int indice) throws BusinessExc {
		OffertaLavoro offerta = new OffertaLavoro();
		try {
			FileData fileData = Utility.readAllRows(offerteFilename);
			List<String[]> righe = fileData.getRighe();
			offerta.setId(Integer.parseInt(righe.get(indice)[0]));
			offerta.setLocalita(righe.get(indice)[1]);
			offerta.setDataInserimento(LocalDate.parse(righe.get(indice)[2]));
			offerta.setAzienda(
					((FileUtenteServiceImpl) utenteService).findAziendaById(Integer.parseInt(righe.get(indice)[3])));
			offerta.setSettore(righe.get(indice)[4]);
			offerta.setSalario(Float.parseFloat(righe.get(indice)[5]));
			Set<Competenza> competenzeRichieste = new HashSet<>();
			for (int j = 6; j <= 15; j++) {
				if (righe.get(indice)[j].equals(String.valueOf(true))) {
					competenzeRichieste.add(Competenza.values()[j - 6]);
				}
			}
			offerta.setCompetenzeRichieste(competenzeRichieste);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}
		return offerta;
	}

	/* Crea una candidatura a partire da una riga */
	private Candidatura createCandidaturaFromRow(int indice) throws BusinessExc {
		Candidatura candidatura = new Candidatura();
		try {
			FileData fileData = Utility.readAllRows(candidatureFilename);
			String[] riga = fileData.getRighe().get(indice);
			candidatura.setId(Integer.parseInt(riga[0]));
			candidatura.setDataCandidatura(LocalDate.parse(riga[1]));
			candidatura.setLavoratore(
					((FileUtenteServiceImpl) utenteService).findLavoratoreById(Integer.valueOf(riga[2])));
			candidatura.setOfferta(findOffertaDiLavoroById(Integer.valueOf(riga[3])));
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessExc();
		}

		return candidatura;
	}

}
