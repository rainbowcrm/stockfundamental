package com.primus.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
public class SecurityAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    DefaultAuthenticationProvider defaultAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider((AuthenticationProvider)defaultAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
          http.authorizeRequests().antMatchers("/scripts/support.js").permitAll().antMatchers("/assets/background.jpg").permitAll()
                  .antMatchers(("/user/createUser")).permitAll().antMatchers(("/user/saveOTP")).permitAll()
                  .anyRequest().authenticated().and().formLogin().and().httpBasic().authenticationEntryPoint(new NoPopupBasicAuthenticationEntryPoint()).and().headers().frameOptions().sameOrigin().and().csrf()
                 .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().formLogin().loginPage("/bologin.html").failureUrl("/bologin.html?error=true")
                  .loginProcessingUrl("/bologin.html").permitAll();
          /*
          .failureHandler(new AuthenticationFailureHandler() {
              @Override
              public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                  UrlPathHelper urlPathHelper =new UrlPathHelper();
                  String contextPath = urlPathHelper.getContextPath(request);
                  response.sendRedirect(contextPath + "/bologin.html?error=true");
              }
          });
           */


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("https://maxcdn.bootstrapcdn.com/**","https://stackpath.bootstrapcdn.com/**",
                "**/scripts/support.js","https://cdnjs.cloudflare.com/**","https://fonts.googleapis.com/**",
                "https://ajax.googleapis.com/**");
    }
}
