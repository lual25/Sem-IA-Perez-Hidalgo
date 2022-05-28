/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handson3;
import jade.core.Agent;
import java.util.concurrent.ThreadLocalRandom;
import jade.core.behaviours.OneShotBehaviour;
/**
 *
 * @author Luis
 */
public class HandsOn3 extends Agent{
    public void setup()
    {
        addBehaviour(new MyOneShotBehaviour());
    }
    private class MyOneShotBehaviour extends OneShotBehaviour {

    public void action() {
        geneticAlgorithm(15, 40, 30, 3, 10);
    } 
    
    public int onEnd() {
      myAgent.doDelete();   
      return super.onEnd();
    } 
  }
    void geneticAlgorithm(int populationsize, int crossoverRate, int mutationRate, int elitism, int error)
    {
        Population p = new Population(populationsize);
        p.printPopulaton();
        while(!p.checkOptimal(error))
        {
            p.produceNewPopulation(crossoverRate, mutationRate, elitism);
            p.printPopulaton();
        }
        System.out.println("Optimal = " + p.getOptimal(error).individual);

    }
    
    public final class Population {
    int populationSize;
    Individual[] population = new Individual[populationSize];
    int[] probabilities = new int[population.length];
    int totalFitness;
    int generation=0;
    Population(int populationSize)
    {
        population= new Individual[populationSize];
        probabilities = new int[populationSize];
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
        System.out.println("Gen: "+generation+" Global Fitness: "+totalFitness);
        //int i=0;
        for (Individual population1 : population) {
            population1.printIndividual();
            //System.out.println(probabilities[i]);
            //i++;
        }
        selectParent().printIndividual();
    }
    int selectGlobalFitness()
    {
        totalFitness=0;
        for (Individual population1 : population) {
            totalFitness += population1.getFitness();
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
        for(int i= 0; i<population.length; i++)
        {
            p = (population[i].getFitness()*100)/selectGlobalFitness();
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
                    newPopulation[i].crossOver(secondParent);
                }
                if(20>ThreadLocalRandom.current().nextInt(0, 100 + 1))
                {
                    newPopulation[i].mutation(25);
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
            int biggest = 0;
            for(int j=0; j<population.length; j++)
            {
                if(population[j].getFitness()>population[biggest].getFitness())
                    if(!population[j].elitism)
                        biggest = j;
            }
            population[biggest].elitism=true;
        }
    }
    Boolean checkOptimal(int error)
    {
        for(Individual i: population)
        {
            if(i.getFitness()>=error)
                return true;
        }
        return false;
    }
    Individual getOptimal(int error)
    {
        for(Individual i: population)
        {
            if(i.getFitness()>=error)
                return i;
        }
        return null;
    }
   public final class Individual {
    String individual=randomGen();
    int fitness;
    boolean elitism = false;
    Individual()
    {
        individual=randomGen();
        selectFitness();
    }
    String getIndividual()
    {
        return individual;
    }
    String randomGen()
    {
        String object = "";
        for(int i=0; i<10; i++)
        {
            object+= String.valueOf(ThreadLocalRandom.current().nextInt(0, 1 + 1)); 
        }
        return object;
    }
    void printIndividual()
    {
        System.out.println(individual+" Fitness: "+fitness);
    }
    int selectFitness()
    {
        fitness = 0;
            for(int j=0; j<individual.length(); j++)
                if(individual.charAt(j) == '1')
                {
                    fitness++;
                }
            return fitness;
    }
    int getFitness()
    {
        return fitness;
    }
    String crossOver(Individual parent)
    {
        String children;
        int crossoverpoint = ThreadLocalRandom.current().nextInt(0, this.getIndividual().length() + 1);
        children = parent.getIndividual().substring(0, crossoverpoint) + this.getIndividual().substring(crossoverpoint);
        individual = children;
        selectFitness();
        return children;
    }
    String mutation(int mutationRate)
    {
        StringBuilder aux = new StringBuilder(individual);
        for(int i=0; i<aux.length(); i++)
        {
            if(mutationRate<=ThreadLocalRandom.current().nextInt(0, 100 + 1))
            {
                if(aux.charAt(i)=='0')
                {
                    aux.setCharAt(i, '1');
                }
                else
                    aux.setCharAt(i, '0');

            }
        }
        individual = aux.toString();
        return aux.toString();
    }
    void setElitism(boolean bool)
    {
        elitism = bool;
    }
}
    }
}
    
    
