/**
 * Copyright (c) 2007-2014 Kaazing Corporation. All rights reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.kaazing.gateway.server.context.resolve;

import static org.kaazing.gateway.service.TransportOptionNames.INACTIVITY_TIMEOUT;
import static org.kaazing.gateway.service.TransportOptionNames.PIPE_TRANSPORT;
import static org.kaazing.gateway.service.TransportOptionNames.SSL_CIPHERS;
import static org.kaazing.gateway.service.TransportOptionNames.SSL_ENCRYPTION_ENABLED;
import static org.kaazing.gateway.service.TransportOptionNames.SSL_PROTOCOLS;
import static org.kaazing.gateway.service.TransportOptionNames.SSL_TRANSPORT;
import static org.kaazing.gateway.service.TransportOptionNames.TCP_TRANSPORT;
import static org.kaazing.gateway.service.TransportOptionNames.WS_PROTOCOL_VERSION;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.kaazing.gateway.service.ConnectOptionsContext;
import org.kaazing.gateway.util.Utils;
import org.kaazing.gateway.util.ssl.SslCipherSuites;
import org.kaazing.gateway.util.ws.WebSocketWireProtocol;

public class DefaultConnectOptionsContext implements ConnectOptionsContext {

    private static final long DEFAULT_WS_INACTIVITY_TIMEOUT_MILLIS = 0L;

    private WebSocketWireProtocol webSocketWireProtocol = WebSocketWireProtocol.RFC_6455;
    private String[] sslCiphers;
    private String[] sslProtocols;
    private long wsInactivityTimeoutMillis;
    private String wsVersion; // added so we can expose to Console
    private URI pipeTransportURI;
    private URI tcpTransportURI;
    private URI sslTransportURI;
    private URI httpTransportURI;
    private boolean sslEncryptionEnabled = true; // default to true
    private String udpInterface;

    public DefaultConnectOptionsContext() {
        this(new HashMap<String, String>());
    }

    public DefaultConnectOptionsContext(Map<String, String> connectOptions) {
        if (connectOptions != null) {

            /*
             * DPW TODO Depricated so removed in 5.0 wsVersion = connectOptions.getWsVersion(); if
             * ("rfc6455".equals(wsVersion)) { webSocketWireProtocol = WebSocketWireProtocol.RFC_6455; } else if
             * ("draft-75".equals(wsVersion)) { webSocketWireProtocol = WebSocketWireProtocol.HIXIE_75; }
             */

            udpInterface = connectOptions.get("udp.interface");

            String sslCiphersStr = connectOptions.get("ssl.ciphers");
            this.sslCiphers = sslCiphersStr != null ? SslCipherSuites.resolveCSV(sslCiphersStr) : null;
            String sslProtocolsStr = connectOptions.get("ssl.protocols");
            this.sslProtocols = sslProtocolsStr != null ? resolveProtocols(sslProtocolsStr) : null;

            String tcpTransport = connectOptions.get("tcp.transport");
            if (tcpTransport != null) {
                tcpTransportURI = URI.create(tcpTransport);
                if (!tcpTransportURI.isAbsolute()) {
                    throw new IllegalArgumentException(String.format("tcp.transport must contain an absolute URI, not \"%s\"",
                            tcpTransport));
                }
            }

            String pipeTransport = connectOptions.get("pipe.transport");
            if (pipeTransport != null) {
                pipeTransportURI = URI.create(pipeTransport);
                if (!pipeTransportURI.isAbsolute()) {
                    throw new IllegalArgumentException(String.format("pipe.transport must contain an absolute URI, not \"%s\"",
                            pipeTransport));
                }
            }

            String sslTransport = connectOptions.get("ssl.transport");
            if (sslTransport != null) {
                sslTransportURI = URI.create(sslTransport);
                if (!sslTransportURI.isAbsolute()) {
                    throw new IllegalArgumentException(String.format("ssl.transport must contain an absolute URI, not \"%s\"",
                            sslTransport));
                }
            }

            String httpTransport = connectOptions.get("http.transport");
            if (httpTransport != null) {
                httpTransportURI = URI.create(httpTransport);
                if (!httpTransportURI.isAbsolute()) {
                    throw new IllegalArgumentException(String.format("http.transport must contain an absolute URI, not \"%s\"",
                            httpTransport));
                }
            }

            Long wsInactivityTimeout = null;
            if (connectOptions != null) {
                String value = connectOptions.get("ws.inactivity.timeout");
                if (value != null) {
                    long val = Utils.parseTimeInterval(value, TimeUnit.MILLISECONDS);
                    if (val > 0) {
                        wsInactivityTimeout = val;
                    }
                }
            }
            this.wsInactivityTimeoutMillis =
                    (wsInactivityTimeout == null) ? DEFAULT_WS_INACTIVITY_TIMEOUT_MILLIS : wsInactivityTimeout;

            Boolean sslEncryptionEnabled = null;
            if (connectOptions != null) {
                String encrypted = connectOptions.get("ssl.encryption");
                if (encrypted != null) {
                    sslEncryptionEnabled = encrypted.equalsIgnoreCase("enabled");
                }
            }

            this.sslEncryptionEnabled = (sslEncryptionEnabled == null) ? true : sslEncryptionEnabled;
        }
    }

    @Override
    public WebSocketWireProtocol getWebSocketWireProtocol() {
        return webSocketWireProtocol;
    }

    @Override
    public String[] getSslCiphers() {
        return sslCiphers;
    }

    @Override
    public String[] getSslProtocols() {
        return sslProtocols;
    }

    @Override
    public String getWsVersion() {
        return wsVersion;
    }

    @Override
    public URI getPipeTransport() {
        return pipeTransportURI;
    }

    @Override
    public URI getTcpTransport() {
        return tcpTransportURI;
    }

    @Override
    public URI getSslTransport() {
        return sslTransportURI;
    }

    @Override
    public URI getHttpTransport() {
        return httpTransportURI;
    }

    @Override
    public long getWsInactivityTimeout() {
        return wsInactivityTimeoutMillis;
    }

    @Override
    public boolean isSslEncryptionEnabled() {
        return sslEncryptionEnabled;
    }

    @Override
    public Map<String, Object> asOptionsMap() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put(SSL_CIPHERS, getSslCiphers());
        result.put(SSL_PROTOCOLS, getSslProtocols());
        result.put(WS_PROTOCOL_VERSION, webSocketWireProtocol);
        result.put(INACTIVITY_TIMEOUT, getWsInactivityTimeout());

        result.put(PIPE_TRANSPORT, getPipeTransport());
        result.put(TCP_TRANSPORT, getTcpTransport());
        result.put(SSL_TRANSPORT, getSslTransport());
        result.put("http[http/1.1].transport", getHttpTransport());

        result.put(SSL_ENCRYPTION_ENABLED, isSslEncryptionEnabled());
        result.put("udp.interface", getUdpInterface());

        return result;
    }

    public String getUdpInterface() {
        return udpInterface;
    }

    // We return a String array here, rather than a list, because the
    // javax.net.ssl.SSLEngine.setEnabledProtocols() method wants a
    // String array.
    public static String[] resolveProtocols(String csv) {
        if (csv != null && !csv.equals("")) {
            return csv.split(",");
        } else {
            return null;
        }
    }

}
