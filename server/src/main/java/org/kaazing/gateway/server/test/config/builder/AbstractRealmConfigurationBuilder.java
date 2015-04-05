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

package org.kaazing.gateway.server.test.config.builder;

import org.kaazing.gateway.server.test.config.LoginModuleConfiguration;
import org.kaazing.gateway.server.test.config.RealmConfiguration;

public abstract class AbstractRealmConfigurationBuilder<R> extends AbstractConfigurationBuilder<RealmConfiguration, R> {

    public AbstractRealmConfigurationBuilder<R> name(String name) {
        configuration.setName(name);
        return this;
    }

    public AbstractRealmConfigurationBuilder<R> description(String description) {
        configuration.setDescription(description);
        return this;
    }

    public abstract AbstractLoginModuleConfigurationBuilder<? extends AbstractRealmConfigurationBuilder<R>> loginModule();

    protected AbstractRealmConfigurationBuilder(RealmConfiguration configuration, R result) {
        super(configuration, result);
    }

    public static class AddLoginModuleBuilder<R extends AbstractRealmConfigurationBuilder<?>> extends
            AbstractLoginModuleConfigurationBuilder<R> {
        protected AddLoginModuleBuilder(R result) {
            super(new LoginModuleConfiguration(), result);
        }

        @Override
        public R done() {
            result.configuration.getLoginModules().add(configuration);
            return super.done();
        }

    }

    public AbstractRealmConfigurationBuilder<R> httpChallengeScheme(String httpChallengeScheme) {
        configuration.setHttpChallengeScheme(httpChallengeScheme);
        return this;
    }

    public AbstractRealmConfigurationBuilder<R> httpHeader(String header) {
        configuration.addHttpHeader(header);
        return this;
    }

    public AbstractRealmConfigurationBuilder<R> httpQueryParameter(String queryParameter) {
        configuration.addHttpQueryParameter(queryParameter);
        return this;
    }

    public AbstractRealmConfigurationBuilder<R> httpCookie(String cookie) {
        configuration.addHttpCookie(cookie);
        return this;
    }

    public AbstractRealmConfigurationBuilder<R> authorizationMode(String chars) {
        configuration.setAuthorizationMode(chars);
        return this;
    }

    public AbstractRealmConfigurationBuilder<R> sessionTimeout(String chars) {
        configuration.setSessionTimeout(chars);
        return this;
    }

}
