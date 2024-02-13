package openai.chatgptservice.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Slf4j
@Configuration
public class WebSecurityConfig {

    @Value("${openai.login.password}")
    private String password;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/css/**", "/js/**").permitAll() // CSS와 JS 경로에 대한 접근 허용
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                                formLogin
                                        .loginPage("/login")
                                        .permitAll()
                        // 여기를 수정하지 않음
                )
                .logout(logout ->
                        logout.permitAll()
                );

        // 로그인 성공 핸들러 설정
        http.formLogin().successHandler(successHandler());

        return http.build();
    }

    // 로그인 성공 후 리다이렉트를 처리할 핸들러
    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler successHandler() {
        var successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl("/"); // 기본 리다이렉트 URL 설정
        return successHandler;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        var user = User.builder()
                .username("user")
                .password(passwordEncoder.encode(password)) // 테스트용 간단한 패스워드
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}