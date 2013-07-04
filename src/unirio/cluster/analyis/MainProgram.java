package unirio.cluster.analyis;

import java.text.DecimalFormat;

import unirio.experiments.monoobjective.analysis.model.MonoExperimentResult;
import unirio.experiments.monoobjective.analysis.reader.MonoExperimentFileReader;
import unirio.experiments.monoobjective.analysis.reader.MonoExperimentFileReaderException;
import unirio.experiments.utils.Calculator;

public class MainProgram
{
	private static final String DIRECTORY_HC = "results\\hc 200N\\";
	
	private void loadInstance(String directory, String name, int solutionSize) throws MonoExperimentFileReaderException
	{
		MonoExperimentFileReader reader = new MonoExperimentFileReader();
		MonoExperimentResult result = reader.execute(directory + name + ".txt", 1, 30, solutionSize);
		double[] executionTime1 = result.getInstanceIndex(0).getExecutionTimes();
		double[] objective1 = result.getInstanceIndex(0).getObjectives();
		
		DecimalFormat df0 = new DecimalFormat("0");
		DecimalFormat df2 = new DecimalFormat("0.00");
		
		Calculator calculator = new Calculator();
		System.out.print(name + "\t");
		System.out.print(df0.format(calculator.calculateAverage(executionTime1)) + "\t");
		System.out.print(df2.format(calculator.calculateStandardDeviation(executionTime1)) + "\t");
		System.out.print(df2.format(calculator.calculateAverage(objective1)) + "\t");
		System.out.print(df2.format(calculator.calculateStandardDeviation(objective1)) + "\t");
		System.out.print(df2.format(calculator.calculateMaximum(objective1)) + "\t");
		System.out.println();
	}
	
	public static void main(String[] args) throws MonoExperimentFileReaderException
	{
		MainProgram mp = new MainProgram();
		mp.loadInstance(DIRECTORY_HC, "javacc", 154);
		mp.loadInstance(DIRECTORY_HC, "javaocr", 59);
		mp.loadInstance(DIRECTORY_HC, "jodamoney", 26);
		mp.loadInstance(DIRECTORY_HC, "jpassword", 96);
		mp.loadInstance(DIRECTORY_HC, "junit", 100);
		mp.loadInstance(DIRECTORY_HC, "jxlscore", 83);
		mp.loadInstance(DIRECTORY_HC, "jxlsreader", 27);
		mp.loadInstance(DIRECTORY_HC, "seemp", 31);
		mp.loadInstance(DIRECTORY_HC, "servlet", 63);
		mp.loadInstance(DIRECTORY_HC, "tinytim", 134);
		mp.loadInstance(DIRECTORY_HC, "xmldom", 119);
		mp.loadInstance(DIRECTORY_HC, "jmetal", 190);
		mp.loadInstance(DIRECTORY_HC, "dom4j", 195);
		mp.loadInstance(DIRECTORY_HC, "xmlapi", 184);
		//System.out.println(mp.rScript);
	}
}