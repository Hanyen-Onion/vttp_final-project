package vttp.batch_b.min_project.server.repository;

import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepository {
    
    @Autowired
    private MongoTemplate template;

    // db.airports.aggregate([
    //     {   $match: {   "name": { $regex: new RegExp(".*" + 'changi' + ".*", "i")}}},
    //     { $project: {   _id:0, iata:1}}
    // ])
    public List<Document> findAirportByName(String airport) {

        Pattern pattern = Pattern.compile(".*" + airport + ".*", Pattern.CASE_INSENSITIVE);

        MatchOperation matchAirport = Aggregation.match(
            Criteria.where("name").regex(pattern)
        );
        ProjectionOperation projectAirport = Aggregation.project("iata")
        .andExclude("_id");

        Aggregation pipeline = Aggregation.newAggregation(
            matchAirport,
            projectAirport
            );

        AggregationResults<Document> results = template.aggregate(pipeline, "airports",Document.class);

        return results.getMappedResults();
    }

    // db.timezones.aggregate([
    //     { $lookup: {
    //         from:'countries',
    //         foreignField:'isoAlpha2',
    //         localField:'IsoAlpha2',
    //         as: 'Currency',
    //         pipeline: [{ $project: {    _id:0, 'currency.code':1,}}]
    //     }},
    //     { $project: {   _id:0, City:1, CountryName:1, Timezone:1, Currency:1}},
    //     { $unwind: '$Currency'}
    // ])
    public List<Document> getCountries() {
        ProjectionOperation projectCountry = Aggregation.project("currency.code")
        .andExclude("_id");

        LookupOperation lookupAirports = LookupOperation.newLookup()
        .from("countries").localField("IsoAlpha2").foreignField("isoAlpha2")
        .pipeline(projectCountry)
        .as("Currency");

        ProjectionOperation project = Aggregation.project("City", "CountryName", "Timezone", "Currency")
        .andExclude("_id");

        AggregationOperation unwindCurrency = Aggregation.unwind("Currency");

        Aggregation pipeline = Aggregation.newAggregation(lookupAirports, project, unwindCurrency);

        AggregationResults<Document> results = template.aggregate(pipeline, "timezones",Document.class);

        return results.getMappedResults();
    }
    

}
