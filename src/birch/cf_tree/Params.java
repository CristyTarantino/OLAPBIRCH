package birch.cf_tree;

import java.io.*;
import java.util.*;

import birch.util.*;
import birch.util.distance.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Params</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>  
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Effettua il caricamento, 
 * da un file di configurazione denominato <code>"config.ini"</code>, 
 * delle informazioni necessarie all'applicazione</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public final class Params {

	// ***************************************** 
	// 		Parametri connessione database
	// *****************************************
	
	/**
	 * Nome del DBMS
	 */
	public static String DBMS;
	
	/**
	 * Nome della classe del driver di connessione al DBMS
	 */
	public static String DRIVER_CLASS_NAME;
	
	/**
	 * Username di accesso al DBMS
	 */
	public static String USER_ID;
	
	/**
	 * Password di accesso al DBMS
	 */
	public static String PASSWORD;
	
	/**
	 * Indirizzo IP del server
	 */
	public static String SERVER;
	
	/**
	 * Nome dello schema
	 */
	public static String DATABASE;
	
	/**
	 * Porta di connessione al server
	 */
	public static String PORT;
	
	// *****************************************
	// 		Parametri di input CF_Tree 
	// *****************************************
	
	/**
	 * Istanza della classe che calcola la distanza tra due cluster
	 */
	public static iDistance DISTANCE;
	
	/**
	 * Branching Factor per i nodi non foglia 
	 */
	public static int B;
	
	/**
	 * Branching Factor per i nodi foglia 
	 */
	public static int L;
	
	/**
	 * Profondità massima dell'albero Birch 
	 */
	public static int MAX_DEPTH;
	
	/**
	 * Opzione di gestione degli outlier 
	 */
	public static boolean OUTLIER_HANDLING_OPTION;
	
	/**
	 * Nome colonna dell'identificativo del cluster
	 */
	public static String CLUSTER_ID;
	
	/**
	 * Nome colonna dell'identificativo del cluster
	 */
	public static int NR_DIMENSION;
	
	/**
	 * Nome file XML contenente il cubo OLAP
	 */
	public static String XML_OLAP_CUBE;
	
	// *****************************************
	// 		Parametri di input DBSCAN 
	// *****************************************
	
	/**
	 * Parametro epsilon del DBSCAN
	 */
	public static String EPSILON;
	
	/**
	 * Parametro minPts del DBSCAN
	 */
	public static String MIN_PTS;
	
	/*
	 * Consente il caricamento di tutte le informazioni 
	 * necessarie all'applicazione 
	 */
	static {
		try {
			Properties datiInConn = LoadProps.getProperties("config.ini");
			
			/*
			 * Interrogazione dell'oggetto Properties
			 * per ricavare i dati della connessione 
			 */
			DBMS = datiInConn.getProperty("Dbms");
			DRIVER_CLASS_NAME = datiInConn.getProperty("Class_Name");
			USER_ID = datiInConn.getProperty("User_ID");
			PASSWORD = datiInConn.getProperty("Password");
			SERVER = datiInConn.getProperty("Server");
			DATABASE = datiInConn.getProperty("Database");
			CLUSTER_ID = datiInConn.getProperty("ColumnClusterID");
			PORT = datiInConn.getProperty("Port");
			XML_OLAP_CUBE = datiInConn.getProperty("XMLOLAPCube");
			DISTANCE = (iDistance) Class.forName(datiInConn.getProperty("distance")).newInstance();
			B = Integer.parseInt(datiInConn.getProperty("branchingFactorB"));
			L = Integer.parseInt(datiInConn.getProperty("branchingFactorL"));
			OUTLIER_HANDLING_OPTION = Boolean.parseBoolean(datiInConn.getProperty("outlier_handling_option"));
			MIN_PTS = datiInConn.getProperty("minPts");
			EPSILON = datiInConn.getProperty("epsilon");
		}
		catch(FileNotFoundException e) {
			System.err.println("Errore in Params - FileNotFoundException");
//			e.printStackTrace();
		}
		catch(IOException e) {
			System.err.println("Errore in Params - IOException");
//			e.printStackTrace();
		} 
		catch (InstantiationException e) {
			System.err.println("Errore in Params - InstantiationException");
//			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			System.err.println("Errore in Params - IllegalAccessException");
//			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			System.err.println("Errore in Params - ClassNotFoundException");
//			e.printStackTrace();
		}
	}
}