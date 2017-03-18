/*
 * This file is part of javapoet-generic-type-support. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of javapoet-generic-type-support,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.javapoet;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class TypicalTypesTest {

    @TestFactory
    @SuppressWarnings({ "nls", "static-method" })
    Stream<DynamicTest> dynamicTestsFromStream() {
        return Stream.of(
                "java.lang.Object[]",
                "java.lang.Object[][]",
                "java.lang.Object[][][]",
                "int[]",
                "java.util.List<java.lang.Object>",
                "java.util.List<java.lang.Object<java.lang.Integer>>",
                "java.util.Map<java.lang.String, java.lang.Object>",
                "java.util.Map<java.lang.String, java.util.List<java.lang.Object<java.lang.Integer>>>",
                "java.util.Map<java.util.List<java.lang.Object<java.lang.Integer>>, java.lang.String>",
                "java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>",
                "java.util.Map<java.util.Map<java.lang.String, java.lang.Object>, java.lang.String>",
                "java.util.Map<java.util.Map<java.lang.String, java.lang.Object>, java.util.Map<java.lang.Integer, java.lang.Double>>")
                .map(genericType -> DynamicTest.dynamicTest(String.format("should parse [%s]", genericType),
                        () -> Assertions.assertEquals(genericType,
                                TypicalTypes.guessTypeName(genericType).toString())));
    }

}
