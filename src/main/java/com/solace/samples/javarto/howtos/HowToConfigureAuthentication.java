package com.solace.samples.javarto.howtos;

import java.util.HashMap;
import java.util.Map;

import com.solacesystems.solclientj.core.SolEnum;
import com.solacesystems.solclientj.core.Solclient;
import com.solacesystems.solclientj.core.SolclientException;
import com.solacesystems.solclientj.core.event.MessageCallback;
import com.solacesystems.solclientj.core.event.SessionEventCallback;
import com.solacesystems.solclientj.core.handle.ContextHandle;
import com.solacesystems.solclientj.core.handle.Handle;
import com.solacesystems.solclientj.core.handle.SessionHandle;

/**
 * Sampler for authentication configuration
 */
public class HowToConfigureAuthentication {

    /**
     * Example how to configure service access to use a basic authentication with user name and
     * password
     *
     * @param host host
     * @param userName user name
     * @param password password
     * @return a map of handles for messaging tasks and lifecycle management
     */
    public static Map<Class<?>, Handle> configureBasicAuthCredentials(String host, String userName,
        String password) {

            Map<Class<?>, Handle> handleMap = new HashMap<>();

            Solclient.init(null);

            ContextHandle context = Solclient.Allocator.newContextHandle();
    
            Solclient.createContextForHandle(context, null);
            handleMap.put(ContextHandle.class, context);
    
            SessionHandle sessionHandle = Solclient.Allocator.newSessionHandle();
            handleMap.put(SessionHandle.class, context);

            String[] sessionProps = new String[8];
            int propsCount = 0;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.HOST;
            sessionProps[propsCount++] = host;

            
            sessionProps[propsCount++] = SessionHandle.PROPERTIES.AUTHENTICATION_SCHEME;
            sessionProps[propsCount++] = SolEnum.AuthenticationScheme.BASIC;
            
            sessionProps[propsCount++] = SessionHandle.PROPERTIES.USERNAME;
            sessionProps[propsCount++] = userName;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.PASSWORD;
            sessionProps[propsCount++] = password;

            context.createSessionForHandle(sessionHandle, sessionProps,
            new MessageCallback() {

                @Override
                public void onMessage(Handle arg0) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
                }
                
            }, new SessionEventCallback() {

                @Override
                public void onEvent(SessionHandle arg0) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'onEvent'");
                }
                
            });

            sessionHandle.connect();

            return handleMap;
    }
  
    /**
    * Example how to configure service access using Kerberos authentication scheme
     *
     * @param kerbServiceName kerberos service name
     * @return a map of handles for messaging tasks and lifecycle management
     * @see <a href="https://docs.solace.com/Configuring-and-Managing/Configuring-Client-Authentication.htm#Config-Kerberos">How
     * to configure Kerberos</a>
     */
    public static Map<Class<?>, Handle> configureKerberosAuthScheme(String host, String kerbServiceName) {
  
            Map<Class<?>, Handle> handleMap = new HashMap<>();

            Solclient.init(null);

            ContextHandle context = Solclient.Allocator.newContextHandle();

            Solclient.createContextForHandle(context, null);
            handleMap.put(ContextHandle.class, context);

            SessionHandle sessionHandle = Solclient.Allocator.newSessionHandle();
            handleMap.put(SessionHandle.class, context);

            String[] sessionProps = new String[8];
            int propsCount = 0;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.HOST;
            sessionProps[propsCount++] = host;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.AUTHENTICATION_SCHEME;
            sessionProps[propsCount++] = SolEnum.AuthenticationScheme.GSS_KRB;

            
            sessionProps[propsCount++] = SessionHandle.PROPERTIES.KRB_SERVICE_NAME;
            sessionProps[propsCount++] = kerbServiceName;

            context.createSessionForHandle(sessionHandle, sessionProps,
            new MessageCallback() {

                @Override
                public void onMessage(Handle arg0) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
                }
                
            }, new SessionEventCallback() {

                @Override
                public void onEvent(SessionHandle arg0) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'onEvent'");
                }
                
            });

            sessionHandle.connect();

            return handleMap;
    }
  
  
    /**
     * For a client to use a client certificate authentication scheme, the host event broker must be
     * properly configured for TLS/SSL connections, and Client Certificate Verification must be
     * enabled for the particular Message VPN that the client is connecting to. On client side client
     * certificate needs to be present in a keystore file.
     *
     * @param privateKeyFile private key file
     * @param privateKeyFilePassword password for the private key file password 
     * @param clientCertificateFile     client certificate file
     * @param trustStoreDir trust store directory
     * @return a map of handles for messaging tasks and lifecycle management
     */
    public static Map<Class<?>, Handle> thing(String host, String privateKeyFile, String privateKeyFilePassword, String clientCertificateFile, String trustStoreDir) {

            Map<Class<?>, Handle> handleMap = new HashMap<>();

            Solclient.init(null);

            ContextHandle context = Solclient.Allocator.newContextHandle();
    
            Solclient.createContextForHandle(context, null);
            handleMap.put(ContextHandle.class, context);
    
            SessionHandle sessionHandle = Solclient.Allocator.newSessionHandle();
            handleMap.put(SessionHandle.class, context);

            String[] sessionProps = new String[12];
            int propsCount = 0;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.HOST;
            sessionProps[propsCount++] = host;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.AUTHENTICATION_SCHEME;
            sessionProps[propsCount++] = SolEnum.AuthenticationScheme.CLIENT_CERTIFICATE;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.SSL_CLIENT_CERTIFICATE_FILE;
            sessionProps[propsCount++] = clientCertificateFile;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.SSL_CLIENT_PRIVATE_KEY_FILE;
            sessionProps[propsCount++] = privateKeyFile;  

            
            sessionProps[propsCount++] = SessionHandle.PROPERTIES.SSL_CLIENT_PRIVATE_KEY_FILE_PASSWORD;
            sessionProps[propsCount++] = privateKeyFilePassword;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.SSL_TRUST_STORE_DIR;
            sessionProps[propsCount++] = trustStoreDir;
 
            context.createSessionForHandle(sessionHandle, sessionProps,
            new MessageCallback() {

                @Override
                public void onMessage(Handle arg0) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
                }
                
            }, new SessionEventCallback() {

                @Override
                public void onEvent(SessionHandle arg0) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'onEvent'");
                }
                
            });

            sessionHandle.connect();

            return handleMap;
    }
  
  
    /**
     * Example how to configure service access to use OAuth 2 authentication with an access token and
     * an optional issuer identifier
     *
     * @param accessToken      access token
     * @param issuerIdentifier Issuer identifier URI
     * @param host host
     * @return a map of handles for messaging tasks and lifecycle management
     */
    public static Map<Class<?>, Handle> configureOauth2WithAccessTokenAuthentication(String host, String accessToken,
        String issuerIdentifier) {

            Map<Class<?>, Handle> handleMap = new HashMap<>();

            Solclient.init(null);

            ContextHandle context = Solclient.Allocator.newContextHandle();
    
            Solclient.createContextForHandle(context, null);
            handleMap.put(ContextHandle.class, context);
    
            SessionHandle sessionHandle = Solclient.Allocator.newSessionHandle();
            handleMap.put(SessionHandle.class, context);

            String[] sessionProps = new String[10];
            int propsCount = 0;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.HOST;
            sessionProps[propsCount++] = host;

            
            sessionProps[propsCount++] = SessionHandle.PROPERTIES.AUTHENTICATION_SCHEME;
            sessionProps[propsCount++] = SolEnum.AuthenticationScheme.OAUTH2;
            
            sessionProps[propsCount++] = SessionHandle.PROPERTIES.OAUTH2_ACCESS_TOKEN;
            sessionProps[propsCount++] = accessToken;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.OAUTH2_ISSUER_IDENTIFIER;
            sessionProps[propsCount++] = issuerIdentifier;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.SSL_VALIDATE_CERTIFICATE;
            sessionProps[propsCount++] = SolEnum.BooleanValue.DISABLE;

            context.createSessionForHandle(sessionHandle, sessionProps,
            new MessageCallback() {

                @Override
                public void onMessage(Handle arg0) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
                }
                
            }, new SessionEventCallback() {

                @Override
                public void onEvent(SessionHandle arg0) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'onEvent'");
                }
                
            });

            sessionHandle.connect();

            return handleMap;
    }
  
    /**
     * Example how to configure service access to use OIDC authentication with an ID token and
     * optional access token
     *
     * @param host        host
     * @param idToken     ID token
     * @param accessToken access token
     * @return a map of handles for messaging tasks and lifecycle management
     */
    public static Map<Class<?>, Handle> configureOIDCAwithIdTokenuthentication(String host, String idToken,
        String accessToken) {
            Map<Class<?>, Handle> handleMap = new HashMap<>();

            Solclient.init(null);

            ContextHandle context = Solclient.Allocator.newContextHandle();
    
            Solclient.createContextForHandle(context, null);
            handleMap.put(ContextHandle.class, context);
    
            SessionHandle sessionHandle = Solclient.Allocator.newSessionHandle();
            handleMap.put(SessionHandle.class, context);

            String[] sessionProps = new String[10];
            int propsCount = 0;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.HOST;
            sessionProps[propsCount++] = host;

            
            sessionProps[propsCount++] = SessionHandle.PROPERTIES.AUTHENTICATION_SCHEME;
            sessionProps[propsCount++] = SolEnum.AuthenticationScheme.OAUTH2;
            
            sessionProps[propsCount++] = SessionHandle.PROPERTIES.OAUTH2_ACCESS_TOKEN;
            sessionProps[propsCount++] = accessToken;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.OIDC_ID_TOKEN;
            sessionProps[propsCount++] = idToken;

            sessionProps[propsCount++] = SessionHandle.PROPERTIES.SSL_VALIDATE_CERTIFICATE;
            sessionProps[propsCount++] = SolEnum.BooleanValue.DISABLE;

            context.createSessionForHandle(sessionHandle, sessionProps,
            new MessageCallback() {

                @Override
                public void onMessage(Handle arg0) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
                }
                
            }, new SessionEventCallback() {

                @Override
                public void onEvent(SessionHandle arg0) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'onEvent'");
                }
                
            });

            sessionHandle.connect();

            return handleMap;
    }
  
    /**
     * Example how to update an expiring OAUTH2 (access or/and ID) tokens.
     * <p>User is in charge to obtain a new valid token in order to use this API
     * <p>Update won't take in effect instantly but upper next reconnection
     *
     * @param sessionHandle        connected or disconnected session handle with at least one outstanding
     *                       reconnection attempt
     * @param newAccessToken new access token
     * @param newOidcIdToken new ID token
     * @throws SolclientException If other transport or communication related errors occur
     */
    public void updateOauth2Token(SessionHandle sessionHandle, String newAccessToken,
        String newOidcIdToken)
        throws SolclientException {
  // The new access token is going to be used for authentication to the broker after broker disconnects a client (i.e due to old token expiration).
  // this token update happens during the next service reconnection attempt.
  // There will be no way to signal to the user if new token is valid. When the new token is not valid,
  // then reconnection will be retried for the remaining number of times or forever if configured so.
  // Usage of ServiceInterruptionListener and accompanied exceptions if any can be used to determine if token update during next reconnection was successful.
      sessionHandle.modifyProperties(new String[] {SessionHandle.PROPERTIES.OAUTH2_ACCESS_TOKEN, newAccessToken});
      sessionHandle.modifyProperties(new String[] {SessionHandle.PROPERTIES.OIDC_ID_TOKEN, newOidcIdToken});
    }
  
  }