package it.univaq.disim.oop.croissantmanager.business;

import java.util.List;
import java.util.Set;

import it.univaq.disim.oop.croissantmanager.domain.Azienda;
import it.univaq.disim.oop.croissantmanager.domain.Candidatura;
import it.univaq.disim.oop.croissantmanager.domain.Lavoratore;
import it.univaq.disim.oop.croissantmanager.domain.OffertaLavoro;

public interface OffertaService {

	/* Metodi relativi alle offerte */

	Set<OffertaLavoro> findAllOfferte() throws BusinessExc;

	OffertaLavoro findOffertaDiLavoroById(int id) throws BusinessExc;

	Set<OffertaLavoro> findOfferteByAzienda(Azienda azienda) throws BusinessExc;

	Set<OffertaLavoro> findBestOfferte(Lavoratore lavoratore, int indice) throws BusinessExc;

	int getMaximumAffinity(Lavoratore lavoratore) throws BusinessExc;

	Set<OffertaLavoro> findOffertaByLocalitaOrKeyword(String localitaOrKeyword) throws BusinessExc;

	void addOfferta(OffertaLavoro offerta) throws BusinessExc;

	void removeOfferta(OffertaLavoro offerta) throws BusinessExc;

	void updateOfferta(OffertaLavoro offerta) throws BusinessExc;

	/* Metodi relativi alle candidature */

	List<Candidatura> findAllCandidature() throws BusinessExc;

	List<Candidatura> findCandidatureByLavoratore(Lavoratore lavoratore) throws BusinessExc;

	void addCandidatura(Candidatura candidatura) throws BusinessExc;

	void removeCandidatura(Candidatura candidatura) throws BusinessExc;

}
