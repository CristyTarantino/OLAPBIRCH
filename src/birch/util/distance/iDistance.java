package birch.util.distance;

import birch.cf_tree.cf_node.cf_entry.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp iDistance</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Fornisce
 * un'interfaccia per il calcolo della distanza tra due {@link CF_Entry}</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public interface iDistance {

	/**
	 * Calcola la distanza che intercorre tra due {@link CF_Entry}
	 * @param entry1 {@link CF_Entry} che rappresenta il primo cluster
	 * @param entry2 {@link CF_Entry} che rappresenta il secondo cluster
	 * @return la distanza tra le {@link CF_Entry} in esame
	 */
	public double distance(CF_Entry entry1, CF_Entry entry2);
}
