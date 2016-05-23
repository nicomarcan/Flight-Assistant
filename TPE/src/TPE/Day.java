package TPE;



public class Day {
	 	public static final Day MONDAY = new Day(0);
	    public static final Day TUESDAY = new Day(1);
	    public static final Day WEDNESDAY = new Day(2);
	    public static final Day THURSDAY = new Day(3);
	    public static final Day FRIDAY = new Day(4);
	    public static final Day SATURDAY = new Day(5);
	    public static final Day SUNDAY = new Day(6);
	    public static final int size = 7;
	    
	    private final int dayIndex;
	  
	    private static final Day days[] = {MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY};
	    
	    private Day (int index){
	    	dayIndex = index;
	    }
	    
	    public static Day getDay(int index){
	    	Day day = null;
	    	switch(index){
	    		case 0: day = MONDAY;
	    				break;
	    		case 1: day = TUESDAY;
	    				break;
	    		case 2: day =  WEDNESDAY;
	    				break;	
	    		case 3: day = THURSDAY;
	    				break;	
	    		case 4: day = FRIDAY;
	    				break;	
	    		case 5: day = SATURDAY;
	    				break;	
	    		case 6: day = SUNDAY;
	    				break;	
	    	}
	    	return day;
	    }
	    
	    
}


