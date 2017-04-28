package ec.fitnesscache;
import ec.*;
import ec.simple.*;
import ec.vector.*;

public class CachedMaxOnes extends Problem implements SimpleProblemForm
    {
    public void evaluate(final EvolutionState state,
        final Individual ind,
        final int subpopulation,
        final int threadnum)
        {
        if (ind.evaluated) return;

        if (!(ind instanceof BitVectorIndividual))
            state.output.fatal("Whoa!  It's not a BitVectorIndividual!!!",null);
        
        int sum=0;
        BitVectorIndividual ind2 = (BitVectorIndividual)ind;
        
        for(int x=0; x<ind2.genome.length; x++)
            sum += (ind2.genome[x] ? 1 : 0);
        
        if (!(ind2.fitness instanceof SimpleFitness))
            state.output.fatal("Whoa!  It's not a SimpleFitness!!!",null);
        ((SimpleFitness)ind2.fitness).setFitness(state,
            /// ...the fitness...
            sum/(double)ind2.genome.length,
            ///... is the individual ideal?  Indicate here...
            sum == ind2.genome.length);
        ind2.evaluated = true;
        }
    }
