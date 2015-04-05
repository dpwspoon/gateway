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

import static org.kaazing.gateway.service.TransportOptionNames.PIPE_TRANSPORT;
import static org.kaazing.gateway.service.TransportOptionNames.SSL_CIPHERS;
import static org.kaazing.gateway.service.TransportOptionNames.SSL_ENCRYPTION_ENABLED;
import static org.kaazing.gateway.service.TransportOptionNames.SSL_NEED_CLIENT_AUTH;
import static org.kaazing.gateway.service.TransportOptionNames.SSL_PROTOCOLS;
import static org.kaazing.gateway.service.TransportOptionNames.SSL_TRANSPORT;
import static org.kaazing.gateway.service.TransportOptionNames.SSL_WANT_CLIENT_AUTH;
import static org.kaazing.gateway.service.TransportOptionNames.SUPPORTED_PROTOCOLS;
import static org.kaazing.gateway.service.TransportOptionNames.TCP_MAXIMUM_OUTBOUND_RATE;
import static org.kaazing.gateway.service.TransportOptionNames.TCP_TRANSPORT;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.kaazing.gateway.service.AcceptOptionsContext;
import org.kaazing.gateway.util.Utils;
import org.kaazing.gateway.util.ssl.SslCipherSuites;

public class DefaultAcceptOptionsContext implements AcceptOptionsContext {
    private static int DEFAULT_WEBSOCKET_MAXIMUM_MESSAGE_SIZE = 128 * 1024; // 128KB
    private static int DEFAULT_HTTP_KEEPALIVE_TIMEOUT = 30; // seconds
    private static final long UNLIMITED_MAX_OUTPUT_RATE = 0xFFFFFFFFL;
    private static long DEFAULT_TCP_MAXIMUM_OUTBOUND_RATE = UNLIMITED_MAX_OUTPUT_RATE; // unlimited

    /**
     * The name of the extended handshake protocol to be sent on the wire.
     */
    public static final String EXTENDED_HANDSHAKE_PROTOCOL_NAME = "x-kaazing-handshake";

    private static final String PING_PONG = "x-kaazing-ping-pong";
    private static final String IDLE_TIMEOUT = "x-kaazing-idle-timeout";
    private static final long DEFAULT_WS_INACTIVITY_TIMEOUT_MILLIS = 0L;

    private static List<String> DEFAULT_WEBSOCKET_PROTOCOLS;
    private static List<String> DEFAULT_WEBSOCKET_EXTENSIONS;

    static {
        // Note: including null in these arrays permits us to process no protocols/extensions
        // without throwing an exception.
        DEFAULT_WEBSOCKET_PROTOCOLS = Arrays.asList(EXTENDED_HANDSHAKE_PROTOCOL_NAME, null);
        DEFAULT_WEBSOCKET_EXTENSIONS = Arrays.asList(PING_PONG, null);
    }

    private final boolean sslEncryptionEnabled;
    private final String[] sslCiphers;
    private final String[] sslProtocols;
    private final boolean sslWantClientAuth;
    private final boolean sslNeedClientAuth;
    private final URI tcpTransportURI;
    private final URI sslTransportURI;
    private final URI httpTransportURI;
    private final int wsMaxMessageSize;
    private final long wsInactivityTimeout;
    private final int httpKeepaliveTimeout;
    private final Map<String, String> binds;
    private final List<String> wsProtocols;
    private final List<String> wsExtensions;
    private final long tcpMaximumOutboundRate;
    private final String udpInterface;
    private final URI pipeTransportURI;

    public DefaultAcceptOptionsContext() {
        this(new HashMap<String, String>(), new HashMap<String, String>());
    }

