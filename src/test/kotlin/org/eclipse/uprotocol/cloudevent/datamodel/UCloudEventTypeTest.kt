/*
 * Copyright (c) 2023 General Motors GTO LLC
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
package org.eclipse.uprotocol.cloudevent.datamodel

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

internal class UCloudEventTypeTest {
    @Test
    @DisplayName("Test the type for a publish event type")
    fun test_type_for_publish() {
        val uCloudEventType: UCloudEventType = UCloudEventType.PUBLISH
        assertEquals("pub.v1", uCloudEventType.type())
    }

    @Test
    @DisplayName("Test the type for a request RPC event type")
    fun test_type_for_request() {
        val uCloudEventType: UCloudEventType = UCloudEventType.REQUEST
        assertEquals("req.v1", uCloudEventType.type())
    }

    @Test
    @DisplayName("Test the type for a response RPC event type")
    fun test_type_for_response() {
        val uCloudEventType: UCloudEventType = UCloudEventType.RESPONSE
        assertEquals("res.v1", uCloudEventType.type())
    }

    @Test
    @DisplayName("Test parsing the publish event type from a string")
    fun test_parse_publish_event_type_from_string() {
        val type = "pub.v1"
        assertTrue(UCloudEventType.valueOfType(type).isPresent())
        assertEquals(UCloudEventType.PUBLISH, UCloudEventType.valueOfType(type).get())
    }

    @Test
    @DisplayName("Test parsing the request event type from a string")
    fun test_parse_request_event_type_from_string() {
        val type = "req.v1"
        assertTrue(UCloudEventType.valueOfType(type).isPresent())
        assertEquals(UCloudEventType.REQUEST, UCloudEventType.valueOfType(type).get())
    }

    @Test
    @DisplayName("Test parsing the response event type from a string")
    fun test_parse_unknown_event_type_from_string() {
        val type = "res.v1"
        assertTrue(UCloudEventType.valueOfType(type).isPresent())
        assertEquals(UCloudEventType.RESPONSE, UCloudEventType.valueOfType(type).get())
    }

    @Test
    @DisplayName("Test parsing an unknown event type from a string")
    fun test_parse_response_event_type_from_string() {
        val type = "unknown.v1"
        assertTrue(UCloudEventType.valueOfType(type).isEmpty())
    }
}