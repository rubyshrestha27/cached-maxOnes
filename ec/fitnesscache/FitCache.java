package ec.fitnesscache;
import ec.*;
import ec.simple.*;
import ec.vector.*;
import java.util.*;
//import java.util.Enumeration;

public class FitCache{
	
	 /* *** Singleton class implementation *** */
	 
	private static FitCache fitnessCache = null;
	private HashMap<Integer[], Double> myCache;
	
	private FitCache(){
		myCache=new HashMap<Integer[], Double>();
	}
	
	private static class SingletonHelper{
		private static final FitCache fitnessCache = new FitCache();
	}
	
	//return SingletonHelper._captain;
	public static FitCache getInstance() {
		if(fitnessCache==null)
		{
			fitnessCache =  SingletonHelper.fitnessCache;
		}
			return fitnessCache;
		
  }
 
	//public static HashMap<String, Double> fitnessCache =  new HashMap<String, Double>();

	public boolean ifEmpty()
	{
		boolean empty = myCache.isEmpty();
		
		return empty;
	}

	public boolean ifExist(Integer[] ind)
	{
		boolean exists = myCache.containsKey(ind);
		return exists;
	}

	public  void setFitness(Integer[] ind, double fitness)
	{
		myCache.put(ind,fitness);
	}
 
	public double getFitness(Object ind)
	{
		return(myCache.get(ind));
	}
	
	public double myFitness(VectorIndividual indiv)
	{
		Object genome;
	    genome = indiv.getGenome();
	    			
	   if (indiv instanceof ec.vector.DoubleVectorIndividual)
	   {
		   getFitness((Double [])genome);
    	}
		else if (indiv instanceof ec.vector.IntegerVectorIndividual)
		{
		  getFitness((Integer [])genome);
		}
		
	}
	
	
	public int hashSize()
	{
		return myCache.size();
	}

	public void printHash()
	{
		System.out.println(myCache);
	}
	
	public void randomImmigrant()
	{
		Object[] immigrant = myCache.keySet().toArray();
		Object key = immigrant[new Random().nextInt(immigrant.length)];
		System.out.println("Random immigrant =>  " + key + " : " + myCache.get(key));

	}
}
