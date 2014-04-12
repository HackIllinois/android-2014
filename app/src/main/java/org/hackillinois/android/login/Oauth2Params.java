package org.hackillinois.android.login;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;

/**
 * 
 * Enum that encapsulates the various OAuth2 connection parameters for the different providers
 * 
 * We capture the following properties for the demo application
 * 
 * clientId
 * clientSecret
 * scope
 * redirectUri
 * apiUrl
 * tokenServerUrl
 * authorizationServerEncodedUrl
 * accessMethod
 * 
 * @author davydewaele
 *
 */
public enum Oauth2Params {

	GOOGLE_PLUS("1024924889757-shkkhkqdnsrd6d0j52733fktqvs1q1hr.apps.googleusercontent.com",
            "https://accounts.google.com/o/oauth2/token",
            "https://accounts.google.com/o/oauth2/auth",
            BearerToken.authorizationHeaderAccessMethod(),
            "email",
            "http://localhost",
            "plus",
            "https://www.googleapis.com/plus/v1/people/me"
    );

    private String clientId;
	private String scope;
	private String redirectUri;
	private String userId;
	private String apiUrl;

	private String tokenServerUrl;
	private String authorizationServerEncodedUrl;
	
	private AccessMethod accessMethod;
	
	Oauth2Params(String clientId,
                 String tokenServerUrl,
                 String authorizationServerEncodedUrl,
                 AccessMethod accessMethod,
                 String scope,
                 String redirectUri,
                 String userId,
                 String apiUrl
    ) {
		this.clientId=clientId;
		this.tokenServerUrl=tokenServerUrl;
		this.authorizationServerEncodedUrl=authorizationServerEncodedUrl;
		this.accessMethod=accessMethod;
		this.scope=scope;
		this.redirectUri =redirectUri;
		this.userId=userId;
		this.apiUrl=apiUrl;
	}
	
	public String getClientId() {
		if (this.clientId==null || this.clientId.length()==0) {
			throw new IllegalArgumentException("Please provide a valid clientId in the Oauth2Params class");
		}
		return clientId;
	}

	public String getScope() {
		return scope;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public String getApiUrl() {
		return apiUrl;
	}
	public String getTokenServerUrl() {
		return tokenServerUrl;
	}

	public String getAuthorizationServerEncodedUrl() {
		return authorizationServerEncodedUrl;
	}
	
	public AccessMethod getAccessMethod() {
		return accessMethod;
	}
	
	public String getUserId() {
		return userId;
	}
}
