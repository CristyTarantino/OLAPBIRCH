package birch.cf_tree;

import java.util.*;

import birch.cf_tree.cf_node.cf_entry.*;
import birch.util.database.*;
import birch.util.database.InterazioneDB.Record;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Outlier</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Consente
 * la gestione degli outlier</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class Outlier {
	private static iDAO dao = DAOFactory.getInstance(DAOFactory.MYSQL);

	/**
	 * Verifica che la CF_EntryLeaf passata sia un outlier
	 * @param numTotPoint il numero dei punti totali presenti nell'albero
	 * @param numTotEntryLeaf il numero di {@link CF_EntryLeaf} contenute nell'albero
	 * @param entryLeaf la {@link CF_EntryLeaf} di riferimento
	 */
	public static boolean isOutlier(int numTotPoint, int numTotEntryLeaf, CF_Entry entryLeaf){
		int avaragePoint = Math.round(((float)numTotPoint)/numTotEntryLeaf);
		return entryLeaf.getNumberPoints() < (avaragePoint/4) ? true : false;
	}

	/**
	 * Memorizza il potenziale outlier
	 * @param cfeLeaf la {@link CF_EntryLeaf} riconosciuta come outlier
	 */
	public static void saveOutlier(CF_Entry cfeLeaf) {
//		System.out.println("\nOutlier:\n"+cfeLeaf.toString());
		dao.initConnection();
			dao.addRecord(cfeLeaf);
		dao.closeConnection();
	}

	/**
	 * Tenta il re-inserimento dei potenziali outlier
	 * @param cfTree L'albero in cui reinserire
	 * @return <code>true</code> se i potenziali outlier sono stati tutti re-inseriti,
	 * <code>false</code> altrimenti
	 */
	public static boolean tryToAbsorb(CF_Tree cfTree) {
		dao.initConnection();
			@SuppressWarnings("unchecked")
			LinkedList<Record<Integer, CF_EntryLeaf>> outliers 
						= (LinkedList<Record<Integer, CF_EntryLeaf>>) dao.getRecords();
			
//			System.out.println("\ntryToAbsorb() outliers da assorbire: " + outliers.size());
			
			Iterator<Record<Integer, CF_EntryLeaf>> iterator = outliers.iterator();
			Record<Integer, CF_EntryLeaf> rec;
			
			while (iterator.hasNext()) {
				 rec = iterator.next();
				
				if (!cfTree.tryToAbsorb(rec.getValue()))
					iterator.remove();
			}
			
			dao.removeRecords(outliers);
		dao.closeConnection();
		
//		System.out.println("\ntryToAbsorb() outliers riassorbiti: " + outliers.size());
		
		return outliers.isEmpty();
	}
}