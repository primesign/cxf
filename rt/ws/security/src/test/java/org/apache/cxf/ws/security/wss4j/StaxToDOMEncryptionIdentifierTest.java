/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cxf.ws.security.wss4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.service.Service;
import org.apache.cxf.transport.local.LocalTransportFactory;
import org.apache.wss4j.common.crypto.CryptoFactory;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.apache.wss4j.stax.ext.WSSConstants;
import org.apache.wss4j.stax.ext.WSSSecurityProperties;
import org.apache.wss4j.stax.securityToken.WSSecurityTokenConstants;
import org.junit.Test;


/**
 * In these test-cases, the client is using StaX and the service is using DOM. The tests are
 * for different Encryption Key Identifier methods.
 */
public class StaxToDOMEncryptionIdentifierTest extends AbstractSecurityTest {
    
    @Test
    public void testEncryptDirectReference() throws Exception {
        // Create + configure service
        Service service = createService();
        
        Map<String, Object> inProperties = new HashMap<String, Object>();
        inProperties.put(WSHandlerConstants.ACTION, WSHandlerConstants.ENCRYPT);
        inProperties.put(WSHandlerConstants.PW_CALLBACK_REF, new TestPwdCallback());
        inProperties.put(WSHandlerConstants.DEC_PROP_FILE, "insecurity.properties");
        WSS4JInInterceptor inInterceptor = new WSS4JInInterceptor(inProperties);
        service.getInInterceptors().add(inInterceptor);
        
        // Create + configure client
        Echo echo = createClientProxy();
        
        Client client = ClientProxy.getClient(echo);
        client.getInInterceptors().add(new LoggingInInterceptor());
        client.getOutInterceptors().add(new LoggingOutInterceptor());
        
        WSSSecurityProperties properties = new WSSSecurityProperties();
        List<WSSConstants.Action> actions = new ArrayList<WSSConstants.Action>();
        actions.add(WSSConstants.ENCRYPT);
        properties.setActions(actions);
        properties.setEncryptionUser("myalias");
        properties.setEncryptionKeyIdentifier(
            WSSecurityTokenConstants.KeyIdentifier_SecurityTokenDirectReference
        );
        properties.setEncryptionSymAlgorithm(WSSConstants.NS_XENC_AES128);
        
        Properties cryptoProperties = 
            CryptoFactory.getProperties("outsecurity.properties", this.getClass().getClassLoader());
        properties.setEncryptionCryptoProperties(cryptoProperties);
        properties.setCallbackHandler(new TestPwdCallback());
        WSS4JStaxOutInterceptor ohandler = new WSS4JStaxOutInterceptor(properties);
        client.getOutInterceptors().add(ohandler);

        assertEquals("test", echo.echo("test"));
    }
    
    @Test
    public void testEncryptIssuerSerial() throws Exception {
        // Create + configure service
        Service service = createService();
        
        Map<String, Object> inProperties = new HashMap<String, Object>();
        inProperties.put(WSHandlerConstants.ACTION, WSHandlerConstants.ENCRYPT);
        inProperties.put(WSHandlerConstants.PW_CALLBACK_REF, new TestPwdCallback());
        inProperties.put(WSHandlerConstants.DEC_PROP_FILE, "insecurity.properties");
        WSS4JInInterceptor inInterceptor = new WSS4JInInterceptor(inProperties);
        service.getInInterceptors().add(inInterceptor);
        
        // Create + configure client
        Echo echo = createClientProxy();
        
        Client client = ClientProxy.getClient(echo);
        client.getInInterceptors().add(new LoggingInInterceptor());
        client.getOutInterceptors().add(new LoggingOutInterceptor());
        
        WSSSecurityProperties properties = new WSSSecurityProperties();
        List<WSSConstants.Action> actions = new ArrayList<WSSConstants.Action>();
        actions.add(WSSConstants.ENCRYPT);
        properties.setActions(actions);
        properties.setEncryptionUser("myalias");
        properties.setEncryptionKeyIdentifier(
            WSSecurityTokenConstants.KeyIdentifier_IssuerSerial
        );
        properties.setEncryptionSymAlgorithm(WSSConstants.NS_XENC_AES128);
        
        Properties cryptoProperties = 
            CryptoFactory.getProperties("outsecurity.properties", this.getClass().getClassLoader());
        properties.setEncryptionCryptoProperties(cryptoProperties);
        properties.setCallbackHandler(new TestPwdCallback());
        WSS4JStaxOutInterceptor ohandler = new WSS4JStaxOutInterceptor(properties);
        client.getOutInterceptors().add(ohandler);

        assertEquals("test", echo.echo("test"));
    }
    
    @Test
    public void testEncryptThumbprint() throws Exception {
        // Create + configure service
        Service service = createService();
        
        Map<String, Object> inProperties = new HashMap<String, Object>();
        inProperties.put(WSHandlerConstants.ACTION, WSHandlerConstants.ENCRYPT);
        inProperties.put(WSHandlerConstants.PW_CALLBACK_REF, new TestPwdCallback());
        inProperties.put(WSHandlerConstants.DEC_PROP_FILE, "insecurity.properties");
        WSS4JInInterceptor inInterceptor = new WSS4JInInterceptor(inProperties);
        service.getInInterceptors().add(inInterceptor);
        
        // Create + configure client
        Echo echo = createClientProxy();
        
        Client client = ClientProxy.getClient(echo);
        client.getInInterceptors().add(new LoggingInInterceptor());
        client.getOutInterceptors().add(new LoggingOutInterceptor());
        
        WSSSecurityProperties properties = new WSSSecurityProperties();
        List<WSSConstants.Action> actions = new ArrayList<WSSConstants.Action>();
        actions.add(WSSConstants.ENCRYPT);
        properties.setActions(actions);
        properties.setEncryptionUser("myalias");
        properties.setEncryptionKeyIdentifier(
            WSSecurityTokenConstants.KeyIdentifier_ThumbprintIdentifier
        );
        properties.setEncryptionSymAlgorithm(WSSConstants.NS_XENC_AES128);
        
        Properties cryptoProperties = 
            CryptoFactory.getProperties("outsecurity.properties", this.getClass().getClassLoader());
        properties.setEncryptionCryptoProperties(cryptoProperties);
        properties.setCallbackHandler(new TestPwdCallback());
        WSS4JStaxOutInterceptor ohandler = new WSS4JStaxOutInterceptor(properties);
        client.getOutInterceptors().add(ohandler);

        assertEquals("test", echo.echo("test"));
    }
    
