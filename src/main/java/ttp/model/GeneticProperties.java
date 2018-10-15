package ttp.model;

import lombok.Getter;

public enum GeneticProperties {

    POPULATION_SIZE("population_size"),
    NUMBER_OF_GENERATIONS("number_of_generations"),
    CROSSOVER_PROBABILITY("crossover_probability"),
    MUTATION_PROBABILITY("mutation_probability"),
    TOURNAMENT_PARTICIPATION("tournament_participation");
    
    @Getter
    private final String key;
    
    private GeneticProperties(String key) {
        this.key = key;
    }
}
