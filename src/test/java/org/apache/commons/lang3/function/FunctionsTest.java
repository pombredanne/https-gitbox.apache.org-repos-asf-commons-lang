/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
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

package org.apache.commons.lang3.function;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests {@link Functions}.
 */
public class FunctionsTest {

    /**
     * Tests {@link Functions#function(Function)}.
     */
    @Test
    public void testFunction() {
        assertEquals("foo", Functions.function(String::valueOf).andThen(String::toString).apply("foo"));
    }

    /**
     * Tests {@link Functions#applyIfNotNull(Object, Function)}.
     */
    @Test
    public void testApplyIfNotNull() {
        assertEquals("foo-bar", Functions.applyIfNotNull("foo", string -> string.concat("-bar")));
        assertNull(Functions.applyIfNotNull("foo", null));
        assertNull(Functions.applyIfNotNull((String) null, string -> fail()));
    }
}
