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

import java.io.File;

import org.kaazing.gateway.server.test.config.ClusterConfiguration;
import org.kaazing.gateway.server.test.config.GatewayConfiguration;
import org.kaazing.gateway.server.test.config.NetworkConfiguration;
import org.kaazing.gateway.server.test.config.SecurityConfiguration;
import org.kaazing.gateway.server.test.config.ServiceConfiguration;
import org.kaazing.gateway.server.test.config.ServiceDefaultsConfiguration;

public abstract class AbstractGatewayConfigurationBuilder<R> extends AbstractConfigurationBuilder<GatewayConfiguration, R> {

    public AbstractGatewayConfigurationBuilder(GatewayConfiguration configuration, R result) {
        super(configuration, result);
    }

    public AbstractGatewayConfigurationBuilder<R> property(String name, String value) {
        configuration.getProperties().put(name, value);
        return this;
    }

    public AbstractGatewayConfigurationBuilder<R> webRootDirectory(File webRootDirectory) {
        configuration.setWebRootDirectory(webRootDirectory);
        return this;
    }

    public AbstractGatewayConfigurationBuilder<R> tempDirectory(File tempDirectory) {
        configuration.setTempDirectory(tempDirectory);
        return this;
    }

    public abstract AbstractSecurityConfigurationBuilder<? extends AbstractGatewayConfigurationBuilder<R>> security();

    public abstract AbstractServiceConfigurationBuilder<? extends AbstractGatewayConfigurationBuilder<R>> service();

    public abstract AbstractClusterConfigurationBuilder<? extends AbstractGatewayConfigurationBuilder<R>> cluster();

    public abstract AbstractServiceDefaultsConfigurationBuilder<? extends AbstractGatewayConfigurationBuilder<R>>
    serviceDefaults();

    public abstract AbstractNetworkBuilder<? extends AbstractGatewayConfigurationBuilder<R>> network();

    public static class SetSecurityBuilder<R extends AbstractGatewayConfigurationBuilder<?>> extends
            AbstractSecurityConfigurationBuilder<R> {
        public SetSecurityBuilder(R result) {
            super(new SecurityConfiguration(), result);
        }

        @Override
        public AddRealmBuilder<SetSecurityBuilder<R>> realm() {
            return new AddRealmBuilder<>(this);
        }

        @Override
        public R done() {
            result.configuration.setSecurity(configuration);
            return super.done();
        }
    }

    public static class AddServiceBuilder<R extends AbstractGatewayConfigurationBuilder<?>> extends
            AbstractServiceConfigurationBuilder<R> {
        protected AddServiceBuilder(R result) {
            super(new ServiceConfiguration(), result);
        }

        @Override
        public AddCrossOriginConstraintBuilder<AddServiceBuilder<R>> crossOrigin() {
            return new AddCrossOriginConstraintBuilder<>(this);
        }

        @Override
        public AddAuthorizationConstraintBuilder<AddServiceBuilder<R>> authorization() {
            return new AddAuthorizationConstraintBuilder<>(this);
        }

        @Override
        public AddNestedPropertyBuilder<AddServiceBuilder<R>> nestedProperty(String propertyName) {
            return new AddNestedPropertyBuilder<>(propertyName, this);
        }

        @Override
        public R done() {
            result.configuration.getServices().add(configuration);
            return super.done();
        }
    }

    public static class SetClusterBuilder<R extends AbstractGatewayConfigurationBuilder<?>> extends
            AbstractClusterConfigurationBuilder<R> {

        protected SetClusterBuilder(R result) {
            super(new ClusterConfiguration(), result);
        }

        @Override
        public R done() {
            result.configuration.setCluster(configuration);
            return super.done();
        }
    }

    public static class SetServiceDefaultsBuilder<R extends AbstractGatewayConfigurationBuilder<?>> extends
            AbstractServiceDefaultsConfigurationBuilder<R> {

        protected SetServiceDefaultsBuilder(R result) {
            super(new ServiceDefaultsConfiguration(), result);
        }

        @Override
        public R done() {
            result.configuration.setServiceDefaults(configuration);
            return super.done();
        }
    }

    public static class AddNetworkBuilder<R extends AbstractGatewayConfigurationBuilder<?>> extends AbstractNetworkBuilder<R> {

        protected AddNetworkBuilder(R result) {
            super(new NetworkConfiguration(), result);
        }

        @Override
        public R done() {
            result.configuration.setNetwork(configuration);
            return super.done();
        }
    }

}
