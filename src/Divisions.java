import java.util.ArrayList;

public class Divisions {
	//Creates new divisions randomly and prints them to the console
	public static void main(String [] args) {
		//Create divisions
		ArrayList<String>[] divisions = (ArrayList<String>[]) new ArrayList[League_Info.divisions.length];
		for (int i=0;i<divisions.length;i++) {
			divisions[i] = new ArrayList<String>();
		}
		for (String owner : League_Info.owners) {
			while(true) {
				int rand_division = Scheduling.randInt(0, League_Info.divisions.length-1);
				if (divisions[rand_division].size() < League_Info.divisions.length-1) {
					divisions[rand_division].add(owner);
					break;
				}
			}
		}
		
		//Print results to console
		System.out.println("Divisions:\n");
		for (int i = 0; i < League_Info.divisions.length; i++) {
			System.out.println(League_Info.divisions[i] + ": " + divisions[i].toString() + "\n");	
		}
	}	
}
