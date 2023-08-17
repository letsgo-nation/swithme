package com.example.swithme.jwt;

import com.example.swithme.entity.TokenBlacklist;
import com.example.swithme.repository.TokenBlacklistRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    // 헤더에 저장되는 key 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 토큰 권한 값의 key 값
    public static final String AUTHORIZATION_KEY = "auth";

    // 토큰 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;
    // application.properties 에서 전역변수를 가져와서 사용
    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct // 의존성 주입이 이뤄진 후에 실행되는 메서드에 사용한다.
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성 메서드
    public String createToken(String username) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
//        Cookie[] cookies = request.getCookies();
//        if(cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
//                    try {
//                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
//                    } catch (UnsupportedEncodingException e) {
//                        return null;
//                    }
//                }
//            }
//        }
//        return null;
    }


    // 토큰 검증
    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Token Error : 토큰 검증 실패");
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Token Error : 토큰 검증 실패");
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Token Error : 토큰 검증 실패");
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("Token Error : 토큰 검증 실패");
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

//    public String substringToken(String tokenValue) {
//        if(StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
//            return tokenValue.substring(7);
//        }
//        throw new NullPointerException("Not Found Token");
//    }
    public boolean isTokenBlacklisted(String tokenValue) {
        TokenBlacklist tokenBlacklist = tokenBlacklistRepository.findByToken(tokenValue).orElse(null);
        return tokenBlacklist != null;
    }
}