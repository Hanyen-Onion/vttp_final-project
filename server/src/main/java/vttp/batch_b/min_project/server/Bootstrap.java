package vttp.batch_b.min_project.server;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import vttp.batch_b.min_project.server.models.dtos.FlightQuery;
import vttp.batch_b.min_project.server.services.FlightService;
import vttp.batch_b.min_project.server.services.AuthService;

@Component
public class Bootstrap implements CommandLineRunner {

    @Autowired
    private FlightService airportSvc;

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

        FlightQuery query = new FlightQuery(
            "TPE",
            "SIN",
            "2025-03-26",
            "2025-04-01",
            "ECONOMY",
            "round-trip",
            1,
            "Asia/Dhaka",
            "=BDT"
        );

        try {
            File file = new File("../data/test.json");
            //airportSvc.getflightData(file, query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     



        
    

        
}
