/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handson2;
/**
 *
 * @author Luis
 */
public class HandsOn2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        geneticAlgorithm(15, 40, 30, 3, 30);
    }
    static Individual geneticAlgorithm(int populationsize, int crossoverRate, int mutationRate, int elitism, int error)
    {
        Population p = new Population(populationsize);
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
}