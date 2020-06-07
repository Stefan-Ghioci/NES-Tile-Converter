package io.github.stefan_ghioci.ai;

public interface Individual
{
    double getFitness();

    void evaluate();

    void mutate();

    Object getSolution();
}
