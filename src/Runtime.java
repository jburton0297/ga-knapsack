
public class Runtime {

	public static void main(String[] args) {
		try {
			if(args.length < 1) {
				throw new Exception("Invalid arguments");
			} else {
				if(args[0].toLowerCase().equals("console")) {
					if(!args[1].matches("^.*\\.txt$")) {
						throw new Exception("Invalid input file path");
					}
					String inputFilePath = args[1];
					int maxIterationCount = Integer.parseInt(args[2]);
					int knapsackCapacity = Integer.parseInt(args[3]);
					int populationCount = Integer.parseInt(args[4]);
					double mutationPercent = Double.parseDouble(args[5]);
					double minFitnessScore = Double.parseDouble(args[6]);
					UAGeneticAlgorithm GA = new UAGeneticAlgorithm();
					GA.RunGeneticAlgorithm(inputFilePath, maxIterationCount, knapsackCapacity, populationCount, mutationPercent, minFitnessScore, null);
				} else if(args[0].toLowerCase().equals("gui")) {
					UAWindow window = new UAWindow();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
