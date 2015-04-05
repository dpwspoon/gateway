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

public class CrossOriginConstraintConfiguration implements Configuration {

    private String _allowOrigin;
    private String _allowMethods;
    private String _allowHeaders;

    public CrossOriginConstraintConfiguration() {
    }

    @Override
    public void accept(ConfigurationVisitor visitor) {
        visitor.visit(this);
    }

    public String getAllowOrigin() {
        if (_allowOrigin == null) {
            return null;
        }
        return _allowOrigin;
    }

    public void setAllowOrigin(String allowOrigin) {
        this._allowOrigin = allowOrigin;
    }

    public String getAllowMethods() {
        if (_allowMethods == null) {
            return null;
        }
        return _allowMethods;
    }

    public void setAllowMethods(String allowMethods) {
        this._allowMethods = allowMethods;
    }

    public String getAllowHeaders() {
        if (_allowHeaders == null) {
            return null;
        }
        return _allowHeaders;
    }

    public void setAllowHeaders(String allowHeaders) {
        this._allowHeaders = allowHeaders;
    }

}
