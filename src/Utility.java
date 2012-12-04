import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Convenience and utilities for calculating 
// intermediate results in the CVRP

public class Utility
{
	//public static Random inputGenerator = new Random(777);

	
	public static ArrayList<Integer> getPool(int length)
	{
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		for (int i = 0; i<length; i++)
			tempList.add(i);
		java.util.Collections.shuffle(tempList, GeneticAlgorithm.inputGenerator);
		return tempList;
	}
	
    //-----------------------------------------------------------------
	
    //////////////////////////
    // COMBINATION OPERATOR //
    //////////////////////////
	
    //-----------------------------------------------------------------
	
	/** ERX - edge recombination operator
	 *  [builds an offspring by preserving edges from both parents]
	 */
    public static ArrayList<Integer> recombinationOperator(CVRPIndividual parent1, CVRPIndividual parent2)
    {
    	List<Integer>[] nList = getNeighbourLists(parent1, parent2);
    	ArrayList<Integer> K = new ArrayList<Integer>();
    	Integer N_star;
    	if(GeneticAlgorithm.inputGenerator.nextBoolean())
    		N_star = parent1.solution.get(0);
    	else
    		N_star = parent2.solution.get(0);
    	
    	
    	Integer N = N_star;
    	ArrayList<Integer> tempList = new ArrayList<Integer>(parent1.solution);
		tempList.remove(N);

    	while (K.size() < parent1.solution.size())
    	{
    		K.add(N);

    		nList = removeFromNeighbourLists(nList, N);
    		
    		if (nList[N].size()>0)
    		{
    			N_star = findSmallestNeighbour(nList, N);
    			tempList.remove(N_star);
    		}
    		else if (K.size()<CVRPIndividual.SOLUTION_LENGTH)
    		{
    			//N_star = tempList.remove(0);
    			N_star = tempList.remove(GeneticAlgorithm.inputGenerator.nextInt((tempList.size())));
    		}
    		N = N_star;  	   	
    	}
    	return K;
    }
    
    /** ERX - given adjacency list neighbour in N's slot with smallest list**/
    public static Integer findSmallestNeighbour(List<Integer>[] nList, Integer N)
    {
     	Integer smallestVal = nList[nList[N].get(0)].size();
     	ArrayList<Integer> candidates = new ArrayList<Integer>();
     	
    	for (int i = 0; i< nList[N.intValue()].size(); i++)
    	{
    		if (nList[nList[N].get(i)].size() < smallestVal)
    		{
    			smallestVal = nList[nList[N].get(i)].size();
    			candidates = new ArrayList<Integer>();
    			candidates.add(nList[N].get(i));
    		}
    		else if (nList[nList[N].get(i)].size() == smallestVal)
    		{
    			candidates.add(nList[N].get(i));
    		}
    	}
    	int resultPos = GeneticAlgorithm.inputGenerator.nextInt(candidates.size());
    	return candidates.get(resultPos);
    }

    /** ERX - given adjacency list remove all occurrences of N **/
    public static List<Integer>[] removeFromNeighbourLists(List<Integer>[] nList, Integer N)
    {
    	for (int i = 0; i<CVRPIndividual.SOLUTION_LENGTH; i++)
    	{
    		if (nList[i].contains(N)) nList[i].remove(N);
       	}
    	return nList;
    }
    
