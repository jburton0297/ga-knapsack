import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public class UAPopulation {

	private int id;
	private int memberCount;
	private TreeSet<UAIndividual> members;
	
	public UAPopulation(int id) {
		this.id = id;
		this.memberCount = 0;
		this.members = new TreeSet<UAIndividual>(new Comparator<UAIndividual>() {

			@Override
			public int compare(UAIndividual o1, UAIndividual o2) {
				if(o1.getFitness() < o2.getFitness()) {
					return -1;
				} else if(o1.getFitness() > o2.getFitness()) {
					return 1;
				} else {
					byte[] o1Genes = o1.getGenes();
					byte[] o2Genes = o2.getGenes();
					boolean equal = true;
					for(int i = 0; i < o1Genes.length; i++) {
						if(o1Genes[i] != o2Genes[i]) {
							equal = false;
							break;
						}
					}
					if(equal) {
						return 0;
					} else {
						int o1Count = o1.getItemCount();
						int o2Count = o2.getItemCount();
						if(o1Count > o2Count) {
							return 1;
						} else {
							return -1;
						}
					}
				}
//				if(equal) {
//					return 0;
//				} else {
//					if(o1.getFitness() < o2.getFitness()) {
//						return 1;
//					} else {
//						return -1;
//					}
//				}
			}
			
		});
	}
	
	public UAIndividual getBestIndividual() {
		return this.members.last();
	}
	
	public UAIndividual getRandomIndividual() {
		List<UAIndividual> membersList = new ArrayList<>(this.members);
		int randomIndex = new Random().nextInt(this.members.size());
		UAIndividual randomMember = membersList.get(randomIndex);
		return randomMember;
	}
	
	public void addIndividual(UAIndividual individual) {
		double fitness = UAGeneticAlgorithm.getFitnessOfIndividual(individual);
		individual.setFitness(fitness);
		if(this.members.add(individual))
			this.memberCount++;
	}
	
	public void removeWorstIndividual() {
		UAIndividual member = this.members.first();
		if(this.members.remove(member))
			this.memberCount--;
	}
	
	public double getMeanFitness() {
		double sum = 0;
		for(UAIndividual member : this.members) {
			sum += member.getFitness();
		} 
		return sum / (double)this.members.size();
	}
	
	public double getSampleVariance() {
		double mean = this.getMeanFitness();
		int var = 0;
		for(UAIndividual member : this.members) {
			var += Math.pow(member.getFitness() - mean, 2) / (this.members.size() - 1);
		}
		return var;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	public TreeSet<UAIndividual> getMembers() {
		return members;
	}

	public void setMembers(TreeSet<UAIndividual> members) {
		this.members = members;
	}
	
}
