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
package org.apache.logging.log4j.jackson.xml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.apache.logging.log4j.core.impl.ExtendedStackTraceElement;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.jackson.ThrowableProxyMixIn;
import org.apache.logging.log4j.jackson.XmlConstants;

public abstract class ThrowableProxyXmlMixIn extends ThrowableProxyMixIn {

    @JacksonXmlProperty(namespace = XmlConstants.XML_NAMESPACE, localName = XmlConstants.ELT_CAUSE)
    private ThrowableProxyMixIn causeProxy;

    @JacksonXmlProperty(isAttribute = true)
    private int commonElementCount;

    @JacksonXmlElementWrapper(namespace = XmlConstants.XML_NAMESPACE, localName = XmlConstants.ELT_EXTENDED_STACK_TRACE)
    @JacksonXmlProperty(namespace = XmlConstants.XML_NAMESPACE, localName = XmlConstants.ELT_EXTENDED_STACK_TRACE_ITEM)
    private ExtendedStackTraceElement[] extendedStackTrace;

    @JacksonXmlProperty(isAttribute = true)
    private String localizedMessage;

    @JacksonXmlProperty(isAttribute = true)
    private String message;

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JsonIgnore
    private transient Throwable throwable;

    @Override
    @JsonIgnore
    public abstract String getCauseStackTraceAsString();

    @Override
    @JsonIgnore
    public abstract String getExtendedStackTraceAsString();

    @Override
    @JsonIgnore
    public abstract StackTraceElement[] getStackTrace();

    @Override
    @JacksonXmlElementWrapper(namespace = XmlConstants.XML_NAMESPACE, localName = XmlConstants.ELT_SUPPRESSED)
    @JacksonXmlProperty(namespace = XmlConstants.XML_NAMESPACE, localName = XmlConstants.ELT_SUPPRESSED_ITEM)
    public abstract ThrowableProxy[] getSuppressedProxies();

    @Override
    @JsonIgnore
    public abstract String getSuppressedStackTrace();

    @Override
    @JsonIgnore
    public abstract Throwable getThrowable();

}
