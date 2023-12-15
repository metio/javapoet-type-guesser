<!--
SPDX-FileCopyrightText: The javapoet-type-guesser Authors
SPDX-License-Identifier: 0BSD
 -->

# javapoet-type-guesser [![Chat](https://img.shields.io/badge/matrix-%23talk.metio:matrix.org-brightgreen.svg?style=social&label=Matrix)](https://matrix.to/#/#talk.metio:matrix.org)

Ever wanted to call `ClassName.bestGuess("int")` or `ClassName.bestGuess("java.util.List<java.lang.Integer>")`?
This project provides an extension to [JavaPoet](https://github.com/square/javapoet) that allows you to do just that.
Simply replace the call to `ClassName.bestGuess(String)` with a call to `TypeGuesser.guessTypeName(String)` which is
able to parse everything that `ClassName` can and additionally is fine with primitives, generics, wildcards and
arrays.

Available in [Maven Central](https://central.sonatype.com/artifact/wtf.metio.javapoet/javapoet-type-guesser)

## License

```
Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
PERFORMANCE OF THIS SOFTWARE.
```
