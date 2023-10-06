package com.letsmove.config;

import com.letsmove.enums.Role;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Value("${spring.queries.users-query}")
    private String usersQuery;
    @Value("${spring.queries.roles-query}")
    private String roleQuery;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication().usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(roleQuery)
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                .antMatchers("/", "/login","/get_comments/{placeID}","/get_comments_tour/{tourID}", "/register", "/registration", "/change_password", "/forgotPassword", "/passwordRecoveryEmail", "/newPasswordUser", "/get_all_tour", "/get_all_place", "/get_all_hotel", "/get_all_cafe", "/get_all_market", "/get_all_attraction", "/get_all_shopping_center", "/get_all_state_institutions")
                .permitAll()
                .antMatchers("/manager_registration", "/manager_register", "/guides_registration", "/guides_register")
                .hasAuthority("USER")
                .antMatchers("/save_city", "/add_city", "/save_active_place", "/check_place", "/check_tour", "/save_active_tour", "/delete_all_tour", "/delete_all_place")
                .hasAuthority("ADMIN")
                .antMatchers("/save_place", "/add_place", "/delete_place", "/get_all_author_place")
                .hasAnyAuthority("MANAGER", "ADMIN")
                .antMatchers("/save_tour", "/add_tour", "/history_tour_for_guide", "/delete_tour", "/get_all_author_tour")
                .hasAuthority("GUIDE")
                .antMatchers("/history_tour_for_user").hasAnyAuthority("USER", "ADMIN", "MANAGER")
                .antMatchers("/look_tour","/save_place_comment", "/getTour",  "/look_place", "/change_rating", "/save_tour_comment")
                .authenticated()
                .anyRequest()
                .authenticated()
                .and().csrf().disable()
                .formLogin().successHandler(customizeAuthenticationSuccessHandler)
                .loginPage("/login").failureUrl("/login?error=true")
                .usernameParameter("login")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").and().exceptionHandling()
                .accessDeniedPage("/access-denied");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/templates/**", "/static/**", "registration");
    }
}