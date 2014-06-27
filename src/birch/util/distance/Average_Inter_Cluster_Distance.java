package birch.util.distance;
//D2

import birch.cf_tree.cf_node.cf_entry.*;
import birch.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Average_Inter_Cluster_Distance</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Calcola
 * la distanza media inter cluster tra due {@link CF_Entry}</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class Average_Inter_Cluster_Distance implements iDistance {

	@Override
	public double distance(CF_Entry entry1, CF_Entry entry2) {
		VectorND first = entry1.getSquareSum().scalarProduct(entry2.getNumberPoints());
		VectorND second = entry2.getSquareSum().scalarProduct(entry1.getNumberPoints());
		first.addictionEquals(second);
		
		double third = 2*entry1.getLinearSum().dotProduct(entry2.getLinearSum());
		double fourth = (first.addictionAllItems() - third)/(entry1.getNumberPoints()*entry2.getNumberPoints());
		return Math.pow(fourth, 0.5);
	}

}
