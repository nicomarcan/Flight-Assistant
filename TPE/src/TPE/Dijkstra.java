package TPE;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import TPE.AirportManager.Node;


/**
 * Algoritmo de Dijsktra con min-priority queue. Usa el PriorityQueue de java . El
 * priorityqueue es para guardar los vertices que aun no fueron visitados y tener listo el mas cercano para visitar 
 * proximo.
 * 
 * El algoritmo crea una instancia de Vertex para cada aeropuerto y luego le agrega a cada vertex su lista de vuelos 
 * salientes, asi cuando se para en cada vertex solo visita los vertex que tiene de destino.
 * 
 * Calcula la distancia hacia vertex vecinos una vez para cada vuelo saliente y le guarda a ese vertex vecino el vuelo de procedencia optimo que se uso para llegarle si hubo. Al final 
 * reconstruye el camino desde el ultimo vertex hacia atras usando los vuelos de procedencia que guardo en cada vertex.
 * 
 * 
 **/
public class Dijkstra {
	
	private RoutePriority priority;
	private Vertex startingVertex;
	private Vertex finalVertex;
	private Set<Flight> flights = new HashSet<Flight>();
	private Collection<Node> airports ;
	private Queue<Vertex> unvisitedVertexes = new PriorityQueue<Vertex>();
	private Map<Airport, Vertex> airportToVertex = new HashMap<Airport, Vertex>();

	AirportManager airportManager = AirportManager.getInstance();

	/**
	 * Se encarga de inicializar cada vertice, eliminando las multiaristas pues solo agrega los vuelos optimos
	 * hacia los destinos posibles.
	 * @param sourceAirport
	 * @param destinationAirport
	 * @param priority
	 * @param departureDays
	 */
	public Dijkstra(Airport sourceAirport, Airport destinationAirport, RoutePriority priority,List<Day> departureDays) {
		this.priority = priority;
		this.flights = new HashSet<Flight>();
		this.airports = airportManager.getAirportsDijkstra(); 
		for(Node n : airports) {
			Vertex vertex = new Vertex(n.airport);
			if(n.airport.equals(sourceAirport)){
				List<Flight> f = bestFlights(n, departureDays, priority);/** si es el origen podria tener dias fijados**/
				vertex.addFlights(f);		
				flights.addAll(f);
			}
			else{
				List<Flight> f = bestFlights(n, Day.getAllDays(), priority);
				vertex.addFlights(f);		
				flights.addAll(f);	
			}
			airportToVertex.put(n.airport, vertex);
		}
		this.startingVertex = airportToVertex.get(sourceAirport);
		this.finalVertex = airportToVertex.get(destinationAirport);
	}
	
	

	public List<Flight> findRoute() {
		if(startingVertex.getFlights().size() == 0){
			return null;
		}
		clearMarks();
		updateDistance(startingVertex, (double) 0);
		unvisitedVertexes.offer(startingVertex);
		Vertex currentVertex = null;
		
		while(!unvisitedVertexes.isEmpty()) {
			currentVertex = unvisitedVertexes.poll();
			if(currentVertex.equals(finalVertex)) 
				return constructRouteToDestination(finalVertex);
			if(!currentVertex.visited){
				currentVertex.visited = true;
				for(Flight flight : currentVertex.getFlights()) {
					Vertex target = getDestinationVertex(flight);
					if(!target.visited){
						if(priority.equals(RoutePriority.PRICE)) {
							if(target.getTotalDistance() > currentVertex.getTotalDistance()+flight.getPrice()){	
								target.setSourceFlight(flight);
								updateDistance(target, currentVertex.getTotalDistance()+flight.getPrice());
							}
						} 
						else  {
							if(target.getTotalDistance() > currentVertex.getTotalDistance()+flight.getFlightTime()){	
								target.setSourceFlight(flight);
								updateDistance(target, currentVertex.getTotalDistance()+flight.getFlightTime());
							}
						}
					}									
				}
			}
			
			
			
		}
		if(finalVertex.getSourceFlight() == null) {
			return null; 
		}	
		return null;
	}
	
	private void clearMarks() {
		for(Vertex v : airportToVertex.values()){
			v.visited = false;
		}
		
	}



	/** construye el camino agarrando sourceflights desde el vertex final hacia atras **/
	public List<Flight> constructRouteToDestination(Vertex destination) {
		List<Flight> route = new LinkedList<Flight>();
		Vertex current = destination;
		
		while(current.getSourceFlight() != null) {
			route.add(0, current.getSourceFlight()); 
			current = getOriginVertex(current.getSourceFlight()); 
		}
		
		return route;
	}
	
	



	private Vertex getOriginVertex(Flight sourceFlight) {
		return airportToVertex.get(airportManager.getAirports().get(sourceFlight.getOrigin()).airport);
	}



	
	public void updateDistance(Vertex vertex, Double distance) {
		unvisitedVertexes.remove(vertex);
		vertex.setTotalDistance(distance);
		unvisitedVertexes.offer(vertex);
	}
	
	
	public Vertex getDestinationVertex(Flight flight) {
		return airportToVertex.get(airportManager.getAirports().get(flight.getTarget()).airport);
	}
	
	
	
	private Flight compareByTime(Flight bestOne, Flight current) {
		if(current == null)
			return bestOne;
		if(bestOne == null || current.getFlightTime() < bestOne.getFlightTime()){
			return current;
		}
		return bestOne;
	}

	private Flight compareByPrice(Flight bestOne, Flight current) {
		if(current == null)
			return bestOne;
		if(bestOne == null || current.getPrice() < bestOne.getPrice()){
			return current;
		}
		return bestOne;
	}
	
	/** devuelve los mejores vuelos a los distintos aeropuertos**/
	private List<Flight> bestFlights(Node airport,List<Day> days,RoutePriority priority){
		List<Flight> bestOnes = new LinkedList<Flight>();
		if(priority == RoutePriority.PRICE){
			for(Map<Day,TreeSet<Flight>> flights :airport.priceFlight.values()){
				Flight bestOne = null;
				for(int i = 0 ; i<days.size();i++){
					TreeSet<Flight> dayFlights = flights.get(days.get(i));
					if(dayFlights.size() != 0){/** podrian no haber vuelos ese dia**/
						Flight current = dayFlights.last();
						bestOne = compareByPrice(bestOne,current);
					}
				}
				if(bestOne != null)
					bestOnes.add(bestOne);
			}
		}else{
				for(Map<Day,TreeSet<Flight>> flights :airport.timeFlight.values()){
					Flight bestOne = null;
					for(int i = 0 ; i<days.size();i++){
						TreeSet<Flight> dayFlights = flights.get(days.get(i));
						if(dayFlights.size() != 0){/** podrian no haber vuelos ese dia**/
							Flight current = dayFlights.last();
							bestOne = compareByTime(bestOne,current);
						}
					}
					if(bestOne != null)
						bestOnes.add(bestOne);
			}
		}
		return bestOnes;
	}
	
	
	
}