package com.cibertec.backend.config.security;


import com.cibertec.backend.utils.constants.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JWTUtils {


    public String generateAccessToken(String mail, List<String> roles) throws Exception{

        Claims claims = Jwts.claims().setSubject(mail);
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(mail)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRATION_JWT))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }


    public boolean isJWTValid(String token) {
        try {
            PublicKey publicKey = getPublicKey();
//            System.out.println("VALID....");
//            System.out.println(token);
//            System.out.println(publicKey);
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token); //
            return true;
        } catch (Exception e) {
            System.out.println("error.");
            System.out.println(e.getMessage());
            return false;
        }
    }


    public static PrivateKey getPrivateKey() throws Exception {

        //String key = new String(Files.readAllBytes(Paths.get(Constants.RSA_PRIVATE_KEY_FILENAME)));

        try {
            ClassLoader classLoader = Constants.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(Constants.RSA_PRIVATE_KEY_FILENAME);

            if (inputStream == null) {
                throw new RuntimeException("No se pudo encontrar la clave privada: " + Constants.RSA_PRIVATE_KEY_FILENAME);
            }

            String key = new String(inputStream.readAllBytes());

            String privateKeyPEM = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePrivate(keySpec);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la clave privada", e);
        }
    }


    public PublicKey getPublicKey() throws Exception {

        //String rsaPublicKey = new String(Files.readAllBytes(Paths.get(Constants.RSA_PUBLIC_KEY_FILENAME)));

        try {
            ClassLoader classLoader = Constants.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(Constants.RSA_PUBLIC_KEY_FILENAME);

            if (inputStream == null) {
                throw new RuntimeException("No se pudo encontrar la clave publica: " + Constants.RSA_PUBLIC_KEY_FILENAME);
            }

            String rsaPublicKey = new String(inputStream.readAllBytes());

        rsaPublicKey = rsaPublicKey.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(rsaPublicKey);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la clave publica", e);
        }

    }




    public <T> T getClaim(String token, Function<Claims,T> claimsTFunction) throws Exception{
        Claims claims=extractAllclaims(token);
        return claimsTFunction.apply(claims);
    }

    public String getUserNameFromToken(String token) throws Exception{
        return getClaim(token, Claims::getSubject);
    }

    public Claims extractAllclaims(String token) throws Exception{
        return Jwts.parserBuilder()
                .setSigningKey(getPrivateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
