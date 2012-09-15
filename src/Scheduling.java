import java.util.ArrayList;
import java.util.Arrays;

public class Scheduling {
	public static void main(String [] args) {
		final int min_weeks_apart = 5;
		int num_restarts = 0;
		
		//Create Schedule
		ArrayList<String>[] div_opps = (ArrayList<String>[]) new ArrayList[League_Info.owners.length];
		ArrayList<String>[] league_opp = findOppLeftAndSetDivOpps(div_opps);
		ArrayList<String>[] opp_copy = copyArraylistArray(league_opp);
		String[][] schedules = new String[12][13];
		boolean reset_needed = true;
		while(reset_needed) {
			//reset looking for possible schedule
			reset_needed = false;
			
			RESTART: for (int week=0; week < 13; week++) {
				for(int owner=0; owner < League_Info.owners.length;owner++) {
					if(opp_copy[owner].size() == 13-week) {
						//still needs an opponent
						int rand_opp = randInt(0,opp_copy[owner].size()-1);
						ArrayList<Integer> opps_tried = new ArrayList<Integer>();
						while(schedules[Arrays.asList(League_Info.owners).indexOf(opp_copy[owner].get(rand_opp))][week] != null) {
							opps_tried.add(rand_opp);
							if(opps_tried.size() == opp_copy[owner].size()) {
								reset_needed = true;
								opp_copy = copyArraylistArray(league_opp);
								schedules = new String[12][13];
								num_restarts++;
								break RESTART;
							} else {
								//try new opponent that hasn't tried yet
								rand_opp = randInt(0,opp_copy[owner].size()-1);
								while(opps_tried.contains(rand_opp)) {
									rand_opp = randInt(0,opp_copy[owner].size()-1);
								}
							}
						}
						//found opponent
						opp_copy[Arrays.asList(League_Info.owners).indexOf(opp_copy[owner].get(rand_opp))].remove(League_Info.owners[owner]);
						schedules[Arrays.asList(League_Info.owners).indexOf(opp_copy[owner].get(rand_opp))][week] = League_Info.owners[owner];
						schedules[owner][week] = opp_copy[owner].get(rand_opp);
						opp_copy[owner].remove(rand_opp);
					}
				}
			}
			if (!reset_needed && !isFinalScheduleOk(schedules, div_opps, min_weeks_apart)) {
				reset_needed = true;
				opp_copy = copyArraylistArray(league_opp);
				schedules = new String[12][13];
				num_restarts++;
			}
		}
		
		//Print out schedule
		System.out.println("Schedule: \n");
		for(int x=0;x<League_Info.owners.length;x++){
			System.out.println(League_Info.owners[x] + " : " + stringArrayOutput(schedules[x]) + "\n");
		}
		System.out.println("\nNumber of Restarts: " + num_restarts);
	}

	//Random integer from lo to hi inclusive
	public static int randInt(int lo, int hi) {
		return lo + (int) (Math.random()*(hi-lo+1));
	}
	
	//Create opponent list for each owner and sets up divisional opponents
	private static ArrayList<String>[] findOppLeftAndSetDivOpps(ArrayList<String>[] div_opps) {
		ArrayList<String>[] league_opps = (ArrayList<String>[]) new ArrayList[League_Info.owners.length];
		for(int i=0;i<league_opps.length;i++) {
			ArrayList<String> owner_opp = new ArrayList<String>(Arrays.asList(League_Info.owners));
			ArrayList<String> div_opp = new ArrayList<String>();
			league_opps[i] = owner_opp;
			div_opps[i] = div_opp;
			owner_opp.remove(League_Info.owners[i]);
			for(int row=0;row < League_Info.divisions_2012.length;row++) {
				for(int col=0;col < League_Info.divisions_2012[row].length;col++) {
					if (League_Info.divisions_2012[row][col].equals(League_Info.owners[i])) {
						for(int x=0;x < League_Info.divisions_2012[row].length;x++) {
							if(!League_Info.divisions_2012[row][x].equals(League_Info.owners[i])) {
								owner_opp.add(League_Info.divisions_2012[row][x]);
								div_opp.add(League_Info.divisions_2012[row][x]);
							}
						}
					}
				}
			}
		}
		return league_opps;
	}
	
	//Print a String Array
	private static String stringArrayOutput(String[] in) {
		String out = "[";
		for (String index : in) {
			if(index != null) {
				out += index + ", ";
			}
		}
		if(out.endsWith(" ")) {
			out = out.substring(0,out.length()-2);
		}
		out += "]";
		return out;
	}
	
	//Makes a copy of an array of string arraylists
	public static ArrayList<String>[] copyArraylistArray(ArrayList<String>[] old) {
		ArrayList<String>[] copy = (ArrayList<String>[]) new ArrayList[old.length];
		for(int i=0;i<copy.length;i++) {
			copy[i] = new ArrayList<String>();
			for(int j=0;j<old[i].size();j++) {
				copy[i].add(old[i].get(j));
			}
		}
		return copy;
	}
	
	//Check for divisional opponents being too close
	public static boolean isFinalScheduleOk(String[][] schedule, ArrayList<String>[] div_opps, int min_weeks_apart) {
		boolean scheduleOk = true;
		int owner = 0;
		int week = 0;
		ArrayList<Integer>[] div_weeks = (ArrayList<Integer>[]) new ArrayList[2];
		div_weeks[0] = new ArrayList<Integer>();
		div_weeks[1] = new ArrayList<Integer>();
		while(scheduleOk && owner < 12 && week < 13) {
			if(schedule[owner][week].equals(div_opps[owner].get(0))) {
				div_weeks[0].add(week);
				if (div_weeks[0].size() == 2 && div_weeks[0].get(1) - div_weeks[0].get(0) < min_weeks_apart) {
					scheduleOk = false;
				}
			} else if (schedule[owner][week].equals(div_opps[owner].get(1))) {
				div_weeks[1].add(week);
				if (div_weeks[1].size() == 2 && div_weeks[1].get(1) - div_weeks[1].get(0) < min_weeks_apart) {
					scheduleOk = false;
				}
			}
			if (week == 12) {
				week = 0;
				owner++;
				div_weeks[0] = new ArrayList<Integer>();
				div_weeks[1] = new ArrayList<Integer>();
			} else {
				week++;
			}
		}
		return scheduleOk;
	}
}