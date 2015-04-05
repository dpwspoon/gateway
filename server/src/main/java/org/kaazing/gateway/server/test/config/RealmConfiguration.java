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

package org.kaazing.gateway.server.test.config;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class RealmConfiguration implements Configuration {

    private String _name;
    private String _description;
    private String _httpChallengeScheme;
    private String _authorizationMode = "challenge";
    private String _sessionTimeout;
    private final List<String> httpHeaders = new ArrayList<>();
    private final List<String> httpQueryParameters = new ArrayList<>();
    private final List<String> httpCookies = new ArrayList<>();
    private final List<LoginModuleConfiguration> loginModules = new LinkedList<>();
    private List<String> userPrincipalClasses = new ArrayList<String>();

    private static CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();

    public RealmConfiguration() {
    }

    @Override
    public void accept(ConfigurationVisitor visitor) {
        visitor.visit(this);
    }

    public Collection<LoginModuleConfiguration> getLoginModules() {
        return loginModules;
    }

    public void setLoginModules(List<LoginModuleConfiguration> newLoginModules) {
        loginModules.clear();
        loginModules.addAll(newLoginModules);
    }

    // name
    public String getName() {
        if (_name == null) {
            return null;
        }
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    // description
    public String getDescription() {
        if (_description == null) {
            return null;
        }
        return _description;
    }

    public void setDescription(String description) {
        if (description == null || asciiEncoder.canEncode(description)) {
            this._description = description;
        } else {
            throw new RuntimeException(
                    "Invalid non US-ASCII character in Realm description. Realm description can only contain US-ASCII values");
        }
    }

    // http challenge scheme
    public String getHttpChallengeScheme() {
        if (_httpChallengeScheme == null) {
            return null;
        }
        return _httpChallengeScheme;
    }

    public void setHttpChallengeScheme(String httpChallengeScheme) {
        this._httpChallengeScheme = httpChallengeScheme;
    }

    // authorization mode
    public String getAuthorizationMode() {
        if (_authorizationMode == null) {
            return null;
        }
        return _authorizationMode;
    }

    public void setAuthorizationMode(String authorizationMode) {
        this._authorizationMode = authorizationMode;
    }

    // session timeout
    public String getSessionTimeout() {
        if (_sessionTimeout == null) {
            return null;
        }
        return _sessionTimeout;
    }

    public void setSessionTimeout(String sessionTimeout) {
        this._sessionTimeout = sessionTimeout;
    }

    // http headers
    public List<String> getHttpHeaders() {
        return httpHeaders;
    }

    public void addHttpHeader(String httpHeader) {
        getHttpHeaders().add(httpHeader);
    }

    // http query parameters
    public List<String> getHttpQueryParameters() {
        return httpQueryParameters;
    }

    public void addHttpQueryParameter(String httpQueryParameter) {
        httpQueryParameters.add(httpQueryParameter);
    }

    // http cookies
    public List<String> getHttpCookies() {
        return httpCookies;
    }

    public void addHttpCookie(String httpCookie) {
        httpCookies.add(httpCookie);
    }

    public List<String> getUserPrincipalClasses() {
        return userPrincipalClasses;
    }

}
