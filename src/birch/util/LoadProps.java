package birch.util;

import java.io.*;
import java.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp LoadProps</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>  
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Carica un file
 * contenente la serializzazione di un'istanza di {@link Properties}</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class LoadProps {

	/**
	 * Legge una lista di proprietà (coppia chiave-valore) 
	 * da un file di testo, che usa la codifica 
	 * ISO 8859-1 character encoding; ogni byte è un carattere Latin1. 
	 * I caratteri non Latin1, e i caratteri speciali,
	 * sono rappresentati in coppie chiave-valore
	 * usando i caratteri di escape (Unicode). 
	 * @param file Nome o percorso (assoluto o relativo)
	 * del file da leggere
	 * @return {@link Properties} contenente le informazioni contenute nel file
	 * @throws IOException se viene generato un errore 
	 * durante la lettura del file di testo
	 * @throws FileNotFoundException se il file non esiste, 
	 * se è una directory, piuttosto che un file,
	 * oppure per altre ragioni che non permettono l'apertura del file.
	 */
	public static Properties getProperties(String file) 
					throws IOException, FileNotFoundException {
		// Ricavo l'input stream del file di testo passato come parametro
		FileInputStream url = new FileInputStream(file);
		
		Properties props = new Properties();
		// Conversione del file di testo in un oggetto Properties
		props.load(url);
		url.close();
		
		return props;
	}
	
	/**
	 * Legge una lista di proprietà (coppia chiave-valore) 
	 * da un file di XML, che usa la seguente DTD
	 * 
	 * <code><!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd"></code>
	 *
	 * @param file Nome o percorso (assoluto o relativo)
	 * del file da leggere
	 * @return {@link Properties} contenente le informazioni contenute nel file
	 * @throws IOException se viene generato un errore 
	 * durante la lettura del file di testo
	 * @throws FileNotFoundException se il file non esiste, 
	 * se è una directory, piuttosto che un file,
	 * oppure per altre ragioni che non permettono l'apertura del file.
	 * @throws InvalidPropertiesFormatException Il file non costituisce
	 * un documento XML valido secondo la sua DTD 
	 */
	public static Properties getPropertiesFromXML(String file) 
			throws IOException, FileNotFoundException, InvalidPropertiesFormatException {
		// Ricavo l'input stream del file xml passato come parametro
		FileInputStream url = new FileInputStream(file);
		
		Properties props = new Properties();
		// Conversione del file xml in un oggetto Properties
		props.loadFromXML(url);
		url.close();
		
		return props;
	}
}
