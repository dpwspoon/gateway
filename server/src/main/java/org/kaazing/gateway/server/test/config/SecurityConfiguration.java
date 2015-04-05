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

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class SecurityConfiguration implements Configuration {

    private final List<RealmConfiguration> realms = new ArrayList<>();

    private KeyStore _keyStore;
    private char[] _keyStorePassword;
    private char[] _trustStorePassword;
    private KeyStore _trustStore;
    private String keyStoreFile;
    private String trustStoreFile;
    private String trustStorePasswordFile;
    private String keyStorePasswordFile;

    public SecurityConfiguration() {
    }

    @Override
    public void accept(ConfigurationVisitor visitor) {
        visitor.visit(this);
    }

    public List<RealmConfiguration> getRealms() {
        return realms;
    }

    public void setRealms(List<RealmConfiguration> newRealms) {
        realms.clear();
        realms.addAll(newRealms);
    }

    // keystore
    public KeyStore getKeyStore() {
        if (_keyStore == null) {
            return null;
        }
        return _keyStore;
    }

    public void setKeyStore(KeyStore keyStore) {
        this._keyStore = keyStore;
    }

    // keyStore password
    public char[] getKeyStorePassword() {
        if (_keyStorePassword == null) {
            return null;
        }
        return _keyStorePassword;
    }

    public void setKeyStorePassword(char[] keyStorePassword) {
        this._keyStorePassword = keyStorePassword;
    }

    // trust store
    public KeyStore getTrustStore() {
        if (_trustStore == null) {
            return null;
        }
        return _trustStore;
    }

    public void setTrustStore(KeyStore trustStore) {
        this._trustStore = trustStore;
    }

    // keystore file

    /**
     * @param keyStoreFile
     * @Deprecated TODO : remove all file touch points
     */
    @Deprecated
    public void setKeyStoreFile(String keyStoreFile) {
        this.keyStoreFile = keyStoreFile;
    }

    @Deprecated
    public String getKeyStoreFile() {
        return keyStoreFile;
    }

    // truststore file
    @Deprecated
    public void setTrustStoreFile(String trustStoreFile) {
        this.trustStoreFile = trustStoreFile;
    }

    @Deprecated
    public String getTrustStoreFile() {
        return trustStoreFile;
    }

    public char[] getTrustStorePassword() {
        if (_trustStorePassword == null) {
            return null;
        }
        return _trustStorePassword;
    }

    public void setTrustStorePassword(char[] trustStorePassword) {
        this._trustStorePassword = trustStorePassword;
    }

    // trust store password file
    @Deprecated
    public String getTrustStorePasswordFile() {
        return trustStorePasswordFile;
    }

    @Deprecated
    public void setTrustStorePasswordFile(String trustStorePasswordFile) {
        this.trustStorePasswordFile = trustStorePasswordFile;
    }

    // key store password file
    @Deprecated
    public String getKeyStorePasswordFile() {
        return keyStorePasswordFile;
    }

    @Deprecated
    public void setKeyStorePasswordFile(String keyStorePasswordFile) {
        this.keyStorePasswordFile = keyStorePasswordFile;
    }

}
