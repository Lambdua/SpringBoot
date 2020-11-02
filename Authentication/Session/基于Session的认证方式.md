# 基于Session的认证方式

## 认证流程
基于Session认证方式的流程是，用户认证成功后，在服务端生成用户相关的数据保存在session(当前会话)，而发 给客户端的  sesssion_id 存放到  cookie 中，这样用客户端请求时带上  session_id 就可以验证服务器端是否存在 session 数据，以此完成用户的合法校验。当用户退出系统或session过期销毁时,客户端的session_id也就无效了。 下图是session认证方式的流程图：

![image-20201027224307281](..\img\session流程图)

基于Session的认证机制由Servlet规范定制，Servlet容器已实现，用户通过HttpSession的操作方法即可实现。

## 创建基础工程

本案例工程使用maven进行构建，使用SpringMVC、Servlet3.0实现。

### 创建maven工程

**maven依赖**

```xml
 <parent>
        <artifactId>Authentication</artifactId>
        <groupId>com.lt</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>Session</artifactId>
    <packaging>war</packaging>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>

    <build>
        <finalName>Session</finalName>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>org.apache.tomcat.maven</groupId>
                    <artifactId>tomcat7-maven-plugin</artifactId>
                    <version>2.2</version>
                </plugin>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <configuration>
                        <source>1.8</source>
                        <target>1.8</target>
                    </configuration>
                </plugin>

                <plugin>
                    <artifactId>maven-resources-plugin</artifactId>
                    <configuration>
                        <encoding>utf-8</encoding>
                        <useDefaultDelimiters>true</useDefaultDelimiters>
                        <resources>
                            <resource>
                                <directory>src/main/resources</directory>
                                <filtering>true</filtering>
                                <includes>
                                    <include>**/*</include>
                                </includes>
                            </resource>
                            <resource>
                                <directory>src/main/java</directory>
                                <includes>
                                    <include>**/*.xml</include>
                                </includes>
                            </resource>
                        </resources>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
```

### spring容器配置

在conﬁg包下定义ApplicationConﬁg.java，它对应web.xml中ContextLoaderListener的配置

```java
/**
 *
 * @author 梁先生
 * @Date 2020/10/27
 * 再此配置除了Controller的其它bean,比如：数据库链接池，事务管理、业务bean等
 **/
@Configurable
@ComponentScan(
        basePackages = "com.lt",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = Controller.class)}
)
public class ApplicationConfig {
}
```

### ServletContext设置

本案例采用Servlet3.0无web.xml方式，在conﬁg包下定义WebConﬁg.java，它对应于DispatcherServlet配置。

```java
/**
 * 相当于springMvc.xml文件
 * @author 梁先生
 * @Date 2020/10/28
 **/
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.lt",
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)})
public class WebConfig implements WebMvcConfigurer {

    /**
     * @description  视图解析器
     * @author liangtao
     * @date 2020/10/28
     * @param
     * @return org.springframework.web.servlet.view.InternalResourceViewResolver
     **/
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/view/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
    /**
    * 在WebConﬁg中新增如下配置，将/直接导向login.jsp页面：
    **/
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
    }
}
```



### 加载 Spring容器

在init包下定义Spring容器初始化类SpringApplicationInitializer，此类实现WebApplicationInitializer接口， Spring容器启动时加载WebApplicationInitializer接口的所有实现类。

```java
/**
 * @author 梁先生
 * @Date 2020/10/28
 **/
public class SpringApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{ApplicationConfig.class};
    }
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

SpringApplicationInitializer相当于web.xml，使用了servlet3.0开发则不需要再定义web.xml。

ApplicationConﬁg.class对应以下配置的application-context.xml，WebConﬁg.class对应以下配置的spring- mvc.xml，web.xml的内容参考：

```xml
<web‐app>
    <listener>
        <listener‐class>
        	org.springframework.web.context.ContextLoaderListener
        </listener‐class>
    </listener>
    <context‐param>
        <param‐name>contextConfigLocation</param‐name>
        <param‐value>/WEB‐INF/application‐context.xml</param‐value>
    </context‐param>
    <servlet>
        <servlet‐name>springmvc</servlet‐name>
        <servlet‐class>
            org.springframework.web.servlet.DispatcherServlet
        </servlet‐class>
        <init‐param>
            <param‐name>contextConfigLocation</param‐name>
            <param‐value>/WEB‐INF/spring‐mvc.xml</param‐value>
        </init‐param>
        <load‐on‐startup>1</load‐on‐startup>
    </servlet>
    <servlet‐mapping>
        <servlet‐name>springmvc</servlet‐name>
        <url‐pattern>/</url‐pattern>
    </servlet‐mapping>
</web‐app>
```

### 认证页面

在webapp/WEB-INF/view下定义认证页面login.jsp，本案例只是测试认证流程，页面没有添加css样式，页面实 现可填入用户名，密码，触发登录将提交表单信息至/login，内容如下：

```jsp
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<html>
<head>
    <title>用户登录</title>
