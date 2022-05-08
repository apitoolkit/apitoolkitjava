package io.apitoolkit.apitoolkitjava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.apitoolkit.apitoolkitjava.filter.ApiToolKitInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final ApiToolKitInterceptor apiToolKitInterceptor;

  public WebConfig(ApiToolKitInterceptor apiToolKitInterceptor) {
    this.apiToolKitInterceptor = apiToolKitInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(apiToolKitInterceptor);
  }

}