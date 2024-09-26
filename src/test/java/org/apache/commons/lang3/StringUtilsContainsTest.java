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

import static org.apache.commons.lang3.Supplementary.CharU20000;
import static org.apache.commons.lang3.Supplementary.CharU20001;
import static org.apache.commons.lang3.Supplementary.CharUSuppCharHigh;
import static org.apache.commons.lang3.Supplementary.CharUSuppCharLow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests {@link StringUtils} - Contains methods
 */
public class StringUtilsContainsTest extends AbstractLangTest {
    @Test
    public void testContains_Char() {
        assertFalse(StringUtils.contains(null, ' '));
        assertFalse(StringUtils.contains("", ' '));
        assertTrue(StringUtils.contains("abc", 'a'));
        assertTrue(StringUtils.contains("abc", 'b'));
        assertTrue(StringUtils.contains("abc", 'c'));
        assertFalse(StringUtils.contains("abc", 'z'));
    }


    /**
     * See https://www.oracle.com/technical-resources/articles/javase/supplementary.html
     */
    @Test
    public void testContains_StringWithBadSupplementaryChars() {
        // Test edge case: 1/2 of a (broken) supplementary char
        assertEquals(0, CharU20001.indexOf(CharUSuppCharLow));
    }


    @Test
    public void testContainsAny_StringCharArray() {
        assertFalse(StringUtils.containsAny(null, (char[]) null));
        assertFalse(StringUtils.containsAny(null, new char[0]));
        assertFalse(StringUtils.containsAny(null, 'a', 'b'));

        assertFalse(StringUtils.containsAny("", (char[]) null));
        assertFalse(StringUtils.containsAny("", new char[0]));
        assertFalse(StringUtils.containsAny("", 'a', 'b'));

        assertFalse(StringUtils.containsAny("zzabyycdxx", (char[]) null));
        assertFalse(StringUtils.containsAny("zzabyycdxx", new char[0]));
        assertTrue(StringUtils.containsAny("zzabyycdxx", 'z', 'a'));
        assertTrue(StringUtils.containsAny("zzabyycdxx", 'b', 'y'));
        assertTrue(StringUtils.containsAny("zzabyycdxx", 'z', 'y'));
        assertFalse(StringUtils.containsAny("ab", 'z'));
    }

    /**
     * See https://www.oracle.com/technical-resources/articles/javase/supplementary.html
     */
    @Test
    public void testContainsAny_StringCharArrayWithBadSupplementaryChars() {
        // Test edge case: 1/2 of a (broken) supplementary char
        assertFalse(StringUtils.containsAny(CharUSuppCharHigh, CharU20001.toCharArray()));
        assertFalse(StringUtils.containsAny("abc" + CharUSuppCharHigh + "xyz", CharU20001.toCharArray()));
        assertEquals(-1, CharUSuppCharLow.indexOf(CharU20001));
        assertFalse(StringUtils.containsAny(CharUSuppCharLow, CharU20001.toCharArray()));
        assertFalse(StringUtils.containsAny(CharU20001, CharUSuppCharHigh.toCharArray()));
        assertEquals(0, CharU20001.indexOf(CharUSuppCharLow));
        assertTrue(StringUtils.containsAny(CharU20001, CharUSuppCharLow.toCharArray()));
    }

    /**
     * See https://www.oracle.com/technical-resources/articles/javase/supplementary.html
     */
    @Test
    public void testContainsAny_StringCharArrayWithSupplementaryChars() {
        assertTrue(StringUtils.containsAny(CharU20000 + CharU20001, CharU20000.toCharArray()));
        assertTrue(StringUtils.containsAny("a" + CharU20000 + CharU20001, "a".toCharArray()));
        assertTrue(StringUtils.containsAny(CharU20000 + "a" + CharU20001, "a".toCharArray()));
        assertTrue(StringUtils.containsAny(CharU20000 + CharU20001 + "a", "a".toCharArray()));
        assertTrue(StringUtils.containsAny(CharU20000 + CharU20001, CharU20001.toCharArray()));
        assertTrue(StringUtils.containsAny(CharU20000, CharU20000.toCharArray()));
        // Sanity check:
        assertEquals(-1, CharU20000.indexOf(CharU20001));
        assertEquals(0, CharU20000.indexOf(CharU20001.charAt(0)));
        assertEquals(-1, CharU20000.indexOf(CharU20001.charAt(1)));
        // Test:
        assertFalse(StringUtils.containsAny(CharU20000, CharU20001.toCharArray()));
        assertFalse(StringUtils.containsAny(CharU20001, CharU20000.toCharArray()));
    }

