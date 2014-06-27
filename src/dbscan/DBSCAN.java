package dbscan;

import java.io.File;
import java.util.Scanner;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.DBScan;

import birch.cf_tree.Params;

/**
 * <p> <b>Title:</b><br /> &nbsp &nbsp &nbsp &nbsp DBSCAN</p>
 * <p> <b>Company:</b><br /> &nbsp &nbsp &nbsp &nbsp Dipartimento di Informatica, 
 * Universit&agrave degli studi di Bari "Aldo Moro"</p>
 * <p> <b>Course:</b><br /> &nbsp &nbsp &nbsp &nbsp Informatica e Tecnologie 
 * per la Produzione del Software</p>
 * <p> <b>Class description:</b><br /> &nbsp &nbsp &nbsp &nbsp Consente l'esecuzione del DBSCAN
 * applicandolo su file ARFF </p>
 * @author Tarantino - Turturo
 * @version 1.0
 */
public class DBSCAN {

	/**
	 * Main dell'algoritmo di clustering DBScan
	 * @param args argomenti (non supportati)
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		boolean flag = true; 
		
		do {
			System.out.println("Inserire livello: ");
			String nomeFile = "level"+in.nextInt()+".arff";
			
			File file = new File(nomeFile);
			if (file.exists()) {
				dbscan(nomeFile);
				flag = false;
			}
			else
				System.out.println("ATTENZIONE! Hai selezionato un livello errato!");
			
		} while(flag);
	}
	
	/*
	 * Esecuzione dell'algoritmo DBSCAN di WEKA
	 */
	private static void dbscan(String nomeFile) {
		String[] options = new String[6];

		options[0] = "-E"; // epsilon
		options[1] = Params.EPSILON; // default 0.6
		options[2] = "-M"; // MinPts
		options[3] = Params.MIN_PTS; // default 9
		options[4] = "-t"; // dataset input
		options[5] = nomeFile;

		try {
			System.out.println(ClusterEvaluation.evaluateClusterer(new DBScan(), options));
		} catch (Exception e) {
			System.err.println("Errore in dbscan() - Exception");
//			e.printStackTrace();
		}
	}
}
