package vttp.batch_b.min_project.server.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.batch_b.min_project.server.exceptions.EmailNotFoundException;
import vttp.batch_b.min_project.server.models.User;
import vttp.batch_b.min_project.server.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    
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

}
