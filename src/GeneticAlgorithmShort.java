import java.util.Random;

// Implementation of the JCell2o1i algorithm

public class GeneticAlgorithmShort {

	private static final int MAX_STEPS = 100;
	private static final int WIDTH = 10;
	private static final int HEIGHT = 10;
	
	private CVRPIndividual[][] individuals;
	private CVRPIndividual[][] newIndividuals;
	private CVRPIndividual[] torodialNeighbours;
	
	public static Random inputGenerator;
	
	GeneticAlgorithmShort(int seed)
	{
		individuals = new CVRPIndividual[WIDTH][HEIGHT];
		newIndividuals = new CVRPIndividual[WIDTH][HEIGHT];
		torodialNeighbours = new CVRPIndividual[5];
		inputGenerator = new Random(seed);
		buildSolutionSpace();
	}
	
    /** Create an array of all individuals in the solution landscape */
    private void buildSolutionSpace()
    {
		for(int x = 0; x < WIDTH; x++)
		{
			for(int y = 0; y < HEIGHT; y++)
			{
				individuals[x][y] = new CVRPIndividual();
			}	
		}
    }
    
    private CVRPIndividual[] getNeighbourhood(int x, int y)
    {
    	int right = (x+1) % WIDTH;
    	
    	int left = (x-1);    	
    	if (left < 0) left+= WIDTH;

    	int top = (y-1);
    	if (top<0) top+= HEIGHT;

    	int bottom = (y+1) % HEIGHT;
    	
    	CVRPIndividual[] neighbours = {individuals[left][y], individuals[x][top], individuals[right][y], individuals[x][bottom], individuals[x][y]}; 
    	return neighbours;
    }
    
    private CVRPIndividual bts(CVRPIndividual[] indiv)
    {
    	//TODO: put pos x,y as a fifth candidate
    	CVRPIndividual finalist1, finalist2, semifinalist;  	
    	semifinalist = (indiv[4].getFitness() > indiv[0].getFitness()) ? indiv[4] : indiv[0];
    	finalist1 = (indiv[1].getFitness() > semifinalist.getFitness()) ? indiv[1] : semifinalist;
    	finalist2 = (indiv[3].getFitness() > indiv[2].getFitness()) ? indiv[3] : indiv[2];
    	return (finalist1.getFitness() > finalist2.getFitness()) ? finalist1 : finalist2;
    	
    }
     
	public static void main(String[] args)
	{
		GeneticAlgorithm randomStart = new GeneticAlgorithm(Integer.parseInt(args[0]));
		double bestSoFar = 10000;
		GeneticAlgorithmShort ga = new GeneticAlgorithmShort(Integer.parseInt(args[0]));
		
		for (int steps = 0; steps < MAX_STEPS; steps++)
		{
			for(int x = 0; x < WIDTH; x++)
			{
				for(int y = 0; y < HEIGHT; y++)
				{
					int r = inputGenerator.nextInt(100);
					
					// Get parents
					ga.torodialNeighbours = ga.getNeighbourhood(x, y);
					CVRPIndividual parent1 = ga.bts(ga.torodialNeighbours);
//					CVRPIndividual parent1 = ga.rouletteWheel(ga.individuals);
					CVRPIndividual parent2 = ga.individuals[x][y];		
					
					// Recombination to give offspring
					CVRPIndividual offspring = new CVRPIndividual();			
					if(r<=65) offspring.solution = Utility.recombinationOperator(parent1, parent2);
					offspring.updateArrayOfRoutes();			
					
					// Mutation
					if(r<=85) Utility.mutationOperator(offspring);
					offspring.updateArrayOfRoutes();
									
					// Local search
//					Utility.localSearch(offspring);
//					offspring.updateArrayOfRoutes();
				
					double fitness = offspring.getFitness();
					double fitnessBefore = ga.individuals[x][y].getFitness();			
					
					if(fitness > fitnessBefore)
					{
						ga.newIndividuals[x][y]=offspring;
					}
					else
					{
						ga.newIndividuals[x][y] = parent2;
					}
					
					double newCost = ga.newIndividuals[x][y].totalCost();
					double capacity = ga.newIndividuals[x][y].overcap();			

					// For printing
					if(newCost<bestSoFar && capacity==0)
					{
						bestSoFar = newCost;
						System.out.println("generation:- " + steps);
						Utility.printCorrectSolution(ga.newIndividuals[x][y]);
						
					}
					
				}
			}	
			ga.individuals = ga.newIndividuals;
		}
		System.out.println("-----------------FINISHED---------------------");
	}
}
