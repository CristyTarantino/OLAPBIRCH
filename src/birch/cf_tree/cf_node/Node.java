package birch.cf_tree.cf_node;

import java.util.*;

import birch.cf_tree.*;
import birch.cf_tree.cf_node.cf_entry.*;
import birch.util.distance.*;


/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Node</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Nodo del
 * {@link CF_Tree}</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public abstract class Node {
	
	/** 
	 * Riferimento alla {@link CF_EntryNonLeaf} padre 
	 */
	protected CF_EntryNonLeaf father;
	
	/** 
	 * {@link LinkedList} di {@link CF_Entry} presenti nel nodo 
	 */
	protected LinkedList<CF_Entry> arrayEntry;
	
	/** 
	 * Branching factor per del nodo 
	 */
	protected int branching_factor;
	
	/**
	 * Identificativo del {@link Node}
	 */
	public String id;
	
	/**
	 * Costruisce un {@link Node}
	 * @param father il padre del nodo corrente
	 * @param branchingFactor branching factor del nodo
	 */
	public Node(CF_Entry father, int branchingFactor) {
		this.father = (CF_EntryNonLeaf) father;
		this.branching_factor = branchingFactor;
		this.arrayEntry = new LinkedList<CF_Entry>();
	}

	/**
	 * Fonde le {@link CF_Entry} passate come parametri
	 * @param entry1 prima {@link CF_Entry} da fondere
	 * @param entry2 seconda {@link CF_Entry} da fondere
	 */
	protected abstract void mergeEntry(CF_Entry entry1, CF_Entry entry2);

	/**
	 * Splitta il nodo corrente
	 * @return il nuovo nodo creato
	 */
	public abstract Node splitNode();

	/**
	 * Splitta il nodo corrente
	 * @param newNode il nuovo nodo in cui posizionare le {@link CF_Entry}
	 * @return il nuovo nodo riempito
	 */
	protected Node splitNode(Node newNode){
		iDistance distanza = Params.DISTANCE;
		
		// Sposto il secondo seed nel nuovo nodo
		newNode.arrayEntry.add( arrayEntry.getLast() );
		arrayEntry.removeLast();
		
		CF_Entry first = arrayEntry.getFirst(),
		 		 last = newNode.arrayEntry.getFirst(),
		 		 tmp = null;
		
		/*
		 * Per ogni elemento non seme, confronta la sua distanza dai semi di riferimento
		 * e inserisce tale elemento nel nodo contenente il seme ad esso più vicino
		 */
		for (int i = arrayEntry.size()-1; i > 0 ; i--) {
			tmp = arrayEntry.get(i);
			
			if (distanza.distance(tmp, last) < distanza.distance(tmp, first)) {
				newNode.arrayEntry.addFirst( tmp );
				arrayEntry.removeLast();
			}
			else
				break; // esce dal for
		}
		
		/* Codice utilizzato quando eseguimo lo split
		 * in mergeClosest().
		 * 
		 * Individuiamo il nodo che supera il branching factor e 
		 * spostiamo le entry eccedenti
		 */
		if (arrayEntry.size() > branching_factor){
			
			int exceeded = branching_factor - arrayEntry.size();
			for (int j = 0; j < exceeded; j++) {
				newNode.arrayEntry.addFirst(arrayEntry.getLast());
				arrayEntry.removeLast();
			}
		}
		else if (newNode.arrayEntry.size() > newNode.branching_factor){
			
			int exceeded = branching_factor - newNode.arrayEntry.size();
			for (int j = 0; j < exceeded; j++) {
				arrayEntry.addLast(newNode.arrayEntry.getFirst());
				newNode.arrayEntry.removeFirst();
			}
		}
		
		//Si forza l'uso del garbage collector
		System.gc();
		
		return newNode;
	}
	
	/**
	 * Restituisce il {@link CF_Entry} più vicino al {@link CF_Entry} in input
	 * @param entry il {@link CF_Entry} di riferimento
	 */
	public CF_Entry closestEntry(CF_Entry entry){
		CF_Entry closest;
		
		// poichè è vuoto, non esistono entry per il confronto 
		if ( this.isEmpty() )
			return null;
		else {
			iDistance temp = Params.DISTANCE;
			double minDist = temp.distance(entry, arrayEntry.getFirst());
			closest = arrayEntry.getFirst();
			
			for (int i = 1; i < arrayEntry.size(); i++) {
				double dist = temp.distance(entry, arrayEntry.get(i));
				
				if(dist < minDist){
					minDist = dist;
					closest = arrayEntry.get(i);
				}
			}
			
			return closest;			
		}
	}
	
	/**
	 * Restituisce il diametro del {@link CF_Entry} ottenuto
	 * dall'unione delle {@link CF_Entry} più vicine
	 * @return il diametro del {@link CF_Entry} risultante
	 */
	public double diameterClosestEntry(){
		double dist, minDist = Double.MAX_VALUE;
		iDistance distanza = Params.DISTANCE;
		CF_Entry[] closest = new CF_Entry[2];
		
		// individuazione della coppia di entry più vicine
		for (int i=0; i < arrayEntry.size(); i++) {
			for(int j=i+1; j < arrayEntry.size(); j++){
				dist = distanza.distance(arrayEntry.get(i), arrayEntry.get(j));
				
				if (dist < minDist) {
					minDist = dist;
					closest[0] = arrayEntry.get(i);
					closest[1] = arrayEntry.get(j);
				}
			}
		}

		CF_EntryLeaf temp = new CF_EntryLeaf(closest[0].getLinearSum().size());
		temp.merge(closest[0]);
		temp.merge(closest[1]);
		
		return temp.getDiameter();
	}

	/**
	 * Inserisce una nuova {@link CF_Entry} 
	 * successivamente a quella più vicina
	 * @param entry la {@link CF_Entry} da inserire
	 * @param closest la {@link CF_Entry} più vicina
	 * @return <code>null</code> se non è avvenuto lo split del nodo, 
	 * il nuovo nodo creato altrimenti
	 */
	public Node insertEntry(CF_Entry entry, CF_Entry closest) {
		Node newNode = null;

		// Inserisco entry succesivamente alla entry più vicina
		arrayEntry.add(arrayEntry.indexOf(closest) + 1, entry);
		
		if(arrayEntry.size() > branching_factor)
			newNode = this.splitNode();
		
		return newNode;
	}

	/**
	 * Crea una {@link CF_Entry} che rappresenta il nodo corrente
	 * @return il {@link CF_Entry} del nodo
	 */
	public CF_Entry makeEntry(){
		CF_EntryNonLeaf entry = new CF_EntryNonLeaf(arrayEntry, this);
		this.father = entry;
		return entry;
	}
	
	/**
	 * Verifica che il nodo sia vuoto
	 * @return <code>true</code> se il nodo è vuoto, 
	 * <code>false</code> altrimenti
	 */
	public boolean isEmpty(){
		return arrayEntry.isEmpty();
	}
	
	/**
	 * Verifica che il nodo sia foglia
	 * @return <code>true</code> se il nodo è foglia,
	 * <code>false</code> altrimenti
	 */
	public abstract boolean isLeaf();

	/**
	 * Aggiorna il {@link CF_Entry} che lo rappresenta nel nodo padre 
	 */
	public void updateFather() {
		this.father.update();
	}
	
	/**
	 * Aggiorna il {@link CF_Entry} che lo rappresenta nel nodo padre,
	 * sommando la {@link CF_Entry} passata come parametro 
	 * @param entry {@link CF_Entry} da aggiunger
	 */
	public void updateFather(CF_EntryLeaf entry) {
		father.merge(entry);
	}

	/**
	 * Restituisce {@link LinkedList} di {@link CF_Entry} contenute nel nodo
	 * @return le entry contenute
	 */
	public LinkedList<CF_Entry> getEntries() {
		return arrayEntry;
	}

	/**
	 * Restituisce il padre del nodo corrente
	 * @return la {@link CF_EntryNonLeaf} padre del nodo
	 */
	public CF_EntryNonLeaf getFather() {
		return father;
	}
	
	/**
	 * Cancella {@link CF_Entry} dal nodo corrente
	 * @param cfeLeaf la {@link CF_Entry} da cancellare
	 */
	public void deleteEntry(CF_Entry cfeLeaf) {
		Iterator<CF_Entry> i = arrayEntry.iterator();
		boolean next = true;
		
		while (next && i.hasNext()) {
			CF_Entry entry = i.next();
			
			if (entry == cfeLeaf) {
				i.remove();
				next = false;
			}
		}
	}
	
	/**
	 * Verifica che il nodo abbia figli foglia 
	 * @return <code>true</code> se il nodo ha figli di tipo {@link LeafNode},
	 * <code>false</code> altrimenti
	 */
	public boolean hasChildrenLeaf() {
		return ((CF_EntryNonLeaf) arrayEntry.getFirst()).getChild().isLeaf();
	}

	/**
	 * Verifica se il nodo può contenere
	 * almeno una {@link CF_Entry}
	 * @return <code>true</code> se è possibile inserire almeno una {@link CF_Entry},
	 * <code>false</code> altrimenti
	 */
	public boolean hasFreeSpace() {
		return arrayEntry.size() < branching_factor;
	}
	
	/**
	 * Verifica se il numero di {@link CF_Entry}
	 * contenute nel nodo supera (maggiore stretto) il valore passato
	 * @param value Il valore di confronto
	 * @return <code>true</code> se supera <code>value</code>,
	 * <code>false</code> altrimenti
	 */
	public boolean exceeded(int value) {
		return arrayEntry.size() > value;
	}
}