package DoAnCuoiKyJava.HeThongHoTroCuocThi.utils;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.OAuthService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuthService oAuthService;
    private final UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        //Cho phép truy cập những đường dẫn này mà không cần xác thực
                        .requestMatchers("/api/**","/css/**", "/jquery/**", "/js/**", "/images/**", "/imagesLogo/**", "/imagesLayoutUser/**", "/fonts/**", "/","/oauth/**", "/register", "/error", "/confirm", "/gmailForgotPassword", "/User/forgotPassword", "/confirmForgotPassword", "/forgotPassword")
                        .permitAll()

                        //Chỉ những người có vai trò "ADMIN" mới được phép truy cập
                        .requestMatchers("/LoaiTruongs", "/Truongs", "/MonThis", "/NoiDungs", "/PhieuKetQuas/add")
                        .hasAnyAuthority("ADMIN")

                        //Chỉ những người có vai trò "ADMIN", "USER", "OIDC_USER" mới được phép truy cập
                        .requestMatchers("/PhieuDangKys")
                        .hasAnyAuthority("ADMIN", "USER", "OIDC_USER")

                        //Chỉ những người có vai trò "ADMIN", "USER", "OIDC_USER" mới được phép truy cập
                        .requestMatchers("/api/**")
                        .hasAnyAuthority("ADMIN", "USER", "OIDC_USER","MANAGER")

                        //Tất cả các yêu cầu khác sẽ yêu cầu người dùng xác thực
                        .anyRequest().authenticated()
                ).logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                ).formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error")
                        .successHandler((request, response, authentication) -> {
                            // Kiểm tra vai trò của người dùng
                            if (authentication.getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN") ||
                                            grantedAuthority.getAuthority().equals("MANAGER"))) {
                                response.sendRedirect("/Admin");
                            } else if (authentication.getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN_SCHOOL"))) {
                                response.sendRedirect("/ADMIN_SCHOOL");
                            } else {
                                response.sendRedirect("/");
                            }
                        })
                        .permitAll()
                ).oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/login")
                                .failureUrl("/login?error")
                                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                        .userService(oAuthService)
                                )
                                .successHandler(
                                        (request, response, authentication) -> {
                                            var oidcUser = (DefaultOidcUser) authentication.getPrincipal();
                                            userService.saveOauthUser(oidcUser.getEmail(), oidcUser.getName());
                                            response.sendRedirect("/");
                                        }
                                )
                                .permitAll()
                ).rememberMe(rememberMe -> rememberMe
                        .key("hutech")
                        .rememberMeCookieName("hutech")
                        .tokenValiditySeconds(24 * 60 * 60)
                        .userDetailsService(userDetailsService())
                ).exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedPage("/403")
                ).sessionManagement(sessionManagement ->
                        sessionManagement
                                .maximumSessions(1)
                                .expiredUrl("/login")
                ).httpBasic(httpBasic -> httpBasic
                        .realmName("hutech")
                ).build();
    }
}