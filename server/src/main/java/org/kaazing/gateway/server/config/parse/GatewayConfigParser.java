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

package org.kaazing.gateway.server.config.parse;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.kaazing.gateway.server.Launcher;
import org.kaazing.gateway.server.test.config.GatewayConfiguration;
import org.slf4j.Logger;

public class GatewayConfigParser {
    /**
     * Namespace for current release.
     */
    private static final String GATEWAY_CONFIG_NS = "http://xmlns.kaazing.org/2014/09/gateway";

    private static final Logger LOGGER = Launcher.getGatewayStartupLogger();

    private final Properties configuration;

    public GatewayConfigParser() {
        this(System.getProperties());
    }

    public GatewayConfigParser(Properties configuration) {
        this.configuration = configuration;
    }

    public GatewayConfiguration parse(final File configFile) throws Exception {
//        long time = 0;
//        if (LOGGER.isDebugEnabled()) {
//            time = System.currentTimeMillis();
//        }
//
//        XMLInputFactory factory = XMLInputFactory.newInstance();
//        XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(configFile));
//        while (reader.hasNext()) {
//            int event = reader.next();
//
//            switch (event) {
//            case XMLStreamConstants.START_ELEMENT:
//
//                break;
//            case XMLStreamConstants.CHARACTERS:
//                throw new UnexpectedXMLException(reader.getLocation(), "XMLStreamConstants.CHARACTERS");
//                break;
//            case XMLStreamConstants.END_ELEMENT:
//
//                break;
//            case XMLStreamConstants.START_DOCUMENT:
//                break;
//                return null;
//            }
//        }
        return null;

    }
}
