package io.apitoolkit.apitoolkitjava.filter;

import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

@Component
public class ApiToolKitInt implements WebRequestInterceptor {

    @Override
    public void preHandle(WebRequest request) throws Exception {
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        // TODO Auto-generated method stub
    }

}
