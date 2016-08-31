/**
 * Copyright 2007-2016, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaazing.gateway.transport.http.security.auth.connector;

import static org.junit.Assert.assertEquals;
import static org.kaazing.gateway.transport.http.HttpMethod.GET;
import static org.kaazing.gateway.transport.http.HttpStatus.CLIENT_UNAUTHORIZED;
import static org.kaazing.gateway.transport.http.HttpStatus.SUCCESS_OK;
import static org.kaazing.test.util.ITUtil.createRuleChain;

import java.net.Authenticator;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionInitializer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.kaazing.gateway.transport.http.HttpConnectSession;
import org.kaazing.gateway.transport.http.HttpConnectorRule;
import org.kaazing.gateway.transport.http.HttpSession;
import org.kaazing.gateway.util.scheduler.SchedulerProvider;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;

public class AuthenticatorIT {

    private final HttpConnectorRule connector = new HttpConnectorRule().setSchedulerProvider(new SchedulerProvider());
    private final K3poRule k3po = new K3poRule();

    @Rule
    public TestRule chain = createRuleChain(connector, k3po);
    
    @Rule
    public ResetAuthenticatorRule authenticator = new ResetAuthenticatorRule();

    private Mockery context;

    @Before
    public void initialize() {
        context = new Mockery();
        context.setThreadingPolicy(new Synchroniser());
        Authenticator.setDefault(new TestAuthenticator());
    }

    @After
    public void cleanUp() {
        Authenticator.setDefault(null);
    }

    @Specification("basic.challenge.and.accept")
    @Test
    public void basicChallengeAndAccept() throws Exception {
        connector.getConnectOptions().put("http.max.authentication.attempts", "1");
        final IoHandler handler = context.mock(IoHandler.class);
        context.checking(new Expectations() {
            {
                oneOf(handler).sessionCreated(with(any(IoSession.class)));
                oneOf(handler).sessionOpened(with(any(IoSession.class)));
                oneOf(handler).sessionClosed(with(any(IoSession.class)));
            }
        });
        Map<String, Object> connectOptions = new HashMap<>();

        ConnectFuture connectFuture = connector.connect("http://localhost:8080/resource", handler, new IoSessionInitializer<ConnectFuture>() {
            @Override
            public void initializeSession(IoSession session, ConnectFuture future) {
                HttpConnectSession connectSession = (HttpConnectSession) session;
                connectSession.setMethod(GET);
            }
        }, connectOptions);

        k3po.finish();
        HttpSession connectSession = (HttpSession) connectFuture.getSession();
        assertEquals(SUCCESS_OK, connectSession.getStatus());
    }

    @Specification("basic.challenge.twice")
    @Test
    public void wontChallengeMoreThenNumberOfAttempts() throws Exception {
        connector.getConnectOptions().put("http.max.authentication.attempts", "1");
        final IoHandler handler = context.mock(IoHandler.class);
        context.checking(new Expectations() {
            {
                oneOf(handler).sessionCreated(with(any(IoSession.class)));
                oneOf(handler).sessionOpened(with(any(IoSession.class)));
                oneOf(handler).sessionClosed(with(any(IoSession.class)));
            }
        });
        Map<String, Object> connectOptions = new HashMap<>();
        
        ConnectFuture connectFuture = connector.connect("http://localhost:8080/resource", handler, new IoSessionInitializer<ConnectFuture>() {
            @Override
            public void initializeSession(IoSession session, ConnectFuture future) {
                HttpConnectSession connectSession = (HttpConnectSession) session;
                connectSession.setMethod(GET);
            }
        }, connectOptions);
        
        k3po.finish();
        HttpSession connectSession = (HttpSession) connectFuture.getSession();
        assertEquals(CLIENT_UNAUTHORIZED, connectSession.getStatus());
    }

}
