/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.logging.log4j.core;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;
import org.slf4j.LoggerFactory;

/**
 *
 */
@SetSystemProperty(key = "logback.configurationFile", value = "logback-subst.xml")
public class LogbackSubstitution {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(LogbackSubstitution.class);

    @Test
    public void testSubst() {
        logger.debug("Hello, {}", "Log4j {}");
        logger.debug("Hello, {}");
    }
}
