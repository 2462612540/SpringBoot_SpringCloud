package jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

public class ParseJwtTest {
    public static void main(String[] args) {
        try {
            Claims claims = Jwts.parser().setSigningKey("itcast")
                    .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLlsI_mmI4iLCJpYXQiOjE1OTA0Nzc0ODQsImV4cCI6MTU5MDQ3NzU0NCwicm9sZSI6ImFkbWluIn0.TiUG99A0DtRbWvpNS_49yQ_s5REAHdVZNb8ZEm73ggY")
                    .getBody();
            System.out.println("用户名id:"+ claims.getId());
            System.out.println("用户名:"+ claims.getSubject());
            System.out.println("登陆时间:"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
            System.out.println("过期时间:"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getExpiration()));
            System.out.println("role:"+ claims.get("role"));
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            System.out.println("已过期");
        }
    }
}
