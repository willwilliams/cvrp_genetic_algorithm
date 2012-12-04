import java.util.ArrayList;
import java.util.Random;

public class CVRPIndividual {
	
	public ArrayList<Integer> solution;
	public ArrayList<ArrayList<Integer>> arrayOfRoutes;
	
	// Max number of possible routes - length of route 
	public static final int K = 75;
	private static final int C = 10;

	private static final int LAMBDA = 1000;
	private static final int F_MAX = 1000000;
	
	// 75 nodes, 8 vehicles
	public static final int SOLUTION_LENGTH = K + C;
	public static final int ONE_OVER_L = 1/SOLUTION_LENGTH;
	
	CVRPIndividual()
	{
		solution = new ArrayList<Integer>();
		arrayOfRoutes = new ArrayList<ArrayList<Integer>>(); //!not good
		initialiseSolution();
		updateArrayOfRoutes();
	}
	
	CVRPIndividual(ArrayList<Integer> sln)
	{
		solution = sln;
		arrayOfRoutes = new ArrayList<ArrayList<Integer>>(); //!not good
		updateArrayOfRoutes();
	}
	
	public void initialiseSolution()
	{
		// Code to initialise to valid solutions
//		int capacity = 1;
//		while (capacity!=0)
//		{
//			solution = new ArrayList<Integer>();
//			for (int i = 0; i<SOLUTION_LENGTH; i++)
//				solution.add(i);
//			java.util.Collections.shuffle(solution);
//			updateArrayOfRoutes();
//			capacity = overcap();
//		}
		
		solution = new ArrayList<Integer>();
		for (int i = 0; i<SOLUTION_LENGTH; i++)
			solution.add(i);
		java.util.Collections.shuffle(solution, GeneticAlgorithm.inputGenerator);
	}
	
	/** Convert solution to array of routes */
	public void updateArrayOfRoutes()
	{
		arrayOfRoutes = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> individualRoute = new ArrayList<Integer>();
		for(int i=0; i<solution.size(); i++)
		{
			if(solution.get(i)<CVRPIndividual.K)
			{
				individualRoute.add(solution.get(i));
				if (solution.size()-1 == i)
				{
					arrayOfRoutes.add(individualRoute);
				}
			}
			else if(individualRoute.size()!=0)
			{
				arrayOfRoutes.add(individualRoute);
				individualRoute = new ArrayList<Integer>();				
			}
		}
	}
	
	/** Overhead of capacity for this individual */
	public int overcap()
	{
		int cap = 0, result = 0;
		for (int i=0; i<arrayOfRoutes.size(); i++)
		{
			for(int j=0; j<arrayOfRoutes.get(i).size(); j++)
			{
				cap += CVRPData.getDemand(arrayOfRoutes.get(i).get(j));
			}
			result += Math.max(cap-CVRPData.VEHICLE_CAPACITY,0);
			cap = 0;
		}
		return result;
	}
	
	/** Total cost for all routes - each route's cost is sum of euclidian dist
 	between each customer - F_VRP(S)*/
	public double totalCost()
	{
		double cost = 0;
		// Go through each route
		for (int i=0; i<arrayOfRoutes.size(); i++)
		{
			// Leaving depot
			cost += CVRPData.getDistance(-1, arrayOfRoutes.get(i).get(0));
			
			// Cost of intermediate steps
			int j;
			for(j=0; j<arrayOfRoutes.get(i).size()-1; j++)
			{
				cost += CVRPData.getDistance(arrayOfRoutes.get(i).get(j), arrayOfRoutes.get(i).get(j+1));
			}
			
			// Cost to return to depot
			cost += CVRPData.getDistance(-1, arrayOfRoutes.get(i).get(j));
		}
		return cost;
	}
	
	public double getFitness()
	{
		return F_MAX - (totalCost()+(overcap()*LAMBDA));
	}
	
	public void printSolution()
	{
		for(int i=0; i< solution.size(); i++)
		{
			System.out.print(solution.get(i) + ",");
		}
		System.out.println("\n");
	}
	
	public void printRoutes()
	{
		// Print routes
		for (int i=0; i<arrayOfRoutes.size(); i++)
		{
			//System.out.print("Route " + i + ": ");
			for(int j=0; j<arrayOfRoutes.get(i).size(); j++)
			{
				System.out.print(arrayOfRoutes.get(i).get(j));
				if (j<arrayOfRoutes.get(i).size()-1)
				{
					System.out.print("->");
				}
			}
			System.out.println("");
		}
	}
	
	public void printAll()
	{
		printSolution();
		System.out.println("cost\n" + totalCost());
		printRoutes();
		System.out.println("overcap\n" + overcap());
		System.out.println("fitness\n" + getFitness());
		
	}
	
	public static void main(String[] args)
	{
		CVRPIndividual test = new CVRPIndividual();
		test.printAll();
	}
	
}
