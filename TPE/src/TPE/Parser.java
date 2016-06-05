package TPE;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {
	
	public static boolean parseCommand(String command) throws ClassNotFoundException, IOException { 
		AirportCreator airportC = new AirportCreator();
		FlightCreator flightC = new FlightCreator();
		FileManager f = new FileManager();
		
		String HELP_MESSAGE = "***HELP MESSAGE***";
		String helpExp = "[hH]";
		String addAirExp = "insert airport [a-z A-Z]{1,3} -?[0-9]+\\.[0-9]+ -?[0-9]+\\.[0-9]+";
		String delAirExp = "delete airport [a-z A-Z]{1,3}";
		String addAllAirExp = "insert all airport [a-z A-Z 0-9]+\\.txt";
		String delAllAirExp = "delete all airport";
		String addFlExp = "insert flight [a-z A-Z]{1,3} [0-9]{1,7} (Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))* [a-z A-Z]{1,3} [a-z A-Z]{1,3} ([0-1][0-9]|2[0-3]):[0-5][0-9] ([1-9]h|[1-9][0-9]h)?[0-5][0-9]m [0-9]+\\.[0-9]+$";
		String delFlExp = "delete flight [a-z A-Z]{1,3} [1-9][0-9]*";
		String addAllFlExp = "insert all flight [a-z A-Z 0-9]+\\.txt";
		String delAllFlExp = "delete all flight";
		String findRouteExp = "findRoute src=[a-z A-Z]{3} dst=[a-z A-Z]{3} priority=(((pr)|(tt))|(ft)) (weekdays=(Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))*)";
		String outputFormatExp = "outputFormat ((text)|(KML))";
		String outputExp = "output ((stdout)|(file [a-z A-Z 0-9]+\\.txt))";
		String exitAndSaveExp = "exitAndSave";
		String quitExp = "quit";
		
		if(Pattern.matches(helpExp, command)) {
			System.out.println(HELP_MESSAGE);
			return false;
		}
		
		/**listo**/
		if(Pattern.matches(addAirExp,command)){
			airportC.addAirport(command);
			return false;
		}
		/**listo**/
		else if(Pattern.matches(addAllAirExp,command)){
			String[] res = command.split(" ");
			List<String> data = f.readAirports(res[3]);
			airportC.addAirports(data);
			return false;
		}
		/**listo**/
		else if(Pattern.matches(delAirExp,command)){
			airportC.deleteAirport(command);
			return false;
		}
		/**listo**/
		else if(Pattern.matches(delAllAirExp, command)){
			airportC.deleteAirports();
			return false;
		}
		/**listo**/
		else if(Pattern.matches(addFlExp, command)){
			flightC.addFlight(command);
			return false;
		}
		/**listo**/
		else if(Pattern.matches(delFlExp, command)){
			flightC.deleteFlight(command);
			return false;
		}
		/**listo**/
		else if(Pattern.matches(addAllFlExp, command)){
			String[] res = command.split(" ");
			List<String> data = f.readFlights(res[3]);
			flightC.addFlights(data);
			return false;
		}
		/**listo**/
		else if(Pattern.matches(delAllFlExp, command)){
			flightC.deleteFlights();
			return false;
		}
		/**poner los metodos en Day**/
		else if(Pattern.matches(findRouteExp, command)){
			System.out.println("*matches find route command*");
			String[] res = command.split(" ");
			String source = res[1].split("=")[1];
			String target = res[2].split("=")[1];
			String p = res[3].split("=")[1];
			
			RoutePriority priority;
			
			if(p.equals(ft))
				priority = RoutePriority.TIME;
			else if (p.equals(pr))
				priority = RoutePriority.PRICE;
			else
				priority = RoutePriority.TOTALTIME;
				
			if(res.length == 5){
				String[] days = res[4].split("-");
				if(!checkDays(days))
				System.out.println("Ingreso dias repetidos");
				List<Day> newDays = getDays(days); 
				airportManager.getInstance().findRoute(source,target,priority,newDays);
			}else
				airportManager.getInstance.findRoute(source,target,priority,Day.getAllDays);
			return false;
		}
		else if(Pattern.matches(outputFormatExp, command)){
			System.out.println("*matches output format command*");
			return false;
		}
		else if(Pattern.matches(outputExp, command)){
			System.out.println("*matches output command*");
			return false;
		}
		else if(Pattern.matches(exitAndSaveExp, command)){
			System.out.println("*matches exit and save command*");
			return false;
		}
		/**listo**/
		else if(Pattern.matches(quitExp, command)){
			System.out.println("Saliendo....");
			return true;
		}
		else
			System.out.println("Ingreso un comando no valido");

		return false;
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

	public static boolean parseArguments(String[] args) throws ClassNotFoundException, IOException {
		AirportCreator airportC = new AirportCreator();
		FlightCreator flightC = new FlightCreator();
		FileManager f = new FileManager();
		
		if(args.length == 1) {
			if(args[0].equals("--delete-airports")) {
				airportC.deleteAirports();
			}
			else if(args[0].equals("--delete-flights")) {
				flightC.deleteFlights();
			}
			else {
				return false;
			}
		}
		else if(args.length == 3) {
			if(args[0].equals("--airport-file")) {
				if(args[2].equals("--append-airports")) {
					System.out.println("APPEND AIRPORTS FROM FILE " + args[1]);
					List<String> data = f.readAirports(args[1]);
					airportC.addAirports(data);
				}
				else if(args[2].equals("--replace-airports")) {
					System.out.println("REPLACE AIRPORTS FROM FILE " + args[1]);
				}
				else {
					return false;
				}
			}
			else if(args[0].equals("--flight-file")) {
				if(args[2].equals("--append-flights")) {
					System.out.println("APPEND FLIGHTS FROM FILE " + args[1]);
				}
				else if(args[2].equals("--replace-flights")) {
					System.out.println("REPLACE FLIGHTS FROM FILE " + args[1]);
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
		
		return true;
		
	}
	
}