    /** ERX - generate adjacency list given two individuals **/
    public static List<Integer>[] getNeighbourLists(CVRPIndividual parent1, CVRPIndividual parent2)
    {
    	@SuppressWarnings("unchecked")
		List<Integer>[] nList = new ArrayList[CVRPIndividual.SOLUTION_LENGTH];
    	ArrayList<Integer> temp;
    	
    	// Add parent1 details to adj matrix
    	for (int i=0; i<CVRPIndividual.SOLUTION_LENGTH; i++)
    	{
    		int currentItem = parent1.solution.get(i);
    		temp = new ArrayList<Integer>();
    		int previous = i-1;
    		if(previous<0) previous += CVRPIndividual.SOLUTION_LENGTH;
    		int next = (i+1) % CVRPIndividual.SOLUTION_LENGTH;
    		int previousItem = parent1.solution.get(previous);
    		int nextItem = parent1.solution.get(next);
    		
			temp.add(previousItem);
			temp.add(nextItem);
    		nList[currentItem] = temp;
    	}
    	
    	// Add parent2 details to adj matrix
    	for (int i=0; i<CVRPIndividual.SOLUTION_LENGTH; i++)
    	{
    		int currentItem = parent2.solution.get(i);
    		temp = new ArrayList<Integer>();
    		int previous = i-1;
    		if(previous<0) previous += CVRPIndividual.SOLUTION_LENGTH;
    		int next = (i+1) % CVRPIndividual.SOLUTION_LENGTH;
    		Integer previousItem = parent2.solution.get(previous);
    		Integer nextItem = parent2.solution.get(next);
    		
   			if(!nList[currentItem].contains(previousItem))
    			nList[currentItem].add(previousItem);     		

   			if(!nList[currentItem].contains(nextItem))
    			nList[currentItem].add(nextItem);    	
    	}
    	return nList;
	}
    
    //-----------------------------------------------------------------

    ////////////////////////
    // MUTATION OPERATORS //
    ////////////////////////    

    //-----------------------------------------------------------------
    
    /** Top level mutation controls which operator is chosen with prob 1/3
     * and mutates each allele with prob (1/lenth of chromosome).
     */
    public static void mutationOperator(CVRPIndividual indiv)
    {
    	for (int i=0; i<CVRPIndividual.SOLUTION_LENGTH; i++)
    	{
    		if(GeneticAlgorithm.inputGenerator.nextInt(CVRPIndividual.SOLUTION_LENGTH)==57)
    		{
		    	int h = GeneticAlgorithm.inputGenerator.nextInt(3);
		    	if (h==0)
		    		insertion(indiv,i);
		    	else if (h==1)
		    		swap(indiv,i);
		    	else
		    		inversion(indiv,i);
    		}
    	}
    }
      
    /** Selects a customer and inserts it in another randomly selected place **/
    public static void insertion(CVRPIndividual mutator, int i)
    {
		Integer val = mutator.solution.remove(GeneticAlgorithm.inputGenerator.nextInt(mutator.solution.size()));
		mutator.solution.add(GeneticAlgorithm.inputGenerator.nextInt(mutator.solution.size()), val);
    }
    
    /** Randomly selects two customers and exchanges them **/
    public static void swap(CVRPIndividual mutator, int i)
    {
        Collections.swap(mutator.solution,i,GeneticAlgorithm.inputGenerator.nextInt(mutator.solution.size()));
    }
    
    /** Reverses the visiting order of the customers
     *  between two randomly selected cut points
     */
    public static void inversion(CVRPIndividual mutator, int i)
    {
    	int cut1 = i;
    	int cut2 = GeneticAlgorithm.inputGenerator.nextInt(mutator.solution.size());
    	int temp;
    	List<Integer> cutList = new ArrayList<Integer>();
    	
    	if (cut1>cut2) {temp = cut1;cut1 = cut2;cut2 = temp;}

    	for (int j = 0; j<=(cut2-cut1); j++)
    		cutList.add(mutator.solution.remove(cut1));
	
    	Collections.reverse(cutList);
    	mutator.solution.addAll(cut1, cutList);
    }
      
    //-----------------------------------------------------------------

    //////////////////
    // LOCAL SEARCH //
    //////////////////    

    //-----------------------------------------------------------------
    
