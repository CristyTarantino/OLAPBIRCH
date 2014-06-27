package birch.util.database;

import java.sql.*;
import java.util.*;

import birch.cf_tree.cf_node.cf_entry.*;
import birch.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp InterazioneDB</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Fornisce i metodi
 * di interazione al database</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class InterazioneDB implements iDAO {
	// La connessione alla base di dati
	private Connection conn;

	@Override
	public void initConnection() {
		// Effettuo la connessione al database
		DB_Access.initConnection();
		conn = DB_Access.getConnection();
	}

	@Override
	public void closeConnection() {
		DB_Access.closeConnection();
	}

	@Override
	public void addRecord(Object tupla) {
		// Creazione della query sql
		String query = "INSERT INTO " + Alias.TABLE_OUTLIERS 
		+ "(" + Alias.NUM_PTS + "," + Alias.LS + ","
		+ Alias.SS + ")" 
		+ "VALUES (?, ?, ?)";

		try {
			// Creazione di uno statement (dichiarazione) con parametri
			PreparedStatement statement =  conn.prepareStatement(query);

			// Impostazione dei parametri della query
			statement.setInt(1, ((CF_EntryLeaf) tupla).getNumberPoints());
			statement.setString(2, ((CF_EntryLeaf) tupla).getLinearSum().toStringFormatted());
			statement.setString(3, ((CF_EntryLeaf) tupla).getSquareSum().toStringFormatted());

			// Esecuzione della query di modifica
			statement.executeUpdate();

			// Chiusura dello statement
			statement.close();
		} 
		catch (SQLException e) {
			System.err.println("Errore in addRecord() - SQLException");
//			e.printStackTrace();
		}
	}

	@Override
	public LinkedList<Record<Integer, CF_EntryLeaf>> getRecords() {
		// Creazione della query sql
		String query = "SELECT * FROM " + Alias.TABLE_OUTLIERS;

		LinkedList<Record<Integer,CF_EntryLeaf>> listOutliers =
					new LinkedList<Record<Integer,CF_EntryLeaf>>();

		try {
			// Creazione di uno statement (dichiarazione) con parametri
			PreparedStatement statement = conn.prepareStatement(query);

			// Esecuzione della query di selezione
			ResultSet risultato = statement.executeQuery();

			// Fintantoche' ci sono tuple nel ResultSet ...
			while(risultato.next()) {
				// ... recupera i dati dal ResultSet
				int id = risultato.getInt(Alias.ID_OUTLIER);
				
				VectorND ls = VectorND.toVectorND(risultato.getString(Alias.LS));
				VectorND ss = VectorND.toVectorND(risultato.getString(Alias.SS));
				
				CF_EntryLeaf partTmp = new CF_EntryLeaf(ls.size());
					partTmp.setNumberPoints(risultato.getInt(Alias.NUM_PTS));
					partTmp.setLinearSum(ls);
					partTmp.setSquareSum(ss);

				listOutliers.add(new Record<Integer, CF_EntryLeaf>(partTmp, id));
			}

			// Chiusura dello statement
			statement.close();
		} 
		catch (SQLException e) {
			System.err.println("Errore in getRecords() - SQLException");
		}

		return listOutliers;
	}

	@Override
	public void removeRecord(Object id) {
		try {
			remove((Integer)id);
		} 
		catch (SQLException e) {
			System.err.println("Errore in removeRecord() - SQLException");
		}
	}

	/*
	 * Rimuove un record dal database
	 * @param id identificativo della tupla da eliminare
	 * @throws SQLException
	 */
	private void remove(int id) throws SQLException {
		// Creazione della query sql
		String query = "DELETE FROM " + Alias.TABLE_OUTLIERS 
		+ " WHERE " + Alias.ID_OUTLIER + "=?";
		
		// Creazione di uno statement (dichiarazione) con parametri
		PreparedStatement statement =  conn.prepareStatement(query);
	
		// Impostazione dei parametri della query
		statement.setInt(1, id);
	
		// Esecuzione della query di modifica
		statement.executeUpdate();
	
		// Chiusura dello statement
		statement.close();
	}

	@Override
	public void removeRecords(Object tuple) {
		@SuppressWarnings("unchecked")
		LinkedList<Record<Integer, CF_EntryLeaf>> reabsorbed 
						= (LinkedList<Record<Integer, CF_EntryLeaf>>) tuple;
		try {
			Iterator<Record<Integer, CF_EntryLeaf>> iterator = reabsorbed.iterator();
			Record<Integer, CF_EntryLeaf> rec;
			
			while (iterator.hasNext()) {
				rec = iterator.next();
				remove(rec.getKey());
			}
			
		} catch (SQLException e) {
			System.err.println("Errore in removeRecords() - SQLException");
		}
	}
	
	@Override
	public int getNumberPoints(String query) {
		int numberPoints = 0;

		try {
			// Creazione di uno statement (dichiarazione)
			Statement statement = conn.createStatement();

			// Esecuzione della query di selezione
			ResultSet risultato = statement.executeQuery(query);

			// Fintantoche' ci sono tuple nel ResultSet ...
			risultato.next();
			
			//... recupera i dati dal ResultSet 
			numberPoints = risultato.getInt(1);
			
			// Chiusura dello statement
			statement.close();
		} 
		catch (SQLException e) {
			System.err.println("Errore in getNumberPoints() - SQLException");
		}
		
		return numberPoints;
	}

	@Override
	public Collection<? extends Object> getPoints(String query, int offset, int nrRow) {
		// Creazione della query sql
		query += " LIMIT ?,?";

		ArrayList<double[]> points = new ArrayList<double[]>();
				       
		try {
			// Creazione di uno statement (dichiarazione) con parametri
			PreparedStatement statement = conn.prepareStatement(query);
			
			// Impostazione dei parametri della query
			statement.setInt(1, offset);
			statement.setInt(2, nrRow);

			// Esecuzione della query di selezione
			ResultSet risultato = statement.executeQuery();
			int nrColumn = risultato.getMetaData().getColumnCount();
			
			// Fintantoche' ci sono tuple nel ResultSet ...
			while(risultato.next()) {
				double[] coor = new double[nrColumn];
				
				//... recupera i dati dal ResultSet
				for (int i = 1; i <= coor.length; i++)
					coor[i-1] = risultato.getDouble(i);
				
				points.add(coor);
			}

			// Chiusura dello statement
			statement.close();
		} 
		catch (SQLException e) {
			System.err.println("Errore in getPoints() - SQLException");
		}

		return points;
	}

	@Override
	public void setClusterID(String query, int id, String clusterID) {
		try {
			// Creazione di uno statement (dichiarazione) con parametri
			PreparedStatement statement = conn.prepareStatement(query);
			
			// Impostazione dei parametri della query
			statement.setString(1, clusterID);
			statement.setInt(2, id);
			
			// Esecuzione della query di aggiornamento
			statement.executeUpdate();

			// Chiusura dello statement
			statement.close();
		} 
		catch (SQLException e) {
			System.err.println("Errore in setClusterID() - SQLException");
		}
		
	}

	/**
	 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Record</p>
	 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
	 * Universit&agrave degli studi di Bari</p>
	 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
	 * per la Produzione del Software</p>  
	 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Record che 
	 * rappresenta una coppia chiave-valore</p>
	 * @author Tarantino - Turturo
	 * @version 1.0
	 */
	public final class Record<K, V> implements Map.Entry<K, V> {
		private V elem;
		private K key;

		/**
		 * Costruisce un record
		 * @param elem Il valore della coppia
		 * @param key La chiave della coppia
		 */
		public Record(V elem, K key){
			this.elem = elem;
			this.key = key;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return elem;
		}

		@Override
		public V setValue(V value) {
			elem = value;
			return elem;
		}
	}
}