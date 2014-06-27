package birch.cf_tree.cf_node.cf_entry;

import java.io.Serializable;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp CF_EntryLeaf</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Tripla 
 * che sintetizza l'informazione circa un cluster di dati
 * a livello foglia dell'albero</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class CF_EntryLeaf extends CF_Entry implements Serializable {
	
	private static final long serialVersionUID = 6050744452643746199L;

	/**
	 * Converte un punto in un del {@link CF_EntryLeaf}
	 * @param point coordinate del punto
	 */
	public CF_EntryLeaf(double[] point) {
		super(point);
	}
	
	/**
	 * Costruisce una {@link CF_EntryLeaf} vuota di dimensione definita
	 * @param dimensione dimensione dello spazio dei punti della {@link CF_EntryLeaf}
	 */
	public CF_EntryLeaf(int dimensione) {
		super(dimensione);
	}

	/**
	 * Testa se è possibile assorbire la {@link CF_EntryLeaf}, 
	 * passata come parametro, con questa 
	 * @param entry la {@link CF_EntryLeaf} da assorbire
	 * @param threshold la soglia da non superare
	 * @return true se è possibile l'assorbimento, false altrimenti
	 */
	public boolean canAbsorb(CF_EntryLeaf entry, double threshold) {
		CF_EntryLeaf temp = new CF_EntryLeaf(linearSum.size());
		temp.merge(entry);
		temp.merge(this);
		
		if(temp.getDiameter() <= threshold)
			return true;
		else
			return false;
	}
}