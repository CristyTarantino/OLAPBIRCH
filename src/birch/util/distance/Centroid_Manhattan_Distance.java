package birch.util.distance;

// D1
import birch.cf_tree.cf_node.cf_entry.*;
import birch.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Centroid_Manhattan_Distance</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Calcola
 * la distanza di Manhattan tra i centroidi di due {@link CF_Entry}</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class Centroid_Manhattan_Distance implements iDistance {

	@Override
	public double distance(CF_Entry entry1, CF_Entry entry2) {
		VectorND centroidThis = entry1.getCentroid();
		VectorND centroidOther = entry2.getCentroid();
		VectorND diff = centroidThis.subtraction(centroidOther);
		
		return Math.abs(diff.addictionAllItems());
	}

}