package com.codepatissier.keki.user.service;

import com.codepatissier.keki.user.dto.GoogleLogin;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


@Service
@RequiredArgsConstructor
public class GoogleService {

    @Value("${google.client-id}")
    private String client_id;

    @Value("${google.redirect_uri}")
    private String redirect_uri;

    @Value("${google.client-secret}")
    private String client_secret;

    private final static String USER_INFO_URL = "https://www.googleapis.com/userinfo/v2/me?access_token=";
    private final static String ACCESS_TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token";

    public String getAuthorizationUrl(HttpSession session) {
        OAuth20Service oauthService = new ServiceBuilder()
                .apiKey(client_id)
                .apiSecret(client_secret)
                .callback(redirect_uri)
                .scope("email")
                .build(GoogleLogin.instance());

        return oauthService.getAuthorizationUrl();
    }

    public String getAccessToken(HttpSession session, String authorize_code) throws IOException {
        String access_Token = "";

        try {
            URL url = new URL(ACCESS_TOKEN_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");

            sb.append("&client_id="+client_id);
            sb.append("&client_secret="+client_secret);
            sb.append("&redirect_uri="+redirect_uri);
            sb.append("&code="+authorize_code);
            bw.write(sb.toString());
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return access_Token;
    }

    public String getUserInfo(String access_Token) throws IOException, ParseException {
        String reqURL = USER_INFO_URL+access_Token;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            String email = element.getAsJsonObject().get("email").getAsString();
            return email;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}