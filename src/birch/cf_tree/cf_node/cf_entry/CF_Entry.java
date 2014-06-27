package birch.cf_tree.cf_node.cf_entry;

import birch.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp CF_Entry</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p> 
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Tripla 
 * che sintetizza l'informazione circa un cluster di dati</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public abstract class CF_Entry {
	
	/**
	 * Numero di punti del cluster
	 */
	protected int numberPoints;
	
	/**
	 * Somma lineare dei punti del cluster
	 */
	protected VectorND linearSum;
	
	/**
	 * Somma quadratica dei punti del cluster
	 */
	protected VectorND squareSum;

	/**
	 * Costruisce una {@link CF_Entry} vuota di dimensione definita
	 * @param dimensione dimensione dello spazio dei punti del {@link CF_Entry}
	 */
	public CF_Entry(int dimensione) {
		linearSum = new VectorND(dimensione);
		squareSum = new VectorND(dimensione);
	}
	
	/**
	 * Converte un punto in un {@link CF_Entry} 
	 * @param point coordinate del punto
	 */
	public CF_Entry(double[] point){
		numberPoints++;
		linearSum = new VectorND(point);
		squareSum = linearSum.square();
	}
	
	/**
	 * Fonde uno specifico {@link CF_Entry} con questo
	 * basandosi sul "CF Additivity theorem"
	 * @param entry la {@link CF_Entry} da fondere
	 */
	// Alias ABSORB()
	public void merge(CF_Entry entry){
		this.numberPoints += entry.numberPoints;
		this.linearSum.addictionEquals( entry.linearSum );
		this.squareSum.addictionEquals( entry.squareSum );
	}
	
	/**
	 * Cancella una specifica {@link CF_Entry}
	 * @param entry la {@link CF_Entry} da cancellare
	 */
	public void delete(CF_Entry entry){
		this.numberPoints -= entry.numberPoints;
		this.linearSum.subtractionEquals( entry.linearSum );
		this.squareSum.subtractionEquals( entry.squareSum );
	}

	/**
	 * Restituisce il centroide del {@link CF_Entry}
	 * @return le coordinate del centroide
	 */
	public VectorND getCentroid() {
		return linearSum.scalarProduct(1.0/numberPoints);
	}
	
	/**
	 * Restituisce il raggio del {@link CF_Entry}
	 * @return la misura del raggio
	 */
	public double getRadius() {
		double radius = 0.0;
		for (int i = 0; i < linearSum.size(); i++)
			radius += squareSum.get(i)/numberPoints 
						- Math.pow(linearSum.get(i)/numberPoints, 2);
		
		return Math.pow(radius, 0.5); // == Math.sqrt
	}
	
	/**
	 * Restituisce il diametro del {@link CF_Entry}
	 * @return la misura del diametro
	 */
	public double getDiameter() {
		double first = this.squareSum.scalarProduct(this.numberPoints).addictionAllItems();
		double second = this.linearSum.dotProduct(this.linearSum);
		double third = (2*(first-second))/(this.numberPoints*(this.numberPoints-1));
		return Math.pow(third, 0.5);
	}
	
	/**
	 * Restituisce il numero di punti del {@link CF_Entry}
	 * @return il numero di punti contenuti
	 */
	public int getNumberPoints() {
		return numberPoints;
	}

	/**
	 * Imposta il numero di punti del {@link CF_Entry}
	 * @param num_pts il numero di punti
	 */
	public void setNumberPoints(int num_pts) {
		numberPoints = num_pts;
	}

	/**
	 * Restituisce la somma lineare del {@link CF_Entry}
	 * @return la somma lineare
	 */
	public VectorND getLinearSum() {
		return linearSum;
	}

	/**
	 * Imposta la somma lineare del {@link CF_Entry}
	 * @param ls la somma lineare
	 */
	public void setLinearSum(VectorND ls) {
		linearSum = ls;
	}

	/**
	 * Restituisce la somma quadratica del {@link CF_Entry}
	 * @return la somma quadratica
	 */
	public VectorND getSquareSum() {
		return squareSum;
	}

	/**
	 * Imposta la somma quadratica del {@link CF_Entry}
	 * @param ss la somma quadratica
	 */
	public void setSquareSum(VectorND ss) {
		squareSum = ss;
	}
	
	@Override
	public String toString() {
		return "N: " + numberPoints + "\nLS: " + linearSum + "\nSS: " + squareSum;
	}

	@Override
	public boolean equals(Object entry) {
		try {
			CF_Entry ent = (CF_Entry) entry;
			
			if(ent == this)
				return true;
			
			boolean n = this.numberPoints == ent.numberPoints;
			boolean ls = this.linearSum.equals(ent.linearSum);
			boolean ss = this.squareSum.equals(ent.squareSum);
			
			return n && ls && ss;
		} 
		catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}
}