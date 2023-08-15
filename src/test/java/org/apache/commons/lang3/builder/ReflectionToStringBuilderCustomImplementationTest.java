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
package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Regression test to ensure the {@link ReflectionToStringBuilder} always
 * uses {@link ReflectionToStringBuilder#getValue(Field)} to get the value of every field in the class.
 * Any changes to this behavior in the future will break this test.
 */
public class ReflectionToStringBuilderCustomImplementationTest extends AbstractLangTest {

    private final String stringField = "string";

    public static class CustomReflectionToStringBuilder extends ReflectionToStringBuilder {

        private static final String CUSTOM_PREFIX = "prefix:";

        public CustomReflectionToStringBuilder(Object object, ToStringStyle toStringStyle) {
            super(object, toStringStyle);
        }

        @Override
        protected Object getValue(Field field) {
            return CUSTOM_PREFIX + super.getValue(field);
        }
    }

    @Test
    public void testBuild() {
        assertEquals("[stringField=prefix:string]",
                new CustomReflectionToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE).build());
    }

}
