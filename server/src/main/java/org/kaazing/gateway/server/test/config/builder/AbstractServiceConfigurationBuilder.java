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

import java.net.URI;

import org.kaazing.gateway.server.test.config.AuthorizationConstraintConfiguration;
import org.kaazing.gateway.server.test.config.CrossOriginConstraintConfiguration;
import org.kaazing.gateway.server.test.config.NestedServicePropertiesConfiguration;
import org.kaazing.gateway.server.test.config.ServiceConfiguration;

public abstract class AbstractServiceConfigurationBuilder<R> extends AbstractConfigurationBuilder<ServiceConfiguration, R> {

    public AbstractServiceConfigurationBuilder<R> balance(URI balance) {
        configuration.addBalance(balance);
        return this;
    }

    public AbstractServiceConfigurationBuilder<R> accept(URI accept) {
        configuration.addAccept(accept);
        return this;
    }

    public AbstractServiceConfigurationBuilder<R> acceptOption(String optionName, String optionValue) {
        configuration.addAcceptOption(optionName, optionValue);
        return this;
    }

    public AbstractServiceConfigurationBuilder<R> connect(URI connect) {
        configuration.addConnect(connect);
        return this;
    }

    public AbstractServiceConfigurationBuilder<R> connectOption(String optionName, String optionValue) {
        configuration.addConnectOption(optionName, optionValue);
        return this;
    }

    public AbstractServiceConfigurationBuilder<R> realmName(String realmName) {
        configuration.setRealmName(realmName);
        return this;
    }

    public AbstractServiceConfigurationBuilder<R> type(String type) {
        configuration.setType(type);
        return this;
    }

    public AbstractServiceConfigurationBuilder<R> property(String propertyName, String propertyValue) {
        configuration.addProperty(propertyName, propertyValue);
        return this;
    }

    public AbstractServiceConfigurationBuilder<R> mimeMapping(String extension, String type) {
        configuration.addMimeMapping(extension, type);
        return this;
    }

    public AbstractServiceConfigurationBuilder<R> description(String description) {
        configuration.setDescription(description);
        return this;
    }

    public AbstractServiceConfigurationBuilder<R> name(String name) {
        configuration.setName(name);
        return this;
    }

    public abstract AbstractNestedPropertyConfigurationBuilder<? extends AbstractServiceConfigurationBuilder<R>> nestedProperty(
        String propertyName);

    public abstract AbstractAuthorizationConstraintConfigurationBuilder<? extends AbstractServiceConfigurationBuilder<R>>
        authorization();

    public abstract AbstractCrossOriginConstraintConfigurationBuilder<? extends AbstractServiceConfigurationBuilder<R>>
        crossOrigin();

    protected AbstractServiceConfigurationBuilder(ServiceConfiguration configuration, R result) {
        super(configuration, result);
    }

    public static class AddCrossOriginConstraintBuilder<R extends AbstractServiceConfigurationBuilder<?>> extends
            AbstractCrossOriginConstraintConfigurationBuilder<R> {
        protected AddCrossOriginConstraintBuilder(R result) {
            super(new CrossOriginConstraintConfiguration(), result);
        }

        @Override
        public R done() {
            result.configuration.getCrossOriginConstraints().add(configuration);
            return super.done();
        }
    }

    public static class AddAuthorizationConstraintBuilder<R extends AbstractServiceConfigurationBuilder<?>> extends
            AbstractAuthorizationConstraintConfigurationBuilder<R> {
        protected AddAuthorizationConstraintBuilder(R result) {
            super(new AuthorizationConstraintConfiguration(), result);
        }

        @Override
        public R done() {
            result.configuration.getAuthorizationConstraints().add(configuration);
            return super.done();
        }
    }

    public static class AddNestedPropertyBuilder<R extends AbstractServiceConfigurationBuilder<?>> extends
            AbstractNestedPropertyConfigurationBuilder<R> {

        final String propertyName;

        protected AddNestedPropertyBuilder(String propertyName, R result) {
            super(new NestedServicePropertiesConfiguration(propertyName), result);
            this.propertyName = propertyName;
        }

        @Override
        public R done() {
            result.configuration.addNestedProperties(configuration);
            return super.done();
        }
    }

}
