package com.axlero.security;

import com.axlero.repository.CustomerRepository;
import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtToken {



        private final SecretKey key;
        private final long jwtExpirationMs;
        private final CustomerRepository customerRepository;

        public JwtToken(
                @Value("${app.jwt.secret}") String secret,
                @Value("${app.jwt.expiration-ms}") long jwtExpirationMs, CustomerRepository customerRepository) {

            this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            this.jwtExpirationMs = jwtExpirationMs;
            this.customerRepository = customerRepository;
        }

        public String generateToken(String customerId, String email,String role){

            long now = System.currentTimeMillis();
            return Jwts.builder()
                    .subject(customerId)
                    .claim("email",email)
                    .claim("role",role)
                    .issuedAt(new Date(now))
                    .expiration(new Date(jwtExpirationMs))
                    .signWith(key)
                    .compact();
        }


        public Jws<Claims> validateToken(String token){
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

        }

        public String getCustomerIdFromToken(String token){

            Claims claims = validateToken(token).getBody();
            return claims.getSubject();
        }

}
