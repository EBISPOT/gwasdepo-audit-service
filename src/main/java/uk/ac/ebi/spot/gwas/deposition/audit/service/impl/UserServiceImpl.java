package uk.ac.ebi.spot.gwas.deposition.audit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.UserRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.service.UserService;
import uk.ac.ebi.spot.gwas.deposition.domain.User;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User getUserDetails(String userId) {
        return userRepository.findById(userId).orElse(null);
    }


    public User findUserDetails(String email) {
       return userRepository.findByEmailIgnoreCase(email).orElse(null);
    }
}
