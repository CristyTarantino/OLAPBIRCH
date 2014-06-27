package birch.util.distance;

// D4
import birch.cf_tree.cf_node.cf_entry.*;
import birch.util.*;


/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Variance_Increase_Distance</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Calcola
 * la distanza ad incremento di varianza tra due {@link CF_Entry}</p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class Variance_Increase_Distance implements iDistance {

	@Override
	public double distance(CF_Entry entry1, CF_Entry entry2) {
		double temp1 = entry1.getLinearSum().dotProduct(entry1.getLinearSum());
		double first = temp1/entry1.getNumberPoints();
		
		double temp2 = entry2.getLinearSum().dotProduct(entry2.getLinearSum());
		double second = temp2/entry2.getNumberPoints();
		
		VectorND temp4 = entry1.getLinearSum().addiction(entry2.getLinearSum());
		double temp3 = temp4.dotProduct(temp4);
		
		double third = temp3/(entry1.getNumberPoints() + entry2.getNumberPoints());
		
		return Math.pow(first + second - third, 0.5);
	}
}