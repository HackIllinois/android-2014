package org.hackillinois.android.login;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class OAuth2Helper {

    private static final String TAG = "OAuth2Helper";

	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory(); 

	private final CredentialStore credentialStore;

	private AuthorizationCodeFlow flow;

	private Oauth2Params oauth2Params; 
	
	public OAuth2Helper(SharedPreferences sharedPreferences, Oauth2Params oauth2Params) {
		this.credentialStore = new SharedPreferencesCredentialStore(sharedPreferences);
		this.oauth2Params = oauth2Params;
		this.flow = new AuthorizationCodeFlow.Builder(oauth2Params.getAccessMethod() , HTTP_TRANSPORT, JSON_FACTORY, new GenericUrl(oauth2Params.getTokenServerUrl()), new ClientParametersAuthentication(oauth2Params.getClientId(),""), oauth2Params.getClientId(), oauth2Params.getAuthorizationServerEncodedUrl()).setCredentialStore(this.credentialStore).build();
	}

	public OAuth2Helper(SharedPreferences sharedPreferences) {
		this(sharedPreferences,Oauth2Params.GOOGLE_PLUS);
	}
	
	public String getAuthorizationUrl() {
        return flow.newAuthorizationUrl().setRedirectUri(oauth2Params.getRedirectUri()).setScopes(convertScopesToString(oauth2Params.getScope())).build();
	}
	
	public void retrieveAndStoreAccessToken(String authorizationCode) throws IOException {
		Log.i(TAG, "retrieveAndStoreAccessToken for code " + authorizationCode);
        Collection<String> scopes = convertScopesToString(oauth2Params.getScope());
		AuthorizationCodeTokenRequest tokenResponse = flow.newTokenRequest(authorizationCode);
                tokenResponse.setScopes(scopes);
                tokenResponse.setRedirectUri(oauth2Params.getRedirectUri());

        TokenResponse response = tokenResponse.execute();
		Log.i(TAG, "Found tokenResponse :");
		Log.i(TAG, "Access Token : " + response.getAccessToken());
		Log.i(TAG, "Refresh Token : " + response.getRefreshToken());
		flow.createAndStoreCredential(response, oauth2Params.getUserId());
	}

	public String executeApiCall() throws IOException {
		Log.i(TAG, "Executing API call at url " + this.oauth2Params.getApiUrl());
		return HTTP_TRANSPORT.createRequestFactory(loadCredential()).buildGetRequest(new GenericUrl(this.oauth2Params.getApiUrl())).execute().parseAsString();
	}

	public Credential loadCredential() throws IOException {
		return flow.loadCredential(oauth2Params.getUserId());
	}

	public void clearCredentials() throws IOException {
        if (flow.getCredentialDataStore() != null && oauth2Params.getUserId() != null) {
            flow.getCredentialDataStore().delete(oauth2Params.getUserId());
        }
	}
	
	private Collection<String> convertScopesToString(String scopesConcat) {
		String[] scopes = scopesConcat.split(",");
		Collection<String> collection = new ArrayList<String>();
		Collections.addAll(collection, scopes);
		return collection;
	}
}
