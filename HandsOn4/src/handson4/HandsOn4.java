/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handson4;
import jade.core.Agent;
import java.util.concurrent.ThreadLocalRandom;
import jade.core.behaviours.OneShotBehaviour;
/**
 *
 * @author Luis
 */
public class HandsOn4 extends Agent{
    public void setup()
    {
        addBehaviour(new MyOneShotBehaviour());
    }
    private class MyOneShotBehaviour extends OneShotBehaviour {

    public void action() {
        int[][] info = {{23, 651}, {26, 762}, {30, 856},{34, 1063},{43, 1190},{48, 1298},{52, 1421},{57, 1440},{58, 1518}};
        geneticAlgorithm(15, 40, 30, 3, 30, info);

    } 
    
    public int onEnd() {
      myAgent.doDelete();   
      return super.onEnd();
    } 
  }
    Individual geneticAlgorithm(int populationsize, int crossoverRate, int mutationRate, int elitism, int error, int[][] info)
    {
        Population p = new Population(populationsize, info);
        p.printPopulaton();
        while(!p.checkOptimal(error))
        {
            p.produceNewPopulation(crossoverRate, mutationRate, elitism);
            p.printPopulaton();
        }
        Individual optimal = p.getOptimal(error);
        System.out.println(optimal.getFuntion());
        return optimal;
    }
    public final class Individual {
    int a;
    int b;
    
    int[][] info = {{1,2}, {2,4},{3,6}, {4,8}, {5,10}, {6,12},{7,14},{8,16},{9,18}};
    //{{23, 651}, {26, 762}, {30, 856},{34, 1063},{43, 1190},{48, 1298},{52, 1421},{57, 1440},{58, 1518}};
    int fitness;
    boolean elitism;
    Individual(int[][] info)
    {
        this.info = info;
        a=randomGen();
        b=randomGen();
        selectFitness(info.length);
    }
    Individual()
    {
        a=randomGen();
        b=randomGen();
        selectFitness(info.length);
    }
    String getIndividual()
    {
        return "dsa";
    }
    int getA()
    {
        return a;
    }
    int getB()
    {
        return a;
    }
    int randomGen()
    {
        return ThreadLocalRandom.current().nextInt(0, 200 + 1); 

    }
    void printIndividual()
    {
        System.out.println("a= "+a+" b= "+b+" Fitness: "+fitness+" Elitism "+elitism);
    }
    int selectFitness(int population)
    {
        int sum = 0;
        for(int i=0; i<info.length; i++)
        {
                int x=info[i][0];
                int y=info[i][1];
                int y1=a+(b*x);
                int dif = (y-y1)/population;
                if(dif<0)
                    dif=dif*-1;
                sum+=dif;
        }
        fitness=sum;
        return sum;
    }
    int getFitness()
    {
        return fitness;
    }
    void crossOver(Individual parent, int mutationRate)
    {
        String[] aBinary = {Integer.toBinaryString(a), Integer.toBinaryString(parent.getA())};
        String[] bBinary = {Integer.toBinaryString(b), Integer.toBinaryString(parent.getB())};
        aBinary=fillUpString(aBinary);
        bBinary=fillUpString(bBinary);
        String aChildren;
        int crossoverpoint = ThreadLocalRandom.current().nextInt(0, aBinary.length-1 + 1);
        aChildren = aBinary[1].substring(0, crossoverpoint) + aBinary[0].substring(crossoverpoint);
        String bChildren;
        crossoverpoint = ThreadLocalRandom.current().nextInt(0, bBinary.length-1 + 1);
        bChildren = bBinary[1].substring(0, crossoverpoint) + bBinary[0].substring(crossoverpoint);
        a = Integer.parseInt(aChildren, 2);
        b=Integer.parseInt(bChildren, 2);
        selectFitness(info.length);
        /*StringBuilder aChildren= new StringBuilder(aBinary[0]);
        StringBuilder bChildren= new StringBuilder(bBinary[0]);
        for(int i=0; i<aBinary[0].length(); i++)
            if(mutationRate<ThreadLocalRandom.current().nextInt(0, 100 + 1))
            {
                aChildren.setCharAt(i, aBinary[1].charAt(i));
            }
        for(int i=0; i<bBinary[0].length(); i++)
            if(mutationRate<ThreadLocalRandom.current().nextInt(0, 100 + 1))
            {
                bChildren.setCharAt(i, bBinary[1].charAt(i));
            }
        a=Integer.parseInt(aChildren.toString(), 2);
        b=Integer.parseInt(bChildren.toString(), 2);
        selectFitness(info.length);*/
    }
    String fill8Bin(String bin)
    {
        String s = "";
        for(int i = bin.length();i<8;i++)
        {
            s+="0";
        }
        return s+bin;
        
    }
    void setElitism(boolean el)
    {
        elitism = el;
    }
    String[] fillUpString(String [] strings)
    {
        if(strings[0].length()==strings[1].length())
            return strings;
        if(strings[0].length()>strings[1].length())
        {
            String s="";
            for(int i= 0; i<strings[0].length()-strings[1].length(); i++)
            {
                s+="0";
            }
            s+=strings[1];
            strings[1]=s;
            return strings;
        }
        else{
            String s="";
            for(int i= 0; i<strings[1].length()-strings[0].length(); i++)
            {
                s+="0";
            }
            s+=strings[0];
            strings[0]=s;
            return strings;
        }
    }
  

    void mutation()
    {
        String aBinary = fill8Bin(Integer.toBinaryString(a));
        String bBinary = fill8Bin(Integer.toBinaryString(b));
        StringBuilder auxA = new StringBuilder(aBinary);
        StringBuilder auxB = new StringBuilder(bBinary);
        for(int i=0; i<auxA.length(); i++)
        {
            if(i>=ThreadLocalRandom.current().nextInt(0, auxA.length()-1 + 1))
            {
                if(auxA.charAt(i)=='0')
                {
                    auxA.setCharAt(i, '1');
                }
                else
                    auxA.setCharAt(i, '0');

            }
        }
        for(int i=0; i<auxB.length(); i++)
        {
            if(i>=ThreadLocalRandom.current().nextInt(0, auxB.length()-1 + 1))
            {
                if(auxB.charAt(i)=='0')
                {
                    auxB.setCharAt(i, '1');
                }
                else
                    auxB.setCharAt(i, '0');

            }
        }
        a=Integer.parseInt(auxA.toString(), 2);
        b=Integer.parseInt(auxB.toString(), 2);
}
    String getFuntion()
    {
        return "y="+a+"+"+b+"x";
    }

}
    public final class Population {
    int populationSize;
    Individual[] population = new Individual[populationSize];
    int[] probabilities = new int[population.length];
    int totalFitness;
    int generation=0;
    Population(int populationSize, int[][] info)
    {
        population= new Individual[populationSize];
        int[] probabilities = new int[populationSize];
        this.populationSize = populationSize;
        for(int i=0; i<populationSize; i++)
        {
            population[i] = new Individual(info);
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

    }
