package vttp.batch_b.min_project.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import vttp.batch_b.min_project.server.repository.AirportRepository;
import vttp.batch_b.min_project.server.services.AirportService;

@Component
public class Bootstrap implements CommandLineRunner {

    @Autowired
    private AirportService airportSvc;

    @Autowired
    private AirportRepository repo;
    
    @Override
    public void run(String...args) {
        
        // int limit = 50;

        // for (int offset = 0; offset < 9301; offset += limit) {
        //     List<Document> airports = airportSvc.getAirports(limit, offset);

        //     System.out.println("Offset: " + offset + ", Batch size: " + airports.size());
            
        //     airportSvc.insertAirports(airports);
        // }
            
    }

        
}
