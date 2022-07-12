/*
 * This file is part of javapoet-type-guesser. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of javapoet-type-guesser,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.javapoet;

import com.squareup.javapoet.ClassName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.lang.reflect.Modifier;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Stream;

class TypeGuesserTest {

    @TestFactory
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
                .map(type -> DynamicTest.dynamicTest(String.format("should parse: %s", type),
                        () -> Assertions.assertEquals(type, TypeGuesser.guessTypeName(type).toString())));
    }

    @TestFactory
    Stream<DynamicTest> shouldThrowForInvalidTypes() {
        return Stream.of(
                        "java.util.List<java.lang.Object",
                        "java.util.List<int>",
                        "java.util.List<java.util.List<java.lang.Object>",
                        "java.util.List<java.util.List<java.util.List<java.lang.Object>>",
                        "java.util.List<? extends java.util.List<? super int>>",
                        "java.util.List<? super java.util.List<? extends char>>")
                .map(type -> DynamicTest.dynamicTest(String.format("should throw for: %s", type),
                        () -> Assertions.assertThrows(IllegalArgumentException.class,
                                () -> TypeGuesser.guessTypeName(type))));
    }

    @TestFactory
    Stream<DynamicTest> shouldIgnoreWhitespace() {
        return Stream.of(
                        new SimpleEntry<>(" java.lang.Object[] ", "java.lang.Object[]"),
                        new SimpleEntry<>(" java.util.List<java.lang.Object> ", "java.util.List<java.lang.Object>"),
                        new SimpleEntry<>(" java.util.List< java.lang.Object > ", "java.util.List<java.lang.Object>"),
                        new SimpleEntry<>(" java.util.List< ? > ", "java.util.List<?>"),
                        new SimpleEntry<>(" java.util.List <java.lang.Object> ", "java.util.List<java.lang.Object>"),
                        new SimpleEntry<>(" java.util.List < java.lang.Object > ", "java.util.List<java.lang.Object>"),
                        new SimpleEntry<>(" java.util.List < ? > ", "java.util.List<?>"))
                .map(entry -> DynamicTest.dynamicTest(
                        String.format("should parse [ %s ] as: %s", entry.getKey(), entry.getValue()),
                        () -> Assertions.assertEquals(entry.getValue(),
                                TypeGuesser.guessTypeName(entry.getKey()).toString())));
    }

    @TestFactory
    Stream<DynamicTest> shouldCoverSwitchStatement() {
        return Stream.of(
                        "\0boolean",
                        "\0byte",
                        "\0short",
                        "\0long",
                        "\0char",
                        "\0float",
                        "\0double",
                        "\0int")
                .map(type -> DynamicTest.dynamicTest("should cover switch statement",
                        () -> Assertions.assertThrows(IllegalArgumentException.class,
                                () -> TypeGuesser.guessType(type))));
    }

    @TestFactory
    Stream<DynamicTest> classNameCannotGuess() {
        return Stream.of(
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
                        "java.util.List<? super java.lang.Number>",
                        "java.util.List<? extends java.util.List<java.lang.Integer>>",
                        "java.util.List<? super java.util.List<java.lang.Integer>>",
                        "java.util.List<? extends java.util.List<? extends java.lang.Integer>>",
                        "java.util.List<? super java.util.List<? super java.lang.Integer>>",
                        "java.util.List<? extends java.util.List<? super java.lang.Integer>>",
                        "java.util.List<? super java.util.List<? extends java.lang.Integer>>",
                        "java.util.List<java.util.List<java.lang.Integer>>",
                        "java.util.Map<java.lang.String, java.lang.Object>")
                .map(type -> DynamicTest.dynamicTest(
                        String.format("ClassName.bestGuess does not support: %s", type),
                        () -> Assertions.assertThrows(IllegalArgumentException.class,
                                () -> ClassName.bestGuess(type))));
    }

    @Test
    void shouldThrowNPEforNullType() {
        Assertions.assertThrows(NullPointerException.class,
                () -> TypeGuesser.guessType(null));
    }

    @Test
    void shouldThrowIAEforEmptyType() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> TypeGuesser.guessType(""));
    }

    @Test
    void shouldThrowNPEforNullTypeName() {
        Assertions.assertThrows(NullPointerException.class,
                () -> TypeGuesser.guessTypeName(null));
    }

    @Test
    void shouldThrowIAEforEmptyTypeName() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> TypeGuesser.guessTypeName(""));
    }

    @Test
    void shouldNotBeInvocable() {
        final var clazz = TypeGuesser.class;

        final var constructors = clazz.getDeclaredConstructors();

        for (final var constructor : constructors) {
            Assertions.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        }
    }

    @Test
    public void shouldBeInvocableViaReflection() throws Exception {
        final var clazz = TypeGuesser.class;
        final var constructor = clazz.getDeclaredConstructors()[0];

        constructor.setAccessible(true);
        final var instance = constructor.newInstance((Object[]) null);

        Assertions.assertNotNull(instance);
    }

}
