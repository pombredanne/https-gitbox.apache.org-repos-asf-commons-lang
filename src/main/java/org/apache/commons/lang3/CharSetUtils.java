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

import org.apache.commons.lang3.stream.Streams;

/**
 * Operations on {@link CharSet} instances.
 *
 * <p>This class handles {@code null} input gracefully.
 * An exception will not be thrown for a {@code null} input.
 * Each method documents its behavior in more detail.</p>
 *
 * <p>#ThreadSafe#</p>
 * @see CharSet
 * @since 1.0
 */
public class CharSetUtils {

    /**
     * Takes an argument in set-syntax, see evaluateSet,
     * and identifies whether any of the characters are present in the specified string.
     *
     * <pre>
     * CharSetUtils.containsAny(null, *)        = false
     * CharSetUtils.containsAny("", *)          = false
     * CharSetUtils.containsAny(*, null)        = false
     * CharSetUtils.containsAny(*, "")          = false
     * CharSetUtils.containsAny("hello", "k-p") = true
     * CharSetUtils.containsAny("hello", "a-d") = false
     * </pre>
     *
     * @see CharSet#getInstance(String...) for set-syntax.
     * @param str  String to look for characters in, may be {@code null}
     * @param set  String[] set of characters to identify, may be {@code null}
     * @return whether or not the characters in the set are in the primary string
     * @since 3.2
     */
    public static boolean containsAny(final String str, final String... set) {
        if (StringUtils.isEmpty(str) || deepEmpty(set)) {
            return false;
        }
        final CharSet chars = CharSet.getInstance(set);
        for (final char c : str.toCharArray()) {
            if (chars.contains(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Takes an argument in set-syntax, see evaluateSet,
     * and returns the number of characters present in the specified string.
     *
     * <pre>
     * CharSetUtils.count(null, *)        = 0
     * CharSetUtils.count("", *)          = 0
     * CharSetUtils.count(*, null)        = 0
     * CharSetUtils.count(*, "")          = 0
     * CharSetUtils.count("hello", "k-p") = 3
     * CharSetUtils.count("hello", "a-e") = 1
     * </pre>
     *
     * @see CharSet#getInstance(String...) for set-syntax.
     * @param str  String to count characters in, may be {@code null}
     * @param set  String[] set of characters to count, may be {@code null}
     * @return the character count, zero if {@code null} string input
     */
    public static int count(final String str, final String... set) {
        if (StringUtils.isEmpty(str) || deepEmpty(set)) {
            return 0;
        }
        final CharSet chars = CharSet.getInstance(set);
        int count = 0;
        for (final char c : str.toCharArray()) {
            if (chars.contains(c)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Determines whether or not all the Strings in an array are
     * empty or not.
     *
     * @param strings String[] whose elements are being checked for emptiness
     * @return whether or not the String is empty
     */
    private static boolean deepEmpty(final String[] strings) {
        return Streams.of(strings).allMatch(StringUtils::isEmpty);
    }

    /**
     * Takes an argument in set-syntax, see evaluateSet,
     * and deletes any of characters present in the specified string.
     *
     * <pre>
     * CharSetUtils.delete(null, *)        = null
     * CharSetUtils.delete("", *)          = ""
     * CharSetUtils.delete(*, null)        = *
     * CharSetUtils.delete(*, "")          = *
     * CharSetUtils.delete("hello", "hl")  = "eo"
     * CharSetUtils.delete("hello", "le")  = "ho"
     * </pre>
     *
     * @see CharSet#getInstance(String...) for set-syntax.
     * @param str  String to delete characters from, may be {@code null}
     * @param set  String[] set of characters to delete, may be {@code null}
     * @return the modified String, {@code null} if {@code null} string input
     */
    public static String delete(final String str, final String... set) {
        if (StringUtils.isEmpty(str) || deepEmpty(set)) {
            return str;
        }
        return modify(str, set, false);
    }

    /**
     * Takes an argument in set-syntax, see evaluateSet,
     * and keeps any of characters present in the specified string.
     *
     * <pre>
     * CharSetUtils.keep(null, *)        = null
     * CharSetUtils.keep("", *)          = ""
     * CharSetUtils.keep(*, null)        = ""
     * CharSetUtils.keep(*, "")          = ""
     * CharSetUtils.keep("hello", "hl")  = "hll"
     * CharSetUtils.keep("hello", "le")  = "ell"
     * </pre>
     *
     * @see CharSet#getInstance(String...) for set-syntax.
     * @param str  String to keep characters from, may be {@code null}
     * @param set  String[] set of characters to keep, may be {@code null}
     * @return the modified String, {@code null} if {@code null} string input
     * @since 2.0
     */
    public static String keep(final String str, final String... set) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty() || deepEmpty(set)) {
            return StringUtils.EMPTY;
        }
        return modify(str, set, true);
    }

    /**
     * Implementation of delete and keep
     *
     * @param str String to modify characters within
     * @param set String[] set of characters to modify
     * @param expect whether to evaluate on match, or non-match
     * @return the modified String, not {@code null}
     */
    private static String modify(final String str, final String[] set, final boolean expect) {
        final CharSet chars = CharSet.getInstance(set);
        final StringBuilder buffer = new StringBuilder(str.length());
        final char[] chrs = str.toCharArray();
        for (final char chr : chrs) {
            if (chars.contains(chr) == expect) {
                buffer.append(chr);
            }
        }
        return buffer.toString();
    }

    /**
     * Squeezes any repetitions of a character that is mentioned in the
     * supplied set.
     *
     * <pre>
     * CharSetUtils.squeeze(null, *)        = null
     * CharSetUtils.squeeze("", *)          = ""
     * CharSetUtils.squeeze(*, null)        = *
     * CharSetUtils.squeeze(*, "")          = *
     * CharSetUtils.squeeze("hello", "k-p") = "helo"
     * CharSetUtils.squeeze("hello", "a-e") = "hello"
     * </pre>
     *
     * @see CharSet#getInstance(String...) for set-syntax.
     * @param str  the string to squeeze, may be {@code null}
     * @param set  the character set to use for manipulation, may be {@code null}
     * @return the modified String, {@code null} if {@code null} string input
     */
    public static String squeeze(final String str, final String... set) {
        if (StringUtils.isEmpty(str) || deepEmpty(set)) {
            return str;
        }
        final CharSet chars = CharSet.getInstance(set);
        final StringBuilder buffer = new StringBuilder(str.length());
        final char[] chrs = str.toCharArray();
        final int sz = chrs.length;
        char lastChar = chrs[0];
        char ch;
        Character inChars = null;
        Character notInChars = null;
        buffer.append(lastChar);
        for (int i = 1; i < sz; i++) {
            ch = chrs[i];
            if (ch == lastChar) {
                if (inChars != null && ch == inChars) {
                    continue;
                }
                if (notInChars == null || ch != notInChars) {
                    if (chars.contains(ch)) {
                        inChars = ch;
                        continue;
                    }
                    notInChars = ch;
                }
            }
            buffer.append(ch);
            lastChar = ch;
        }
        return buffer.toString();
    }

    /**
     * CharSetUtils instances should NOT be constructed in standard programming.
     * Instead, the class should be used as {@code CharSetUtils.evaluateSet(null);}.
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public CharSetUtils() {
    }
}
