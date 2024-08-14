package tutorin.com.service;

import tutorin.com.entities.JwtClaims;
import tutorin.com.model.User;

public interface JwtService {
    String generateToken(User user);
    boolean verifyJwtToken(String token);
    JwtClaims getClaimsByToken(String token);
}
