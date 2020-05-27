package jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreatJwt {
    public static void main(String[] args) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("666")
                .setSubject("小明")//jwt所面向的用户
                .setIssuedAt(new Date())//jwt的签发时间
                .signWith(SignatureAlgorithm.HS256, "itcast")
                .setExpiration(new Date(new Date().getTime() + 60000))//过期时间
                .claim("role","admin");//自定义属性
        System.out.println(jwtBuilder.compact());
    }
}
