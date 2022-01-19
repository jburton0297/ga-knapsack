import java.util.HashMap;
import java.util.Random;

public class UAIndividual {

	private int geneCount;
	private byte[] genes;
	private double mutatePercent;
	private double fitness;
	
	public UAIndividual(int geneCount, double mutatePercent) {
		this.geneCount = geneCount;
		this.genes = new byte[geneCount];
		this.mutatePercent = mutatePercent;
		this.fitness = 0;
	}
	
	public void mutate() {
		boolean mutate = false;
		for(int i = 0; i < this.genes.length; i++) {
			mutate = new Random().nextInt(1000) <= mutatePercent * 1000;
			if(mutate) {
				if(this.genes[i] == 0) {
					this.genes[i] = 1;
				} else {
					this.genes[i] = 0;
				}
			}
		}
	}
	
	public static UAIndividual crossover(UAIndividual i1, UAIndividual i2) {
		if(i1.getGeneCount() != i2.getGeneCount())
			return null;
		
		//int crossoverPoint = new Random().nextInt(i1.getGeneCount());
		byte[] parent1Genes = i1.getGenes();
		byte[] parent2Genes = i2.getGenes();
		UAIndividual child = new UAIndividual(i1.getGeneCount(), i1.getMutatePercent());
		byte[] childGenes = child.getGenes();
		for(int i = 0; i < childGenes.length; i++) {
			if(new Random().nextInt(100) < 50) {
				childGenes[i] = parent1Genes[i];
			} else {
				childGenes[i] = parent2Genes[i];
			}
		}
		return child;
	}
	
	public int getItemCount() {
		int count = 0;
		for(int i = 0; i < this.genes.length; i++) {
			if(this.genes[i] == 1) {
				count++;
			}
		}
		return count;
	}
	
	public double getTotalValue(HashMap<Integer, UAItem> itemMap) {
		double totalValue = 0D;
		for(int i = 0; i < this.genes.length; i++) {
			if(this.genes[i] == 1) {
				totalValue += itemMap.get(i).getValue();
			}
		}
		return totalValue;
	}
	
	public int getGeneCount() {
		return geneCount;
	}

	public void setGeneCount(int geneCount) {
		this.geneCount = geneCount;
	}

	public byte[] getGenes() {
		return genes;
	}

	public void setGenes(byte[] genes) {
		this.genes = genes;
	}

	public double getMutatePercent() {
		return mutatePercent;
	}

	public void setMutatePercent(double mutatePercent) {
		this.mutatePercent = mutatePercent;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
}
