package com.momo.taskManagement.service.impl;

import com.momo.taskManagement.service.interfaces.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {


    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private final String ISSUER = "code_from_momo";
    private JwtServiceImpl() {
    }

    public Optional<String> getUsernameFromToken(String jwtToken) {

        var claimsOptional = parseToken(jwtToken);
        System.out.println(claimsOptional.map(Claims::getSubject));
        return claimsOptional.map(Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token, Claims::getExpiration);
    }


    public  <T> T getClaimsFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        System.out.println(claims);
        return claimResolver.apply(claims);
    }

    public  Claims getAllClaimsFromToken(String token) {
        return (Claims) Jwts.parser().verifyWith(secretKey).build().parseEncryptedClaims(token);

    }


    public  boolean validateToken(String jwtToken) {
        Optional<Claims> optionalClaims = parseToken(jwtToken);
        if (optionalClaims.isPresent()) {
            Claims claims = optionalClaims.get();
            if (!isTokenExpired(claims)) {
                return true;
            } else {
                System.out.println("Token has been expired......");
                return false;
            }
        }
        return false;
    }

    public  Optional<Claims> parseToken(String jwtToken) {
        var jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        try {
            return Optional.of(jwtParser.parseSignedClaims(jwtToken).getPayload());
        } catch (JwtException e) {
            log.error("Jwt Exception occurs");
        } catch (IllegalArgumentException e) {
            log.error("Illegal Argument error");
        }
        return Optional.empty();
    }

    //-----------------
    public boolean isTokenExpired(Claims token) {
        final Date expiration = token.getExpiration();
        return expiration.before(new Date());
    }


    public String generateToken(String username) {

        var currentDate = new Date();
        var jwtExpirationInMinutes = 2;
        var expiration = DateUtils.addMinutes(currentDate, jwtExpirationInMinutes);
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(ISSUER)
                .subject(username)
                .signWith(secretKey)
                .issuedAt(currentDate)
                .expiration(expiration)
                .compact();

    }

    public String generateRefreshToken(String username) {
        var currentDate = new Date();
        var jwtExpirationInHours = 2;
        var expiration = DateUtils.addHours(currentDate, jwtExpirationInHours);
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(ISSUER)
                .subject(username)
                .signWith(secretKey)
                .issuedAt(currentDate)
                .expiration(expiration)
                .compact();
    }

}
