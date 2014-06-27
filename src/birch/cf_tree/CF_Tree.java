package birch.cf_tree;

import java.util.*;

import birch.cf_tree.cf_node.*;
import birch.cf_tree.cf_node.cf_entry.*;


/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp CF_Tree</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp CF_Tree, ossia un 
 * albero bilanciato in altezza</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class CF_Tree implements Iterable<LeafNode> {
	
	private double threshold;
	private int depth;
	private Node root;
	private LeafNode headLeafNode;
	private int numberPoint;
	private int numberCF_EntryLeaf;
	private boolean flag;
	private boolean outlier_handling_option;

	/**
	 * Costruisce un {@link CF_Tree} di dati d-dimensionali
	 */
	public CF_Tree() {
		root = new LeafNode(null);
		headLeafNode = (LeafNode) root;
		threshold = 0;
		depth = 0;
		numberPoint = 0;
		numberCF_EntryLeaf = 0;
		outlier_handling_option = Params.OUTLIER_HANDLING_OPTION;
	}

	/**
	 * Restituisce la profondità raggiunta, in quel momento,
	 * dal {@link CF_Tree}. 
	 * Si ricorda che la profondità di un nodo è 
	 * la lunghezza del percorso dalla radice al nodo.
	 * @return la profondità del {@link CF_Tree}
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Inserzione di un punto nell'albero
	 * @param coordinates coordinate del punto da aggiungere
	 */
	public Node insert(double[] coordinates) {
		CF_EntryLeaf entry = new CF_EntryLeaf(coordinates);
		numberPoint++;
		return insert(root, entry);
	}
	
	/*
	 * Inserzione ricorsiva di un {@link CF_Entry}
	 * @param currentNode nodo corrente da cui iniziare a cercare
	 * @param entry {@link CF_Entry} da aggiungere
	 */
	private Node insert(Node currentNode, CF_EntryLeaf entry){
		Node newNode;
		CF_EntryNonLeaf newEntry;
		
		if ( !currentNode.isLeaf() ) {
			Node ci = ((NonLeafNode) currentNode).closestChild(entry);
			newNode = insert(ci, entry);
			
			/*
			 * Se l'inserimento di entry è avvenuto senza split di currentNode
			 * oppure non è stata aggiunta un'altra entry in currentNode 
			 * (entry assorbita), si aggiorna quest'ultimo.
			 */
			if (newNode == null) {
				ci.updateFather(entry);
				return null;
			} 
			else { 
				// currentNode è stato splittato e 
				// newNode contiene il nuovo nodo creato 
				ci.updateFather();
				
				// crea la entry associata a newNode
				newEntry = (CF_EntryNonLeaf) newNode.makeEntry();
				
				// cerca di inserire newEntry in currentNode
				newNode = currentNode.insertEntry(newEntry, ci.getFather());
				
				/* 
				 * se l'inserimento è avvenuto senza split di currentNode,
				 * newNode sarà null, altrimenti sarà il nodo contenente newEntry 
				 */
//	EQUIVALE A	if (newNode == null & currentNode.getNumberEntry() > 2) {
				if (newNode == null & currentNode.exceeded(2)) {
					((NonLeafNode)currentNode).mergeClosest(ci.getFather(), newEntry);
					return null;
				}
				else {
					if (currentNode == root) {
						createNewRoot(currentNode, newNode);
						depth++;
						return null;
					} 
					else
						return newNode;
				}
			}
		} 
		else { // currentNode è un nodo foglia
			CF_EntryLeaf li = (CF_EntryLeaf) currentNode.closestEntry(entry);
			
			// currentNode non vuoto AND li capace di contenere la nuova entry
			if (li != null && li.canAbsorb(entry, threshold) ) {
				((LeafNode) currentNode).absorb(li, entry);
				return null;
			} 
			else { // currentNode vuoto OPPURE li non può contenere la nuova entry
				newNode = currentNode.insertEntry(entry, li);
				numberCF_EntryLeaf++;
				
				/* se l'aggiunta di entry nel nodo foglia non ha causato uno split
				 * del medesimo, allora newNode sarà uguale a NULL, altrimenti sarà 
				 * il nodo in cui è stato inserito entry
				 */
				if (newNode == null)
					return null;
				else {
					if (currentNode == root) {
						createNewRoot(currentNode, newNode);
						depth++;
						return null;
					} 
					else
						return newNode;
				}
			}
			
		}
	}

	/*
	 * Aumenta la profondità del {@link CF_Tree} di uno
	 * @param oldRoot la vecchia root dell'albero
	 * @param nodo il nuovo nodo, fratello della vecchia root
	 */
	private void createNewRoot(Node oldRoot, Node nodo){
		NonLeafNode newRoot = new NonLeafNode(null);
		
		CF_EntryNonLeaf oldRootEntry = (CF_EntryNonLeaf) oldRoot.makeEntry();
		CF_EntryNonLeaf nodoEntry = (CF_EntryNonLeaf) nodo.makeEntry();
		
		newRoot.insertEntry(nodoEntry, null);
		newRoot.insertEntry(oldRootEntry, null);
		
		root = newRoot;
	}

	/**
	 * Ricostruisce il {@link CF_Tree} secondo un
	 * nuovo valore di soglia
	 * @return il {@link CF_Tree} ricostruito
	 */
	public CF_Tree rebuild(){
		if( this.root.isLeaf() )
			return this;
		else
			return rebuildOldTree(true);
	}

	/*
	 * Ricostruisce il {@link CF_Tree} secondo un
	 * nuovo valore di soglia
	 * @param calThreshold <code>true</code> se deve ricalcolare la soglia,
	 * <code>false</code> altrimenti
	 * @return il {@link CF_Tree} ricostruito
	 */
	private CF_Tree rebuildOldTree(boolean calThreshold) {
		CF_Tree newTree = new CF_Tree();
		newTree.numberPoint = this.numberPoint;
		
		// Verifica se bisogna ricalcolare la threshold
		if (calThreshold)
			newTree.threshold = calThreshold();
		else
			newTree.threshold = this.threshold;
		
		// Verifica se bisogna eseguire la gestione degli outlier
		if (outlier_handling_option)
			rebuildWithOutliers(newTree);
		else		
			rebuildWithoutOutliers(newTree);
		
		return newTree;
	}

	/*
	 * Ricostruisce il {@link CF_Tree} secondo un
	 * nuovo valore di soglia con il trattamento degli outlier
	 * @param newTree il {@link CF_Tree} ricostruito
	 */
	private void rebuildWithOutliers(CF_Tree newTree) {
		LeafNode currentPath = (LeafNode) leftMostPath();
		
		while(currentPath != null) {
			for (CF_Entry cfeLeaf : currentPath.getEntries()) {
				boolean outlier = Outlier.isOutlier(this.numberPoint, this.numberCF_EntryLeaf ,(CF_EntryLeaf) cfeLeaf);
				if(!outlier)
					newTree.insert(newTree.root, (CF_EntryLeaf) cfeLeaf);
				else {
					Outlier.saveOutlier(cfeLeaf);
					flag = true;
				}
			}
			
			currentPath = (LeafNode) leftMostPath();
		}
		
		//Si forza l'uso del garbage collector
		System.gc();
		
		/*
		 * Si cerca di far assorbire gli outliers. 
		 * Non è detto che ciò sia possibile per tutti
		 */
		if(flag)
			flag = Outlier.tryToAbsorb(newTree);
		
		//Si forza l'uso del garbage collector
		System.gc();
	}

	/*
	 * Ricostruisce il {@link CF_Tree} secondo un
	 * nuovo valore di soglia senza il trattamento degli outlier
	 * @param newTree il nuovo albero da ricostruire
	 */
	private void rebuildWithoutOutliers(CF_Tree newTree) {
		LeafNode currentPath = (LeafNode) leftMostPath();
		
		while(currentPath != null) {
			for (CF_Entry cfeLeaf : currentPath.getEntries()) {
				newTree.insert(newTree.root, (CF_EntryLeaf) cfeLeaf);
			}
			
			currentPath = (LeafNode) leftMostPath();
		}
		
		//Si forza l'uso del garbage collector
		System.gc();
	}
	
	/**
	 * Condensa il {@link CF_Tree}
	 * (Fase 2 del BIRCH)
	 * @return il {@link CF_Tree} compattato
	 */
	public CF_Tree condenseTree() {
		return rebuildOldTree(false);
	}

	/*
	 * Calcola il nuovo valore di soglia
	 * @return il nuovo valore
	 */
	private double calThreshold() {
		/*
		 * La soluzione adottata è stata quella di
		 * assegnare, come nuovo valore di soglia, 
		 * la media dei diametri delle più vicine entry
		 * individuate nei nodi.
		 * Un'altra soluzione possibile è 
		 * la sommatoria dei possibili diametri diviso
		 * il numero dei punti presenti nell'albero
		 */
		double diameter = 0.0;
		int i = 0;
		
		for (LeafNode leaf : this) {
			if(leaf.exceeded(1)) {
				diameter += leaf.diameterClosestEntry();
				i++;
			}
		}
		
//		System.out.println("oldT: "+this.threshold);
//		System.out.println("newT: "+diameter/i);
		
		return diameter/i;
	}

	/*
	 * Restituisce il {@link LeafNode} più a sinistra del {@link CF_Tree}
	 * @return currentNode <code>null</code> se il nodo radice
	 * è vuoto; altrimenti ritorna il {@link LeafNode} più a sinistra nel {@link CF_Tree}
	 */
	private Node leftMostPath(){
		if(root.isEmpty()){
			headLeafNode = null;
			return null;
		}
		else
			return realLeftMostPath(root);
	}

	/*
	 * Restituisce il {@link LeafNode} più a sinistra del {@link CF_Tree}
	 * @return ritorna il {@link LeafNode} più a sinistra nel {@link CF_Tree}
	 */
	private Node realLeftMostPath(Node currentNode) {
		if(!currentNode.isLeaf()){
			CF_EntryNonLeaf ci = (CF_EntryNonLeaf) currentNode.getEntries().getFirst();
			Node leftMostPath = realLeftMostPath(ci.getChild());

			/*
			 * Se ci è padre del nodo foglia appena staccato, 
			 * 	oppure
			 * se ci non ha un figlio foglia pieno 
			 * (si trova almeno al terzo livello partendo dal livello foglia),
			 * 		cancella ci;
			 * altrimenti 
			 * 		aggiorna il suo valore
			 */
			if(ci.getChild()== null || ci.getChild().isEmpty())
				currentNode.deleteEntry(ci);
			else
				ci.update();
				
			return leftMostPath;
		}
		else{
			headLeafNode = ((LeafNode) currentNode).getNextNode();
			((LeafNode) currentNode).deleteFromTree();
			
			return currentNode;
		}
	}
	
	/**
	 * Verifica se una {@link CF_EntryLeaf} può essere 
	 * riassorbita nel {@link CF_Tree}, senza causare
	 * la crescita della dimensione dell'albero stesso 
	 * @param outlier {@link CF_EntryLeaf} da aggiungere
	 * @return <code>true</code> se il riassorbimento è stato effettuato,
	 * <code>false</code> altrimenti
	 */
	public boolean tryToAbsorb(CF_EntryLeaf outlier) {
		return tryToAbsorb(root, outlier);
	}
	
	/*
	 * Verifica se una {@link CF_EntryLeaf} può essere 
	 * riassorbita nel {@link CF_Tree}, senza causare
	 * la crescita della dimensione dell'albero stesso 
	 * @param currentNode nodo corrente da cui iniziare a cercare
	 * @param outlier {@link CF_EntryLeaf} da aggiungere
	 * @return <code>true</code> se il riassorbimento è stato effettuato,
	 * <code>false</code> altrimenti
	 */
	private boolean tryToAbsorb(Node currentNode, CF_EntryLeaf outlier) {
	
		if ( !currentNode.isLeaf() ) {
			Node ci = ((NonLeafNode) currentNode).closestChild(outlier);
			boolean result = tryToAbsorb(ci, outlier);
			
			/*
			 * Se outlier è stato assorbito,
			 * aggiorno currentNode
			 */
			if (result) {
				ci.updateFather(outlier);
				return true;
			} 
			else 
				return false;
		} 
		else { // currentNode è un nodo foglia
			CF_EntryLeaf li = (CF_EntryLeaf) currentNode.closestEntry(outlier);
			
			// li capace di contenere la nuova entry
			if (li.canAbsorb(outlier, threshold) ) {
				((LeafNode) currentNode).absorb(li, outlier);
				// outlier assorbito
				return true; 
			} 
			// li non può contenere la nuova entry
			else if(currentNode.hasFreeSpace()) {
				currentNode.insertEntry(outlier, li);
				numberCF_EntryLeaf++;
				return true;
			}
			else
				/* l'aggiunta di outlier nel nodo foglia causerebbe uno split
				 * del medesimo, causando la crescita della dimensione dell'albero
				 */
				return false;
		}
	}

	
	/**
	 * Individua il cluster di appartenenza 
	 * del punto passato come parametro
	 * @param coordinates coordinate del punto da aggiungere 
	 * @return l'identificativo del cluster di appartenenza
	 */
	public String labeling(double[] coordinates) {
		CF_EntryLeaf entry = new CF_EntryLeaf(coordinates);
		String id = root.id;
		Node currentNode = root;
		
		while ( !currentNode.isLeaf() ){
			currentNode = ((NonLeafNode) currentNode).closestChild(entry);
			id += ";"+currentNode.id;
		}
		
		return id;
	}
	
	/**
	 * Effettua l'etichettatura
	 * dei nodi del {@link CF_Tree}
	 */
	public void mapping(){
		// Visita in Ampiezza
		int id = 0, nrNode = 1, nrNodeNextLevel = 0;
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(root);
		boolean flagLeafNode = false;
		
		while( !flagLeafNode ) {
			Node nodo = queue.removeFirst();
			nodo.id = Integer.toString(id);
			id++;

//			for (CF_Entry child : nodo.arrayEntry)
			for (CF_Entry child : nodo.getEntries())
				queue.add(((CF_EntryNonLeaf) child).getChild());
			
//			nrNodeNextLevel += nodo.arrayEntry.size();
			nrNodeNextLevel += nodo.getEntries().size();
			nrNode--;
			
			if(nrNode == 0){
				nrNode = nrNodeNextLevel;
				nrNodeNextLevel = 0; 
				id = 0;
				flagLeafNode = queue.getFirst().isLeaf();
//				flagLeafNode = queue.getFirst() == headLeafNode;
			}
		}
		
		for (int i = 0; i < queue.size(); i++){
			queue.get(i).id = Integer.toString(id);
			id++;
		}
	}

	/**
	 * Restituisce i {@link Node} del livello
	 * specificato come parametro
	 * @param level livello richiesto
	 */
	public LinkedList<Node> visitaAmpiezza(int level) {
		// Visita in Ampiezza
		int nrReset = 0, nrNode = 1, nrNodeNextLevel = 0;
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(root);
		
		// Se viene richiesto il Livello 0 ossia il livello radice
		if(level == 0)
			return queue;
		
		while( nrReset != level) {
			Node nodo = queue.removeFirst();

//			for (CF_Entry child : nodo.arrayEntry)
			for (CF_Entry child : nodo.getEntries())
				queue.add(((CF_EntryNonLeaf) child).getChild());
			
//			nrNodeNextLevel += nodo.arrayEntry.size();
			nrNodeNextLevel += nodo.getEntries().size();
			nrNode--;
			
			if(nrNode == 0){
				nrReset++;
				
				nrNode = nrNodeNextLevel;
				nrNodeNextLevel = 0; 
			}
		}
		
		return queue;
	}

	@Override
	public Iterator<LeafNode> iterator() {
		return new CF_TreeIterator();
	}
	
	/*
	 * Classe che implementa 
	 * l'Iteratore sui {@link LeafNode}
	 */
	private class CF_TreeIterator implements Iterator<LeafNode> {
		LeafNode currentNode = headLeafNode;
		
		@Override
		public boolean hasNext() {
			return currentNode != null;
		}

		@Override
		public LeafNode next() {
			if (currentNode == headLeafNode){
				currentNode = currentNode.getNextNode();
				return headLeafNode;
			}
			else { 
				LeafNode tmp = currentNode;
				currentNode = currentNode.getNextNode();
				return tmp;
			}
			
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
}