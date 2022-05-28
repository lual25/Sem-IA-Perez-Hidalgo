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
public final class Individual {
    int a;
    int b;
    int[][] info = {{23, 651}, {26, 762}, {30, 856},{34, 1063},{43, 1190},{48, 1298},{52, 1421},{57, 1440},{58, 1518}};
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