    @Test
    public void testContainsAny_StringString() {
        assertFalse(StringUtils.containsAny(null, (String) null));
        assertFalse(StringUtils.containsAny(null, ""));
        assertFalse(StringUtils.containsAny(null, "ab"));

        assertFalse(StringUtils.containsAny("", (String) null));
        assertFalse(StringUtils.containsAny("", ""));
        assertFalse(StringUtils.containsAny("", "ab"));

        assertFalse(StringUtils.containsAny("zzabyycdxx", (String) null));
        assertFalse(StringUtils.containsAny("zzabyycdxx", ""));
        assertTrue(StringUtils.containsAny("zzabyycdxx", "za"));
        assertTrue(StringUtils.containsAny("zzabyycdxx", "by"));
        assertTrue(StringUtils.containsAny("zzabyycdxx", "zy"));
        assertFalse(StringUtils.containsAny("ab", "z"));
    }

    /**
     * See https://www.oracle.com/technical-resources/articles/javase/supplementary.html
     */
    @Test
    public void testContainsAny_StringWithBadSupplementaryChars() {
        // Test edge case: 1/2 of a (broken) supplementary char
        assertFalse(StringUtils.containsAny(CharUSuppCharHigh, CharU20001));
        assertEquals(-1, CharUSuppCharLow.indexOf(CharU20001));
        assertFalse(StringUtils.containsAny(CharUSuppCharLow, CharU20001));
        assertFalse(StringUtils.containsAny(CharU20001, CharUSuppCharHigh));
        assertEquals(0, CharU20001.indexOf(CharUSuppCharLow));
        assertTrue(StringUtils.containsAny(CharU20001, CharUSuppCharLow));
    }

    /**
     * See https://www.oracle.com/technical-resources/articles/javase/supplementary.html
     */
    @Test
    public void testContainsAny_StringWithSupplementaryChars() {
        assertTrue(StringUtils.containsAny(CharU20000 + CharU20001, CharU20000));
        assertTrue(StringUtils.containsAny(CharU20000 + CharU20001, CharU20001));
        assertTrue(StringUtils.containsAny(CharU20000, CharU20000));
        // Sanity check:
        assertEquals(-1, CharU20000.indexOf(CharU20001));
        assertEquals(0, CharU20000.indexOf(CharU20001.charAt(0)));
        assertEquals(-1, CharU20000.indexOf(CharU20001.charAt(1)));
        // Test:
        assertFalse(StringUtils.containsAny(CharU20000, CharU20001));
        assertFalse(StringUtils.containsAny(CharU20001, CharU20000));
    }


    @Test
    public void testContainsNone_CharArray() {
        final String str1 = "a";
        final String str2 = "b";
        final String str3 = "ab.";
        final char[] chars1 = { 'b' };
        final char[] chars2 = { '.' };
        final char[] chars3 = { 'c', 'd' };
        final char[] emptyChars = {};
        assertTrue(StringUtils.containsNone(null, (char[]) null));
        assertTrue(StringUtils.containsNone("", (char[]) null));
        assertTrue(StringUtils.containsNone(null, emptyChars));
        assertTrue(StringUtils.containsNone(str1, emptyChars));
        assertTrue(StringUtils.containsNone("", emptyChars));
        assertTrue(StringUtils.containsNone("", chars1));
        assertTrue(StringUtils.containsNone(str1, chars1));
        assertTrue(StringUtils.containsNone(str1, chars2));
        assertTrue(StringUtils.containsNone(str1, chars3));
        assertFalse(StringUtils.containsNone(str2, chars1));
        assertTrue(StringUtils.containsNone(str2, chars2));
        assertTrue(StringUtils.containsNone(str2, chars3));
        assertFalse(StringUtils.containsNone(str3, chars1));
        assertFalse(StringUtils.containsNone(str3, chars2));
        assertTrue(StringUtils.containsNone(str3, chars3));
    }

