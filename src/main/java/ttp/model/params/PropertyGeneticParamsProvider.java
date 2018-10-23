package ttp.model.params;

import java.util.Properties;

import lombok.Getter;
import ttp.model.params.GeneticParams.GeneticParamsBuilder;

public class PropertyGeneticParamsProvider {

    public static GeneticParams forProperties(Properties props) {
        GeneticParamsBuilder params = GeneticParams.builder();
        params.populationSize(Integer.parseInt(props.getProperty(GeneticProperties.POPULATION_SIZE.getKey())));
        params.numberOfGenerations(
                Integer.parseInt(props.getProperty(GeneticProperties.NUMBER_OF_GENERATIONS.getKey())));
        params.crossoverProbability(
                Double.parseDouble(props.getProperty(GeneticProperties.CROSSOVER_PROBABILITY.getKey())));
        params.mutationProbability(
                Double.parseDouble(props.getProperty(GeneticProperties.MUTATION_PROBABILITY.getKey())));
        params.tournamentSize(
                Double.parseDouble(props.getProperty(GeneticProperties.TOURNAMENT_SIZE.getKey())));
        return params.build();
    }

    private PropertyGeneticParamsProvider() {
    }

    public enum GeneticProperties {

        POPULATION_SIZE("population_size"), 
        NUMBER_OF_GENERATIONS("number_of_generations"), 
        CROSSOVER_PROBABILITY("crossover_probability"), 
        MUTATION_PROBABILITY("mutation_probability"), 
        TOURNAMENT_SIZE("tournament_size");

        @Getter
        private final String key;

        private GeneticProperties(String key) {
            this.key = key;
        }
    }
}
