package birch.util;

import java.io.Serializable;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp VectorND</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Implementa un vettore
 * n-dimensionale</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class VectorND implements Serializable{

	private static final long serialVersionUID = -1790604974300873022L;
	
	private double[] vector;
	private int size;

	/**
	 * Costruisce un {@link VectorND} di dimensione pari 
	 * a quella passata come parametro
	 * @param dimensione numero di elementi del vettore
	 */
	public VectorND( int dimensione ) {
		if ( dimensione <= 0 )
			throw new IllegalArgumentException( "Can not construct 0-dimensional vector" );
		vector = new double[ dimensione ];
		size = vector.length;
	}

	/**
	 * Costruisce un {@link VectorND} di dimensione pari 
	 * a quella passata come parametro, 
	 * con ogni elemento pari al parametro <code>item</code>
	 * @param dimensione numero di elementi del vettore
	 * @param item elemento di riempimento 
	 */
	public VectorND( int dimensione, double item ) {
		if ( dimensione <= 0 )
			throw new IllegalArgumentException( "Can not construct 0-dimensional vector" );

		vector = new double[ dimensione ];
		for ( int i = dimensione-1; i >= 0; --i )
			vector[i] = item;

		size = vector.length;
	}

	/**
	 * Costruisce un {@link VectorND} a partire da un array
	 * @param vettore l'array
	 */
	public VectorND( double [] vettore ) {
		if ( vettore.length == 0 )
			throw new IllegalArgumentException(" Can not construct 0-dimensional vector" );

		vector = vettore;
		size = vector.length;
	}

	/**
	 * Prodotto scalare tra due vettori
	 * @param otherVector il vettore con cui effettuare il prodotto
	 * @return il risultato della prodotto scalare
	 */
	public double dotProduct( VectorND otherVector ) {
		if ( !validateDimension( otherVector ) )
			throw new IllegalArgumentException( "Illegal vector dimension" );

		double prod = 0;
		double B[] = otherVector.vector;

		for (int i = 0; i < size; ++i )
			prod += this.vector[i] * B[i];

		return prod;
	}
	
	/**
	 * Prodotto di uno scalare per un vettore
	 * @param scalar scalare da moltiplicare
	 * @return vettore in cui ogni elemento è stato moltiplicato per lo scalare
	 */
	public VectorND scalarProduct(double scalar) {
		VectorND result = new VectorND(this.size);
		
		for (int i = 0; i < this.size; i++)
			result.vector[i] = this.vector[i]*scalar;

		return result;
	}
	
	/**
	 * Eleva al quadrato tutti gli elementi di questo
	 * @return il vettore con tutti gli elementi al quadrato
	 */
	public VectorND square() {
		VectorND result = new VectorND(size);
		
		for (int i = 0; i < size(); i++)
			result.vector[i] = this.vector[i] * this.vector[i];
		
		return result;
	}
	
	/**
	 * Somma tra loro gli elementi di questo vettore
	 * @return la somma degli elementi
	 */
	public double addictionAllItems() {
		double result = 0.0;
		
		for (double item : this.vector)
			result += item;
		
		return result;
	}

	/**
	 * Restituisce l'i-esimo elemento del vettore
	 * @param index l'indice
	 * @return l'i-esimo elemento
	 */
	public double get( int index ) {
		return vector[index];
	}

	/**
	 * Imposta il valore dell'i-esimo elemento
	 * @param index l'indice
	 * @param value valore dell'elemento
	 */
	public void set( int index, double value ) {
		vector[index] = value;
	}

	/**
	 * Somma di due vettori
	 * @param otherVector il vettore da sommare a questo
	 * @return il vettore risultante
	 */
	public VectorND addiction( VectorND otherVector ) {
		if ( !validateDimension( otherVector ) )
			throw new IllegalArgumentException( "Illegal vector dimension" );

		double [] b = new double[ size ];

		for ( int i = 0; i < size; ++i )
			b[i] = otherVector.get(i) + vector[i];

		return new VectorND( b );
	}

	/**
	 * Somma il vettore di input a questo
	 * @param otherVector il vettore da sommare a questo
	 */
	public void addictionEquals(VectorND otherVector) {
		if ( !validateDimension(otherVector) )
			throw new IllegalArgumentException( "Illegal vector dimension" );

		for ( int i = 0; i < size; ++i )
			this.vector[i] += otherVector.vector[i];
	}

	/**
	 * Differenza di due vettori
	 * @param otherVector il vettore da sottrarre a questo
	 * @return il vettore risultante
	 */
	public VectorND subtraction( VectorND vec ) {
		return addiction( vec.scalarProduct( -1 ) );
	}

	/**
	 * Sottrae il vettore di input a questo
	 * @param otherVector il vettore da sottrarre a questo
	 */
	public void subtractionEquals( VectorND vec ) {
		addictionEquals( vec.scalarProduct( -1 ) );
	}

	/**
	 * Restituisce la dimensione del vettore
	 * @return dimensione del vettore
	 */
	public int size() {
		return size;
	}

	/**
	 * Restituisce una stringa formattata
	 * rappresentativa dell'oggetto
	 * @return stringa rappresentativa dell'oggetto
	 */
	public String toStringFormatted() {
		String temp = "[ ";

		for ( int i = 0; i < size; ++i )
			temp += doubleToString( vector[i], 2 ) + " ";

		temp += "]";

		return temp;
	}
	
	@Override
	public String toString(){
		String temp = "";

		for ( int i = 0; i < size - 1; ++i )
			temp += Double.toString(vector[i]) + ",";

		temp += Double.toString(vector[size - 1]);

		return temp;
	}
	
	/**
	 * Restituisce un nuovo {@link VectorND} inizializzato al valore
	 * rappresentato dalla specifica stringa, come quella ottenuta
	 * attraverso il metodo toString della classe {@link VectorND}
	 * @param s la stringa da convertire
	 * @return il {@link VectorND} rappresentato dalla stringa
	 */
	public static VectorND toVectorND(String s) {
		String[] tmp = s.substring(2, s.length()-1).split(" ");
		double[] vector = new double[tmp.length];
		
		for (int i = 0; i < tmp.length; i++)
			vector[i] = Double.parseDouble(tmp[i]);
		
		return new VectorND(vector);
	}
	
	@Override
	public VectorND clone() throws CloneNotSupportedException {
		VectorND cloneVector = new VectorND(vector.clone());
		return cloneVector;
	}

	@Override
	public boolean equals( Object v1 ) {
		try {
			VectorND v = (VectorND) v1;
			if ( v == this )
				return true;

			if ( !validateDimension( v ) )
				return false;

			for ( int i = 0; i < size; ++i )
				if ( vector[i] != v.get(i) )
					return false;

			return true;
		} 
		catch (ClassCastException e) {
			return false;
		}
	}


	/*
	 * Verifica che la dimensione del vettore in input
	 * sia uguale a questa
	 * @param vettore da confrontare
	 * @return <code>true</code> se hanno la stessa dimensione,
	 * <code>false</code> altrimenti
	 */
	private boolean validateDimension( VectorND otherVector ) {
		return this.size == otherVector.size;
	}

	/*
	 * Arrotonda un valore numerico a dmp cifre dopo la virgola
	 * @param d valore da arrotondare
	 * @param dmp numero di cifre dopo la virgola
	 * @return cifra arrotondata a dmp cifre dopo la virgola
	 */
	private String doubleToString( double d, int dmp ) {
		int f = (int)Math.pow( 10, dmp );
		int v = (int) ( d * f );
		return (v % f == 0) ? String.valueOf( ((double)v)/f ) : String.valueOf( ((double)v)/(double)(f) );
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}
}