    /**
     * See https://www.oracle.com/technical-resources/articles/javase/supplementary.html
     */
    @Test
    public void testContainsNone_CharArrayWithBadSupplementaryChars() {
        // Test edge case: 1/2 of a (broken) supplementary char
        assertTrue(StringUtils.containsNone(CharUSuppCharHigh, CharU20001.toCharArray()));
        assertEquals(-1, CharUSuppCharLow.indexOf(CharU20001));
        assertTrue(StringUtils.containsNone(CharUSuppCharLow, CharU20001.toCharArray()));
        assertEquals(-1, CharU20001.indexOf(CharUSuppCharHigh));
        assertTrue(StringUtils.containsNone(CharU20001, CharUSuppCharHigh.toCharArray()));
        assertEquals(0, CharU20001.indexOf(CharUSuppCharLow));
        assertFalse(StringUtils.containsNone(CharU20001, CharUSuppCharLow.toCharArray()));
    }

    /**
     * See https://www.oracle.com/technical-resources/articles/javase/supplementary.html
     */
    @Test
    public void testContainsNone_CharArrayWithSupplementaryChars() {
        assertFalse(StringUtils.containsNone(CharU20000 + CharU20001, CharU20000.toCharArray()));
        assertFalse(StringUtils.containsNone(CharU20000 + CharU20001, CharU20001.toCharArray()));
        assertFalse(StringUtils.containsNone(CharU20000, CharU20000.toCharArray()));
        // Sanity check:
        assertEquals(-1, CharU20000.indexOf(CharU20001));
        assertEquals(0, CharU20000.indexOf(CharU20001.charAt(0)));
        assertEquals(-1, CharU20000.indexOf(CharU20001.charAt(1)));
        // Test:
        assertTrue(StringUtils.containsNone(CharU20000, CharU20001.toCharArray()));
        assertTrue(StringUtils.containsNone(CharU20001, CharU20000.toCharArray()));
    }

    @Test
    public void testContainsNone_String() {
        final String str1 = "a";
        final String str2 = "b";
        final String str3 = "ab.";
        final String chars1 = "b";
        final String chars2 = ".";
        final String chars3 = "cd";
        assertTrue(StringUtils.containsNone(null, (String) null));
        assertTrue(StringUtils.containsNone("", (String) null));
        assertTrue(StringUtils.containsNone(null, ""));
        assertTrue(StringUtils.containsNone(str1, ""));
        assertTrue(StringUtils.containsNone("", ""));
        assertTrue(StringUtils.containsNone("", chars1));
        assertTrue(StringUtils.containsNone(str1, chars1));
        assertTrue(StringUtils.containsNone(str1, chars2));
        assertTrue(StringUtils.containsNone(str1, chars3));
        assertFalse(StringUtils.containsNone(str2, chars1));
        assertTrue(StringUtils.containsNone(str2, chars2));
        assertTrue(StringUtils.containsNone(str2, chars3));
        assertFalse(StringUtils.containsNone(str3, chars1));
        assertFalse(StringUtils.containsNone(str3, chars2));
        assertTrue(StringUtils.containsNone(str3, chars3));
    }

    /**
     * See https://www.oracle.com/technical-resources/articles/javase/supplementary.html
     */
    @Test
    public void testContainsNone_StringWithBadSupplementaryChars() {
        // Test edge case: 1/2 of a (broken) supplementary char
        assertTrue(StringUtils.containsNone(CharUSuppCharHigh, CharU20001));
        assertEquals(-1, CharUSuppCharLow.indexOf(CharU20001));
        assertTrue(StringUtils.containsNone(CharUSuppCharLow, CharU20001));
        assertEquals(-1, CharU20001.indexOf(CharUSuppCharHigh));
        assertTrue(StringUtils.containsNone(CharU20001, CharUSuppCharHigh));
        assertEquals(0, CharU20001.indexOf(CharUSuppCharLow));
        assertFalse(StringUtils.containsNone(CharU20001, CharUSuppCharLow));
    }

