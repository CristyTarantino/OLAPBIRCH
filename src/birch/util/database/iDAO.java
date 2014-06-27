package birch.util.database;

import java.util.Collection;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp iDAO</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Interfaccia che definisce
 * i metodi d'interazione al database</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public interface iDAO {
	
	/**
	 * Crea una connessione al database
	 */
	public void initConnection();

	/**
	 * Consente la chiusura della connessione alla base di dati
	 */
	public void closeConnection();

	/**
	 * Aggiunge un record al database
	 * @param tupla il record da aggiungere
	 */
	public void addRecord(Object tupla);
	
	/**
	 * Restituisce tutti i record nel database
	 * @return i record nel database
	 */
	public Collection<? extends Object> getRecords();
	
	/**
	 * Rimuove un record dal database
	 * @param tupla il record da eliminare
	 */
	public void removeRecord(Object tupla);
	
	/**
	 * Rimuove i record dal database
	 * @param tuple i record da eliminare
	 */
	public void removeRecords(Object tuple);

	/**
	 * Restituisce i punti della query indicata
	 * limitata ad un numero di righe richieste.
	 * La numerazione delle righe inizia da zero
	 * @param query la query da eseguire
	 * @param offset punto di partenza
	 * @param nrRow numero di tuple richieste
	 * @return i punti richiesti
	 */
	public Collection<? extends Object> getPoints(String query, int offset, int nrRow);
	
	/**
	 * Restituisce il numero di tuple
	 * presenti nel database
	 * @param query query da eseguire
	 * @return numero di tuple
	 */
	public int getNumberPoints(String query);

	/**
	 * Imposta l'identificativo del cluster di appartenenza
	 * @param query query da eseguire
	 * @param id identificativo del punto
	 * @param clusterID identificativo del cluster
	 */
	public void setClusterID(String query, int id, String clusterID);
}