package TPE;

import java.util.LinkedList;
import java.util.List;

public class FlightCreator {
	AirportManager airportM = AirportManager.getInstance();

	public void addFlight(String command) {
		String[] res = command.split(" ");
		String[] days = res[4].split("-");
		if(!checkDays(days)){
			System.out.println("Ingreso dias repetidos");
		}
		List<Day> newDays = getDays(days); 
		String[] hoursAndMin = res[7].split(":");
		Integer departureTime = new Integer(hoursAndMin[0])*60+ new Integer(hoursAndMin[1]);
		String[] hours = res[8].split("h");
		Integer flightTime;
		if(hours.length == 1){
			String[] mins = hours[0].split("m");
			 flightTime = new Integer(mins[0]);
		}else{
			String[]mins = hours[1].split("m");
			flightTime = new Integer(hours[0])*60+ new Integer(mins[0]);
		}
		Double price = new Double(res[9]);
		System.out.println(flightTime);
		System.out.println(price);
		
		System.out.println(departureTime);
		Flight f = new Flight(res[2], res[3], newDays, res[5], res[6], departureTime, flightTime, price);
		airportM.addFlight(f);
		System.out.println(airportM.getFlights());
	} 
	
	private List<Day> getDays(String[] days) {
		List<Day> ans = new LinkedList<Day>();
		for(int i = 0; i <days.length;i++){
			switch(days[i]){
			case "Lu": ans.add(Day.MONDAY);
						break;
			case "Ma": ans.add(Day.TUESDAY);
						break;
			case "Mi": ans.add(Day.WEDNESDAY);
						break;
			case "Ju": ans.add(Day.THURSDAY);
						break;
			case "Vi": ans.add(Day.FRIDAY);
						break;
			case "Sa": ans.add(Day.SATURDAY);
						break;
			case "Do": ans.add(Day.SUNDAY);
						break;		
			}
		}
		return ans;
	}

	private boolean checkDays(String[] days) {
		for(int i = 0; i < days.length;i++){
			for(int j = i+1 ; j < days.length;j++){
				if(days[i].equals(days[j])){
					return false;
				}
			}
		}
		return true;
		
	}

	public void addFlights(List<String> data) {
		
	}
	
	public void deleteFlight(String command) {
		String[] res = command.split(" ");
		String airline = res[2];
		String flightNumber = res[3];
		System.out.println(airline+" "+flightNumber);
		airportM.deleteFlight(airline, flightNumber);
	}
	
}
