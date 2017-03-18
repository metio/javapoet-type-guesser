/*
 * This file is part of javapoet-generic-type-support. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of javapoet-generic-type-support,
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

@SuppressWarnings({ "nls", "javadoc" })
public final class TypicalTypes {

    private static final String OPEN_ANGLE_BRACKET     = "<";
    private static final String CLOSING_ANGLE_BRACKET  = ">";
    private static final String CLOSING_SQUARE_BRACKET = "]";
    private static final String COMMA                  = ",";

    public static TypeName guessTypeName(final String type) {
        if (type.endsWith(CLOSING_SQUARE_BRACKET)) {
            final String typeNameWithoutArraySuffix = type.substring(0, type.length() - 2);
            return ArrayTypeName.of(guessTypeName(typeNameWithoutArraySuffix));
        }
        return guessType(type);
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
        if (type.contains(OPEN_ANGLE_BRACKET)) {
            return guessGenericType(type);
        }
        return ClassName.bestGuess(type);
    }

    private static TypeName guessGenericType(final String type) {
        final String rawPart = type.substring(0, type.indexOf(OPEN_ANGLE_BRACKET));
        final String genericPart = type.substring(type.indexOf(OPEN_ANGLE_BRACKET) + 1,
                type.lastIndexOf(CLOSING_ANGLE_BRACKET));
        final ClassName rawType = ClassName.bestGuess(rawPart);
        final TypeName[] typeArguments = guessGenericTypeArguments(genericPart);
        return ParameterizedTypeName.get(rawType, typeArguments);
    }

    private static TypeName[] guessGenericTypeArguments(final String genericPart) {
        final List<TypeName> types = new ArrayList<>();
        String inputToParse = genericPart;
        while (inputToParse != null && !inputToParse.isEmpty()) {
            if (inputToParse.contains(COMMA)) {
                final int indexOfComma = inputToParse.indexOf(COMMA);
                if (inputToParse.contains(OPEN_ANGLE_BRACKET)) {
                    final int indexOfAngel = inputToParse.indexOf(OPEN_ANGLE_BRACKET);
                    if (indexOfComma < indexOfAngel) {
                        types.add(guessObjectType(inputToParse.substring(0, indexOfComma).trim()));
                        inputToParse = inputToParse.substring(indexOfComma + 1, inputToParse.length()).trim();
                    } else {
                        final int endIndex = calculateGenericEndIndex(inputToParse);
                        types.add(guessObjectType(inputToParse.substring(0, endIndex).trim()));
                        inputToParse = inputToParse.substring(Math.min(endIndex + 1, inputToParse.length())).trim();
                    }
                } else {
                    types.add(guessObjectType(inputToParse.substring(0, indexOfComma).trim()));
                    inputToParse = inputToParse.substring(indexOfComma + 1, inputToParse.length()).trim();
                }
            } else {
                types.add(guessObjectType(inputToParse));
                inputToParse = "";
            }
        }

        return types.toArray(new TypeName[types.size()]);
    }

    private static int calculateGenericEndIndex(final String inputToParse) {
        int countOfOpenAngels = 0;
        int countOfClosingAngels = 0;
        for (int index = 0; index < inputToParse.length(); index++) {
            if (inputToParse.codePointAt(index) == OPEN_ANGLE_BRACKET.charAt(0)) {
                countOfOpenAngels++;
            }
            if (inputToParse.codePointAt(index) == CLOSING_ANGLE_BRACKET.charAt(0)) {
                countOfClosingAngels++;
                if (countOfOpenAngels == countOfClosingAngels) {
                    return index + 1;
                }
            }
        }
        return inputToParse.length();
    }

}
