/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.products.qotdp.rest;

import com.blazartech.products.blazarusermanagement.tokenutil.JwtTokenUtil;
import com.blazartech.products.blazarusermanagement.tokenutil.JwtTokenUtilImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * a mock implementation of the token utilities
 * @author scott
 */
public class TestJwtTokenUtil implements JwtTokenUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtilImpl.class);

    @Value("${blazartech.jwt.expiry:0}")
    public long tokenExpiry;

    private String getSecret() {
        return "IAmaSecret";
    }

    //retrieve username from jwt token
    @Override
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    @Override
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(getSecret()).parseClaimsJws(token).getBody();
    }

    //check if the token has expired, which for these mock purposes will be false
    private Boolean isTokenExpired(String token) {
        logger.info("checking for expiration");
        return false;
    }

    //generate token for user
    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String[] authorities = new String[userDetails.getAuthorities().size()];
        userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList())
                .toArray(authorities);
        claims.put("roles", authorities);
        return doGenerateToken(claims, userDetails.getUsername());
    }
    
    @Override
    public Collection<String> getRoles(String token) {
        return Arrays.asList("ROLE_QUOTE_OF_THE_DAY_USER");
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string 
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiry * 1000))
                .signWith(SignatureAlgorithm.HS512, getSecret()).compact();
    }

    //validate token
    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    private static final String BEARER_HEADER = "Bearer ";
    
    @Override
    public String getToken(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");

        String jwtToken = null;

        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER_HEADER)) {
            jwtToken = requestTokenHeader.substring(BEARER_HEADER.length());
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        
        return jwtToken;
    }
}