    /** 2OPT helper function to perform the edge switching */
    public static void search2OPT(CVRPIndividual mutator, int i1, int i2, int routeNum)
    {
    	if(i2<i1)
    	{
    		int temp = i1;
    		i1 = i2;
    		i2 = temp;
    	}
    	
    	int cut1 = mutator.solution.indexOf(mutator.arrayOfRoutes.get(routeNum).get(i1));
    	int cut2 = mutator.solution.indexOf(mutator.arrayOfRoutes.get(routeNum).get(i2));
    	
    	
    	List<Integer> cutList = new ArrayList<Integer>();
    	for (int j = 0; j<=(cut2-cut1); j++)
    		cutList.add(mutator.solution.remove(cut1));
	
    	Collections.reverse(cutList);
    	mutator.solution.addAll(cut1, cutList);
    }
    
    /** Top level local search routines */
    public static CVRPIndividual localSearch(CVRPIndividual indiv)
    {
    	double startingFitness;
    	ArrayList<Integer> currentBest = new ArrayList<Integer>(indiv.solution);
    	
    	// 2-OPT (swap people intra each route)
    	for (int i=0; i<indiv.arrayOfRoutes.size(); i++)
    	{
    		startingFitness = indiv.getFitness();
        	ArrayList<Integer> ali = indiv.arrayOfRoutes.get(i);
        	int sizeT = ali.size();
        	
        	//TODO: reduce time complexity from O(n^2)
        	for(int j=0;j<sizeT; j++)
        	{
        		for(int k=0; k<sizeT; k++)
        		{
        			if(j<k)
        			{
        				search2OPT(indiv, j, k, i);
        	        	indiv.updateArrayOfRoutes();
        	        	if(indiv.getFitness() > startingFitness)
        	        	{
        	        		currentBest = new ArrayList<Integer>(indiv.solution);
        	        	}
        	        	else
        	        	{
        	        		indiv.solution = new ArrayList<Integer>(currentBest);
        	        	}
        				
        			}
        		}
        	}

        	indiv.updateArrayOfRoutes();
        	// Check if worse
    	}
    	  	
    	// 1 LAMBDA-INTERCHANGE (swap people inter-routes)
    	// select 2 random routes
    	if(indiv.arrayOfRoutes.size()>1)
    	{
	    	int randomRoute1 = GeneticAlgorithm.inputGenerator.nextInt(indiv.arrayOfRoutes.size());
	    	int randomRoute2 = GeneticAlgorithm.inputGenerator.nextInt(indiv.arrayOfRoutes.size());
	    	while (randomRoute1 == randomRoute2)
	    	{
	    		randomRoute2 = GeneticAlgorithm.inputGenerator.nextInt(indiv.arrayOfRoutes.size());
	    	}
	       	int sizeRR1 = indiv.arrayOfRoutes.get(randomRoute1).size();
	       	int sizeRR2 = indiv.arrayOfRoutes.get(randomRoute2).size();
	
	       	// select 2 random individuals from their own routes
	    	int point1 = GeneticAlgorithm.inputGenerator.nextInt(sizeRR1);
	    	int point2 = GeneticAlgorithm.inputGenerator.nextInt(sizeRR2);
	    	
	    	// swap them
	    	Integer candidate1 = indiv.arrayOfRoutes.get(randomRoute1).get(point1);
	    	Integer candidate2 = indiv.arrayOfRoutes.get(randomRoute2).get(point2);
	    	
    		int p1 = indiv.solution.indexOf(candidate1);
    		int p2 = indiv.solution.indexOf(candidate2);
	    	
	    	indiv.solution.set(p1, candidate2);
	    	indiv.solution.set(p2, candidate1);  		
    	}
    	return indiv;
    }
    
    // -----------------------------------------------------------
    
    /////////////////
    //    TESTS    //
    /////////////////    
    
    // -----------------------------------------------------------
    
    public static void testRecombination()
    {
    	System.out.println("Testing recombination --------------------");
		
    	CVRPIndividual test1 = new CVRPIndividual();
		test1.printSolution();		
		
		CVRPIndividual test2 = new CVRPIndividual();
		test2.printSolution();
		
		CVRPIndividual result = new CVRPIndividual();
		result.solution = Utility.recombinationOperator(test1, test2);
		result.printSolution();
		Collections.sort(result.solution);
		result.printSolution();
    }
    
