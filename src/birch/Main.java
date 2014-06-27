package birch;

import java.io.*; 
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import birch.cf_tree.*;
import birch.cf_tree.cf_node.*;
import birch.util.database.*;


/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp Main</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Main class </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class Main {

	private static final iDAO DAO = DAOFactory.getInstance(DAOFactory.MYSQL);
	private static int nrPuntiElaborati = 0;
	private static int nrRebuild = 0;

	/**
	 * Main dell'algoritmo di clustering BIRCH
	 * @param args argomenti (non supportati)
	 */
	public static void main(String[] args) {

		CF_Tree tree = new CF_Tree();

		System.out.println("\n======================================================");
		GregorianCalendar gcAvvio = new GregorianCalendar();
		System.out.print(gcAvvio.get(Calendar.DAY_OF_MONTH)+"/"
							+ (gcAvvio.get(Calendar.MONTH)+1)+"/"
							+ (gcAvvio.get(Calendar.YEAR)));
		System.out.println("\t"+gcAvvio.get(Calendar.HOUR_OF_DAY)+":"
								+gcAvvio.get(Calendar.MINUTE)+":"
								 +gcAvvio.get(Calendar.SECOND) );
		System.out.println("======================================================");
		
		System.out.println("\nInizio pre-clustering");
		System.out.println("======================================================");
			tree = insert(tree);
		
//		System.out.println("\nFase 2");
//		System.out.println("======================================================");
//			tree = tree.condenseTree();
		
		GregorianCalendar gcFine = new GregorianCalendar();
		System.out.print(gcFine.get(Calendar.DAY_OF_MONTH)+"/"
							+ (gcFine.get(Calendar.MONTH)+1)+"/"
							+ (gcFine.get(Calendar.YEAR)));
		System.out.println("\t"+gcFine.get(Calendar.HOUR_OF_DAY)+":"
							+gcFine.get(Calendar.MINUTE)+":"
							 +gcFine.get(Calendar.SECOND) );


		System.out.println("\nFine pre-clustering");
		System.out.println("======================================================");

		System.out.println("\n\n======================================================");
		System.out.println("\tInformazioni di riepilogo");
		System.out.println("======================================================");
		System.out.println("Punti processati: " + nrPuntiElaborati);
		System.out.println("Profondita': " + tree.getDepth());
		System.out.println("Profondita' massima: " + Params.MAX_DEPTH);
		System.out.println("N° rebuild effettuati: " + nrRebuild);
		int secondi = (int) (gcFine.getTimeInMillis() - gcAvvio.getTimeInMillis()) / 1000;
		System.out.println("Tempo impiegato: " + secondi +" s");
		System.out.println("Qualita' cluster (Q2): " + calQuality2(tree));
		System.out.println("======================================================");
		
		System.out.println("\nLabeling");
		System.out.println("======================================================");
			labeling(tree);
		System.out.println("\nFine Labeling");
		System.out.println("======================================================");
		
		System.out.println("\nSalvataggio file ARFF");
		System.out.println("======================================================");
			for (int i = 1; i < tree.getDepth() + 1; i++)
				arffSaver(tree.visitaAmpiezza(i), i+1);
		
	}

	/*
	 * Recupera i dati dal dataset e li inserisce nel {@link CF_Tree}
	 * @param tree L'albero in cui inserire i punti
	 */
	@SuppressWarnings("unchecked")
	private static CF_Tree insert(CF_Tree tree) {
		DAO.initConnection();
			String[] queries = query();
	
			int nrPoints = DAO.getNumberPoints(queries[0]);
			
			int offset = 0;
			ArrayList<double[]> points;
	
			for (; nrPoints > 0; nrPoints -= 10, offset += 10) {
				points = (ArrayList<double[]>) DAO.getPoints(queries[1], offset, 10);
	
				if(tree.getDepth() > Params.MAX_DEPTH) {
					tree = tree.rebuild();
					nrRebuild++;
					System.out.println("\n======================================================");
					System.out.println("Rebuild");
					System.out.println("Profondita': "+tree.getDepth());
					System.out.println("Profondita' massima: "+Params.MAX_DEPTH);
					System.out.println("======================================================");
				}
	
				for (double[] coordinates : points)
					tree.insert(coordinates);
	
				nrPuntiElaborati += points.size();
				
//				System.out.println("\nPunti Elaborati: "+nrPuntiElaborati);
//				GregorianCalendar gc = new GregorianCalendar();
//				System.out.print(gc.get(Calendar.DAY_OF_MONTH)+"/"
//								+ (gc.get(Calendar.MONTH)+1)+"/"
//								+ (gc.get(Calendar.YEAR)));
//				System.out.println("\t"+gc.get(Calendar.HOUR_OF_DAY)+":"
//										+gc.get(Calendar.MINUTE)+":"
//										 +gc.get(Calendar.SECOND) );
//				System.out.println("======================================================");
	
			}

		DAO.closeConnection();

		return tree;
	}

	/*
	 * Labeling dei punti inseriti nel {@link CF_Tree}
	 * @param tree {@link CF_Tree} da utilizzare per il labeling 
	 */
	@SuppressWarnings("unchecked")
	private static void labeling(CF_Tree tree) {
		tree.mapping();

		DAO.initConnection();
			String[] queries = query();
			
			int nrPoints = DAO.getNumberPoints(queries[0]);
			
			int offset = 0; 
			String clusterID;
			ArrayList<double[]> points;
			double[] point;
	
			for (; nrPoints > 0; nrPoints -= 10, offset += 10) {
				points = (ArrayList<double[]>) DAO.getPoints(queries[3], offset, 10);
	
				for (double[] coordinates : points){
					point = new double[coordinates.length -1];
	
					for (int i = 0; i < coordinates.length-1; i++)
						point[i] = coordinates[i];
	
					clusterID = tree.labeling(point);
	
					DAO.setClusterID(queries[2], (int) coordinates[coordinates.length-1], clusterID);
				}
			}

		DAO.closeConnection();
	}

	/*
	 * Salvataggio dei risultati della fase di pre-clustering in un file ARFF
	 */
	private static String arffSaver(LinkedList<birch.cf_tree.cf_node.Node> nodeList, int level) {
		try {
			String file = "level"+level+".arff";
			PrintWriter out = new PrintWriter(file);
			out.println("@relation \'dataset\'");

			for (int i = 0; i < Params.NR_DIMENSION; i++)
				out.println("@attribute att#"+(i+1)+" real");

			out.println("@data");

			for (birch.cf_tree.cf_node.Node node : nodeList)
				out.println( node.getFather().getCentroid() );

			out.close();

			return file;

		} catch (FileNotFoundException e) {
			System.err.println("Errore in arffSaver() - FileNotFoundException");
//			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Recupera le query OLAP dal file XML
	 * @return le query da utilizzare
	 */
	private static String[] query() {
		try {
			File file = new File(Params.XML_OLAP_CUBE);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();

			NodeList nodeLstTable = doc.getElementsByTagName("Table");
			NodeList nodeLstMeasure = doc.getElementsByTagName("Measure");
			NodeList nodeLstAttribute = doc.getElementsByTagName("Attribute");
			NodeList nodeLstHierarchy = doc.getElementsByTagName("Hierarchy");
			NodeList nodeLstDimension = doc.getElementsByTagName("Dimension");
			String maxDepth = doc.getElementsByTagName("Depth").item(0).getAttributes().getNamedItem("value").getNodeValue();
			Params.MAX_DEPTH = Integer.parseInt(maxDepth);


			String factTable = nodeLstTable.item(0).getAttributes().getNamedItem("name").getNodeValue();
			String dimensionTable = nodeLstTable.item(1).getAttributes().getNamedItem("name").getNodeValue();
			String frKey_Dim1 = nodeLstDimension.item(0).getAttributes().getNamedItem("foreignKey").getNodeValue();
			String prKey_Dim1 = nodeLstHierarchy.item(0).getAttributes().getNamedItem("primaryKey").getNodeValue();

			String[] nameColumns = new String[nodeLstAttribute.getLength() + nodeLstMeasure.getLength()];
			Params.NR_DIMENSION = nameColumns.length;

			Node node;
			int i;

			for (i = 0; i < nodeLstAttribute.getLength(); i++) {
				node = nodeLstAttribute.item(i);
				nameColumns[i] = node.getAttributes().getNamedItem("column").getNodeValue();
			}

			for (int j = 0; i < nameColumns.length; i++, j++) {
				node = nodeLstMeasure.item(j);
				nameColumns[i] = node.getAttributes().getNamedItem("column").getNodeValue();
			}

			// Creazione della query sql "select"
			String query = "select "; 


			for (i = 0; i < nameColumns.length -1; i++) 
				query += nameColumns[i] +",";

			query += nameColumns[i];

			query += " from " + factTable + ", " + dimensionTable 
			+ " where " + factTable+"."+frKey_Dim1 + " = " + dimensionTable+"."+prKey_Dim1;

			// Creazione della query sql "count"
			String queryCount = "SELECT count(*) FROM " + factTable + ", " + dimensionTable
			+ " where " + factTable+"."+frKey_Dim1 + " = " + dimensionTable+"."+prKey_Dim1;

			// Creazione della query sql "update"
			String queryUpdate = "UPDATE " + dimensionTable + " SET " + Params.CLUSTER_ID + " = ? " 
			+"WHERE "+prKey_Dim1+"=?";

			// Creazione della query sql "select"
			String querySelectLab = "select "; 


			for (i = 0; i < nameColumns.length; i++) 
				querySelectLab += nameColumns[i] +",";

			querySelectLab += prKey_Dim1;

			querySelectLab += " from " + factTable + ", " + dimensionTable 
			+ " where " + factTable+"."+frKey_Dim1 + " = " + dimensionTable+"."+prKey_Dim1;

			return new String[]{queryCount, query, queryUpdate, querySelectLab};

		} catch (Exception e) {
			System.err.println("Errore in query() - Exception");
//			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Calcola la misura di qualità sui {@link LeafNode}
	 * secondo la formula indicata in [ZRL95]
	 * @param tree {@link CF_Tree} su cui applicare
	 * la misura di qualità
	 * @return il risultato della misura
	 */
	private static double calQuality2(CF_Tree tree) {
		double den = 0.0, num = 0.0;
	
		for (LeafNode leafNode : tree) {
			int N = leafNode.getFather().getNumberPoints();
			double diameter = leafNode.getFather().getDiameter();
			
			// NaN perchè non si può calcolare il diametro 
			// dei cluster con numero pts == 1
			if(!Double.isNaN(diameter)) {
				num += N*(N-1)*Math.pow(diameter, 2);
				den += N*(N-1);
			}
		}
		
		return num/den;
	}
}