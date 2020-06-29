package io.github.stefan_ghioci.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.github.stefan_ghioci.tools.Miscellaneous.formatDouble;
import static io.github.stefan_ghioci.tools.Miscellaneous.getRandomElement;

public abstract class EvolutionaryAlgorithm
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EvolutionaryAlgorithm.class.getSimpleName());

    private Individual lastBest;

    public void run(int populationSize, int stagnationFactor, double mutationChance)
    {
        LOGGER.info("Running steady-state evolutionary algorithm with {} individuals and {} stagnation factor",
                    populationSize,
                    stagnationFactor);

        List<Individual> population = generatePopulation(populationSize);

        int iterationCounter = 0;
        int stagnationTime = 0;

        population.forEach(Individual::evaluate);

        setLastBest(best(population));
        LOGGER.info("Initial best fitness: {}", formatDouble(lastBest.getFitness()));

        while (stagnationTime < stagnationFactor || lastBest.getFitness() == 0)
        {
            Individual mother = select(population);
            Individual father = select(population);

            Individual offspring = crossover(mother, father);

            if (Math.random() < mutationChance)
                offspring.mutate();

            offspring.evaluate();

            Individual worst = worst(population);

            if (offspring.getFitness() < worst.getFitness())
            {
                population.remove(worst);
                population.add(offspring);
            }


            Individual newBest = best(population);

            double improvement = calculateImprovement(newBest);

            if (improvement != 0)
            {
                LOGGER.info("Iteration {}, best fitness {} with {}% improvement",
                            iterationCounter,
                            formatDouble(newBest.getFitness()),
                            formatDouble(improvement));
                setLastBest(newBest);
                stagnationTime = 1;
            }
            else
            {
                stagnationTime++;
            }

            iterationCounter++;
        }

        LOGGER.info("Algorithm ran for {} iterations, last best fitness {}",
                    iterationCounter,
                    formatDouble(lastBest.getFitness()));
    }

    private double calculateImprovement(Individual newBest)
    {
        return ((lastBest.getFitness() - newBest.getFitness()) / lastBest.getFitness() * 100);
    }


    private Individual worst(List<Individual> population)
    {
        return population.stream()
                         .max(Comparator.comparingDouble(Individual::getFitness))
                         .orElse(population.get(0));
    }

    private List<Individual> generatePopulation(int populationSize)
    {
        return IntStream.range(0, populationSize)
                        .mapToObj(i -> generateIndividual())
                        .collect(Collectors.toList());
    }


    private Individual best(List<Individual> population)
    {
        return population.stream()
                         .min(Comparator.comparingDouble(Individual::getFitness))
                         .orElse(population.get(0));
    }

    protected abstract Individual generateIndividual();

    protected Individual select(List<Individual> population)
    {
//        int populationThreshold = (int) (population.size() * 0.75);
//        return getRandomElement(population.stream()
//                                          .sorted(Comparator.comparingDouble(Individual::getFitness).reversed())
//                                          .limit(populationThreshold)
//                                          .collect(Collectors.toList()));
        return getRandomElement(population);
    }

    protected abstract Individual crossover(Individual mother, Individual father);

    public void setLastBest(Individual lastBest)
    {
        this.lastBest = lastBest;
    }
}
