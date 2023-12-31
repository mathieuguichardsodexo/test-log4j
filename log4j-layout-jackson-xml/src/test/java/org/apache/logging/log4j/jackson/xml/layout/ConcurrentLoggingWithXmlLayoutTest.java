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
package org.apache.logging.log4j.jackson.xml.layout;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.test.junit.LoggerContextRule;
import org.junit.AfterClass;
import org.junit.ClassRule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

/**
 * Like the test for LOG4J2-1769.
 */
public class ConcurrentLoggingWithXmlLayoutTest {

    private class LoggingThread extends Thread {
        private final Set<Thread> threads;
        private final Logger log;

        LoggingThread(final Set<Thread> threads, final Logger log) {
            this.threads = threads;
            this.log = log;
        }

        @Override
        public void run() {
            log.info(threads.size());
            try {
                for (int i = 0; i < 64; i++) {
                    log.info("First message.");
                    log.info("Second message.");
                }
            } finally {
                threads.remove(this);
            }
        }
    }

    @ClassRule
    public static LoggerContextRule context = new LoggerContextRule("log4j2-xml-layout.xml");

    private static final String PATH = "target/test-xml-layout.log";

    @AfterClass
    public static void after() {
        new File(PATH).delete();
    }

    @Test
    public void testConcurrentLogging() throws Throwable {
        final Logger log = context.getLogger(ConcurrentLoggingWithXmlLayoutTest.class);
        final Set<Thread> threads = Collections.synchronizedSet(new HashSet<Thread>());
        final List<Throwable> thrown = Collections.synchronizedList(new ArrayList<Throwable>());

        for (int x = 0; x < Runtime.getRuntime().availableProcessors() * 2; x++) {
            final Thread t = new LoggingThread(threads, log);
            threads.add(t);

            // Appender is configured with ignoreExceptions="false";
            // any exceptions are propagated to the caller, so we can catch them here.
            t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(final Thread t, final Throwable e) {
                    thrown.add(e);
                }
            });
            t.start();
        }

        while (!threads.isEmpty()) {
            log.info("not done going to sleep...");
            Thread.sleep(10);
        }

        // if any error occurred, fail this test
        if (!thrown.isEmpty()) {
            throw thrown.get(0);
        }

        // simple test to ensure content is not corrupted
        if (new File(PATH).exists()) {
            final List<String> lines = Files.readAllLines(new File(PATH).toPath(), Charset.defaultCharset());
            for (final String line : lines) {
                assertThat(line, startsWith("<Event "));
                assertThat(line, endsWith("</Event>"));
            }
        }
    }
}
