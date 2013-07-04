package unirio.cluster.optimizer;

import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.management.modelmbean.XMLParseException;

import unirio.cluster.generic.controller.CDAReader;
import unirio.cluster.generic.controller.ClusteringCalculator;
import unirio.cluster.generic.model.Project;

public class MainProgram
{
	public static int MAX_INCREMENTOS = 1000;
	
	private static String[] instanceFilenamesReals = 
	{ 
		"data\\odem\\jstl-1.0.6 18C.odem",
		"data\\odem\\jnanoxml.odem",
		"data\\odem\\joda-money 26C.odem",
		"data\\odem\\jxls-reader 27C.odem", 
		"data\\odem\\seemp.odem", 
		"data\\odem\\apache_zip.odem",
		"data\\odem\\udt-java.odem",
		"data\\odem\\javaocr 59C.odem", 
		"data\\odem\\servletapi-2.3 74C.odem", 
		"data\\odem\\pfcda_base.odem",
		"data\\odem\\forms-1.3.0.odem",
		"data\\odem\\jscatterplot.odem",
		"data\\odem\\jfluid-1.7.0.odem",
		"data\\odem\\jxls-core 83C.odem",
		"data\\odem\\jpassword 96C.odem", 
		"data\\odem\\junit-3.8.1 100C.odem", 
		"data\\odem\\xmldom.odem", 
		"data\\odem\\tinytim 134C.odem", 
		"data\\odem\\jkaryoscope.odem",
		"data\\odem\\gae_plugin_core.odem",		
		"data\\odem\\javacc.odem", 
		"data\\odem\\JavaGeom.odem",
		"data\\odem\\jdendogram.odem",
		"data\\odem\\xmlapi.odem",
		"data\\odem\\jmetal.odem", 
		"data\\odem\\dom4j-1.5.2 195C.odem", 
		"data\\odem\\pdf_renderer.odem",
		"data\\odem\\Jung-graph.odem", 
		"data\\odem\\jconsole-1.7.0.odem",
		"data\\odem\\Jung-visualization.odem", 
		"data\\odem\\pfcda_swing.odem",
		"data\\odem\\jpassword 269C.odem", 
		"data\\odem\\jml.odem",
		"data\\odem\\notepad-full.odem",
		"data\\odem\\poormans_2.3 304C.odem", 
		"data\\odem\\log4j-1.2.16 308C.odem",
		"data\\odem\\jtreeview.odem",
		"data\\odem\\jace.odem",
		"data\\odem\\javaws-7.odem",
		"data\\odem\\res_cobol.odem",/*
		"data\\odem\\y_base.odem",
		"data\\odem\\lwjgl.odem",
		"data\\odem\\apache_ant_taskdef.odem",
		"data\\odem\\iTextPDF-v5.3.1.odem",
		"data\\odem\\apache_lucene_core.odem",
		"data\\odem\\eclipse_jgit.odem",
		"data\\odem\\apache_ant.odem",
		"data\\odem\\y_layout.odem",*/
		"" 
	};

	private static Vector<Project> readInstances(String[] filenames) throws XMLParseException
	{
		Vector<Project> instances = new Vector<Project>();
		CDAReader reader = new CDAReader();

		for (String filename : filenames)
			if (filename.length() > 0)
				instances.add(reader.execute(filename));

		return instances;
	}

	protected static double calculateMQ(Project project, int[] solution) throws Exception
	{
		ClusteringCalculator calculator = new ClusteringCalculator(project, project.getClassCount());

		for (int i = 0; i < solution.length; i++)
			calculator.moveClass(i, solution[i]);

		return calculator.calculateModularizationQuality();
	}

	protected static int calculateEVM(Project project, int[] solution) throws Exception
	{
		ClusteringCalculator calculator = new ClusteringCalculator(project, project.getClassCount());

		for (int i = 0; i < solution.length; i++)
			calculator.moveClass(i, solution[i]);

		return calculator.calculateEVM();
	}

	private static int calculateMoves(int[] startSolution, int[] solution) throws Exception
	{
		int moves = 0;

		for (int i = 0; i < startSolution.length; i++)
			if (startSolution[i] != solution[i])
				moves++;

		return moves;
	}

	private static void annotateMovements(int[] currentSolution, int[] lastSolution, int[] movements)
	{
		for (int i = 0; i < movements.length; i++)
			if (currentSolution[i] != lastSolution[i])
				movements[i]++;
	}

