package io.apitoolkit.apitoolkitjava.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class ApiToolKitFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		if (response.getCharacterEncoding() == null) {
			response.setCharacterEncoding("UTF-8"); // Or whatever default. UTF-8 is good for World Domination.
		}

		ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

		try {
			filterChain.doFilter(request, wrappedResponse);
			wrappedResponse.flushBuffer();
		} catch (Exception e) {
			// TODO: figure out a way to handle this exception
		}
	}

}
