package com.frazao.lacodeamorrest.config.seguranca;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Filtro para liberar acesso dos apps/páginas ao servidor
 * 
 * @author ffrazao
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) res;
		String method = ((HttpServletRequest) req).getMethod();

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
//		response.setHeader("Access-Control-Allow-Methods", "DELETE, GET, OPTIONS, POST, PUT"); 
//		response.setHeader("Access-Control-Allow-Methods", "*"); 
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");
//		response.setHeader("Access-Control-Allow-Headers", "*");

		// se for Options concluir a requisição e interromper o filter chain
		if ("OPTIONS".equalsIgnoreCase(method)) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, res);
		}
	}

	public void init(FilterConfig filterConfig) {
		// not needed
	}

	public void destroy() {
		// not needed
	}

}