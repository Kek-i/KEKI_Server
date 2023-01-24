package com.codepatissier.keki.user.service;

import com.codepatissier.keki.user.dto.NaverLogin;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaverService {

    @Value("${naver.client-id}")
    private String client_id;

    @Value("${naver.redirect_uri}")
    private String redirect_uri;

    @Value("${naver.client-secret}")
    private String client_secret;

    private final static String SESSION_STATE = "oauth_state";
    private final static String PROFILE_API_URL = "https://openapi.naver.com/v1/nid/me";

    public String getAuthorizationUrl(HttpSession session) {

        String state = UUID.randomUUID().toString();
        session.setAttribute(SESSION_STATE, state);

        OAuth20Service oauthService = new ServiceBuilder()
                .apiKey(client_id)
                .apiSecret(client_secret)
                .callback(redirect_uri)
                .build(NaverLogin.instance());

        return oauthService.getAuthorizationUrl();
    }

    public OAuth2AccessToken getAccessToken(HttpSession session, String code) throws IOException {

        OAuth20Service oauthService = new ServiceBuilder()
                .apiKey(client_id)
                .apiSecret(client_secret)
                .callback(redirect_uri)
                .build(NaverLogin.instance());

        OAuth2AccessToken accessToken = oauthService.getAccessToken(code);
        return accessToken;
    }

    public String getUserInfo(OAuth2AccessToken oauthToken) throws IOException, ParseException {
        OAuth20Service oauthService =new ServiceBuilder()
                .apiKey(client_id)
                .apiSecret(client_secret)
                .callback(redirect_uri).build(NaverLogin.instance());

        OAuthRequest request = new OAuthRequest(Verb.GET, PROFILE_API_URL, oauthService.getConfig());
        oauthService.signRequest(oauthToken, request);
        Response response = request.send();
        String apiResult = response.getBody();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj;

        jsonObj = (JSONObject) jsonParser.parse(apiResult);
        JSONObject response_obj = (JSONObject) jsonObj.get("response");

        String email = (String) response_obj.get("email");
        return email;
    }

}
