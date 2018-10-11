/**
 * The index 0 in the Linked List for time records, will always indicate the CLA , the index 1 will indicate
 * CLS and any index beyond would indicate CLRs.
 * 
 */
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author proy4
 * @studentID 200205116
 *
 */
public class RandomTime {
	
	
	private double delta_new_arrival= 0, delta_service_completion=0, delta_orbitting_time =0,min_time=0;
	private double lambda_inter,lambda_retran;
	
	
	LinkedList<Double> time_record = new LinkedList<Double>();
	LinkedList<Double> time_record_clone = new LinkedList<Double>();
	LinkedList<Double> orbitting_packets = new LinkedList<Double>();
	LinkedList<Double> clr_print = new LinkedList<Double>();
	RandomTimeGenerator next_time = new RandomTimeGenerator();
	
	void setInitialConditions(double cla, double cls,double clr)
	{
		time_record.add((Double) cla);
		time_record.add((Double) cls);
		time_record.add((Double) clr);
		
	}
	
	void setDeltaConditions(double lambda,double cls,double lambda2)
	{
		lambda_inter = lambda;
		lambda_retran = lambda2;
		delta_new_arrival = next_time.generateNextDeltaInterArrival(lambda_inter);
		delta_service_completion = cls;
		delta_orbitting_time = next_time.generateNextDeltaRetrans(lambda_retran);
		
	}
	
	void setNextDeltaInterArrival()
	{
		delta_new_arrival = next_time.generateNextDeltaInterArrival(lambda_inter);
	}
	
	void setNextDeltaRetrans()
	{
		delta_orbitting_time = next_time.generateNextDeltaRetrans(lambda_retran);
	}
	LinkedList<Double> getCLRValues()
	{
		clr_print.clear();
		for(int i = 2; i<time_record.size();i++)
			clr_print.add((Double) time_record.get(i));
		
		return clr_print;
			
	}
	/*
	 * Getters and Setters for CLA
	 */
	
	double getTimeRecord(int index)
	{
		return time_record.get(index);
	}
	
	Double getNextNewPacketArrivalTime()
	{
		return time_record.get(0);
	}
	
	void setNextNewPacketArrivalTime(double master_clock)
	{
		double temp =0;
		setNextDeltaInterArrival();
		temp = master_clock +delta_new_arrival;
		time_record.add(0,(Double) temp);
		time_record.remove(1);
	}
	
	/*
	 * Getters and Setters for CLS
	 */
	
	Double getNextServiceCompletionTime()
	{
		return time_record.get(1);
	}
	
	void setNextServiceCompletionTime(double master_clock)
	{
		double temp = 0;
		temp = master_clock +delta_service_completion;
		time_record.add(1,(Double) temp);
		time_record.remove(2);
	}
	
	/*
	 * Getters and Setters for CLR
	 */
	double setNextOrbittingTime(double mcl)
	{
		double temp=0;
		setNextDeltaRetrans();
		temp = mcl + delta_orbitting_time;
		time_record.add((Double) temp);
		if(time_record.get(2) == 999999)
		{
			time_record.remove(2);
		}
		return temp;
	}
	
	/*
	 * Methods for orbitting packets
	 */
	
	void addToOrbittingPackets(double clr_temp)
	{
		orbitting_packets.add((Double) clr_temp);
	}
	
	LinkedList<Double> getListOfOrbittingPackets()
	{
		return orbitting_packets;
	}
	void removeFromOrbittingPackets(int index_to_remove)
	{
		time_record.remove(index_to_remove);
	}
	@SuppressWarnings("unchecked")
	double findMinTime()
	{
		time_record_clone = (LinkedList<Double>) time_record.clone();
		Collections.sort(time_record_clone);
		min_time = time_record_clone.get(0);
		return min_time;
	}
	
	LinkedList<Integer> countOccurrencesAndIndices()
	{
		LinkedList<Integer> indices = new LinkedList<Integer>();
		
		for(int i = 0;i<time_record.size();i++)
		{
			if(time_record.get(i) == min_time)
				indices.add((Integer) i);
		}
		return indices;
	}
	
	void printLL()
	{
		System.out.println(time_record);
	}
	
	
	
	
	

}
