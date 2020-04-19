[![Build Status](https://travis-ci.com/mtumilowicz/kotlin-dsl-lambda-workshop.svg?branch=master)](https://travis-ci.com/mtumilowicz/kotlin-dsl-lambda-workshop)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
# kotlin-dsl-lambda-workshop

* reference
    * https://kotlinlang.org/docs/reference/scope-functions.html
    * https://www.manning.com/books/kotlin-in-action
    * please refer previously: https://github.com/mtumilowicz/groovy258-dsl-closure-workshop
        * this workshop is analogous but in kotlin
        
## preface
* goals of this workshop:
    * introduce fundamental kotlin concepts
        * extension function
        * lambda with receiver
        * infix operator
        * invoke operator
        * overloading operators
        * object destructuring
    * understanding what is state machine and how it works
    * becoming acquainted with DSL and how kotlin supports it
* workshop: `workshop` package, answers: `answers` package

## extension function
* top-level function
    * you can place functions directly at the top level of a source file, outside of any class
    * when you compile the file, some classes will be produced
        * JVM can only execute code  in classes
    * example
        ```
        package strings // filename: StringUtils.kt
        fun isNotEmpty(...): Boolean { ... }
        
        /* Java */
        package strings;
        public class StringUtilsKt { // corresponds to the filename
            public static Boolean isNotEmpty(...) { ... }
        }
        ```
* extension function it’s a function that can be called as a member of a class but is defined outside of it
    * example
        ```
        fun String.lastChar(): Char = this.get(this.length - 1)
        fun String.lastChar(): Char = get(length - 1) // this is implicit
        ```
        * receiver type: `String` - type on which the extension is defined
        * receiver object: `this` - the instance of that type
    
## lambdas with receivers
* the ability to call methods of a different object in the body of a lambda
* example (from standard library)
    * `apply` - calls the specified function `block` with `this` value as its receiver and returns 
    `this` value
        ```
        @kotlin.internal.InlineOnly
        public inline fun <T> T.apply(block: T.() -> Unit): T {
            contract {
                callsInPlace(block, InvocationKind.EXACTLY_ONCE)
            }
            block()
            return this
        }
        ```
    * use case
        ```
        val name = StringBuilder().apply {
            append("M")
            append("i")
            append("chal")
            toString()
        }
        println(name) // Michal
        ```
    * `T.run(block: T.() -> R)`
        * calls the specified function `block` with `this` value as its receiver and returns its result
    * `with(receiver: T, block: T.() -> R)`
        * calls the specified function `block` with `receiver` as its receiver and returns its result
* lambdas with receiver and extension functions
    * note that an extension function is, in a sense, a function with a receiver
        * `this` refers to the instance of the type the function is extending
    * analogy
        * regular function <-> regular lambda
        * extension function <-> lambda with a receiver
    * method-name conflicts
        * use `this@OuterClass.conflictedMethod()`
* an extension function type describes a block of code that can be called as an extension function
    * syntax example
        * `String.(Int, Int) -> Unit`
            * receiver type: `String`
            * parameter types: `(Int, Int)`
            * return type: `Unit`
    * use-case example
        ```
        val sign: String.(name: String) -> String = { name ->
            StringBuilder(this)
                .append(name)
                .toString()
        }
    
        println("Approved by: ".sign("Michal Tumilowicz")) // Approved by: Michal Tumilowicz
        ```
    * instead of passing the object as an argument - invoke the lambda as if it were an extension function
        * it's perfectly ok to invoke example above as
            ```
            sign("Approved by: ", "Michal Tumilowicz")
            ```
* therefore, a lambda with a receiver looks exactly the same as a regular lambda in the source code

## infix
* example
    * `1 to "one"` is same as `1.to("one")`
* the method name is placed between the target object and the parameter, with no 
extra separators
* to unlock infixing, you need to mark function with the `infix` modifier

## invoke operator
* we could invoke function types (single abstract method: `invoke`) in a very simple way
    ```
    val function: (String) -> Boolean = {...}
    val argument = "Michal"
    function(argument) // internally calls SAM
    ```
* invoke is an operator
* example
    ```
    class Issues(
        val data: MutableList<String> = mutableListOf()
    ) {
        fun add(s: String) {
            data.add(s)
        }
    
        operator fun invoke(body: Issues.() -> Unit) {
            body()
        }
    }
    ```
    adding feature to call lambda outside of brackets we get
    ```
    val issues = Issues()
    issues.add("a")
    
    issues { // issues({add("abc")}) -> issues() {add("abc")} -> issues {add("abc")}
        add("abc")
    }
    ```
  
## destructuring
* example
    ```
    val p = Point(10, 20)
    val (x, y) = p  
    ```
    ```
    for ((key, value) in map) {
        println("$key -> $value")
    }
    ```
* to initialize each variable - a function named `componentN` is called
    * N is the position of the variable in the declaration
* for a data class, the compiler generates a `componentN` function for every property declared 
in the primary constructor
    * vanilla example
        ```
        class Point(val x: Int, val y: Int) {
            operator fun component1() = x
            operator fun component2() = y
        }      
        ```
* use-case
    ```
    val (name, ext) = "filename.exe".split(".", limit = 2)
    println("$name $ext") // filename exe
    ```
    * destructuring could throw exceptions in case of too few arguments
        * `Exception in thread "main" java.lang.IndexOutOfBoundsException: Index: 1, Size: 1`

## operator overloading
* every overloaded operator is defined as a function
* there are many operators to overload
    * arithmetic
    * compound assignment (ex. `=+`)
    * unary (ex. `-obj`)
    * comparison
    * contains (ex. `2 in setOf(1, 2)`)
    * rangeTo (ex. `1..10`)
    * others: https://kotlinlang.org/docs/reference/operator-overloading.html

## dsl
* there’s no well-defined boundary between a DSL and a regular API
    * one trait especially characteristic for DSL - structure (grammar)
* in API - no inherent structure in the sequence of calls, no context maintained between one call 
and the next
    * sometimes called command-query API
* the method calls in a DSL exist in a larger structure, defined by the grammar of the DSL
    * In a Kotlin - structure is created by nesting lambdas and chained method calls
* grammar is what allows us to call an internal DSL a language
    * similar to a natural language such as English
        * the function names usually act as verbs ( groupBy , orderBy )
        * their arguments fulfill the role of nouns ( Country.name )
        * single operation (tense) can be composed out of multiple function calls (words)
        * the type checker ensured the calls are combined in a meaningful way 
* kotlin support for DSL
    * extension function
    * infix call
    * operator overloading
        * `2 in listOf(1, 2) // listOf(1,2).contains(2)`
        * `map["key"] // map.get("key")`
    * lambda outside of parentheses
        * `file.use { it.read() } // file.use({ f -> f.read() } ) `
    * lambda with a receiver