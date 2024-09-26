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
package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests {@link StringUtils} - StartsWith/EndsWith methods
 */
public class StringUtilsStartsEndsWithTest extends AbstractLangTest {
    private static final String foo    = "foo";
    private static final String bar    = "bar";
    private static final String foobar = "foobar";
    private static final String FOO    = "FOO";
    private static final String BAR    = "BAR";
    private static final String FOOBAR = "FOOBAR";


    @Test
    public void testEndsWithAny() {
        assertFalse(StringUtils.endsWithAny(null, (String) null), "StringUtils.endsWithAny(null, null)");
        assertFalse(StringUtils.endsWithAny(null, "abc"), "StringUtils.endsWithAny(null, new String[] {abc})");
        assertFalse(StringUtils.endsWithAny("abcxyz", (String) null), "StringUtils.endsWithAny(abcxyz, null)");
        assertTrue(StringUtils.endsWithAny("abcxyz", ""), "StringUtils.endsWithAny(abcxyz, new String[] {\"\"})");
        assertTrue(StringUtils.endsWithAny("abcxyz", "xyz"), "StringUtils.endsWithAny(abcxyz, new String[] {xyz})");
        assertTrue(StringUtils.endsWithAny("abcxyz", null, "xyz", "abc"), "StringUtils.endsWithAny(abcxyz, new String[] {null, xyz, abc})");
        assertFalse(StringUtils.endsWithAny("defg", null, "xyz", "abc"), "StringUtils.endsWithAny(defg, new String[] {null, xyz, abc})");
        assertTrue(StringUtils.endsWithAny("abcXYZ", "def", "XYZ"));
        assertFalse(StringUtils.endsWithAny("abcXYZ", "def", "xyz"));
        assertTrue(StringUtils.endsWithAny("abcXYZ", "def", "YZ"));

        /*
         * Type null of the last argument to method endsWithAny(CharSequence, CharSequence...)
         * doesn't exactly match the vararg parameter type.
         * Cast to CharSequence[] to confirm the non-varargs invocation,
         * or pass individual arguments of type CharSequence for a varargs invocation.
         *
         * assertFalse(StringUtils.endsWithAny("abcXYZ", null)); // replace with specific types to avoid warning
         */
        assertFalse(StringUtils.endsWithAny("abcXYZ", (CharSequence) null));
        assertFalse(StringUtils.endsWithAny("abcXYZ", (CharSequence[]) null));
        assertTrue(StringUtils.endsWithAny("abcXYZ", ""));

        assertTrue(StringUtils.endsWithAny("abcxyz", new StringBuilder("abc"), new StringBuffer("xyz")), "StringUtils.endsWithAny(abcxyz, StringBuilder(abc), StringBuffer(xyz))");
        assertTrue(StringUtils.endsWithAny(new StringBuffer("abcxyz"), new StringBuilder("abc"), new StringBuffer("xyz")), "StringUtils.endsWithAny(StringBuffer(abcxyz), StringBuilder(abc), StringBuffer(xyz))");
    }


    @Test
    public void testStartsWithAny() {
        assertFalse(StringUtils.startsWithAny(null, (String[]) null));
        assertFalse(StringUtils.startsWithAny(null, "abc"));
        assertFalse(StringUtils.startsWithAny("abcxyz", (String[]) null));
        assertFalse(StringUtils.startsWithAny("abcxyz"));
        assertTrue(StringUtils.startsWithAny("abcxyz", "abc"));
        assertTrue(StringUtils.startsWithAny("abcxyz", null, "xyz", "abc"));
        assertFalse(StringUtils.startsWithAny("abcxyz", null, "xyz", "abcd"));
        assertTrue(StringUtils.startsWithAny("abcxyz", ""));
        assertFalse(StringUtils.startsWithAny("abcxyz", null, "xyz", "ABCX"));
        assertFalse(StringUtils.startsWithAny("ABCXYZ", null, "xyz", "abc"));

        assertTrue(StringUtils.startsWithAny("abcxyz", new StringBuilder("xyz"), new StringBuffer("abc")), "StringUtils.startsWithAny(abcxyz, StringBuilder(xyz), StringBuffer(abc))");
        assertTrue(StringUtils.startsWithAny(new StringBuffer("abcxyz"), new StringBuilder("xyz"), new StringBuffer("abc")), "StringUtils.startsWithAny(StringBuffer(abcxyz), StringBuilder(xyz), StringBuffer(abc))");
    }

}
