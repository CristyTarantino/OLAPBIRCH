package birch.util.database;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp DAOFactory</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>  
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Factory che permette 
 * la creazione di istanze di DataAccessObjects a seconda del DBMS utilizzato</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class DAOFactory {
	
	/**
	 * Nome che rappresenta il dbms MySQL
	 */
	public final static String MYSQL = new String("mysql");
	
	/**
	 * Restituisce una classe di comunicazione con il database, 
	 * a seconda del DBMS utilizzato nell'applicazione
	 * @return Classe di comunicazione con la base di dati
	 */
	public static iDAO getInstance(String dbms) {
		
		/*
		 *  Nel nostro caso ritorna la classe associata al dbms MySql.
		 *  In caso di + db, dovremo aggiungere un altro if 
		 *  e un'altra costante pubblica  
		 */
		if(dbms.equals(MYSQL))
			return new InterazioneDB();
		else
			return null;
	}
}