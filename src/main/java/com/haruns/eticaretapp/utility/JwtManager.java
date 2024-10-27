package com.haruns.eticaretapp.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class JwtManager {
	@Value("${java15.jwt.secret-key}")
	private String secretKey;
	@Value("${java15.jwt.issuer}")
	private String issuer;
	private final Long exDate=1000L*6000;
	
	public String createToken(String authId){
		Date createdDate=new Date(System.currentTimeMillis());
		Date expirationDate=new Date(System.currentTimeMillis()+exDate);
		Algorithm algorithm=Algorithm.HMAC512(secretKey);
		String token = JWT.create()
		                  .withAudience()
		                  .withIssuer(issuer)
		                  .withIssuedAt(createdDate)
		                  .withExpiresAt(expirationDate)
		                  .withClaim("authId", authId)
		                  .withClaim("key","JX_15_TJJJ")
		                  .sign(algorithm);
		return token;
	}
	
	public Optional<String> validateToken(String token){
		try{
			Algorithm algorithm=Algorithm.HMAC512(secretKey);
			JWTVerifier verifier=JWT.require(algorithm).build();
			DecodedJWT decodedJWT = verifier.verify(token);
			if (Objects.isNull(decodedJWT))
				return Optional.empty();
			String authId = decodedJWT.getClaim("authId").asString();
			return Optional.of(authId);
		}
		catch (Exception exception) {
			return Optional.empty();
		}
		
		
	}
}