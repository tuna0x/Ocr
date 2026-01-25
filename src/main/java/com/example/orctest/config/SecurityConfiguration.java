package com.example.orctest.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;


import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

import net.sourceforge.tess4j.Tesseract;
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        String [] whiteList={
            "/",
            "/api/v1/upload",
            "/api/v1/upload-async",
            "/api/v1/search",
            "/api/v1/auth/login"
        };
        http
        .securityMatcher(whiteList)
            .csrf(c-> c.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(
                authz ->  authz
                .requestMatchers(whiteList).permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/export/docx/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/export/txt/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/users/**").permitAll()
                .anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                // .authenticationEntryPoint(customAuthenticationEntryPoint))

            // .exceptionHandling(
            //         exceptions -> exceptions
            //                 .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()) //401
            //                 .accessDeniedHandler(new BearerTokenAccessDeniedHandler())) //403
            .formLogin(f->f.disable())
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            return http.build();
    }


        @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("permission");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }





    @Bean
    public Tesseract tesseract() {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("vie+eng");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
        return tesseract;
    }
}
