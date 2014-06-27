package birch.util.database;

import java.sql.*;

import birch.cf_tree.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp DB_Access</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>  
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Permette di gestire 
 * la connessione ad una base di dati</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class DB_Access {
	
	// La connessione con database
	private static Connection conn = null;

	/**
	 * Provvede al caricamento del driver 
	 * ed all'inizializzazione della connessione
	 */
	public static void initConnection(){
		try {
			// Registrazione del driver manager
			Class.forName(Params.DRIVER_CLASS_NAME);
			String url = Params.DBMS + "://" + Params.SERVER + ":" + Params.PORT 
						+ "/" + Params.DATABASE;
			
			// Connessione al db
			conn = DriverManager.getConnection(url, Params.USER_ID, Params.PASSWORD);
		}
		catch(Exception e) {
			System.err.println("Errore driver!!!");
		}
	}
	
	/**
	 * Restituisce la connessione alla base di dati
	 * @return La connessione alla base di dati
	 */
	public static Connection getConnection(){
		return conn;
	}
	
	/**
	 * Consente la chiusura della connessione alla base di dati
	 */
	public static void closeConnection(){
		try {
			conn.close();
		}
		catch (SQLException e) {
			System.err.println("Errore nella chiusura della connessione!");
		}
	}
}