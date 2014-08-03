package simo.transport.backend;

import java.util.ArrayList;


public class MockInformationExtractor implements TransportDAO {

	@Override
	public ArrayList<String> getStations() {
	    ArrayList<String> mockStations = new ArrayList<String> () ;
	    
	    String[] values = new String[] {"Android", "iPhone", "WindowsMobile",
        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
        "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
        "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
        "Android", "iPhone", "WindowsMobile"};

	    for (int i = 0; i < values.length; i++) {
	    	mockStations.add(values[i]);
	    }
	    
		return mockStations;
	}

}
