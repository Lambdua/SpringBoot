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

        http.csrf().disable();
        http
                .formLogin()
                //同 login.html的 form action中指向的一样，只想
                .loginProcessingUrl("/login")
                .loginPage("/login.html")
                //默认username  见过滤器 UsernamePasswordAuthenticationFilter
                .usernameParameter("uname")
                //默认password
                .passwordParameter("pwd")
                /**
                 * 注意，下面的三个登录成功，和三个登录失败的处理方法，security只会执行最后定义的一个成功/失败处理方式。
                 * 根据定义的顺序进行覆盖。 三种适用于不同的场景.转发是服务器行为，重定向是客户端行为
                 * 1. default 内部跳转
                 * 2. forward 外部跳转 调用request.forward方法
                 * 3. handler 更具体的处理
                 */

                /**
                 * defaultSuccessUrl 是一个重载方法
                 * 假如我们还没有登录认证，在浏览器输入一个不存在的url，例如localhost:8080/test
                 * 那通过此前的配置security会帮我们导向登录页面，然后当我们登录成功后你会发现跳转的路径变成了/test，
                 * 而不是设置的/defaultSuccess。
                 * 使用第二个方法，并且第二参数置为true，那么就不会出现上面问题，会直接转到/main。
                 */
                //这里的是内部跳转，发送的是get请求，且可以获取 Authtication认证对象
                .defaultSuccessUrl("/defaultSuccess", true)
                //登录成功handler
                .successHandler((request, response, authentication) -> {
                    System.out.println("内部successHandler");
                    System.out.println(authentication.getPrincipal());
                    System.out.println("登录的用户事： " + authentication.getName());
                    System.out.println("内部successHandler");
                })
                //登录成功重定向url
                .successForwardUrl("/loginSuccess")
                .failureHandler((request, response, e) -> {
                    System.out.println("failureHandler ");
                    System.out.println("失败原因：" + e.getMessage());
                    System.out.println("failureHandler ");
                })
                .failureUrl("/defaultLoginFailure")
                .failureForwardUrl("/loginFailure")
        ;

        /**
         * security 的拦截器配置按照定义顺序进行拦截。
         * 一个请求过来时，按照执行顺序一次拦截，顺序中匹配到任何一个拦截器（无论是拦截，还是放行),后续的拦截器都不再执行。
         * 所以定义时，首先定义最精细的拦截器，将最宽泛的放在最后。
         */
        http.authorizeRequests()
                /*
                每一个参数是一个ant表达式
                通配符	说明
                ?	匹配任何单字符
                *	匹配0或者任意数量的字符
                **	匹配0或者更多的目录
                */
                .antMatchers("/login.html", "/loginError.html").permitAll()
                .antMatchers("/js/**","/css/**").permitAll()
                /*
                   正则匹配，匹配正则表达式
                 */
                //放行图片
                .regexMatchers(".+[.]png").permitAll()
//                .regexMatchers(HttpMethod.GET,".+[.]png").permitAll()
                .anyRequest().authenticated();
    }

}
