package vttp.batch_b.min_project.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import vttp.batch_b.min_project.server.models.dtos.AirportQuery;
import vttp.batch_b.min_project.server.services.AirportService;
import vttp.batch_b.min_project.server.services.AuthService;

@Component
public class Bootstrap implements CommandLineRunner {

    @Autowired
    private AirportService airportSvc;

    @Autowired
    private AuthService authSvc;
    
    @Override
    public void run(String...args) {
        
        // int limit = 50;
        // for (int offset = 0; offset < 9301; offset += limit) {
        //     List<Document> airports = airportSvc.getAirports(limit, offset);
        //     System.out.println("Offset: " + offset + ", Batch size: " + airports.size());
        //     airportSvc.insertAirports(airports);
        // }

        AirportQuery query = new AirportQuery(
            "SIN",
            "TPE",
            "2025-03-23",
            "2025-03-29",
            "Economy",
            "one-way",
            1
        );

        //authSvc.getAmadeusAccessToken();
       airportSvc.getFlightOffer(query);


        
    }

        
}
