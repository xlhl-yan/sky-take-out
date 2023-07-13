package com.xlhl.sky.config;

import com.xlhl.sky.interceptor.JwtTokenAdminInterceptor;
import com.xlhl.sky.interceptor.JwtTokenUserInterceptor;
import com.xlhl.sky.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;
import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Resource(name = "jwtTokenAdminInterceptor")
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Resource(name = "jwtTokenUserInterceptor")
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        //客户端登录校验
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        //客户端登录校验
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/shop/status", "/user/user/login");
    }


    /**
     * 通过knife4j生成接口文档
     *
     * @return
     */
    @Bean
    public Docket docketAdmin() {
        log.info("准备设置接口文档......");
        ApiInfo apiInfo = new ApiInfoBuilder()
                //项目名称
                .title("苍穹外卖项目接口文档")
                //版本
                .version("2.0")
                //标题
                .description("苍穹外卖项目接口文档")
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("管理端接口")
                .apiInfo(apiInfo)
                .select()
                //指定包扫描
                .apis(RequestHandlerSelectors.basePackage("com.xlhl.sky.controller.admin"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 通过knife4j生成接口文档
     *
     * @return
     */
    @Bean
    public Docket docketUser() {
        log.info("准备设置接口文档......");
        ApiInfo apiInfo = new ApiInfoBuilder()
                //项目名称
                .title("苍穹外卖项目接口文档")
                //版本
                .version("2.0")
                //标题
                .description("苍穹外卖项目接口文档")
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户端接口")
                .apiInfo(apiInfo)
                .select()
                //指定包扫描
                .apis(RequestHandlerSelectors.basePackage("com.xlhl.sky.controller.user"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 设置静态资源映射
     *
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("静态资源开始映射......");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 扩展SpringMVC的消息转换器
     *
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建一个消息转换器类
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //为消息转化器指定一个对象转换器，将JavaBean转换为JSON格式数据
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将自定义消息转换器注入容器中并且指定索引为0 / 优先级提升
        converters.add(0, messageConverter);
    }
}
