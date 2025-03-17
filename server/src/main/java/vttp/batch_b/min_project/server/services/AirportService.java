package vttp.batch_b.min_project.server.services;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.batch_b.min_project.server.models.Airport;
import vttp.batch_b.min_project.server.repository.AirportRepository;

@Service
public class AirportService {
    @Autowired
    private AirportRepository airRepo;

    public void getDataWithAirport(String airport) {

        Document doc = airRepo.findAirportByName(airport);

        Airport air = new Airport();
        

    }




}

// public void insertAirports(List<Document> docs) {
//     mongoRepo.insertMany(docs);
// }

// public List<Document> getAirports(int limit, int offset) {
//     List<Airport> airports = airportRepo.getAllByBatch(limit, offset);

//     List<Document> docs = airports.stream()
//         .map(obj -> toDoc(obj))
//         .collect(Collectors.toList());
    
//     return docs;
// }


