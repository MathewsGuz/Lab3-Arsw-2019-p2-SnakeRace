package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrimeFinderThread extends Thread{

	public Object queue= Control.queue;
	int a,b;
        boolean isPause;

	private List<Integer> primes;
	
	public PrimeFinderThread(int a, int b) {
		super();
                this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
	}

        @Override
	public void run(){
            for (int i= a;i < b;i++){
                if(!isPause){
                    if (isPrime(i)){
                        primes.add(i);
//                        System.out.println(i);
                    }
                }else{
                    synchronized(queue){
                        try {
                            queue.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(PrimeFinderThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }i--;                   
                } 
            }
	}
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
            return primes;
	}
        
        public void pause(){
            isPause=true;
        }
        
        public void play(){
//            synchronized(queue){
//                queue.notify();
//            }
            isPause=false;
            
        }
}
