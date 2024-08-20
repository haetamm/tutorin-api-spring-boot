package tutorin.com.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.JwtClaims;
import tutorin.com.model.User;
import tutorin.com.service.JwtService;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

   @Value("${tutorin_api.jwt.secret}")
   private String JWT_SECRET;

   @Value("${tutorin_api.jwt.issuer}")
   private String JWT_ISSUER;
   @Value("${tutorin_api.jwt.expiration}")
   private long JWT_EXPIRATION;


   @Transactional(rollbackFor = Exception.class)
   @Override
   public String generateToken(User user) {
       try {
           Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
           return JWT.create()
                   .withSubject(user.getId())
                   .withIssuedAt(Instant.now())
                   .withExpiresAt(Instant.now().plusSeconds(JWT_EXPIRATION))
                   .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                   .withIssuer(JWT_ISSUER)
                   .sign(algorithm);
       }catch (JWTCreationException e){
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, StatusMessages.ERROR_CREATING_JWT);
       }
   }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            algorithm.verify(JWT.require(algorithm).build().verify(parseToken(token)));
            return true;
        } catch (JWTCreationException e) {
            log.error("Invalid JWT Signature/Claims : {}", e.getMessage());
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JwtClaims getClaimsByToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(JWT_ISSUER).build().verify(parseToken(token));
            return JwtClaims.builder()
                    .userId(decodedJWT.getSubject())
                    .roles(decodedJWT.getClaim("roles").asList(String.class))
                    .build();
        } catch (JWTCreationException e){
            log.error("Invalid JWT Signature/Claims : {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, StatusMessages.ERROR_CREATING_JWT);
        }
    }

    private String parseToken(String bearerToken) {
        return bearerToken.replace("Bearer ", "");
    }
}
