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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

public class CheckedPredicateTest {

    @DisplayName("an unchecked predicate should throw an exception")
    @Test
    void uncheckedPredicateExceptionRethrowingTest() {
        //try
        Path path = Paths.get("dfd eefd");
        Predicate<Path> uncheckedPredicate = CheckedPredicate.unchecked(CheckedPredicateTest::isEmptyDirectory);

        //then
        Assertions.assertThrows(RuntimeException.class, () -> uncheckedPredicate.test(path));
    }

    private static boolean isEmptyDirectory(Path file) throws IOException {
        return Files.list(file).count() != 0;
    }
}
