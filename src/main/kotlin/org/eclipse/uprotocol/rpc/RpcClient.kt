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

package org.eclipse.uprotocol.rpc

import java.util.concurrent.CompletableFuture
import org.eclipse.uprotocol.transport.datamodel.UPayload
import org.eclipse.uprotocol.v1.UAttributes
import org.eclipse.uprotocol.v1.UUri

/**
 * RpcClient is an interface used by code generators for uProtocol services defined in proto files such as
 * the core uProtocol services found in https://github.com/eclipse-uprotocol/uprotocol-core-api. the interface
 * provides a clean contract for all transports to implement to be able to support RPC on their platform. Each
 * platform MUST implement this interface. For more details please refer to
 * https://github.com/eclipse-uprotocol/uprotocol-spec/blob/main/up-l2/README.adoc[RpcClient Specifications]
 */
interface RpcClient {
    /**
     * Support for RPC method invocation.
     * @param topic topic of the method to be invoked (i.e. the name of the API we are calling).
     * @param payload The request message to be sent to the server.
     * @param attributes Metadata for the method invocation (i.e. priority, timeout, etc.)
     * @return Returns the CompletableFuture with the result or exception.
     */
    fun invokeMethod(topic: UUri?, payload: UPayload?, attributes: UAttributes?): CompletableFuture<UPayload?>?
}