	private static int countDownturns(int[] movements)
	{
		int count = 0;

		for (int i = 0; i < movements.length; i++)
			if (movements[i] > 1)
				count += (movements[i] - 1);

		return count;
	}

	private static void write(PrintWriter pw, String text)
	{
		System.out.println(text);
		pw.println(text);
		pw.flush();
	}
	
	protected static String format(double fitness)
	{
		DecimalFormat df4 = new DecimalFormat("0.0000");
		String sFitness = df4.format(fitness);
		
		while (sFitness.length() < 7)
			sFitness = ' ' + sFitness;
		
		return sFitness;
	}
	
	private static String format(int n)
	{
		DecimalFormat df0 = new DecimalFormat("0");
		String s = df0.format(n);
		
		while (s.length() < 4)
			s = ' ' + s;
		
		return s;
	}
	
	private static double calculateFitness(Project project, int[] solution) throws Exception
	{
		return calculateEVM(project, solution);
	}
	
	private static void showSolution(PrintWriter pw, HillClimbingClustering hc, Project project, String type, int[] startSolution, int[] solution, int[] movements) throws Exception
	{
		double fitness = calculateFitness(project, solution);
		int moves = calculateMoves(startSolution, solution);
		int downturns = (movements != null) ? countDownturns(movements) : 0;
		write(pw, type + "; FIT: " + format(fitness) + "; MV: " + format(moves) + "; DT: " + format(downturns) + "; SOL: " + hc.printSolution(solution));
	}

	public static final void main(String[] args) throws Exception
	{
		Vector<Project> instances = new Vector<Project>();
		instances.addAll(readInstances(instanceFilenamesReals));
		PrintWriter pw = new PrintWriter(new File("saida.txt"));

		for (Project project : instances)
			write(pw, project.getName() + " (" + project.getClassCount() + " classes; " + project.getPackageCount() + " packages; " + project.getDependencyCount() + " deps)");

		write(pw, "");

		for (Project project : instances)
		{
			int cycle = 0;
			//for (int cycle = 0; cycle < 30; cycle++)
			//{
				int classCount = project.getClassCount();
				long maxEvaluations = 1000 * classCount * classCount;
				write(pw, "Cycle #" + cycle + ": " + project.getName());
	
				HillClimbingClustering hccComplete = new HillClimbingClustering(project, maxEvaluations, project.getClassCount());
				HillClimbingClustering hccIncremental = new HillClimbingClustering(project, maxEvaluations, 5);
				int[] classOrder = hccComplete.generateBasicClassOrder(classCount);
	
				int[] movements = new int[project.getClassCount()];
				int[] startSolution = hccComplete.createStartingSolution();
				double initialFitness = calculateFitness(project, startSolution);
				showSolution(pw, hccComplete, project, "S", startSolution, startSolution, movements);
	
				int[] completeMovements = new int[project.getClassCount()];
				int[] completeSolution = hccComplete.execute(startSolution, classOrder);
				double completeFitness = calculateFitness(project, completeSolution);
				showSolution(pw, hccComplete, project, "C", startSolution, completeSolution, completeMovements);
	
				int[] incrementalSolution = startSolution;
				double incrementalFitness = calculateFitness(project, incrementalSolution);

				if (incrementalFitness == completeFitness)
					showSolution(pw, hccIncremental, project, "I", startSolution, startSolution, movements);
				
				int passos = 0;
				
				while (passos < MAX_INCREMENTOS && Math.abs(incrementalFitness - completeFitness) > 0.01)
				{
					int[] currentSolution = hccIncremental.execute(incrementalSolution, classOrder);
					annotateMovements(currentSolution, incrementalSolution, movements);
					incrementalFitness = calculateFitness(project, currentSolution);
					showSolution(pw, hccIncremental, project, "I" + passos, startSolution, currentSolution, movements);
					incrementalSolution = currentSolution;
					passos++;
				}
	
				int completeMoves = calculateMoves(startSolution, completeSolution);
				int incrementalMoves = calculateMoves(startSolution, incrementalSolution);
				int downturns = countDownturns(movements);
				write(pw, project.getName() + "; " + format(initialFitness) + "; " + format(completeFitness) + "; " + format(completeMoves) + "; " + format(passos) + "; " + format(incrementalFitness) + "; " + format(incrementalMoves) + "; " + format(downturns));
				write(pw, "");
			//}
		}

		pw.close();
	}
}