    public DefaultAcceptOptionsContext(Map<String, String> acceptOptions, Map<String, String> defaultOptionsConfig) {
        defaultOptionsConfig.putAll(acceptOptions);
        acceptOptions = defaultOptionsConfig;
        this.binds = new HashMap<>();

        Boolean sslEncryptionEnabled = null;
        String encrypted = acceptOptions.get("ssl.encryption");
        if (encrypted != null) {
            sslEncryptionEnabled = encrypted.equalsIgnoreCase("encrypted");
        }

        boolean wantClientAuth = false;
        boolean needClientAuth = false;

        if (acceptOptions != null) {
            String verifyClient = acceptOptions.get("ssl.verify-client");
            if (verifyClient != null) {
                if (verifyClient.equalsIgnoreCase("REQUIRED")) {
                    wantClientAuth = false;
                    needClientAuth = true;

                } else if (verifyClient.equalsIgnoreCase("OPTIONAL")) {
                    wantClientAuth = true;
                    needClientAuth = false;

                } else {
                    wantClientAuth = false;
                    needClientAuth = false;
                }
            }
        }

        String udpInterface = null;
        if (acceptOptions != null) {
            udpInterface = acceptOptions.get("udp.interface");
        }

        String sslCiphers = null;
        if (acceptOptions != null) {
            sslCiphers = acceptOptions.get("ssl.ciphers");
        }

        String sslProtocols = null;
        if (acceptOptions != null) {
            sslProtocols = acceptOptions.get("ssl.protocols");
        }

        String pipeTransport = null;
        if (acceptOptions != null) {
            pipeTransport = acceptOptions.get("pipe.transport");
        }

        String tcpTransport = null;
        if (acceptOptions != null) {
            tcpTransport = acceptOptions.get("tcp.transport");
        }

        String sslTransport = null;
        if (acceptOptions != null) {
            sslTransport = acceptOptions.get("ssl.transport");
        }

        String httpTransport = null;
        if (acceptOptions != null) {
            httpTransport = acceptOptions.get("http.transport");
        }

        Long httpKeepaliveTimeout = null;
        if (acceptOptions != null) {
            String value = acceptOptions.get("http.keepalive.timeout");
            if (value != null) {
                long val = Utils.parseTimeInterval(value, TimeUnit.SECONDS);
                if (val > 0) {
                    httpKeepaliveTimeout = val;
                }
            }
        }

        Integer wsMaxMessageSize = null;
        if (acceptOptions != null) {
            String wsMax = acceptOptions.get("ws.maximum.message.size");
            if (wsMax != null) {
                wsMaxMessageSize = Utils.parseDataSize(wsMax);
            }
        }

        Long wsInactivityTimeout = null;
        if (acceptOptions != null) {
            String value = acceptOptions.get("ws.inactivity.timeout");
            if (value != null) {
                long val = Utils.parseTimeInterval(value, TimeUnit.MILLISECONDS);
                if (val > 0) {
                    wsInactivityTimeout = val;
                }
            }
        }

        Long tcpMaxOutboundRate = null;
        if (acceptOptions != null) {
            String tcpMax = acceptOptions.get("tcp.maximum.outbound.rate");
            if (tcpMax != null) {
                tcpMaxOutboundRate = Utils.parseDataRate(tcpMax);
            }
        }

        this.sslEncryptionEnabled = (sslEncryptionEnabled == null) ? true : sslEncryptionEnabled;
        this.sslCiphers = (sslCiphers != null) ? SslCipherSuites.resolveCSV(sslCiphers) : null;
        this.sslProtocols = (sslProtocols != null) ? resolveProtocols(sslProtocols) : null;
        this.sslWantClientAuth = acceptOptions.get("ssl.verify-client").equalsIgnoreCase("optional");
        this.sslNeedClientAuth = acceptOptions.get("ssl.verify-client").equalsIgnoreCase("required");

        if (pipeTransport != null) {
            this.pipeTransportURI = URI.create(pipeTransport);
            if (!this.pipeTransportURI.isAbsolute()) {
                throw new IllegalArgumentException(String.format("pipe.transport must contain an absolute URI, not \"%s\"",
                        pipeTransport));
            }

        } else {
            this.pipeTransportURI = null;
        }

        if (tcpTransport != null) {
            this.tcpTransportURI = URI.create(tcpTransport);
            if (!this.tcpTransportURI.isAbsolute()) {
                throw new IllegalArgumentException(String.format("tcp.transport must contain an absolute URI, not \"%s\"",
                        tcpTransport));
            }

        } else {
            this.tcpTransportURI = null;
        }

        if (sslTransport != null) {
            this.sslTransportURI = URI.create(sslTransport);
            if (!this.sslTransportURI.isAbsolute()) {
                throw new IllegalArgumentException(String.format("ssl.transport must contain an absolute URI, not \"%s\"",
                        sslTransport));
            }
        } else {
            this.sslTransportURI = null;
        }

        if (httpTransport != null) {
            this.httpTransportURI = URI.create(httpTransport);
            if (!this.httpTransportURI.isAbsolute()) {
                throw new IllegalArgumentException(String.format("http.transport must contain an absolute URI, not \"%s\"",
                        httpTransport));
            }
        } else {
            this.httpTransportURI = null;
        }

        // We are documenting that a configured outbound rate of 0 means unlimited and, and we will treat as unlimited
        // rates
        // at or above 0xFFFFFFFF. Handle these cases here at the edge so the rest of our code doesn't need to worry.
        this.tcpMaximumOutboundRate =
                (tcpMaxOutboundRate == null) ? DEFAULT_TCP_MAXIMUM_OUTBOUND_RATE
                        : (tcpMaxOutboundRate == 0 || tcpMaxOutboundRate > UNLIMITED_MAX_OUTPUT_RATE) ? UNLIMITED_MAX_OUTPUT_RATE
                                : tcpMaxOutboundRate;

        this.wsMaxMessageSize = (wsMaxMessageSize == null) ? DEFAULT_WEBSOCKET_MAXIMUM_MESSAGE_SIZE : wsMaxMessageSize;
        this.wsInactivityTimeout = (wsInactivityTimeout == null) ? DEFAULT_WS_INACTIVITY_TIMEOUT_MILLIS : wsInactivityTimeout;
        this.httpKeepaliveTimeout =
                (httpKeepaliveTimeout == null) ? DEFAULT_HTTP_KEEPALIVE_TIMEOUT : httpKeepaliveTimeout.intValue();
        // Hard code supported protocols and extensions.
        this.wsProtocols = DEFAULT_WEBSOCKET_PROTOCOLS;

        // KG-9977 add x-kaazing-idle-timeout extension if configured
        if (this.wsInactivityTimeout > 0) {
            ArrayList<String> extensions = new ArrayList<>(DEFAULT_WEBSOCKET_EXTENSIONS);
            extensions.add(IDLE_TIMEOUT);
            this.wsExtensions = extensions;
        } else {
            this.wsExtensions = DEFAULT_WEBSOCKET_EXTENSIONS;
        }

        // if there were default options, overlay the service accept options to have the final options for this service
        if (acceptOptions != null) {
            addBind("ws", acceptOptions.get("ws.bind"));
            addBind("wss", acceptOptions.get("wss.bind"));
            addBind("http", acceptOptions.get("http.bind"));
            addBind("https", acceptOptions.get("https.bind"));
            addBind("ssl", acceptOptions.get("ssl.bind"));
            addBind("tcp", acceptOptions.get("tcp.bind"));
        }

        this.udpInterface = udpInterface;
    }

