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
import java.util.HashSet;
import java.util.Set;

public class ClusterConfiguration implements Configuration {

    private final Set<URI> accepts;
    private final Set<URI> connects;

    private String _name;
    private String _awsAccessKeyId;
    private String _awsSecretKeyId;

    public ClusterConfiguration() {

        accepts = new HashSet<>();
        connects = new HashSet<>();
    }

    @Override
    public void accept(ConfigurationVisitor visitor) {
        visitor.visit(this);
    }

    // accepts
    public void addAccept(URI acceptURI) {
        accepts.add(acceptURI);
    }

    public Set<URI> getAccepts() {
        return accepts;
    }

    // connect
    public void addConnect(URI connectURI) {
        accepts.add(connectURI);
    }

    public Set<URI> getConnects() {
        return connects;
    }

    // AwsAccessKey

    public String getAwsAccessKeyId() {
        if (_awsAccessKeyId == null) {
            return null;
        }
        return _awsAccessKeyId;
    }

    public void setAwsAccessKeyId(String awsAccessKeyId) {
        this._awsAccessKeyId = awsAccessKeyId;
    }

    // AwsSecretKey
    public String getAwsSecretKeyId() {
        if (_awsSecretKeyId == null) {
            return null;
        }
        return _awsSecretKeyId;
    }

    public void setAwsSecretKeyId(String awsSecretKeyId) {
        this._awsSecretKeyId = awsSecretKeyId;
    }

    // Name
    public String getName() {
        if (_name == null) {
            return null;
        }
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

}
