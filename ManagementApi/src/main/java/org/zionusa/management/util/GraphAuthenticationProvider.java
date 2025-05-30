package org.zionusa.management.util;

import com.microsoft.graph.auth.BaseAuthentication;
import com.microsoft.graph.auth.enums.NationalCloud;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import java.util.List;

public class GraphAuthenticationProvider extends BaseAuthentication implements IAuthenticationProvider {
    public GraphAuthenticationProvider(String clientId, List<String> scopes, String clientSecret, String tenant, NationalCloud nationalCloud) {
        super(scopes, clientId, GetAuthority(nationalCloud == null ? NationalCloud.Global : nationalCloud, tenant), (String) null, nationalCloud == null ? NationalCloud.Global : nationalCloud, tenant, clientSecret);
    }

    @Override
    public void authenticateRequest(IHttpRequest request) {
        try {
            String accessToken = null;
            long duration = System.currentTimeMillis() - this.getStartTime();
            if (this.getResponse() != null && duration > 0L && duration < this.getResponse().getExpiresIn() * 1000L) {
                accessToken = this.getResponse().getAccessToken();
            } else {
                OAuthClientRequest authRequest = this.getTokenRequestMessage();
                accessToken = this.getAccessTokenNewRequest(authRequest);
            }

            request.addHeader("Authorization", "Bearer " + accessToken);
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    private OAuthClientRequest getTokenRequestMessage() throws OAuthSystemException {
        String tokenUrl = this.getAuthority() + "/oauth2/v2.0/token";
        OAuthClientRequest.TokenRequestBuilder token = OAuthClientRequest.tokenLocation(tokenUrl).setClientId(this.getClientId()).setGrantType(GrantType.CLIENT_CREDENTIALS).setScope(this.getScopesAsString());
        if (this.getClientSecret() != null) {
            token.setClientSecret(this.getClientSecret());
        }

        return token.buildBodyMessage();
    }

    private String getAccessTokenNewRequest(OAuthClientRequest request) throws OAuthSystemException, OAuthProblemException {
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        this.setStartTime(System.currentTimeMillis());
        this.setResponse(oAuthClient.accessToken(request));
        return this.getResponse().getAccessToken();
    }
}
