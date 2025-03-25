package vttp.batch_b.min_project.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.batch_b.min_project.server.exceptions.EmailNotFoundException;
import vttp.batch_b.min_project.server.models.User;
import vttp.batch_b.min_project.server.models.dtos.Country;
import vttp.batch_b.min_project.server.repository.LocationRepository;
import vttp.batch_b.min_project.server.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LocationRepository locatRepo;
    
    public Boolean createNewUser(User user) {
        return userRepo.createNewUser(user);
    }

    public Boolean isUserRegistered (String email) {
        Optional<User> opt = userRepo.findUserByEmail(email);
        return opt.isPresent();
    }

    public User getUserWithEmail(String email) {
        Optional<User> opt = userRepo.findUserByEmail(email);
        if (opt.isPresent()) {
            //user from db
            User dbUserInfo = opt.get();
            return dbUserInfo;
        }
        throw new EmailNotFoundException("Email not found: %s does not exists".formatted(email));
    }

    public List<Country> getAllCountries() {

        List<Country> countries = new ArrayList<>();
        locatRepo.getCountries().forEach(doc -> {
            Document currencyObj = (Document) doc.get("Currency");
            Country c = new Country(
                doc.getString("CountryName") + "," + doc.getString("City"),
                doc.getString("Timezone"),
                currencyObj.getString("code")
            );
            countries.add(c);
        });
        return  countries;
    }

}
