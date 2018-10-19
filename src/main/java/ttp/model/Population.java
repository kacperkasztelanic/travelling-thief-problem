package ttp.model;

import java.util.Comparator;
import java.util.Random;

import lombok.Getter;
import lombok.ToString;
import ttp.algorithm.FitnessFunction;
import ttp.algorithm.KnapsackSolver;
import ttp.model.wrapper.ProblemInfo;
import ttp.utils.ArrayUtils;

@ToString(includeFieldNames = false, of = { "members" })
public class Population {

    private static final Random random = new Random();

    @Getter
    private final Individual[] members;

    private final GeneticParams geneticParams;
    private final ProblemInfo problemInfo;
    private final KnapsackSolver knapsackSolver;
    private final FitnessFunction fittnessFunction;

    public static Population of(Individual[] members, GeneticParams geneticParams, ProblemInfo problemInfo,
            KnapsackSolver knapsackSolver, FitnessFunction fittnessFunction) {
        return new Population(members, geneticParams, problemInfo, knapsackSolver, fittnessFunction);
    }

    private Population(Individual[] members, GeneticParams geneticParams, ProblemInfo problemInfo,
            KnapsackSolver knapsackSolver, FitnessFunction fittnessFunction) {
        this.members = members;
        this.geneticParams = geneticParams;
        this.problemInfo = problemInfo;
        this.knapsackSolver = knapsackSolver;
        this.fittnessFunction = fittnessFunction;
    }

    public static Population randomPopulation(GeneticParams geneticParams, ProblemInfo problemInfo,
            KnapsackSolver knapsackSolver, FitnessFunction fittnessFunction) {
        int size = geneticParams.getPopulationSize();
        int[] nodes = problemInfo.getProblem().getNodes().stream().mapToInt(Node::getId).toArray();
        Individual[] members = new Individual[size];
        for (int i = 0; i < size; i++) {
            members[i] = Individual.of(ArrayUtils.shuffledCopy(nodes), problemInfo, knapsackSolver, fittnessFunction);
        }
        return Population.of(members, geneticParams, problemInfo, knapsackSolver, fittnessFunction);
    }

    public Population nextGeneration() {
        int numberOfChildren = (int) (geneticParams.getCrossoverProbability() * geneticParams.getPopulationSize());
        Individual[] children = crossover(numberOfChildren);
        Individual[] clonedParents = cloneParentsIntoChildren(geneticParams.getPopulationSize() - numberOfChildren);
        Individual[] newMembers = new Individual[geneticParams.getPopulationSize()];
        System.arraycopy(children, 0, newMembers, 0, children.length);
        System.arraycopy(clonedParents, 0, newMembers, children.length, clonedParents.length);
        Population newPopulation = Population.of(newMembers, geneticParams, problemInfo, knapsackSolver,
                fittnessFunction);
        newPopulation.invokeMutations();
        return newPopulation;
    }

    public Individual[] crossover(int numberOfChildren) {
        Individual[] children = new Individual[numberOfChildren];
        for (int i = 0; i < numberOfChildren; i++) {
            Individual[] parents = select(2);
            Individual child = parents[0].crossover(parents[1]);
            children[i] = child;
        }
        return children;
    }

    public Individual[] cloneParentsIntoChildren(int numberOfParentsToClone) {
        Individual[] clones = new Individual[numberOfParentsToClone];
        Individual[] selected = select(numberOfParentsToClone);
        for (int i = 0; i < numberOfParentsToClone; i++) {
            clones[i] = Individual.of(selected[i]);
        }
        return clones;
    }

    private Individual[] select(int n) {
        return random.ints((long) (geneticParams.getTournamentSize() * members.length * n), 0, members.length)
                .mapToObj(i -> members[i])
                .sorted(Comparator.comparingDouble((Individual i) -> i.getResult().getValue()).reversed()).limit(n)
                .toArray(Individual[]::new);
    }

    public void invokeMutations() {
        for (int i = 0; i < members.length; i++) {
            members[i] = members[i].mutate(geneticParams.getMutationProbability());
        }
    }
}