    @Override
    public Map<String, String> getBinds() {
        return binds;
    }

    @Override
    public boolean isSslEncryptionEnabled() {
        return sslEncryptionEnabled;
    }

    @Override
    public Integer getSessionIdleTimeout(String scheme) {
        Integer ret = null;
        if (scheme.equals("http") || scheme.equals("https")) {
            ret = httpKeepaliveTimeout;
        }
        return ret;
    }

    @Override
    public Integer getHttpKeepaliveTimeout() {
        return httpKeepaliveTimeout;
    }

    @Override
    public int getWsMaxMessageSize() {
        return wsMaxMessageSize;
    }

    @Override
    public long getWsInactivityTimeout() {
        return wsInactivityTimeout;
    }

    @Override
    public long getTcpMaximumOutboundRate() {
        return tcpMaximumOutboundRate;
    }

    @Override
    public List<String> getWsProtocols() {
        return wsProtocols;
    }

    @Override
    public List<String> getWsExtensions() {
        return wsExtensions;
    }

    @Override
    public URI getInternalURI(URI externalURI) {
        String authority = externalURI.getAuthority();
        String internalAuthority = binds.get(externalURI.getScheme());
        if (internalAuthority != null) {
            if (!internalAuthority.equals(authority)) {
                try {
                    return new URI(externalURI.getScheme(), internalAuthority, externalURI.getPath(), externalURI.getQuery(),
                            externalURI.getFragment());
                } catch (URISyntaxException e) {
                    // ignore
                }
            }
            return externalURI;
        }

        // there's no binding for this URI, return null
        return null;
    }

