/**
 * @author Parami Roy
 * @StudentID 200205116
 *
 */
import java.io.*;
import java.util.*;
public class SimulationRand {

	/**
	 * @param args
	 */
	
	
	static void printRow(double mcl,double cla,double cls,double buffer_size,LinkedList<Double> orbitting_packets)
	{
		System.out.format("%f\t\t%f\t\t%f\t\t%f\t\t",mcl,cla,cls,buffer_size);
		System.out.print(orbitting_packets);
		System.out.println();
		
	}
	
	static void printIntro()
	{
		System.out.println("***NOTE:***\n"
				+ "In this simulation code, the value 999999 has been used as a HIGH value to initialise the CLS, CLR before the first real calculation of "
				+ "these values.\n The index 0 in the Linked List for time records, will always indicate the CLA , the index 1 will indicateCLS and any index beyond would indicate CLRs.\nThus first few rows may have 999999 printed .\nAlso , in the intial condition of the systeam ( at Master Clock = 0 , CLA(next new arrival) in time stamp 2 ,the"
				+ "buffer size = 0 , CLS = 999999(as per above assumption), CLR = 999999.\n Here the inter-arrival time and the re-transmission time are changing in every new event involving those"
				+ "based on the random number seed that has been set and the mean values\n that has been taken as input.\nThe Random Number Seed is Set as"
				+ "2 by me.***\n\n*********************************************************************************************"
				+ "************************************************************************************");
	}
	public static void main(String[] args) throws FileNotFoundException{
		// TODO Auto-generated method stub
		
		/*
		 * Variables
		 */
		
		double cla,cls,clr,buffer_size = 0,max_buffer_size=0,master_clock = 0;
		Scanner time_records = new Scanner(System.in);
		RandomTime time_values = new RandomTime();
		LinkedList<Integer> occ_indices = new LinkedList<Integer>();
		
		/*
		 * Enter condition of the system at master clock =  0;
		 *
		 */
		cla = 2;
		cls = 999999;
		clr = 999999;
		buffer_size = 0;
		
		time_values.setInitialConditions(cla,cls,clr);
		
		PrintStream new_file = new PrintStream(new File("output.txt")); 
		PrintStream console = System.out;
		
		
		printIntro();
		
		
		System.out.println("Please enter the master clock limit until which you want to execute the simulation:");
		master_clock = time_records.nextDouble();
		
		System.out.println("Please enter the maximum buffer size:");
		max_buffer_size = time_records.nextDouble();
		
		System.out.println("Enter the rate value (lambda) for inter-arrival time of new packet:");
		cla = time_records.nextDouble();
		
		System.out.println("Enter the service time of each packet");
		cls = time_records.nextDouble();
		
		System.out.println("Enter the rate value (lambda) for orbitting time units");
		clr = time_records.nextDouble();
		System.setOut(new_file);
		
		time_values.setDeltaConditions(cla,cls,clr);
		
		double min_value=time_values.findMinTime();
		occ_indices = time_values.countOccurrencesAndIndices();
		
		/*
		 * Simulation part
		 */
		LinkedList<Double> temp_clr = new LinkedList<Double>();
		temp_clr.add(0,(Double) time_values.getTimeRecord(2));
	    System.out.println("MC\t\t\tCLA\t\t\tCLS\t\t\tbuff_size\t\t CLR(s) \t");
	    printRow(0,time_values.getTimeRecord(0),time_values.getTimeRecord(1),buffer_size,temp_clr);

		int flag_main = 0;
		for(double mcl= min_value ; mcl<=master_clock;)
		{
			
			
			for(int i = occ_indices.size()-1;i>=0;i--)
			{
				if(occ_indices.get(i)>=2)
				{
					//Apply CLR logics
					if(buffer_size>=max_buffer_size)
					{
						double temp = time_values.setNextOrbittingTime(mcl);
						time_values.addToOrbittingPackets(temp);
						time_values.removeFromOrbittingPackets(occ_indices.get(i));
					}
					
					while(buffer_size<max_buffer_size)
					{
						buffer_size++;
						time_values.removeFromOrbittingPackets(occ_indices.get(i));
					}
					
					
					min_value=time_values.findMinTime();
					occ_indices = time_values.countOccurrencesAndIndices();
					printRow(mcl,time_values.getTimeRecord(0),time_values.getTimeRecord(1),buffer_size,time_values.getCLRValues());
					
					mcl = min_value;
					continue;
				}
				
			int index = occ_indices.get(i),flag = 0;
			
			if((index == 1 ) &&(time_values.getNextNewPacketArrivalTime() == time_values.getTimeRecord(index)))
				{
					//Apply CLA logics
					if(buffer_size<max_buffer_size)
					{
						buffer_size++;
						if(time_values.getNextServiceCompletionTime()== 999999)
							time_values.setNextServiceCompletionTime(mcl);
						
					}
					else
					{
						double clr_temp = time_values.setNextOrbittingTime(mcl);
						time_values.addToOrbittingPackets(clr_temp);
					}
					time_values.setNextNewPacketArrivalTime(mcl);
					flag_main = 1;
					printRow(mcl,time_values.getTimeRecord(0),time_values.getTimeRecord(1),buffer_size,time_values.getCLRValues());
					
					min_value=time_values.findMinTime();
					occ_indices = time_values.countOccurrencesAndIndices();
					mcl = min_value;
					continue;
				}
			
				if(occ_indices.get(i)==1 || flag_main == 1)
				{
					//Apply CLS Logics
					if(buffer_size>0)
					{
						buffer_size--;
						
					}
					time_values.setNextServiceCompletionTime(mcl);
					printRow(mcl,time_values.getTimeRecord(0),time_values.getTimeRecord(1),buffer_size,time_values.getCLRValues());
					min_value=time_values.findMinTime();
					occ_indices = time_values.countOccurrencesAndIndices();
					mcl = min_value;
					flag_main = 0;
					flag = 1;
					continue;
					
				}
				
				if(occ_indices.get(i)==0 || flag == 0)
				{
					//Apply CLA logics
					if(buffer_size<max_buffer_size)
					{
						buffer_size++;
						if(time_values.getNextServiceCompletionTime()== 999999)
							time_values.setNextServiceCompletionTime(mcl);
						
					}
					else
					{
						double clr_temp = time_values.setNextOrbittingTime(mcl);
						time_values.addToOrbittingPackets(clr_temp);
					}
					time_values.setNextNewPacketArrivalTime(mcl);
					printRow(mcl,time_values.getTimeRecord(0),time_values.getTimeRecord(1),buffer_size,time_values.getCLRValues());
					min_value=time_values.findMinTime();
					occ_indices = time_values.countOccurrencesAndIndices();
					mcl = min_value;
					
					
				}
			}
			
		}
		
		System.setOut(console);
		/*
		 * Scanner record closure
		 */
		time_records.close();
	}

}
