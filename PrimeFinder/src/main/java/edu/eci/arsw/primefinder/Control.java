/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    public final static Object queue =new Object();
    
    
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }
        boolean flag=pft[0].isAlive();
        
        for(int i = 1;i < NTHREADS;i++ ) {
            flag=flag || pft[i].isAlive();
        }
        while(flag){
            
                try {
                    this.sleep(TMILISECONDS);
                    ArrayList<Integer> resultado = new ArrayList<Integer>();
                    for(int i = 0;i < NTHREADS;i++ ) {
                        pft[i].pause();                      
                    }
                    for(int i = 0;i < NTHREADS;i++ ) {
                        resultado.addAll(pft[i].getPrimes());
                    }
                    System.out.print("pause");
                    System.out.print(resultado.size());
                    (new BufferedReader(new InputStreamReader(System.in))).readLine();
                    System.out.print("volvio en game");
                    synchronized(queue){
                        queue.notifyAll();
                    }
                    for(int i = 0;i < NTHREADS;i++ ) {
                        pft[i].play();
                    }
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            flag=pft[0].isAlive();
            for(int i = 1;i < NTHREADS;i++ ) {
                flag=flag || pft[i].isAlive();
            }
        }
    }
    
}
