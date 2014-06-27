package birch.cf_tree.cf_node.cf_entry;

import java.util.*;

import birch.cf_tree.cf_node.Node;
import birch.util.VectorND;


/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp CF_EntryNonLeaf</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Tripla 
 * che sintetizza l'informazione circa un cluster di dati
 * a livello non foglia dell'albero</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class CF_EntryNonLeaf extends CF_Entry {

	/**
	 * Riferimento al nodo figlio
	 */
	protected Node child;
	
	/**
	 * Converte un punto in un {@link CF_EntryNonLeaf} 
	 * @param point coordinate del punto
	 * @param child nodo figlio
	 */
	public CF_EntryNonLeaf(double[] point, Node child) {
		super(point);
		this.child = child;
	}
	
	/**
	 * Converte una {@link List} di {@link CF_Entry} 
	 * in un {@link CF_EntryNonLeaf} 
	 * @param entries {@link List} di {@link CF_Entry}
	 * @param child nodo figlio
	 */
	public CF_EntryNonLeaf(List<CF_Entry> entries, Node child) {
		super(entries.get(0).linearSum.size());
		
		for (CF_Entry item : entries)
			this.merge(item);
		
		this.child = child;
	}
	
	/**
	 * Restituisce il nodo figlio
	 * @return il nodo figlio
	 */
	public Node getChild() {
		return child;
	}
	
	/**
	 * Annulla il riferimento al nodo figlio
	 */
	public void resetChild() {
		child = null;
	}
	
	/**
	 * Aggiorna il suo stato in base al contenuto del figlio
	 */
	public void update() {
		LinkedList<CF_Entry> array = child.getEntries();
		this.linearSum = new VectorND(this.linearSum.size());
		this.squareSum = new VectorND(this.linearSum.size());
		this.numberPoints = 0;
		
		for (CF_Entry entry : array) {
			this.merge(entry);
		}
	}
	
	@Override
	public boolean equals(Object entry) {
		return super.equals(entry) && this.child == ((CF_EntryNonLeaf) entry).child;
	}
}