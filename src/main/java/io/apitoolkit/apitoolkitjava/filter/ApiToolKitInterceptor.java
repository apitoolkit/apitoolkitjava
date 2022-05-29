package io.apitoolkit.apitoolkitjava.filter;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.util.Map;
import java.util.TimeZone;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

@Component
public class ApiToolKitInterceptor extends WebRequestHandlerInterceptorAdapter {

	private PubSub pubSub;

	public ApiToolKitInterceptor(WebRequestInterceptor requestInterceptor,PubSub pubSub) {
		super(requestInterceptor);
		this.pubSub = pubSub;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		var payload = getPayload(request, response);
		ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
		String json = mapper.writeValueAsString( payload );
		System.out.println(json);
		pubSub.publishWithErrorHandler(json);
	}

	protected ApitoolkitPayload getPayload(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println(request.getMethod() + " " + request.getRequestURI() + " " + pubSub.getApiToolKitKey());
		var payload = new ApitoolkitPayload();

		payload.setHost(request.getRemoteHost());
		payload.setMethod(request.getMethod());
		// TODO: get this from the pubsub bean
		payload.setProjectID(pubSub.getProjectID());
		// TODO: check that we are getting the right value 
		// (include the query params)
		payload.setRawURL(request.getRequestURI());
		payload.setRequestHeaders(getRequestHeaders(request));

		String requestBody = request.getReader().lines().reduce("", String::concat);
		payload.setRequestBody(requestBody);

		String queryString = request.getQueryString();

		payload.setQueryParams(getQueryParams(queryString));

		// TODO: fix unchecked cast warning
		Map<String, String> pathParams = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		payload.setPathParams(pathParams);

		var DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"); // 2019-10-12T07:20:50.52Z
		DateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		var before = new Date();
		var beforeMilli = before.getTime();

		payload.setResponseBody(getBody(response));
		
		// TODO: get the url path
		payload.setResponseHeaders(getResponseHeaders(response));
		payload.setStatusCode(response.getStatus());
		
		payload.setTimestamp(DateFormat.format(before));
		var after = new Date();
		var duration = after.getTime() - beforeMilli;

		payload.setDuration(duration * 1000000);

		return payload;
	}

	private String getBody(HttpServletResponse response) throws IOException {
		ContentCachingResponseWrapper wrapper =  (ContentCachingResponseWrapper) response;
		String s = new String(wrapper.getContentAsByteArray(),wrapper.getCharacterEncoding());
		wrapper.copyBodyToResponse();
		return s;
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

	// TODO: refactor this to use same method in extracting headers from response
	// and request
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