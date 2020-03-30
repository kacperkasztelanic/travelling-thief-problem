package ttp.model.params;

import java.util.Properties;

import lombok.Getter;

public class PropertyGeneticParamsProvider {

    public static GeneticParams forProperties(Properties props) {
        return GeneticParams.builder()//
                .populationSize(Integer.parseInt(props.getProperty(GeneticProperties.POPULATION_SIZE.getKey())))//
                .numberOfGenerations(
                        Integer.parseInt(props.getProperty(GeneticProperties.NUMBER_OF_GENERATIONS.getKey())))//
                .crossoverProbability(
                        Double.parseDouble(props.getProperty(GeneticProperties.CROSSOVER_PROBABILITY.getKey())))//
                .mutationProbability(
                        Double.parseDouble(props.getProperty(GeneticProperties.MUTATION_PROBABILITY.getKey())))//
                .tournamentSize(Double.parseDouble(props.getProperty(GeneticProperties.TOURNAMENT_SIZE.getKey())))//
                .build();
    }

    private PropertyGeneticParamsProvider() {
    }

    public enum GeneticProperties {

        POPULATION_SIZE("population_size"),//
        NUMBER_OF_GENERATIONS("number_of_generations"),//
        CROSSOVER_PROBABILITY("crossover_probability"),//
        MUTATION_PROBABILITY("mutation_probability"),//
        TOURNAMENT_SIZE("tournament_size");

        @Getter
        private final String key;

        GeneticProperties(String key) {
            this.key = key;
        }
    }
}
