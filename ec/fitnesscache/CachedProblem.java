package ec.fitnesscache;
import ec.*;
import ec.simple.*;
import ec.vector.*;
import ec.util.*;

public class CachedProblem extends Problem {

    private FitCache myCache; 
    private Problem myProblem; 

    public void setup(final EvolutionState state, final Parameter base){
	super.setup(state,base);
	myCache = FitCache.getInstance(); 
	// getting the name of the Problem class from the config file
	// we use as parameter name "eval.problem.original"
	// whenever we use CachedProblem it should be specified as the value 
	// of the ec.problem parameter
	// ec.problem = ec.fitcaches.CachedProblem
	Parameter pm = base.push("original");
	// Parameter p = new Parameter(new String[]{"eval","problem","original"});
	String problemName = state.parameters.getString(pm,null);
	//System.out.println(problemName);
	// get a Class object for that class name
	Class<?> c = Class.forName(problemName);

	// get an instance of the corresponding Problem class
	myProblem = c.newInstance();
    }

    public void evaluate(final EvolutionState state,
			 final Individual ind,
			 final int subpopulation,
			 final int threadnum)
    {
        if (ind.evaluated) return;
	
	double fitnessValue; 
	VectorIndividual indiv = (VectorIndividual)ind;
	Object genome= indiv.getGenome();
	if(myCache.ifExist((Integer[]) genome)){
	    // get the fitness from mycache
	    fitnessValue = myCache.getFitness((Integer[]) genome);
	    
	    if (!(ind.fitness instanceof SimpleFitness))
		state.output.fatal("Whoa!  It's not a SimpleFitness!!!",null);
	    (
	     (SimpleFitness)ind.fitness).setFitness(state,
						    /// ...the fitness...
						    fitnessValue,
						    ///... is the individual ideal?  Indicate here...
						    false);
	}else{
	    // get the fitness from myProblem
	    myProblem.evaluate(state, ind, subpopulation, threadnum);
	    fitnessValue = ((SimpleFitness)(ind.fitness)).fitness();
	    
	    // add it to cache 
	    myCache.setFitness((Integer[]) genome, fitnessValue);
	}
	ind.evaluated = true;
    }
}

