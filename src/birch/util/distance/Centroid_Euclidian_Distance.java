package birch.util.distance;

// D0
import birch.cf_tree.cf_node.cf_entry.*;
import birch.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Centroid_Euclidian_Distance</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Calcola
 * la distanza euclidea tra i centroidi di due {@link CF_Entry}</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public final class Centroid_Euclidian_Distance implements iDistance {
	
	@Override
	public double distance(CF_Entry entry1, CF_Entry entry2) {
		VectorND centroidThis = entry1.getCentroid();
		VectorND centroidOther = entry2.getCentroid();
		
		VectorND first = centroidThis.subtraction(centroidOther);
		double second = first.dotProduct(first);
		
		return Math.pow(second, 0.5);
	}
}