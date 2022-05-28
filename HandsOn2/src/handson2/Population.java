/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handson2;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Luis
 */
public final class Population {
    int populationSize;
    Individual[] population = new Individual[populationSize];
    int[] probabilities = new int[population.length];
    int totalFitness;
    int generation=0;
    Population(int populationSize)
    {
        population= new Individual[populationSize];
        int[] probabilities = new int[populationSize];
        this.populationSize = populationSize;
        for(int i=0; i<populationSize; i++)
        {
            population[i] = new Individual();
        }
        selectGlobalFitness();
        getProbabilities();
    }
    void printPopulaton()
    {
        System.out.println("-------Gen: "+generation+" Global Fitness: "+totalFitness+"---------");
        //int i=0;
        for (Individual population1 : population) {
            population1.printIndividual();
            //System.out.println(probabilities[i]);
            //i++;
        }
        System.out.println("Parents: ");
        selectParent().printIndividual();
        selectParent().printIndividual();
    }
    int selectGlobalFitness()
    {
        totalFitness=0;
        for (int i=0; i<population.length; i++) {
            //System.out.println("totalfitness: "+totalFitness+"+=population "+population[i].getFitness());
            totalFitness += population[i].getFitness();
        }
        
        return totalFitness;
    }
    Individual selectParent()
    {
        Individual parent = new Individual();
            int random = ThreadLocalRandom.current().nextInt(0, probabilities[probabilities.length-1] + 1);
            for(int i=0; i<probabilities.length; i++)
            {
                if(random<=probabilities[i])
                {
                    parent=population[i];
                    break;
                }
            }
        return parent;
    }
    int[] getProbabilities()
    {
        int[] prob = new int[population.length];
        int q=0;
        int p;
        int sum = sumaFitnessGlobalFitness(population);
        for(int i= 0; i<population.length; i++)
        {
            p = ((totalFitness - population[i].getFitness())*100)/sum;
            q+=p;
            prob[i]=q;
        }
        probabilities= prob;
        return prob;
    }
    Individual[] produceNewPopulation(int crossoverRate, int mutationRate, int elitism)
    {
        Individual[] newPopulation = population;
        //int[] p = getProbabilities(fitness(population));
        for(int i=0; i<population.length; i++)
        {
            if(!population[i].elitism)
            {
                if(crossoverRate>ThreadLocalRandom.current().nextInt(0, 100 + 1))
                {
                    Individual secondParent = selectParent();
                    newPopulation[i].crossOver(secondParent, mutationRate);
                }
                if(25>ThreadLocalRandom.current().nextInt(0, 100 + 1))
                {
                    newPopulation[i].mutation();
                }
            }
            else
                population[i].setElitism(false);
        }
        generation++;
        population=newPopulation;
        selectGlobalFitness();
        getProbabilities();
        SelectElitism(elitism);
        return newPopulation;
        
    }
    void SelectElitism(int elitism)
    {
        for(int i=0; i<elitism; i++)
        {
            int lowest = 0;
            for(int j=0; j<population.length; j++)
            {
                if(population[j].getFitness()<population[lowest].getFitness())
                    if(!population[j].elitism)
                        lowest = j;
            }
            population[lowest].elitism=true;
        }
    }
        int sumaFitnessGlobalFitness(Individual[] vector)
    {
        int[] resta = new int[vector.length];
        int res = 0;
        for (int i=0; i<vector.length; i++) {
            resta[i] = totalFitness - vector[i].getFitness();
        }
        for( int i=0; i<resta.length; i++)
        {
            res=res+resta[i];
        }
        return res;
    }
    void print(float[] array)
    {
        for(double num:array)
        {
            System.out.println("Prob: "+num);
        }
    }
    Boolean checkOptimal(int error)
    {
        for(Individual i: population)
        {
            if(i.getFitness()<=error)
                return true;
        }
        return false;
    }
    Individual getOptimal(int error)
    {
        for(Individual i: population)
        {
            if(i.getFitness()<=error)
                return i;
        }
        return null;
    }
}
