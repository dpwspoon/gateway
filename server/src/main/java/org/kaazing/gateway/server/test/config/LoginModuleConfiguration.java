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

import java.util.HashMap;
import java.util.Map;

public class LoginModuleConfiguration implements Configuration {

    private String _type;
    private String _success;
    private final Map<String, String> options = new HashMap<>();

    public LoginModuleConfiguration() {
    }

    @Override
    public void accept(ConfigurationVisitor visitor) {
        visitor.visit(this);
    }

    // Type
    public String getType() {
        if (_type == null) {
            return null;
        }
        return _type;
    }

    public void setType(String type) {
        this._type = type;
    }

    // Success
    public String getSuccess() {
        if (_success == null) {
            return null;
        }
        return _success;
    }

    public void setSuccess(String success) {
        this._success = success;
    }

    // Options
    public Map<String, String> getOptions() {
        return options;
    }

    public void addOption(String optionKey, String optionValue) {
        options.put(optionKey, optionValue);
    }

}
