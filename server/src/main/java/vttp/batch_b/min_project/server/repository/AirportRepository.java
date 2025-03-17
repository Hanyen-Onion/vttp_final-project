package vttp.batch_b.min_project.server.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;



@Repository
public class AirportRepository {
    
    @Autowired
    private MongoTemplate template;

    // db.timezones.aggregate([
    //     {   $lookup:  {
    //             from:'airports',
    //             foreignField:'city',
    //             localField:'City',
    //             as: 'airport',
    //             pipeline: [ { $project: {iata:1, _id:0, name:1}} ]
    //     }},
    //     {   $lookup: {
    //             from:'countries',
    //             foreignField:'isoAlpha3',
    //             localField:'IsoAlpha3',
    //             as: 'currency',
    //             pipeline: [ { $project: {'currency.code':1, _id:0}}]
    //     }},
    //     {   $match: {'airports.name':{   $regex:'changi', $options:'i'}}}
    // ])
    public Document findAirportByName(String airport) {
        
        ProjectionOperation projectAirport = Aggregation.project("iata","name")
            .andExclude("_id");

        LookupOperation lookupAirports = LookupOperation.newLookup()
            .from("airports").localField("City").foreignField("city")
            .pipeline(projectAirport)
            .as("airport");

        ProjectionOperation projectCountry = Aggregation.project("currency.code")
            .andExclude("_id");

        LookupOperation lookupCountries = LookupOperation.newLookup()
            .from("countries").localField("IsoAlpha3").foreignField("isoAlpha3")
            .pipeline(projectCountry)
            .as("currency");

        MatchOperation matchAirport = Aggregation.match(
            Criteria.where("airports.name").regex(airport,"i")
        );

        Aggregation pipeline = Aggregation.newAggregation(lookupAirports, lookupCountries, matchAirport);

        AggregationResults<Document> results = template.aggregate(pipeline, "timezones",Document.class);

        return results.getMappedResults().get(0);
    }

    //db.airports.inserMany({})
    public void insertMany(List<Document> airports) {
        //Collection<Document> newDocs = 
        template.insert(airports, "airports");
    }

    // public List<Airport> getAllByBatch(int limit, int offset) {
    //     List<Airport> airports = template.query(
    //         SQL_SELECT_AIRPORTS, 
    //         (rs, rowNum) -> {
    //             Airport air = new Airport();
    //             air.setName(rs.getString("name"));
    //             air.setCountry(rs.getString("country"));
    //             air.setCity(rs.getString("city"));
    //             air.setIata(rs.getString("iata_code"));
    //             air.setIcao(rs.getString("icao_code"));
    //             air.setLat(rs.getDouble("lat_decimal"));
    //             air.setLon(rs.getDouble("lon_decimal"));
    //             System.out.println(">>>sql count: " + rowNum);
    //             return air;
    //         },
    //         limit, offset);

    //     return (Collections.unmodifiableList(airports));
    // }

    

}
