package com.codepatissier.keki.user.service;

import java.util.Date;


import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.user.dto.PostUserRes;
import io.jsonwebtoken.*;


import com.codepatissier.keki.user.entity.User;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.codepatissier.keki.common.BaseResponseStatus.NULL_TOKEN;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final int accessTokenExpiryDate = 3600000;
    private final int refreshTokenExpiryDate = 604800000;

    @Value("${auth.key}")
    private String key;

    public Long getUserIdx() throws BaseException{
        Jws<Claims> claims = getClaims();
        return claims.getBody().get("userIdx", Long.class);
    }

    public Jws<Claims> getClaims() throws BaseException{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        if(token == null) throw new BaseException(NULL_TOKEN);
        token = token.replaceAll("^Bearer( )*", "");

        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException expiredJwtException) {
            System.out.println(expiredJwtException);
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return claims;
    }

    public PostUserRes createToken(User user) {
        String accessToken = createAccessToken(user.getUserIdx());
        String refreshToken = createRefreshToken(user.getUserIdx());
        return new PostUserRes(accessToken, refreshToken, user.getRole());
    }

    public String createRefreshToken(Long userIdx) {
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + refreshTokenExpiryDate))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        return refreshToken;
    }

    public String createAccessToken(Long userIdx) {
        Date now = new Date();
        String accessToken = Jwts.builder()
                .claim("userIdx", userIdx)
                .setSubject(userIdx.toString())
                .setExpiration(new Date(now.getTime() + accessTokenExpiryDate))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        return accessToken;
    }

}