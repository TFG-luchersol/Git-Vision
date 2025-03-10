package org.springframework.samples.gitvision.configuration.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${gitVision.app.jwt.secret}")
	private String jwtSecret;

	@Value("${gitVision.app.jwt.expiration}")
	private int jwtExpirationMs;

	public String generateJwtToken(Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		Map<String, Object> claims = new HashMap<>();
		claims.put("authorities",
				userPrincipal.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList()));
		claims.put("id", userPrincipal.getId());
		byte[] keyString = Decoders.BASE64.decode(jwtSecret);
		Key key = Keys.hmacShaKeyFor(keyString);
		return Jwts.builder()
					.claims()
					.add(claims)
					.subject(userPrincipal.getUsername())
					.issuedAt(new Date())
					.expiration(new Date((new Date()).getTime() + jwtExpirationMs))
					.and()
					.signWith(key)
					.compact();
					
	}

    public Claims getClaimsFromJwtToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        return Jwts.parser()
				.verifyWith(key)
				.build()  					
				.parseSignedClaims(token)
				.getPayload();
    }

	public String getUserNameFromJwtToken(String token) {
		Claims claims = getClaimsFromJwtToken(token);
		return claims.getSubject();
	}

	public Long getIdByAuthentication(Object authentication) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) ((Authentication) authentication).getPrincipal();
		return userDetailsImpl.getId();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));  
			Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
