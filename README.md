<!--
SPDX-FileCopyrightText: The javapoet-type-guesser Authors
SPDX-License-Identifier: 0BSD
-->

# javapoet-type-guesser

Ever wanted to call `ClassName.bestGuess("int")` or `ClassName.bestGuess("java.util.List<java.lang.Integer>")`?
This project provides an extension to [JavaPoet](https://github.com/square/javapoet) that allows you to do just that.
Simply replace the call to `ClassName.bestGuess(String)` with a call to `TypeGuesser.guessTypeName(String)` which is
able to parse everything that `ClassName` can and additionally is fine with primitives, generics, wildcards and
arrays.

Available in [Maven Central](https://central.sonatype.com/artifact/wtf.metio.javapoet/javapoet-type-guesser)

## License

[0BSD](LICENSES/0BSD.txt).
