package it.univaq.disim.oop.croissantmanager.controller;

// Implementata dal controller della schermata principale per risolvere il problema del passaggio degli oggetti tra
// controllori
public interface DataInitializable<T> {

	// La keyword default elimina l'obbligo di fare overriding anche quando non
	// necessario
	default void initializeData(T t) {

	}
}
