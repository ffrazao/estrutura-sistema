package com.frazao.lacodeamorrest.config.seguranca;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
public class TokenEnhancerConfig implements TokenEnhancer {

	public TokenEnhancerConfig() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		DefaultOAuth2AccessToken tempResult = (DefaultOAuth2AccessToken) accessToken;

		Map<String, Object> details = new HashMap<>();
		Object userDetails = null;
		if (authentication.getUserAuthentication() != null && authentication.getUserAuthentication().getDetails() != null) {
			userDetails = authentication.getUserAuthentication().getDetails();
		}
		if (userDetails != null) {
			details = (Map<String, Object>) userDetails;
		}
		details.put("Teste", "Valor");

		tempResult.setAdditionalInformation(details);

		return tempResult;
	}

}
