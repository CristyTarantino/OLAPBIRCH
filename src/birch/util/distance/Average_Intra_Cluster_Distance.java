package birch.util.distance;

// D3
import birch.cf_tree.cf_node.cf_entry.*;
import birch.util.*;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Average_Intra_Cluster_Distance</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Calcola
 * la distanza media intra cluster tra due {@link CF_Entry}</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class Average_Intra_Cluster_Distance implements iDistance {

	@Override
	public double distance(CF_Entry entry1, CF_Entry entry2) {
		double Ntot = entry1.getNumberPoints() + entry2.getNumberPoints();
		VectorND first = entry1.getSquareSum().addiction(entry2.getSquareSum());
		VectorND second = first.scalarProduct(2*Ntot);
		
		VectorND third = entry1.getLinearSum().addiction(entry2.getLinearSum()); 
		double fourth = 2*third.dotProduct(third);
		
		double temp = second.addictionAllItems() - fourth;
		
		return Math.pow(temp/(Ntot*(Ntot-1)), 0.5);
	}
}