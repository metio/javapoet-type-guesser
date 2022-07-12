# javapoet-type-guesser

Ever wanted to call `ClassName.bestGuess("int")` or `ClassName.bestGuess("java.util.List<java.lang.Integer>")`? This project provides an extension to [JavaPoet](https://github.com/square/javapoet) that allows you to do just that. Simply replace the call to `ClassName.bestGuess(String)` with a call to `TypeGuesser.guessTypeName(String)` which is able to parse everything that `ClassName` can and additionally is fine with primitives, generics, wildcards and arrays.

## License

```
To the extent possible under law, the author(s) have dedicated all copyright
and related and neighboring rights to this software to the public domain
worldwide. This software is distributed without any warranty.

You should have received a copy of the CC0 Public Domain Dedication along with
this software. If not, see https://creativecommons.org/publicdomain/zero/1.0/.
```
