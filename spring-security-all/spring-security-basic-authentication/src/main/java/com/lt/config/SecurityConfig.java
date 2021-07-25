package com.lt.config;

import com.lt.service.CustUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService createUserDetailsService() {
        return new CustUserDetailsService();
    }

    @Bean(name = "bcryptPasswordEncoder")
    public PasswordEncoder passwordEncoder1() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 这里我们使用一个空的密码编码器
     *
     * @return org.springframework.security.crypto.password.PasswordEncoder
     * @author liangtao
     * @date 2021/7/24
     **/
    @Bean(name = "planPasswordEncoder")
    public PasswordEncoder passwordEncoder2() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        };
    }

    /**
     * 设置多个认证管理器时，会按照顺序依次进行认证，直到有一个认证成功，就算认证通过，所有认证失败，返回401
     *
     * @param auth
     * @return void
     * @author liangtao
     * @date 2021/7/24
     **/
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                //第一个认证管理器，我们使用自定义用户信息获取service和bcrypt密码编码器
                .userDetailsService(createUserDetailsService()).passwordEncoder(passwordEncoder1())
                //设置用户密码管理器
                .and()
                //第二个认证管理器，我们使用内存模型，使用不编码的密码管理器
                .inMemoryAuthentication()
                //这里必须设置一个密码编码器，否则抛异常
                .passwordEncoder(passwordEncoder2())
                //$2a$10$2hXj92VlQ7fS3KBVWlTUpeHYijS5OQi7Wgo2heJvsBnBkB/DMynJ6
                .withUser("liangtao")
                .password("liangtaopwd").roles("USER");
        ;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/**").hasRole("USER").and().httpBasic();

        http.csrf().disable();
        http
                .formLogin()
                //同 login.html的 form action中指向的一样，只想
                .loginProcessingUrl("/login")
                .loginPage("/login.html")
                //这里的是内部跳转，发送的是get请求，且可以获取 Authtication认证对象
                /**
                 * defaultSuccessUrl 是一个重载方法
                 * 假如我们还没有登录认证，在浏览器输入一个不存在的url，例如localhost:8080/test
                 * 那通过此前的配置security会帮我们导向登录页面，然后当我们登录成功后你会发现跳转的路径变成了/test，
                 * 而不是设置的/defaultSuccess。
                 * 使用第二个方法，并且第二参数置为true，那么就不会出现上面问题，会直接转到/main。
                 */
                .defaultSuccessUrl("/defaultSuccess",true)
                //登录成功重定向url,这里配置了成功重定向url，就不会再走上面配置的默认 defaultSuccessUrl
                .successForwardUrl("/user/getUserList");
        http.authorizeRequests().antMatchers("/login.html").permitAll()
                .anyRequest().authenticated();


    }

}