    /**
     * See https://www.oracle.com/technical-resources/articles/javase/supplementary.html
     */
    @Test
    public void testContainsNone_StringWithSupplementaryChars() {
        assertFalse(StringUtils.containsNone(CharU20000 + CharU20001, CharU20000));
        assertFalse(StringUtils.containsNone(CharU20000 + CharU20001, CharU20001));
        assertFalse(StringUtils.containsNone(CharU20000, CharU20000));
        // Sanity check:
        assertEquals(-1, CharU20000.indexOf(CharU20001));
        assertEquals(0, CharU20000.indexOf(CharU20001.charAt(0)));
        assertEquals(-1, CharU20000.indexOf(CharU20001.charAt(1)));
        // Test:
        assertTrue(StringUtils.containsNone(CharU20000, CharU20001));
        assertTrue(StringUtils.containsNone(CharU20001, CharU20000));
    }

    @Test
    public void testContainsOnly_CharArray() {
        final String str1 = "a";
        final String str2 = "b";
        final String str3 = "ab";
        final char[] chars1 = { 'b' };
        final char[] chars2 = { 'a' };
        final char[] chars3 = { 'a', 'b' };
        final char[] emptyChars = {};
        assertFalse(StringUtils.containsOnly(null, (char[]) null));
        assertFalse(StringUtils.containsOnly("", (char[]) null));
        assertFalse(StringUtils.containsOnly(null, emptyChars));
        assertFalse(StringUtils.containsOnly(str1, emptyChars));
        assertTrue(StringUtils.containsOnly("", emptyChars));
        assertTrue(StringUtils.containsOnly("", chars1));
        assertFalse(StringUtils.containsOnly(str1, chars1));
        assertTrue(StringUtils.containsOnly(str1, chars2));
        assertTrue(StringUtils.containsOnly(str1, chars3));
        assertTrue(StringUtils.containsOnly(str2, chars1));
        assertFalse(StringUtils.containsOnly(str2, chars2));
        assertTrue(StringUtils.containsOnly(str2, chars3));
        assertFalse(StringUtils.containsOnly(str3, chars1));
        assertFalse(StringUtils.containsOnly(str3, chars2));
        assertTrue(StringUtils.containsOnly(str3, chars3));
    }

    @Test
    public void testContainsOnly_String() {
        final String str1 = "a";
        final String str2 = "b";
        final String str3 = "ab";
        final String chars1 = "b";
        final String chars2 = "a";
        final String chars3 = "ab";
        assertFalse(StringUtils.containsOnly(null, (String) null));
        assertFalse(StringUtils.containsOnly("", (String) null));
        assertFalse(StringUtils.containsOnly(null, ""));
        assertFalse(StringUtils.containsOnly(str1, ""));
        assertTrue(StringUtils.containsOnly("", ""));
        assertTrue(StringUtils.containsOnly("", chars1));
        assertFalse(StringUtils.containsOnly(str1, chars1));
        assertTrue(StringUtils.containsOnly(str1, chars2));
        assertTrue(StringUtils.containsOnly(str1, chars3));
        assertTrue(StringUtils.containsOnly(str2, chars1));
        assertFalse(StringUtils.containsOnly(str2, chars2));
        assertTrue(StringUtils.containsOnly(str2, chars3));
        assertFalse(StringUtils.containsOnly(str3, chars1));
        assertFalse(StringUtils.containsOnly(str3, chars2));
        assertTrue(StringUtils.containsOnly(str3, chars3));
    }

    @Test
    public void testContainsWhitespace() {
        assertFalse(StringUtils.containsWhitespace(""));
        assertTrue(StringUtils.containsWhitespace(" "));
        assertFalse(StringUtils.containsWhitespace("a"));
        assertTrue(StringUtils.containsWhitespace("a "));
        assertTrue(StringUtils.containsWhitespace(" a"));
        assertTrue(StringUtils.containsWhitespace("a\t"));
        assertTrue(StringUtils.containsWhitespace("\n"));
    }
}
