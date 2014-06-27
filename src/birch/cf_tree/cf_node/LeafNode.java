package birch.cf_tree.cf_node;

import birch.cf_tree.*;
import birch.cf_tree.cf_node.cf_entry.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp LeafNode</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Nodo del
 * {@link CF_Tree} a livello foglia</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class LeafNode extends Node {

	/**
	 *  Riferimento al {@link LeafNode} precedente
	 */
	protected LeafNode prev;
	
	/**
	 *  Riferimento al {@link LeafNode} successivo
	 */
	protected LeafNode next;
	
	/**
	 * Costruisce un {@link LeafNode}
	 * @param father il padre del nodo corrente
	 */
	public LeafNode(CF_EntryNonLeaf father) {
		super(father, Params.L);
	}

	/**
	 * Fonde le {@link CF_Entry} passate come parametri
	 * @param closest prima {@link CF_Entry} da fondere
	 * @param entry seconda {@link CF_Entry} da fondere
	 */
	public void absorb(CF_Entry closest, CF_Entry entry) {
		mergeEntry(closest, entry);
	}

	@Override
	public Node splitNode() {
		LeafNode newNode = (LeafNode) super.splitNode(new LeafNode(father));
		
		/*
		 * il successivo del nuovo nodo
		 * dovrà essere il successivo
		 * del medesimo
		 */
		newNode.next = this.next;
		
		/*
		 * il precedente del nuovo nodo
		 * dovrà essede il medesimo 
		 */
		newNode.prev = this;
		
		/*
		 * Se il successivo non è nullo,
		 * il nodo successivo al medesimo
		 * dovrà puntare al nuovo nodo
		 */
		if( this.next != null )
			this.next.prev = newNode;
		
		/*
		 * il successivo del medesimo
		 * sarà il nuovo nodo
		 */
		this.next = newNode;
		
		return newNode;
	}

	@Override
	protected void mergeEntry(CF_Entry entry1, CF_Entry entry2){
		entry1.merge(entry2);
	}

	@Override
	public boolean isLeaf() {
		return true;
	}
	
	/**
	 * Restituisce il nodo precedente a questo
	 * @return il nodo precedente
	 */
	public LeafNode getPrevNode() {
		return prev;
	}
	
	/**
	 * Restituisce il nodo successivo a questo
	 * @return il nodo successivo
	 */
	public LeafNode getNextNode() {
		return next;
	}
	
	/**
	 * Cancella il nodo medesimo dal {@link CF_Tree}
	 * di appartenenza
	 */
	public void deleteFromTree(){
	//*******Annullamento dei puntatori ai fratelli
		
		/*
		 * Se il precedente non è nullo,
		 * il nodo precedente deve puntare 
		 * a quello successivo del medesimo
		 */
		if(prev != null)
			prev.next = next;
		if(next != null)
			/*
			 * Se il successivo non è nullo,
			 * il nodo successivo deve puntare 
			 * a quello precedente del medesimo
			 */
			next.prev = prev;
		
		/*
		 * in fine si annullano i puntatori del medesimo nodo
		 */
		next = prev = null;
		
	//*******Annullamento dei puntatori padre-figlio
		father.resetChild();
		father = null;
	}
}