import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JTextArea;

public class UAGeneticAlgorithm {

	static String inputFilePath;
	static int maxIterationCount;
	static int knapsackCapacity;
	static int populationCount;
	static double mutationPercent;
	static double minFitnessScore;
	public static HashMap<Integer, UAItem> itemMap;
	
	private UAWindow window;
	
	public void RunGeneticAlgorithm(String filepath, int maxItCount, int knapCapacity, int popCount, double mutPercent, double minFitScore, UAWindow window) {
	
		if(window != null) {
			clearWindow(window);
		}
		
		BufferedReader br = null;
		try {
		
			inputFilePath = filepath;
			maxIterationCount = maxItCount;
			knapsackCapacity = knapCapacity;
			populationCount = popCount;
			mutationPercent = mutPercent;
			minFitnessScore = minFitScore;
			
			// Get item list
			itemMap = new HashMap<>();
			br = new BufferedReader(new FileReader(inputFilePath));
			String line;
			int itemIndex = 0;
			while((line = br.readLine()) != null) {
				String[] values = line.split(",");
				UAItem item = new UAItem(
					Integer.parseInt(values[0]),
					Integer.parseInt(values[1])
				);
				itemMap.put(itemIndex++, item);
			}
			
			// Genetic algorithm
			GeneticAlgorithmResult result = GeneticAlgorithm(window);
		
			// Print results
			printResult(result, window);
			
		} catch(NumberFormatException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public static GeneticAlgorithmResult GeneticAlgorithm(UAWindow window) {
		UAPopulation population = new UAPopulation(0);
		for(int i = 0; i < populationCount; i++) {
			UAIndividual member = new UAIndividual(itemMap.size(), mutationPercent);
			byte[] genes = member.getGenes();
			for(int j = 0; j < genes.length; j++) {
				if(new Random().nextInt(100) < 50) {
					genes[j] = 0;
				} else {
					genes[j] = 1;
				}
			}
			population.addIndividual(member);
		}
		
		int iterationCount = 0;
		int maxSameFitnessCount = maxIterationCount / 10;
		int sameFitnessSteps = 0;
		UAIndividual bestIndividual = population.getBestIndividual(); 
		double currentMaxFitness = bestIndividual.getFitness();
		double previousMaxFitness = 0D;
		double bestval = 0D;
		do {
			
			// Iteration step
			for(int i = 0; i < populationCount; i++) {
				UAIndividual x = population.getRandomIndividual();
				UAIndividual y = population.getRandomIndividual();
				UAIndividual child = UAIndividual.crossover(x, y);
				child.mutate();
				population.addIndividual(child);
			}
			while(population.getMemberCount() > populationCount) {
				population.removeWorstIndividual();
			}
			bestIndividual = population.getBestIndividual();
			previousMaxFitness = currentMaxFitness;
			currentMaxFitness = bestIndividual.getFitness();
			bestval = bestIndividual.getTotalValue(itemMap);
			
			if(previousMaxFitness == currentMaxFitness) {
				sameFitnessSteps++;
			} else {
				sameFitnessSteps = 0;
			}
			
			double meanFitness = population.getMeanFitness();
			double sampleVariance = population.getSampleVariance();
			if(window == null) {
				System.out.printf("mean=%-6.2fvar=%-6.2f it=%d bestval=%.2f %n", meanFitness, sampleVariance, iterationCount, bestval);
			} else {
				printToWindow(window, String.format("mean=%-6.2fvar=%-6.2f it=%d%n", meanFitness, sampleVariance, iterationCount));
			}
			
			iterationCount++;
			
		} while(iterationCount < maxIterationCount && currentMaxFitness < minFitnessScore && sameFitnessSteps <= maxSameFitnessCount);
		
		double totalValue = 0D;
		int totalWeight = 0;
		byte[] bestGenes = bestIndividual.getGenes();
		for(int i = 0; i < bestGenes.length; i++) {
			if(bestGenes[i] == 1) {
				totalValue += itemMap.get(i).getValue();
				totalWeight += itemMap.get(i).getWeight();
			}
		}
		GeneticAlgorithmResult result = new GeneticAlgorithmResult(
			totalWeight,
			totalValue,
			bestGenes
		);
		return result;
	}
	
	public static double getFitnessOfIndividual(UAIndividual individual) {
		int totalWeight = 0;
		int totalValue = 0;
		byte[] genes = individual.getGenes();
		for(int i = 0; i < genes.length; i++) {
			if(genes[i] == 1) {
				UAItem item = itemMap.get(i);
				int itemWeight = item.getWeight();
				totalWeight += itemWeight;
				int itemValue = item.getValue();
				totalValue += itemValue; 
			}
		}
		if(totalWeight <= knapsackCapacity && totalWeight != 0) {
			return ((double)totalValue / (double)totalWeight) * individual.getItemCount(); //return (double)totalWeight; <- baseline
		} else {
			return 0D;
		}
	}
	
	public static void printResult(GeneticAlgorithmResult result, UAWindow window) {
		StringBuilder layoutSb = new StringBuilder();
		for(byte b : result.getLayout()) layoutSb.append(b);
		if(window == null) {
			System.out.printf("%-15s%-5d%n", "Total Weight:", result.getTotalWeight());
			System.out.printf("%-15s%-3.2f%n", "Total Value:", result.getTotalValue());
			System.out.printf("%-15s%-"+layoutSb.length()+"s", "Layout:", layoutSb.toString());
		} else {
			printToWindow(window, String.format("%-15s%-5d%n", "Total Weight:", result.getTotalWeight()));
			printToWindow(window, String.format("%-15s%-3.2f%n", "Total Value:", result.getTotalValue()));
			printToWindow(window, String.format("%-15s%-"+layoutSb.length()+"s", "Layout:", layoutSb.toString()));
		}
	}
	
	public static void printToWindow(UAWindow window, String text) {
		JTextArea resultsTextArea = window.getResultsTextArea();
		String oldText = resultsTextArea.getText();
		resultsTextArea.setText(oldText + text);
	}
	
	public static void clearWindow(UAWindow window) {
		JTextArea resultsTextArea = window.getResultsTextArea();
		resultsTextArea.setText("");
	}
	
}

class GeneticAlgorithmResult {
	
	private int totalWeight;
	private double totalValue;
	private byte[] layout;
	
	public GeneticAlgorithmResult(int totalWeight, double totalValue, byte[] layout) {
		this.totalWeight = totalWeight;
		this.totalValue = totalValue;
		this.layout = layout;
	}

	public int getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(int totalWeight) {
		this.totalWeight = totalWeight;
	}

	public double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

	public byte[] getLayout() {
		return layout;
	}

	public void setLayout(byte[] layout) {
		this.layout = layout;
	}
	
}

class UAItem {
	
	private int weight;
	private int value;
	
	public UAItem(int weight, int value) {
		this.weight = weight;
		this.value = value;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
