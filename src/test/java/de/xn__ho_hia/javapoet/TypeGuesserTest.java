/*
 * This file is part of javapoet-type-guesser. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of javapoet-type-guesser,
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
class TypeGuesserTest {

    @TestFactory
    @SuppressWarnings({ "nls", "static-method" })
    Stream<DynamicTest> shouldParseValidTypes() {
        return Stream.of(
                "java.lang.Object",
                "java.lang.Object[]",
                "java.lang.Object[][]",
                "java.lang.Object[][][]",
                "boolean",
                "byte",
                "short",
                "long",
                "char",
                "float",
                "double",
                "int",
                "boolean[]",
                "byte[][]",
                "short[][][]",
                "long[][][][]",
                "char[][][][][]",
                "float[][][][][][]",
                "double[][][][][][][]",
                "int[][][][][][][][]",
                "java.util.List<java.lang.Object>",
                "java.util.List<? extends java.lang.Number>",
                "java.util.List<?>",
                "java.util.List<? super java.lang.Number>",
                "java.util.List<? extends java.util.List<java.lang.Integer>>",
                "java.util.List<? super java.util.List<java.lang.Integer>>",
                "java.util.List<? extends java.util.List<? extends java.lang.Integer>>",
                "java.util.List<? super java.util.List<? super java.lang.Integer>>",
                "java.util.List<? extends java.util.List<? super java.lang.Integer>>",
                "java.util.List<? super java.util.List<? extends java.lang.Integer>>",
                "java.util.List<java.util.List<java.lang.Integer>>",
                "java.util.Map<java.lang.String, java.lang.Object>",
                "java.util.Map<java.lang.String, java.util.List<java.util.List<java.lang.Integer>>>",
                "java.util.Map<java.util.List<java.util.List<java.lang.Integer>>, java.lang.String>",
                "java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>",
                "java.util.Map<java.util.Map<java.lang.String, java.lang.Object>, java.lang.String>",
                "java.util.Map<java.util.Map<java.lang.String, java.lang.Object>, java.util.Map<java.lang.Integer, java.lang.Double>>",
                "com.google.common.collect.ArrayTable<java.lang.String, java.lang.Integer, java.lang.Object>",
                "com.google.common.collect.ArrayTable<? extends java.util.Map<java.lang.String, java.util.Map<java.lang.Integer, java.lang.Double>>, char[][][][][], ? super java.util.List<? super java.lang.Number>>")
                .map(genericType -> DynamicTest.dynamicTest(String.format("should parse: %s", genericType),
                        () -> Assertions.assertEquals(genericType,
                                TypeGuesser.guessTypeName(genericType).toString())));
    }

}
