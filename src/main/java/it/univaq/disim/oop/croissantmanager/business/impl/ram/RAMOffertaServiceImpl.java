package it.univaq.disim.oop.croissantmanager.business.impl.ram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import it.univaq.disim.oop.croissantmanager.business.BusinessExc;
import it.univaq.disim.oop.croissantmanager.business.NoOffersFoundExc;
import it.univaq.disim.oop.croissantmanager.business.OffertaService;
import it.univaq.disim.oop.croissantmanager.domain.Azienda;
import it.univaq.disim.oop.croissantmanager.domain.Candidatura;
import it.univaq.disim.oop.croissantmanager.domain.Competenza;
import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
import it.univaq.disim.oop.croissantmanager.domain.OffertaLavoro;

public class RAMOffertaServiceImpl implements OffertaService {

	private static Set<OffertaLavoro> offerte = new LinkedHashSet<>();
	private static int lastId = 1;
	private static List<Candidatura> candidatureInviate = new ArrayList<>();
	private static int lastCandidaturaId = 1;

	public RAMOffertaServiceImpl() {
	}

	/* Metodi relativi alle candidature */

	@Override
	public Set<OffertaLavoro> findAllOfferte() throws BusinessExc {
		return (new HashSet<OffertaLavoro>(offerte));
	}

	@Override
	public OffertaLavoro findOffertaDiLavoroById(int id) throws BusinessExc {
		for (OffertaLavoro offerta : offerte) {
			if (offerta.getId() == Integer.valueOf(id)) {
				return offerta;
			}
		}
		throw new NoOffersFoundExc();
	}

	@Override
	public Set<OffertaLavoro> findOfferteByAzienda(Azienda azienda) throws BusinessExc {
		Set<OffertaLavoro> result = new HashSet<>();
		for (OffertaLavoro offerta : offerte) {
			/*
			 * Verifico se l'offerta sia stata inserita da un'azienda con una ragione
			 * corrispondente a quella dell'azienda passata come parametro
			 */
			if (offerta.getAzienda().getRagione().equals(azienda.getRagione())) {
				result.add(offerta);
			}
		}

		return result;
	}

	@Override
	public Set<OffertaLavoro> findBestOfferte(Lavoratore lavoratore, int indice) throws BusinessExc {
		Set<OffertaLavoro> result = new HashSet<>();

		for (OffertaLavoro offerta : offerte) {
			Set<Competenza> competenze = new HashSet<>(offerta.getCompetenzeRichieste());

			/*
			 * Il metodo retainAll rimuove dalla collection tutte le competenze non
			 * possedute ANCHE dal lavoratore. In questo modo, in competenze rimangono
			 * soltanto le competenze comuni
			 */

			competenze.retainAll(lavoratore.getCompetenzePossedute());
			if ((competenze.size()) >= indice) {
				result.add(offerta);
			}
		}

		return result;
	}

	public int getMaximumAffinity(Lavoratore lavoratore) {
		int max = 0;

		for (OffertaLavoro offerta : offerte) {
			Set<Competenza> competenzeRichieste = new HashSet<>(offerta.getCompetenzeRichieste());

			/*
			 * Il metodo retainAll rimuove dalla collection tutte le competenze non
			 * possedute ANCHE dal lavoratore. In questo modo, in competenze rimangono
			 * soltanto le competenze comuni
			 */

			competenzeRichieste.retainAll(lavoratore.getCompetenzePossedute());
			if (competenzeRichieste.size() > max) {
				max = competenzeRichieste.size();
			}
		}

		return max;
	}

	@Override
	public Set<OffertaLavoro> findOffertaByLocalitaOrKeyword(String localitaOrKeyword) throws BusinessExc {
		Set<OffertaLavoro> result = new HashSet<>();
		for (OffertaLavoro offerta : offerte) {

			/*
			 * "Cerco" la stringa passata come parametro tra la ragione dell'azienda che ha
			 * inserito l'offerta, la località e il settore
			 */

			if (offerta.getLocalita().equals(localitaOrKeyword)
					|| offerta.getAzienda().getRagione().equals(localitaOrKeyword)
					|| offerta.getSettore().equals(localitaOrKeyword)) {
				result.add(offerta);
			}
		}
		return result;
	}

	@Override
	public void addOfferta(OffertaLavoro offerta) throws BusinessExc {
		offerta.setId(lastId++);
		offerte.add(offerta);
	}

	@Override
	public void removeOfferta(OffertaLavoro offerta) throws BusinessExc {
		for (OffertaLavoro offertaLavoro : offerte) {
			if (offertaLavoro.getId() == offerta.getId()) {
				offerte.remove(offertaLavoro);

				/* Rimuovo anche eventuali candidature relative all'offerta appena eliminata */

				for (Candidatura candidatura : candidatureInviate) {
					if (candidatura.getOfferta().equals(offerta)) {
						removeCandidatura(candidatura);
					}
				}
				return;
			}
		}
	}

	@Override
	public void updateOfferta(OffertaLavoro offerta) throws BusinessExc {
		for (OffertaLavoro offertaLavoro : offerte) {
			if (offertaLavoro.equals(offerta)) {
				offertaLavoro = offerta;
				return;
			}
		}
	}

	/* Metodi relativi alle candidature */

	@Override
	public List<Candidatura> findAllCandidature() throws BusinessExc {
		return candidatureInviate;
	}

	@Override
	public List<Candidatura> findCandidatureByLavoratore(Lavoratore lavoratore) throws BusinessExc {
		List<Candidatura> result = new ArrayList<>();

		for (Candidatura candidatura : candidatureInviate) {
			if (candidatura.getLavoratore().getId() == lavoratore.getId()) {
				result.add(candidatura);
			}
		}

		return result;
	}

	@Override
	public void addCandidatura(Candidatura candidatura) throws BusinessExc {
		candidatura.setId(lastCandidaturaId++);
		candidatureInviate.add(candidatura);
	}

	@Override
	public void removeCandidatura(Candidatura candidatura) throws BusinessExc {
		for (Candidatura cand : candidatureInviate) {
			if (cand.getId() == candidatura.getId()) {
				candidatureInviate.remove(candidatura);
				return;
			}
		}
	}

}
