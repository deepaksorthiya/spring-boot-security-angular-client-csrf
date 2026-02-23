package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.*;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Configuration
@EnableWebSecurity(/*debug = true*/)
public class WebAppSecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebAppSecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ServerProperties serverProperties) throws Exception {
        // https://docs.spring.io/spring-security/reference/5.8/migration/servlet/exploits.html#_i_am_using_angularjs_or_another_javascript_framework
        CookieCsrfTokenRepository tokenRepository = getCookieCsrfTokenRepository(serverProperties);
        // Use only the handle() method of XorCsrfTokenRequestAttributeHandler and the
        // default implementation of resolveCsrfTokenValue() from CsrfTokenRequestHandler
        CsrfTokenRequestHandler requestHandler = new SpaCsrfTokenRequestHandler();
        PathPatternRequestMatcher.Builder mvc = PathPatternRequestMatcher.withDefaults();
        http
                .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
                .formLogin(login -> login
                        .loginProcessingUrl("/api/login")
                        .successHandler((request, response, authentication) -> writeToResponse(response, HttpStatus.OK, authentication)) // Just return 200 instead of redirecting to '/'
                        .failureHandler(WebAppSecurityConfig::prepareResponse)
                ) // We will use form login to authenticate users from the Angular frontend, it's okay to use a Controller though
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler(new CsrfTokenAwareLogoutSuccessHandler(tokenRepository)) // Handler that generates and save a new CSRF token on logout
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(WebAppSecurityConfig::prepareResponse)
                        .accessDeniedHandler(WebAppSecurityConfig::prepareResponse)
                )
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // allow all js css files
                        .requestMatchers(mvc.matcher("/index.html"), mvc.matcher("/*.js"), mvc.matcher("/*.txt"), mvc.matcher("/*.json"), mvc.matcher("/*.map"), mvc.matcher("/*.css")).permitAll()
                        //allow all actuator endpoints and all static content
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations(), EndpointRequest.toAnyEndpoint()).permitAll()
                        // allow all preflight request
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/server-info", "/error", "/api/exception").permitAll()
                        .anyRequest().authenticated()
                )
                .anonymous(AbstractHttpConfigurer::disable)
                // cors for angular
                .cors(Customizer.withDefaults())
                // SPA CSRF Config
                .csrf(csrf -> csrf
                        .csrfTokenRepository(tokenRepository)
                        .csrfTokenRequestHandler(requestHandler)
                );

        return http.build();
    }

    private static CookieCsrfTokenRepository getCookieCsrfTokenRepository(ServerProperties serverProperties) {
        CookieCsrfTokenRepository cookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        cookieCsrfTokenRepository.setCookieCustomizer(cookie -> {
            // this settings should be change when host is changed other localhost
            // also will not work when backend and frontend running on different host
            cookie.sameSite(serverProperties.getServlet().getSession().getCookie().getSameSite().attributeValue());
            // secure cookie only works with localhost and https
            cookie.secure(serverProperties.getServlet().getSession().getCookie().getSecure());
            // setting twice as issue was in old browser
            cookie.httpOnly(false);
        });
        return cookieCsrfTokenRepository;
    }

    static final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
        private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
        private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
            /*
             * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection of
             * the CsrfToken when it is rendered in the response body.
             */
            this.xor.handle(request, response, csrfToken);
            /*
             * Render the token value to a cookie by causing the deferred token to be loaded.
             */
            csrfToken.get();
        }

        @Override
        public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
            String headerValue = request.getHeader(csrfToken.getHeaderName());
            /*
             * If the request contains a request header, use CsrfTokenRequestAttributeHandler
             * to resolve the CsrfToken. This applies when a single-page application includes
             * the header value automatically, which was obtained via a cookie containing the
             * raw CsrfToken.
             *
             * In all other cases (e.g. if the request contains a request parameter), use
             * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
             * when a server-side rendered form includes the _csrf request parameter as a
             * hidden input.
             */
            return (StringUtils.hasText(headerValue) ? this.plain : this.xor).resolveCsrfTokenValue(request, csrfToken);
        }
    }

    private static void prepareResponse(HttpServletRequest request, HttpServletResponse response, Exception authException) throws IOException {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setType(URI.create(request.getRequestURI()));
        problemDetail.setTitle(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setDetail(authException.getMessage());

        writeToResponse(response, HttpStatus.UNAUTHORIZED, problemDetail);
    }

    private static void writeToResponse(HttpServletResponse response, HttpStatus httpStatus, Object object) throws IOException {
        LOGGER.info("calling writeToResponse with status {} and object {}", httpStatus, object);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());

        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(object));
        writer.flush();
        writer.close();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN", "USER")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    // cors for angular
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(List.of(CorsConfiguration.ALL));
        config.setAllowedMethods(List.of(CorsConfiguration.ALL));
        config.setExposedHeaders(List.of(CorsConfiguration.ALL));
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost",
                "http://localhost:4200",
                "http://localhost:8080",
                "https://render.com",
                "https://spring-angular-csrf-frontend.onrender.com",
                "https://spring-ren-prod-angular-nested-routing.onrender.com"
        ));
        //config.setAllowedOriginPatterns(List.of(CorsConfiguration.ALL));
        config.setAllowCredentials(true); // This is important since we are using session cookies
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    //required to expose actuator/auditevents endpoint
    @Bean
    public AuditEventRepository auditEventRepository() {
        return new InMemoryAuditEventRepository();
    }

}
