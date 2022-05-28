/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handson5;
import jade.core.Agent;
import jade.core.behaviours.*;
import java.util.Random;
/**
 *
 * @author Luis
 */
public class HandsOn5 extends Agent{
    public void setup(){
        System.out.println("Hola mundo");
        addBehaviour(new PSOAlgorithm());
}
    private class PSOAlgorithm extends OneShotBehaviour {
        public void action() {
            PSOalgoritm p = new  PSOalgoritm(10, 0, 300);
            float[] c = {2, 2};
            float[] b = {1, 1};
            p.algorytm(1000, c, 0.4f, 10f);
        }
      public int onEnd() {
        myAgent.doDelete();
        return super.onEnd();
    } 
    }
    public class Particle {
    float[] _currentPosition = new float[2];
    float[] _currentVelocity = new float[2];
    //float[] _personalBest = new float[2];
    float _pbx;
    float _pby;
    double _currentFitness;
    double _bestFitness;
    Particle(float min, float max, int N)
    {
        _currentPosition = SelectRandomPosition(min, max);
        setInitialVelocity();
        updatePosition();
        float[][] info = {{23, 651}, {26, 762}, {30, 856},{34, 1063},{43, 1190},{48, 1298},{52, 1421},{57, 1440},{58, 1518}};
        CalculateFitness(info, N);
        _pbx= _currentPosition[0];
        _pby= _currentPosition[1];
        //_personalBest = _currentPosition;
        _bestFitness = _currentFitness;
    }
    void setInitialVelocity()
    {
        for(int i=0; i<2; i++)
        {
            _currentVelocity[i] =(0.1f*_currentPosition[i]);
        }
    }
    float[] SelectRandomPosition(float min, float max)
    {
        float[] _randomPosition = new float[2];
        for(int i =0; i<2; i++)
        {
            _randomPosition[i] = (float) (min + Math.random() * (max - min));
        }
        return _randomPosition;
    }
    void print()
    {
        System.out.println(_currentPosition[0]+", "+_currentPosition[1]+"  Fit: "+_currentFitness +" PB:" +_pbx+", "+_pby+"  Fit: "+_bestFitness + "  Velocity: "+_currentVelocity[0]+" , "+_currentVelocity[1]);
    }
    void CalculateFitness(float [][] info, float population)
    {
        float sum = 0;
        for(int i=0; i<info.length; i++)
        {
                float x=info[i][0];
                float y=info[i][1];
                float y1=_currentPosition[0]+(_currentPosition[1]*x);
                float dif = (y-y1)/population;
                if(dif<0)
                    dif=dif*-1;
                sum+=dif;
        }
        _currentFitness=sum;
    }
    
    double GetFitness()
    {
        return _currentFitness;
    }
    float[] getPositionNextDat()
    {
        float[] _newPosition = new float[2];
        for(int i=0; i<2; i++)
        {
            _newPosition[i]=_currentPosition[i]+_currentVelocity[i];
        }
        return _newPosition;
    }
   float[] getVelocity(float[] globalBest, float w, float[] c)
    {
        Random random = new Random();
        float[] _newVelocity = new float[2];
        float[] _pb = {_pbx, _pby};
        //float r1 = random.nextFloat();
        //float r2 = random.nextFloat();
        for(int i=0; i<2; i++)
        {
            _newVelocity[i] = (w*_currentVelocity[i])+((c[0]*random.nextFloat())*(_pb[i]-_currentPosition[i]))+((c[1]*random.nextFloat())*(globalBest[i]-_currentPosition[i]));
            //System.out.println(_newVelocity[i]+"=("+w+"*"+_currentVelocity[i]+")+(("+c[0]+"*"+r1+")*("+_pb[i]+"-"+_currentPosition[i]+")))+(("+c[1]+"*"+r2+")*("+globalBest[i]+"-"+_currentPosition[i]+"));");
        }
        return _newVelocity;
    }
    
    void updateVelocity(float gbx, float gby, float w, float[] c)
    {
        float[] globalBest = {gbx, gby};
        _currentVelocity = getVelocity(globalBest, w, c);
    }
    void updatePosition()
    {
        float[] _newPosition = getPositionNextDat();
        for(int i=0; i<2; i++)
        {
            _currentPosition[i]=_newPosition[i];
        }
    }
    void updatePB()
    {
        if(_currentFitness < _bestFitness)
        {
            //System.out.println("PB updated New: "+ _currentFitness + " Current: "+_bestFitness);
            _pbx = _currentPosition[0];
            _pby = _currentPosition[1];
            _bestFitness = _currentFitness;
        }
    }
}
    public class PSOalgoritm {
        Particle [] _swarm;
    //float[] _globalBest = new float[2];
    int n;
    float _gbx;
    float _gby;
    double _globalBestFitness;
    float[][] info = {{23, 651}, {26, 762}, {30, 856},{34, 1063},{43, 1190},{48, 1298},{52, 1421},{57, 1440},{58, 1518}};
    PSOalgoritm(int N, float min, float max)
    {
        n = N;
        _swarm = new Particle[n];
        for(int i=0; i<n; i++)
        {
            _swarm[i] = new Particle(min, max, n); 
        }
        //_globalBest = _swarm[0]._personalBest;
        _gbx = _swarm[0]._pbx;
        _gby = _swarm[0]._pby;
        _globalBestFitness = _swarm[0]._bestFitness;
    }
    void print()
    {
        for(Particle i:_swarm)
        {
            i.print();
        }
    }
    void algorytm(int max_iter, float[] c, float w, float error)
    {
        int cont=0;
        //for(int i=0; i<max_iter; i++)
        while(_globalBestFitness > error && cont < max_iter)
        {
            int x=1;
            for(Particle p: _swarm)
            {
                //System.out.println(x);
                x++;
                p.CalculateFitness(info, n);
                p.updatePB();
                updateGB(p);
                
                p.print();
           

                p.updateVelocity(_gbx, _gby, w, c);
                p.updatePosition();
            }
            System.out.println("----a= "+_gbx+"    b= "+_gby+"    fitness: "+ _globalBestFitness+"----");
            cont++;
        }
        System.out.println("y="+_gbx+"+"+_gby+"x");
    }
    void updateGB(Particle p)
    {
        if(p._currentFitness < _globalBestFitness)
        {
            //System.out.println("GB updated New: "+ p._currentFitness + " Current: "+_globalBestFitness);
            _globalBestFitness = p._currentFitness;
            _gbx = p._currentPosition[0];
            _gby = p._currentPosition[1];
        }
    }
}
}
