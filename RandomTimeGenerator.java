
import java.util.*;

/**
 * @author Parami Roy
 * @StudentID 200205116
 *
 */
public class RandomTimeGenerator {
	
	int seed = 2;
	
	Random gen = new Random(seed);
	
	double generateNextDeltaInterArrival(double lambda)
	{
		
		return -(Math.log(gen.nextDouble()/lambda));
	}
	
	double generateNextDeltaRetrans(double lambda2)
	{
		return -(Math.log(gen.nextDouble()/lambda2));
	}
	

}