    public static void testInversion()
    {
    	System.out.println("Testing inversion --------------------");
		CVRPIndividual test1 = new CVRPIndividual();
		test1.printSolution();
		
		
		for (int i=0; i<CVRPIndividual.SOLUTION_LENGTH; i++)
    	{
    		if(GeneticAlgorithm.inputGenerator.nextInt(CVRPIndividual.SOLUTION_LENGTH)==57)
    		{
    			System.out.println("allel mutating");
	    		inversion(test1,i);
    		}
    	}
		
		test1.printSolution();
		Collections.sort(test1.solution);
		test1.printSolution();
    }
    
    public static void testSwap()
    {
    	System.out.println("Testing swap --------------------");
		CVRPIndividual test1 = new CVRPIndividual();
		test1.printSolution();
		
		for (int i=0; i<CVRPIndividual.SOLUTION_LENGTH; i++)
    	{
    		if(GeneticAlgorithm.inputGenerator.nextInt(CVRPIndividual.SOLUTION_LENGTH)==57)
    		{
    			System.out.println("allel mutating");
	    		swap(test1,i);
    		}
    	}
		
		test1.printSolution();
		Collections.sort(test1.solution);
		test1.printSolution();
    }
    
    public static void testInsertion()
    {
    	System.out.println("Testing insertion --------------------");
		CVRPIndividual test1 = new CVRPIndividual();
		test1.printSolution();
		
		
		for (int i=0; i<CVRPIndividual.SOLUTION_LENGTH; i++)
    	{
    		if(GeneticAlgorithm.inputGenerator.nextDouble()<=CVRPIndividual.ONE_OVER_L)
    		{
    			System.out.println("allel mutating");
	    		insertion(test1,i);
    		}
    	}
		
		test1.printSolution();
		Collections.sort(test1.solution);
		test1.printSolution();
    }
    
    public static void testLocalSearch()
    {
    	System.out.println("Testing local search --------------------");
//		CVRPIndividual test1 = new CVRPIndividual();
//		test1.printSolution();
		
		CVRPIndividual test1 = new CVRPIndividual();
		test1.solution = new ArrayList<Integer>();
		test1.solution.add(0);
		test1.solution.add(1);
		test1.solution.add(2);
		test1.solution.add(3);
		test1.solution.add(4);
		test1.solution.add(5);
		test1.solution.add(6);
		test1.solution.add(7);
		test1.solution.add(81);
		test1.solution.add(8);
		test1.solution.add(9);
		test1.solution.add(10);
		test1.solution.add(11);
		test1.solution.add(12);
		test1.solution.add(13);
		test1.solution.add(14);
		test1.solution.add(15);
		test1.printSolution();
		
		test1.updateArrayOfRoutes();
		
		Utility.localSearch(test1);
		test1.printSolution();
		Collections.sort(test1.solution);
		test1.printSolution();
    }
    
    public static void printCorrectSolution(CVRPIndividual me)
    {
    	System.out.println("login ww8655");
		System.out.println("cost " + me.totalCost());

		// Print routes
		for (int i=0; i<me.arrayOfRoutes.size(); i++)
		{
			System.out.print("1->");
			for(int j=0; j<me.arrayOfRoutes.get(i).size(); j++)
			{
				System.out.print(me.arrayOfRoutes.get(i).get(j)+2);
				if (j<me.arrayOfRoutes.get(i).size()-1)
				{
					System.out.print("->");
				}
			}
			System.out.print("->1");
			System.out.println("");
		}
    }
    
    
	public static void main(String[] args)
	{
		GeneticAlgorithm ga = new GeneticAlgorithm(27);
//		Utility.testRecombination();
//		Utility.testInversion();
//		Utility.testSwap();
//		Utility.testInsertion();
		Utility.testLocalSearch();
	}
}
