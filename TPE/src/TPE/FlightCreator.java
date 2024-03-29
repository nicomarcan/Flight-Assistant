package TPE;

import java.util.List;
/**Esta clase se encarga de crear y borrar vuelos a partir de la entrada elegida por el usuario
 * (archivo o entrada estandar)
 */
public class FlightCreator {
	AirportManager airportM = AirportManager.getInstance();
	private final int  minsPerHour=60;
	
	public void addFlight(String airline, String flightNumber, String daysS, String origin, String target,
					String departureTimeS, String flightTimeS, double price) {
		String[] days = daysS.split("-");
		if(!Day.checkDays(days)){
			System.out.println("Ingreso dias repetidos");
		}
		List<Day> newDays = Day.getDays(days); 
		String[] hoursAndMin = departureTimeS.split(":");
		Integer departureTime = new Integer(hoursAndMin[0])*minsPerHour+ new Integer(hoursAndMin[1]);
		String[] hours = flightTimeS.split("h");
		Integer flightTime;
		if(hours.length == 1){
			String[] mins = hours[0].split("m");
			 flightTime = new Integer(mins[0]);
		}else{
			String[]mins = hours[1].split("m");
			flightTime = new Integer(hours[0])*minsPerHour+ new Integer(mins[0]);
		}

		Flight f = new Flight(airline, flightNumber, newDays, origin, target, departureTime, flightTime, price);
		airportM.addFlight(f);
	} 
	
	

	
	public void deleteFlight(String command) {
		String[] res = command.split(" ");
		String airline = res[2];
		String flightNumber = res[3];
		System.out.println(airline+" "+flightNumber);
		airportM.deleteFlight(airline, flightNumber);
	}

	public void deleteFlights() {
		airportM.deleteFlights();
		
	}
	
}
