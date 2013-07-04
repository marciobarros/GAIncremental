package unirio.cluster.generic.controller;

import java.util.Vector;

import unirio.cluster.generic.model.Project;
import unirio.cluster.generic.model.ProjectClass;
import unirio.cluster.generic.model.ProjectPackage;

@SuppressWarnings("unused")
public class Publicador
{
	private static final int BARS = 50;
	
	private int getMaximumDependenciesClass(Project project)
	{
		int maximum = 0;
		
		for (int i = project.getClassCount()-1; i >= 0; i--)
		{
			ProjectClass _class = project.getClassIndex(i);
			maximum = Math.max(_class.getDependencyCount(), maximum);
		}
		
		return maximum;
	}
	
	private void printDependenciesHistogram(Project project)
	{
		int[] histogram = new int[BARS];
		
		for (int i = project.getClassCount()-1; i >= 0; i--)
		{
			ProjectClass _class = project.getClassIndex(i);
			int position = Math.min(_class.getDependencyCount(), BARS-1);
			histogram[position]++;
			//System.out.print(_class.getDependencyCount() + "\t");
		}
		
		for (int i = 0; i < BARS; i++)
			System.out.print(histogram[i] + "\t");

		System.out.println();
	}
	
	private void printCompositionHistogram(Project project)
	{
		int[] histogram = new int[project.getPackageCount()];
		
		for (int i = project.getClassCount()-1; i >= 0; i--)
		{
			ProjectPackage _package = project.getClassIndex(i).getPackage();
			int position = project.getIndexForPackage(_package);
			histogram[position]++;
		}
		
		for (int i = 0; i < histogram.length; i++)
			System.out.print(histogram[i] + "\t");

		System.out.println();
	}

	public void printProperties (Project project)
	{
		System.out.print(project.getName() + "\t");
		System.out.print(project.getClassCount() + "\t");
		System.out.print(project.getPackageCount() + "\t");
		System.out.print(project.getDependencyCount() + "\t");
		//printDependenciesHistogram(project);
		printCompositionHistogram(project);
	}

	public void printProperties (Vector<Project> projects)
	{
		System.out.println("Name\tClasses\tPackages\tDeps");

		for(Project p: projects)
			printProperties (p);
	}
}