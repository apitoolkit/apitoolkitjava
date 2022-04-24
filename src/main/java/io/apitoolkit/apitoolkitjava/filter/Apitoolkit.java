package io.apitoolkit.apitoolkitjava.filter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

// https://stackoverflow.com/questions/36739808/how-can-i-get-pathvariable-inside-filterregistrationbean-spring-boot
// https://stackoverflow.com/questions/12249721/spring-mvc-3-how-to-get-path-variable-in-an-interceptor/65503332#65503332

@Component
public class Apitoolkit extends OncePerRequestFilter {

  private static String APITOOLKIT_API_KEY = "";
  private ClientMetadata metadata = new ClientMetadata();

  public void getClientMetadata() {
    String apitoolkitProductRoute = "https://app.apitoolkit.io/api/client_metadata";
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + APITOOLKIT_API_KEY);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<Object> response = restTemplate.exchange(apitoolkitProductRoute, HttpMethod.GET, requestEntity,
        Object.class);

    ModelMapper mapper = new ModelMapper();

    metadata = mapper.map(response.getBody(), metadata.getClass());
  }

  public Apitoolkit(@Value("${apitoolkit.apiKey}") String apiKey) {
    // TODO: make sure the api is set
    APITOOLKIT_API_KEY = apiKey;
    this.getClientMetadata();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    System.out.println(request.getMethod() + " " + request.getRequestURI() + " " + APITOOLKIT_API_KEY);
    var payload = new ApitoolkitPayload();

    payload.setHost(request.getRemoteHost());
    payload.setMethod(request.getMethod());
    payload.setProjectID(metadata.getProject_id());
    // TODO: check that we are getting the right value
    payload.setRawURL(request.getRequestURI());
    payload.setRequestHeaders(getRequestHeaders(request));

    String requestBody = request.getReader().lines().reduce("", String::concat);
    payload.setRequestBody(requestBody);

    String queryString = request.getQueryString();

    payload.setQueryParams(getQueryParams(queryString));

    var attr = request.getAttributeNames();

    while (attr.hasMoreElements()) {
      System.out.println("pathparams : " + attr.nextElement());
      
    }
    // TODO: payload.setPathParams();
    var before = new Date();
    var beforeMilli = before.getTime();
    filterChain.doFilter(request, response);

    payload.setResponseHeaders(getResponseHeaders(response));
    payload.setStatusCode(response.getStatus());
    
    payload.setTimestamp(before.toString());
    var after = new Date();
    var duration = after.getTime() - beforeMilli;

    payload.setDuration(duration*1000000);

    System.out.println(payload.toString());
  }

  private Map<String, List<String>> getRequestHeaders(HttpServletRequest request) {
    Map<String, List<String>> headers = new HashMap<>();
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      headers.put(headerName, Collections.list(request.getHeaders(headerName)));
    }
    return headers;
  }

  private Map<String, List<String>> getResponseHeaders(HttpServletResponse response) {
    Map<String, List<String>> headers = new HashMap<>();
    Collection<String> headerNames = response.getHeaderNames();
    for (String headerName : headerNames) {
      headers.put(headerName, List.of(response.getHeader(headerName)));
    }
    return headers;
  }

  private Map<String, List<String>> getQueryParams(String queryString) {
    if (queryString == null) {
      return null;
    }
    Map<String, List<String>> queryParams = new HashMap<>();
    String[] params = queryString.split("&");
    for (String param : params) {
      String[] p = param.split("=");
      queryParams.put(p[0], List.of(p[1]));
    }
    return queryParams;
  }
}