</head>
<body>
<form action="login" method="post">
    用户名：<input type="text" name="username"><br>
    密&nbsp;&nbsp;&nbsp;码:
    <input type="password" name="password"><br>
    <input type="submit" value="登录">
</form>
</body>
</html>
```

### maven启动

使用maven命令启动项目

```shel
mvn clean tomcat7:run
```

访问：`http://localhost:8080/Session/`

![image-20201028224751669](F:\ownCode\SpringBootAll\Authentication\img\session登录界面)

## 认证功能实现

### 1. 认证接口

用户进入认证页面，输入账号和密码，点击登录，请求/login进行身份认证。

### 2. 定义认证实现
用于对传来的用户名、密码校验，若成功则返回该用户的详细信息，否则抛出错误异

```java
/**
 * 认证服务,不使用接口+实现类的方式，因为觉得不是很有必要
 *
 * @author 梁先生
 * @Date 2020/10/28
 **/
@Service
public class AuthenticationService {
    /**
     * 用户信息
     */
    private static final Map<String, UserDto> USER_MAP = MapUtil.newHashMap(2);
    static {
        Set<String> authorities1 = new HashSet<>();
        //这个p1我们人为让它和/r/r1对应
        authorities1.add("p1");
        Set<String> authorities2 = new HashSet<>();
        //这个p2我们人为让它和/r/r2对应
        authorities2.add("p2");
        USER_MAP.put("zhangsan", new UserDto("1010", "zhangsan", "123", "张三", "133443", authorities1));
        USER_MAP.put("lisi", new UserDto("1011", "lisi", "456", "李四", "144553", authorities2));
    }

    /**
     * 用户认证，校验用户信息身份是否合法,这里模拟了两个用户，来认证
     *
     * @param authenticationRequest 用户认证请求 包含用户名和密码
     * @return com.lt.model.UserDto 认证成功的信息
     * @author liangtao
     * @date 2020/10/28
     **/
    public UserDto authentication(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest == null
                || StrUtil.isEmpty(authenticationRequest.getUsername())
                || StrUtil.isEmpty(authenticationRequest.getPassword())) {
            throw new RuntimeException("认证参数为空");
        }
        //根据用户名去查询数据库,这里模拟
        UserDto userDto = getUserDtoByUserName(authenticationRequest.getUsername());
        if (userDto == null || !authenticationRequest.getPassword().equals(userDto.getPassword())) {
            throw new RuntimeException("账号或密码错误");
        }
        return userDto;
    }

    /**
     * 根据username获取用户DTO
     *
     * @param userName 用户名
     * @return com.lt.model.UserDto 用户信息
     * @author liangtao
     * @date 2020/10/28
     **/
    private UserDto getUserDtoByUserName(String userName) {
        return USER_MAP.get(userName);
    }
}
```

### 3.  认证信息类，响应信息简单样例

```java
/**
 * @author liangtao
 * @date 2020/10/28
 **/
@Data
public class AuthenticationRequest {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}

/**
 * @author liangtao
 * @date 2020/10/28
 **/
@Data
@AllArgsConstructor
public class UserDto {
    public static final String SESSION_USER_KEY = "_user";
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String mobile;
    /**
     * 用户权限
     */
    private Set<String> authorities;
}

```

### 4. 登录Controller
对/login请求处理,它调用AuthenticationService完成认证并返回登录结果提示信息

```java
/**
 * @author 梁先生
 * @description 用户登录Controller
 * @Date 2020/10/28
 **/
@RestController
public class LoginController {

    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(value = "/login", produces = "text/plain;charset=utf-8")
    public String login(AuthenticationRequest authenticationRequest, HttpSession session) {
        UserDto userDto = authenticationService.authentication(authenticationRequest);
        //存入session
        session.setAttribute(UserDto.SESSION_USER_KEY, userDto);
        return userDto.getUsername() + "登录成功";
    }

    @GetMapping(value = "/logout", produces = {"text/plain;charset=UTF-8"})
    public String logout(HttpSession session) {
        session.invalidate();
        return "退出成功";
    }
}

```

### 5. 启动测试

用户： 

- zhangsan/123
- lisi/456

到目前为止最基础的认证功能已经完成，它仅仅实现了对用户身份凭证的校验，若某用 户认证成功，只能说明他是该系统的一个合法用户，仅此而已。

## 会话功能实现

会话是指用户登入系统后，系统会记住该用户的登录状态，他可以在系统连续操作直到退出系统的过程。 认证的目的是对系统资源的保护，每次对资源的访问，系统必须得知道是谁在访问资源，才能对该请求进行合法性 拦截。因此，在认证成功后，一般会把认证成功的用户信息放入Session中，在后续的请求中，系统能够从Session 中获取到当前用户，用这样的方式来实现会话机制。

