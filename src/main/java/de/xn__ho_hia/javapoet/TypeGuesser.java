/*
 * This file is part of javapoet-type-guesser. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of javapoet-type-guesser,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.javapoet;

import java.util.ArrayList;
import java.util.List;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.WildcardTypeName;

/**
 * An extension for JavaPoet's {@link ClassName#bestGuess(String)} that adds support for primitive and generic types as
 * well as wildcard using sub- and supertypes.
 */
@SuppressWarnings("nls")
public final class TypeGuesser {

    private static final String OPEN_ANGLE_BRACKET     = "<";
    private static final String CLOSING_ANGLE_BRACKET  = ">";
    private static final String CLOSING_SQUARE_BRACKET = "]";
    private static final String COMMA                  = ",";
    private static final String QUESTION_MARK          = "?";
    private static final String EXTENDS                = "extends";
    private static final String SUPER                  = "super";

    private TypeGuesser() {
        // utility class, call #guessTypeName() directly
    }

    /**
     * A replacement for {@link ClassName#bestGuess(String)} that adds support for various new types.
     *
     * @param type
     *            The fully qualified type to guess
     * @return The guessed {@link TypeName}.
     */
    public static TypeName guessTypeName(final String type) {
        try {
            final String trimmedInput = type.trim();
            if (trimmedInput.endsWith(CLOSING_SQUARE_BRACKET)) {
                final String typeNameWithoutArraySuffix = trimmedInput.substring(0, trimmedInput.length() - 2);
                return ArrayTypeName.of(guessTypeName(typeNameWithoutArraySuffix));
            }
            return guessType(trimmedInput);
        } catch (final StringIndexOutOfBoundsException exception) {
            throw new IllegalArgumentException(type, exception);
        }
    }

    private static TypeName guessType(final String type) {
        switch (type) {
            case "boolean":
                return TypeName.BOOLEAN;
            case "byte":
                return TypeName.BYTE;
            case "short":
                return TypeName.SHORT;
            case "long":
                return TypeName.LONG;
            case "char":
                return TypeName.CHAR;
            case "float":
                return TypeName.FLOAT;
            case "double":
                return TypeName.DOUBLE;
            case "int":
                return TypeName.INT;
            default:
                return guessObjectType(type);
        }
    }

    private static TypeName guessObjectType(final String type) {
        if (type.startsWith(QUESTION_MARK)) {
            return guessWildcardType(type);
        }
        if (type.contains(OPEN_ANGLE_BRACKET)) {
            return guessGenericType(type);
        }
        return ClassName.bestGuess(type);
    }

    private static TypeName guessWildcardType(final String type) {
        final int indexOfExtends = type.indexOf(EXTENDS);
        final int indexOfSuper = type.indexOf(SUPER);
        if (firstBeforeSecond(indexOfExtends, indexOfSuper)) {
            return guessExtendingType(type);
        } else if (firstBeforeSecond(indexOfSuper, indexOfExtends)) {
            return guessSuperType(type);
        }
        return WildcardTypeName.subtypeOf(TypeName.get(Object.class));
    }

    private static TypeName guessExtendingType(final String type) {
        return WildcardTypeName.subtypeOf(
                guessTypeName(type.substring(type.indexOf(EXTENDS) + EXTENDS.length())));
    }

    private static TypeName guessSuperType(final String type) {
        return WildcardTypeName.supertypeOf(
                guessTypeName(type.substring(type.indexOf(SUPER) + SUPER.length())));
    }

    private static TypeName guessGenericType(final String type) {
        final String rawPart = type.substring(0, type.indexOf(OPEN_ANGLE_BRACKET)).trim();
        final String genericPart = type.substring(type.indexOf(OPEN_ANGLE_BRACKET) + 1,
                type.lastIndexOf(CLOSING_ANGLE_BRACKET));
        final ClassName rawType = ClassName.bestGuess(rawPart);
        final TypeName[] typeArguments = guessGenericTypeArguments(genericPart);
        return ParameterizedTypeName.get(rawType, typeArguments);
    }

    private static TypeName[] guessGenericTypeArguments(final String genericPart) {
        final List<TypeName> types = new ArrayList<>();
        String inputToParse = genericPart;
        while (!inputToParse.isEmpty()) {
            final int indexOfComma = inputToParse.indexOf(COMMA);
            final int indexOfAngel = inputToParse.indexOf(OPEN_ANGLE_BRACKET);

            if (firstBeforeSecond(indexOfComma, indexOfAngel)) {
                types.add(guessTypeName(inputToParse.substring(0, indexOfComma)));
                inputToParse = inputToParse.substring(indexOfComma + 1);
            } else if (firstBeforeSecond(indexOfAngel, indexOfComma)) {
                final int endIndex = calculateEndIndexOfGenericType(inputToParse);
                types.add(guessTypeName(inputToParse.substring(0, endIndex)));
                inputToParse = inputToParse.substring(Math.min(endIndex + 1, inputToParse.length()));
            } else {
                types.add(guessTypeName(inputToParse));
                inputToParse = "";
            }
        }

        return types.toArray(new TypeName[types.size()]);
    }

    private static boolean firstBeforeSecond(final int first, final int second) {
        return isFirst(first, second) || isOnly(first, second);
    }

    private static boolean isFirst(final int first, final int second) {
        return exists(first, second) && first < second;
    }

    private static boolean isOnly(final int first, final int second) {
        return exists(first) && !firstBeforeSecond(second, first);
    }

    private static boolean exists(final int first, final int second) {
        return exists(first) && exists(second);
    }

    private static boolean exists(final int index) {
        return index >= 0;
    }

    private static int calculateEndIndexOfGenericType(final String inputToParse) {
        int countOfOpenAngels = 0;
        int countOfClosingAngels = 0;
        int endIndex = inputToParse.length();
        for (int index = 0; index < inputToParse.length(); index++) {
            if (inputToParse.codePointAt(index) == OPEN_ANGLE_BRACKET.charAt(0)) {
                countOfOpenAngels++;
            }
            if (inputToParse.codePointAt(index) == CLOSING_ANGLE_BRACKET.charAt(0)) {
                countOfClosingAngels++;
                if (countOfOpenAngels == countOfClosingAngels) {
                    endIndex = index + 1;
                    break;
                }
            }
        }
        return endIndex;
    }

}
