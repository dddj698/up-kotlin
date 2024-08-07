/**
 * SPDX-FileCopyrightText: 2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.eclipse.uprotocol.uuid.factory

import com.github.f4b6a3.uuid.UuidCreator
import org.eclipse.uprotocol.v1.UUID
import org.eclipse.uprotocol.v1.uUID
import java.time.Instant
import java.util.*


sealed class UuidFactory {
    abstract operator fun invoke(instant: Instant = Instant.now()): UUID
}

/**
 * Factory to generate UUID following Uuidv6 data model.
 *
 * @param instant An instantaneous point on the time-line
 * @return return UUID generated in UUIDV6
 */
@Suppress("KDocUnresolvedReference")
data object UUIDV6 : UuidFactory() {
    override operator fun invoke(instant: Instant): UUID {
        val uuidJava: java.util.UUID = UuidCreator.getTimeOrdered(
            Objects.requireNonNullElse(instant, Instant.now()), null, null
        )
        return uUID {
            msb = uuidJava.mostSignificantBits
            lsb = uuidJava.leastSignificantBits
        }
    }
}

/**
 * Factory to generate UUID following Uuidv8 data model.
 *
 * @param instant An instantaneous point on the time-line
 * @return return UUID generated in UUIDV8
 */
@Suppress("KDocUnresolvedReference")
data object UUIDV8 : UuidFactory() {
    /**
     * uProtocol UUIDv8 data model
     * UUIDv8 can only be built using the static factory methods of the class
     * given that the UUIDv8 datamodel is based off the previous UUID generated.
     * The UUID is based off the draft-ietf-uuidrev-rfc4122bis and UUIDv7 with
     * some modifications that are discussed below. The diagram below shows the
     * specification for the UUID:
     * 0                   1                   2                   3
     * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                         unix_ts_ms                            |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |           unix_ts_ms          |  ver  |         counter       |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |var|                          rand_b                           |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                           rand_b                              |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     *
     *
     * | Field      | RFC2119 |
     * | -----      | --------|
     * | unix_ts_ms | 48 bit big-endian unsigned number of Unix epoch timestamp in milliseconds as per Section 6.1
     * of RFC
     * | ver        | MUST be 8 per Section 4.2 of draft-ietf-uuidrev-rfc4122bis
     * | counter    | MUST be a 12 bit counter field that is reset at each unix_ts_ms tick, and incremented for each
     * UUID generated
     * within the 1ms precision of unix_ts_ms The counter provides the ability to generate 4096 events within 1ms
     * however the precision of the clock is still 1ms accuracy
     * | var        | MUST be The 2 bit variant defined by Section 4.1 of RFC |
     * |rand_b      | MUST 62 bits random number that is generated at initialization time of the uE only and reused
     * otherwise |
     */
    private const val UUIDV8_VERSION = 8
    private const val MAX_COUNT = 0xfff

    //private val lsb: Long = (Random().nextLong() and 0x3fffffffffffffffL) or (-0x8000000000000000L).toLong()
    private val lsBits: Long = (Random().nextLong() and 0x3fffffffffffffffL) or (1L shl 63)

    // Keep track of the time and counters
    private var msBits = (UUIDV8_VERSION shl 12 // Version is 8
            ).toLong()

    @Synchronized
    override operator fun invoke(instant: Instant): UUID {
        val time: Long = instant.toEpochMilli()

        // Check if the current time is the same as the previous time
        if (time == msBits shr 16) {
            // Increment the counter if we are not at MAX_COUNT
            if (msBits and 0xFFFL < MAX_COUNT) {
                msBits++
            }

            // The previous time is not the same tick as the current, so we reset msb
        } else {
            msBits = time shl 16 or (8L shl 12)
        }
        return uUID {
            msb = msBits
            lsb = lsBits
        }
    }
}
