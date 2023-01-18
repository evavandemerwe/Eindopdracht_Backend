package nl.novi.breedsoft.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import nl.novi.breedsoft.service.BreedSoftUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private RSAKey rsaKey;

    /**
     * A method that returns a password encoder that uses the BCrypt hashing function
     * @return BCryptPasswordEncoder instance with default strength of 10
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * A method that returns an instance of the BreedSoft user details service for managing user security
     * @return BreedSoft UserDetailsService
     */
    @Bean
    UserDetailsService breedsoftUserDetailsService() {
        return new BreedSoftUserDetailsService();
    }

    /**
     * A method that creates a security filter chain configuration to be applied on URL path regarding token requests.
     * Highest order security filter chain, first to be applied
     * @param http HttpSecurity chain to which this chain will be added
     * @return SecurityFilterChain
     * @throws Exception can be thrown on configuring the filter chain
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public SecurityFilterChain tokenSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/token")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/token").permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // CSRF disabled because we are working with stateless sessions
                .csrf(csrf -> csrf.disable())
                // To get the token from token endpoint
                .httpBasic(withDefaults())
                .build();
    }

    /**
     * A method that creates a security filter chain configuration to be applied on all URL paths order than token
     * @param http HttpSecurity chain to which this chain will be added
     * @return SecurityFilterChain
     * @throws Exception can be thrown on configuring the filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF disabled because we are working with stateless sessions
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    /**
     * A method that retunrs a JSON Web Key source that is used to sign a token on the Authorization server
     * @return JWKSource
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }

    /**
     * A method that returns a JwtEncoder used of encoding a JSON Web token
     * @param jwks Encoder parameters containing JWS-headers and JWS-claims
     * @return JwtEncoder
     */
    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwks){
        return new NimbusJwtEncoder(jwks);
    }

    /**
     * A method that returns a JwtDecoder used of decoding a JSON Web token
     * @return JwtDecoder
     */
    @Bean
    JwtDecoder jwtDecoder() throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    // Is used to filter the user data from the JWT Token
    // we filter the roles from the user in JWT the token

    /**
     * A method that returns an JwtAuthenticationConverter to convert raw JSON Web tokens
     * into authentication token.
     * @return JwtAuthenticationConverter
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
