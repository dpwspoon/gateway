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

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServiceConfiguration implements Configuration {

    private final List<AuthorizationConstraintConfiguration> authorizationConstraints;
    private final List<CrossOriginConstraintConfiguration> crossOriginConstraints;

    private String _name;
    private String _type;
    private String _realmName;
    private String _description;

    private final Set<URI> balances;
    private final Set<URI> accepts;
    private final Map<String, String> acceptOptions;
    private final Set<URI> connects;
    private final Map<String, String> connectOptions;
    private final Map<String, String> mimeMappings;
    private final Map<String, String> properties;
    private final List<NestedServicePropertiesConfiguration> nestedProperties;

    public ServiceConfiguration() {
        balances = new HashSet<>();
        accepts = new HashSet<>();
        acceptOptions = new HashMap<>();
        connects = new HashSet<>();
        connectOptions = new HashMap<>();
        mimeMappings = new HashMap<>();
        properties = new HashMap<>();
        authorizationConstraints = new LinkedList<>();
        crossOriginConstraints = new LinkedList<>();
        nestedProperties = new LinkedList<>();
    }

    @Override
    public void accept(ConfigurationVisitor visitor) {
        visitor.visit(this);
    }

    public List<AuthorizationConstraintConfiguration> getAuthorizationConstraints() {
        return authorizationConstraints;
    }

    public List<CrossOriginConstraintConfiguration> getCrossOriginConstraints() {
        return crossOriginConstraints;
    }

    // accept
    public void addAccept(URI acceptURI) {
        accepts.add(acceptURI);
    }

    public Set<URI> getAccepts() {
        return accepts;
    }

    // balance
    public void addBalance(URI balanceURI) {
        balances.add(balanceURI);
    }

    public Set<URI> getBalances() {
        return balances;
    }

    // connect
    public void addConnect(URI connectURI) {
        connects.add(connectURI);
    }

    public Set<URI> getConnects() {
        return connects;
    }

    // connect options
    public void addConnectOption(String key, String value) {
        connectOptions.put(key, value);
    }

    public Map<String, String> getConnectOptions() {
        return connectOptions;
    }

    // mime mapping
    public void addMimeMapping(String key, String value) {
        mimeMappings.put(key, value);
    }

    public Map<String, String> getMimeMappings() {
        return mimeMappings;
    }

    // accept options
    public void addAcceptOption(String key, String value) {
        acceptOptions.put(key, value);
    }

    public Map<String, String> getAcceptOptions() {
        return acceptOptions;
    }

    // description
    public void setDescription(String description) {
        this._description = description;
    }

    public String getDescription() {
        if (_description == null) {
            return null;
        }
        return _description;
    }

    // name
    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        if (_name == null) {
            return null;
        }
        return _name;
    }

    // realm name
    public void setRealmName(String realmName) {
        this._realmName = realmName;
    }

    public String getRealmName() {
        if (_realmName == null) {
            return null;
        }
        return _realmName;
    }

    // type
    public void setType(String type) {
        this._type = type;
    }

    public String getType() {
        if (_type == null) {
            return null;
        }
        return _type;
    }

    // properties
    public List<NestedServicePropertiesConfiguration> getNestedProperties() {
        return nestedProperties;
    }

    public void addNestedProperties(NestedServicePropertiesConfiguration configuration) {
        nestedProperties.add(configuration);
    }

    // properties
    public Map<String, String> getProperties() {
        return properties;
    }

    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

}