    @Override
    public void addBind(String scheme, String hostPort) {
        if (hostPort != null) {
            if (!hostPort.contains(":")) {
                try {
                    int port = Integer.parseInt(hostPort);
                    this.binds.put(scheme, "0.0.0.0:" + port);
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("Failed to add bind for scheme " + scheme + " to port " + hostPort, ex);
                }
            } else {
                this.binds.put(scheme, hostPort);
            }
        }
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
    public boolean getSslWantClientAuth() {
        return sslWantClientAuth;
    }

    @Override
    public boolean getSslNeedClientAuth() {
        return sslNeedClientAuth;
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
    public URI getPipeTransport() {
        return pipeTransportURI;
    }

    @Override
    public String getUdpInterface() {
        return udpInterface;
    }

    public Map<String, Object> asOptionsMap() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put(SUPPORTED_PROTOCOLS, getWsProtocols().toArray(new String[getWsProtocols().size()]));
        result.put("ws.extensions", getWsExtensions());
        result.put("ws[ws/rfc6455].ws[ws/rfc6455].extensions", getWsExtensions());
        result.put("ws[ws/draft-7x].ws[ws/draft-7x].extensions", getWsExtensions());
        result.put("ws.maxMessageSize", getWsMaxMessageSize());
        result.put("ws[ws/rfc6455].ws[ws/rfc6455].maxMessageSize", getWsMaxMessageSize());
        result.put("ws[ws/draft-7x].ws[ws/draft-7x].maxMessageSize", getWsMaxMessageSize());
        result.put("ws.inactivityTimeout", getWsInactivityTimeout());
        result.put("ws[ws/rfc6455].ws[ws/rfc6455].inactivityTimeout", getWsInactivityTimeout());
        result.put("ws[ws/draft-7x].ws[ws/draft-7x].inactivityTimeout", getWsInactivityTimeout());

        result.put("http[http/1.1].keepAliveTimeout", getHttpKeepaliveTimeout());
        result.put(PIPE_TRANSPORT, getPipeTransport());
        result.put(SSL_CIPHERS, getSslCiphers());
        result.put(SSL_PROTOCOLS, getSslProtocols());
        result.put(SSL_ENCRYPTION_ENABLED, isSslEncryptionEnabled());
        result.put(SSL_WANT_CLIENT_AUTH, getSslWantClientAuth());
        result.put(SSL_NEED_CLIENT_AUTH, getSslNeedClientAuth());

        result.put(TCP_TRANSPORT, getTcpTransport());
        result.put(SSL_TRANSPORT, getSslTransport());
        result.put("http[http/1.1].transport", getHttpTransport());

        result.put(TCP_MAXIMUM_OUTBOUND_RATE, getTcpMaximumOutboundRate());
        result.put("udp.interface", getUdpInterface());

        for (Map.Entry<String, String> entry : getBinds().entrySet()) {
            /*
             * For lookups out of this COPY of the options, we need to translate the scheme names into hierarchical
             * transport names, i.e.:
             * 
             * ssl -> ssl.tcp.bind http -> http.tcp.bind https -> http.ssl.tcp.bind ws -> ws.tcp.bind wss ->
             * ws.ssl.tcp.bind
             */

            String internalBindOptionName = resolveInternalBindOptionName(entry.getKey());
            if (internalBindOptionName != null) {
                result.put(internalBindOptionName, entry.getValue());
            } else {
                throw new RuntimeException("Cannot apply unknown bind option '" + entry.getKey() + "'.");
            }
        }

        return result;
    }

    private String resolveInternalBindOptionName(String externalBindOptionName) {
        if (externalBindOptionName.equals("tcp")) {
            return "tcp.bind";
        } else if (externalBindOptionName.equals("ssl")) {
            return "ssl.tcp.bind";
        } else if (externalBindOptionName.equals("http")) {
            return "http.tcp.bind";
        } else if (externalBindOptionName.equals("https")) {
            return "http.ssl.tcp.bind";
        } else if (externalBindOptionName.equals("ws")) {
            return "ws.http.tcp.bind";
        } else if (externalBindOptionName.equals("wss")) {
            return "ws.http.ssl.tcp.bind";
        } else if (externalBindOptionName.equals("wsn")) {
            return "wsn.http.tcp.bind";
        } else if (externalBindOptionName.equals("wsn+ssl")) {
            return "wsn.http.ssl.tcp.bind";
        } else if (externalBindOptionName.equals("wsx")) {
            return "wsn.http.wsn.http.tcp.bind";
        } else if (externalBindOptionName.equals("wsx+ssl")) {
            return "wsn.http.wsn.http.ssl.tcp.bind";
        } else if (externalBindOptionName.equals("httpxe")) {
            return "http.http.tcp.bind";
        } else if (externalBindOptionName.equals("httpxe+ssl")) {
            return "http.http.ssl.tcp.bind";
        }
        return null;
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
