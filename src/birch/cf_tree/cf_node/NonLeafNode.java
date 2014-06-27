package birch.cf_tree.cf_node;

import birch.cf_tree.*;
import birch.cf_tree.cf_node.cf_entry.*;
import birch.util.distance.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp NonLeafNode</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Nodo del
 * CF_Tree a livello non foglia</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class NonLeafNode extends Node {

	/**
	 * Costruisce un {@link NonLeafNode}
	 * @param father il padre del nodo corrente
	 */
	public NonLeafNode(CF_EntryNonLeaf father) {
		super(father, Params.B);
	}

	/**
	 * Fonde la coppia di {@link CF_Entry} più vicine nel nodo, 
	 * ad eccezione di quella passata come parametro
	 * @param splitEntry1 prima {@link CF_Entry} da escludere
	 * @param splitEntry2 seconda {@link CF_Entry} da escludere
	 */
	public void mergeClosest(CF_EntryNonLeaf splitEntry1, CF_EntryNonLeaf splitEntry2){
		double minDist = Double.MAX_VALUE, dist;
		boolean first, second;
		iDistance distanza = Params.DISTANCE;
		CF_Entry closestEntry1 = null, closestEntry2 = null;
		int posClosestEntry1 = 0;
		
		for (int i = 0; i < arrayEntry.size(); i++) {
			for (int j = i+1; j < arrayEntry.size(); j++) {
				/*
				 * verifico se i cf_entry in esame
				 * sono uguali a quelli passati come parametri 
				 */
				first = arrayEntry.get(i).equals(splitEntry1) 
								|| arrayEntry.get(i).equals(splitEntry2);
				second = arrayEntry.get(j).equals(splitEntry1) 
								|| arrayEntry.get(j).equals(splitEntry2);
				
				if ( !(first && second) ) {			
					dist = distanza.distance(arrayEntry.get(i), arrayEntry.get(j));
					
					if (dist < minDist){
						minDist = dist;
						closestEntry1 = arrayEntry.get(i);
						posClosestEntry1 = i;
						closestEntry2 = arrayEntry.get(j);
					}
				}
			}
		}
		
		// risultato della fusione nella prima delle due entry
		mergeEntry(closestEntry1, closestEntry2);
		
		if( ((CF_EntryNonLeaf) closestEntry1).getChild().arrayEntry.size() > 
					((CF_EntryNonLeaf) closestEntry1).getChild().branching_factor) {
			
			Node newNodeSplit = ((CF_EntryNonLeaf) closestEntry1).getChild().splitNode();
			((CF_EntryNonLeaf) closestEntry1).getChild().updateFather();
			
			// Inserisco la entry di newNodeSplit 
			// succesivamente alla entry più vicina (closestEntry1)
			arrayEntry.add(posClosestEntry1 + 1, newNodeSplit.makeEntry() );
		}
		
	}

	@Override
	public Node splitNode(){
		return super.splitNode(new NonLeafNode(father));
	}

	@Override
	protected void mergeEntry(CF_Entry entry1, CF_Entry entry2) {
		// Sposto le entry contenute nei figli di entry2 nel figlio di entry1
		Node childEntry2 = ((CF_EntryNonLeaf) entry2).getChild();
		((CF_EntryNonLeaf) entry1).getChild().arrayEntry.addAll(childEntry2.arrayEntry);
		
		// fondo entry2 in entry1
		entry1.merge(entry2);
		
		// SOLO nel caso di figli foglia, aggiorno i loro puntatori prev e next
		if ( childEntry2.isLeaf() ) {
			LeafNode prevNode = ((LeafNode) childEntry2).prev;
			LeafNode nextNode = ((LeafNode) childEntry2).next;
			
			/*
			 * Se il precedente non è nullo,
			 * il nodo precedente deve puntare 
			 * a quello successivo a quello da eliminare
			 */
			if(prevNode != null)
				prevNode.next = nextNode;
			if(nextNode != null)
				/*
				 * Se il successivo non è nullo,
				 * il nodo successivo deve puntare 
				 * a quello precedente a quello da eliminare
				 */
				nextNode.prev = prevNode;
			
			/*
			 * in fine si annullano i puntatori di quello da eliminare
			 */
			((LeafNode) childEntry2).next = ((LeafNode) childEntry2).prev = null;
		}
		
		// cancello entry2
		this.arrayEntry.remove(entry2);
		
		//Si forza l'uso del garbage collector
		System.gc();
	}

	@Override
	public boolean isLeaf() {
		return false;
	}
	
	/**
	 * Restituisce il {@link LeafNode} più vicino alla {@link CF_Entry} 
	 * passata come parametro
	 * @param entry la {@link CF_Entry} di confronto
	 * @return il {@link LeafNode} più vicino
	 */
	public Node closestChild(CF_Entry entry){
		return ((CF_EntryNonLeaf)closestEntry(entry)).getChild();
	}
}