package uk.ac.ebi.spot.gwas.deposition.audit.service;

import uk.ac.ebi.spot.gwas.deposition.domain.User;

public interface UserService {

    User getUserDetails(String userId);

    User findUserDetailsUsingEmail(String email);
}