    @Test
    public void testEncryptX509() throws Exception {
        // Create + configure service
        Service service = createService();
        
        Map<String, Object> inProperties = new HashMap<String, Object>();
        inProperties.put(WSHandlerConstants.ACTION, WSHandlerConstants.ENCRYPT);
        inProperties.put(WSHandlerConstants.PW_CALLBACK_REF, new TestPwdCallback());
        inProperties.put(WSHandlerConstants.DEC_PROP_FILE, "insecurity.properties");
        inProperties.put(WSHandlerConstants.IS_BSP_COMPLIANT, "false");
        WSS4JInInterceptor inInterceptor = new WSS4JInInterceptor(inProperties);
        service.getInInterceptors().add(inInterceptor);
        
        // Create + configure client
        Echo echo = createClientProxy();
        
        Client client = ClientProxy.getClient(echo);
        client.getInInterceptors().add(new LoggingInInterceptor());
        client.getOutInterceptors().add(new LoggingOutInterceptor());
        
        WSSSecurityProperties properties = new WSSSecurityProperties();
        List<WSSConstants.Action> actions = new ArrayList<WSSConstants.Action>();
        actions.add(WSSConstants.ENCRYPT);
        properties.setActions(actions);
        properties.setEncryptionUser("myalias");
        properties.setEncryptionKeyIdentifier(
            WSSecurityTokenConstants.KeyIdentifier_X509KeyIdentifier
        );
        properties.setEncryptionSymAlgorithm(WSSConstants.NS_XENC_AES128);
        
        Properties cryptoProperties = 
            CryptoFactory.getProperties("outsecurity.properties", this.getClass().getClassLoader());
        properties.setEncryptionCryptoProperties(cryptoProperties);
        properties.setCallbackHandler(new TestPwdCallback());
        WSS4JStaxOutInterceptor ohandler = new WSS4JStaxOutInterceptor(properties);
        client.getOutInterceptors().add(ohandler);

        assertEquals("test", echo.echo("test"));
    }
    
    @Test
    public void testEncryptEncryptedKeySHA1() throws Exception {
        // Create + configure service
        Service service = createService();
        
        Map<String, Object> inProperties = new HashMap<String, Object>();
        inProperties.put(WSHandlerConstants.ACTION, WSHandlerConstants.ENCRYPT);
        inProperties.put(WSHandlerConstants.PW_CALLBACK_REF, new TestPwdCallback());
        inProperties.put(WSHandlerConstants.DEC_PROP_FILE, "insecurity.properties");
        WSS4JInInterceptor inInterceptor = new WSS4JInInterceptor(inProperties);
        service.getInInterceptors().add(inInterceptor);
        
        // Create + configure client
        Echo echo = createClientProxy();
        
        Client client = ClientProxy.getClient(echo);
        client.getInInterceptors().add(new LoggingInInterceptor());
        client.getOutInterceptors().add(new LoggingOutInterceptor());
        
        WSSSecurityProperties properties = new WSSSecurityProperties();
        List<WSSConstants.Action> actions = new ArrayList<WSSConstants.Action>();
        actions.add(WSSConstants.ENCRYPT);
        properties.setActions(actions);
        properties.setEncryptionUser("myalias");
        properties.setEncryptionKeyIdentifier(
            WSSecurityTokenConstants.KeyIdentifier_EncryptedKeySha1Identifier
        );
        properties.setEncryptionSymAlgorithm(WSSConstants.NS_XENC_AES128);
        
        Properties cryptoProperties = 
            CryptoFactory.getProperties("outsecurity.properties", this.getClass().getClassLoader());
        properties.setEncryptionCryptoProperties(cryptoProperties);
        properties.setCallbackHandler(new TestPwdCallback());
        WSS4JStaxOutInterceptor ohandler = new WSS4JStaxOutInterceptor(properties);
        client.getOutInterceptors().add(ohandler);

        assertEquals("test", echo.echo("test"));
    }
    
    private Service createService() {
        // Create the Service
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory.setServiceBean(new EchoImpl());
        factory.setAddress("local://Echo");
        factory.setTransportId(LocalTransportFactory.TRANSPORT_ID);
        Server server = factory.create();
        
        Service service = server.getEndpoint().getService();
        service.getInInterceptors().add(new LoggingInInterceptor());
        service.getOutInterceptors().add(new LoggingOutInterceptor());
        
        return service;
    }
    
    private Echo createClientProxy() {
        JaxWsProxyFactoryBean proxyFac = new JaxWsProxyFactoryBean();
        proxyFac.setServiceClass(Echo.class);
        proxyFac.setAddress("local://Echo");
        proxyFac.getClientFactoryBean().setTransportId(LocalTransportFactory.TRANSPORT_ID);
        
        return (Echo)proxyFac.create();
    }
}
