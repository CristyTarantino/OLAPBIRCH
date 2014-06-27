package birch.util.database;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbspAlias</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>  
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Contiene 
 * i nomi delle colonne delle tabelle presenti nel database</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class Alias {
	
	/**
	 * Nome della tabella <code>outliers</code>
	 */
	public final static String TABLE_OUTLIERS = new String("outliers");
	
		/**
		 * Nome usato per riferirsi al campo identificativo 
		 * della tabella <code>outliers</code> 
		 */
		public final static String ID_OUTLIER = new String("id_outlier");
		
		/**
		 * Nome usato per riferirsi al campo "Number Points" 
		 * della tabella <code>outliers</code> 
		 */
		public final static String NUM_PTS = new String("num_pts");
		
		/**
		 * Nome usato per riferirsi al campo "Linear Sum" 
		 * della tabella <code>outliers</code> 
		 */
		public final static String LS = new String("ls");
		
		/**
		 * Nome usato per riferirsi al campo "Square Sum" 
		 * della tabella <code>outliers</code> 
		 */
		public final static String SS = new String("ss");

}