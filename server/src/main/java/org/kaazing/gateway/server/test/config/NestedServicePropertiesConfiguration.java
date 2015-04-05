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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class NestedServicePropertiesConfiguration implements Configuration {

    private String configElementName;
    private final Map<String, String> suppressibleSimpleProperties = new HashMap<>();
    private final Map<String, String> simpleProperties = new HashMap<String, String>();
    private final Stack<NestedServicePropertiesConfiguration> nestedServiceProperties = new Stack<>();

    public NestedServicePropertiesConfiguration(String configElementName) {
        this.configElementName = configElementName;
    }

    public Map<String, String> getSimpleProperties() {
        return simpleProperties;
    }

    public void addSimpleProperty(String key, String value) {
        simpleProperties.put(key, value);
    }

    public Collection<NestedServicePropertiesConfiguration> getNestedProperties() {
        return nestedServiceProperties;
    }

    public String getConfigElementName() {
        return configElementName;
    }

    public void setConfigElementName(String configElementName) {
        this.configElementName = configElementName;
    }

    @Override
    public void accept(ConfigurationVisitor visitor) {
        visitor.visit(this);
    }

}
