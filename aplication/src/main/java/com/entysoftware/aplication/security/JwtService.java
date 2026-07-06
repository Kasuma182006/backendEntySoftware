package com.entysoftware.aplication.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final long EXPIRATION_MS = 1000L * 60 * 60 * 24;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey obtenerKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generarToken(String identificacion, String rol) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .subject(identificacion)
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(obtenerKey())
                .compact();
    }

    public Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(obtenerKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extraerIdentificacion(String token) {
        return extraerClaims(token).getSubject();
    }

    public String extraerRol(String token) {
        return extraerClaims(token).get("rol", String.class);
    }
}
