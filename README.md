# kotlin-dsl-lambda-workshop

* reference
    * https://kotlinlang.org/docs/reference/scope-functions.html
    * https://www.manning.com/books/kotlin-in-action

## extension function
* top-level function
    * JVM can only execute code  in classes
    * you can place functions directly at the top level of a source file, outside of any class
        * when you compile the file, some classes will be produced
    ```
    package strings // filename: join.kt
    fun joinToString(...): String { ... }
    
    /* Java */
    package strings;
    public class JoinKt { // Corresponds to join.kt, the filename
    public static String joinToString(...) { ... }
    }
    ```
* it’s a function that can be called as a member of a class but is defined outside of it
* example
    ```
    fun String.lastChar(): Char = this.get(this.length - 1)
    fun String.lastChar(): Char = get(length - 1) // this is implicit
    ```
    * receiver type: String - type on which the extension is defined
    * receiver object: this - the instance of that type
    
## lambdas with receivers
* the ability to call methods of a different object in the body of a
  lambda without any additional qualifiers.
   
* run - Calls the specified function [block] and returns its result
    ```
    @kotlin.internal.InlineOnly
    public inline fun <R> run(block: () -> R): R {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        return block()
    }  
    ```
* run - Calls the specified function [block] with `this` value as its receiver and returns its result.
    ```
    @kotlin.internal.InlineOnly
    public inline fun <T, R> T.run(block: T.() -> R): R {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        return block()
    }
    ```
* with - calls the specified function [block] with the given [receiver] as its receiver and 
returns its result
    ```
    @kotlin.internal.InlineOnly
    public inline fun <T, R> with(receiver: T, block: T.() -> R): R {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        return receiver.block()
    }
    ```
* apply - Calls the specified function [block] with `this` value as its receiver and returns `this` value
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

* also - Calls the specified function [block] with `this` value as its argument and returns `this` value.
    ```
    @kotlin.internal.InlineOnly
    @SinceKotlin("1.1")
    public inline fun <T> T.also(block: (T) -> Unit): T {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        block(this)
        return this
    }
    ```
* let - Calls the specified function [block] with `this` value as its argument and returns its result.
    ```
    @kotlin.internal.InlineOnly
    public inline fun <T, R> T.let(block: (T) -> R): R {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        return block(this)
    }
    ```
* takeIf - Returns `this` value if it satisfies the given [predicate] or `null`, if it doesn't.
    ```
    @kotlin.internal.InlineOnly
    @SinceKotlin("1.1")
    public inline fun <T> T.takeIf(predicate: (T) -> Boolean): T? {
        contract {
            callsInPlace(predicate, InvocationKind.EXACTLY_ONCE)
        }
        return if (predicate(this)) this else null
    }
    ```
* takeUnless - Returns `this` value if it _does not_ satisfy the given [predicate] or `null`, if it does.
    ```
    @kotlin.internal.InlineOnly
    @SinceKotlin("1.1")
    public inline fun <T> T.takeUnless(predicate: (T) -> Boolean): T? {
        contract {
            callsInPlace(predicate, InvocationKind.EXACTLY_ONCE)
        }
        return if (!predicate(this)) this else null
    }
    ```

* Lambdas with receiver and extension functions
    * In the body of an extension function, this refers to the instance of the type
      the function is extending, and it can be omitted to give you direct access to the
      receiver’s members
    * Note that an extension function is, in a sense, a function with a receiver
        * Regular function Regular lambda
        * Extension function Lambda with a receiver
    * Method-name conflicts
        * What happens if the object you pass as a parameter to with has a method with the
          same name as the class in which you’re using with
        * this@OuterClass.conflictedMethod()
        
## infix
* In an infix call, the method name is placed immediately between the target object
  name and the parameter, with no extra separators
    * 1.to("one") 
    * 1 to "one" 
* To allow a function to be called using the infix notation, you need to mark it with the infix modifier.

## destructuring
* destructuring declarations
    * allows you
      to unpack a single composite value and use it to initialize several separate variables
    * val p = Point(10, 20)
    * val (x, y) = p
    * Under the hood, the destructuring declaration once again uses the principle of con-
      ventions. To initialize each variable in a destructuring declaration, a function named
      componentN is called, where N is the position of the variable in the declaration
    * For a data class, the compiler gen-
      erates a componentN function for
      every property declared in the primary
      constructor
        class Point(val x: Int, val y: Int) {
            operator fun component1() = x
            operator fun component2() = y
        }
    * example
        val (name, ext) = "filename.exe".split(".", limit = 2)
        println("$name $ext")
        * Exception in thread "main" java.lang.IndexOutOfBoundsException: Index: 1, Size: 1
    * it’s not possible to define an infinite number of such componentN func-
      tions so the syntax would work with an arbitrary number of items
    * example
        for ((key, value) in map) {
            println("$key -> $value")
        }

### dsl
* Table 11.1 Kotlin support for clean syntax
    * Extension function
        * Regular syntax StringUtil.capitalize(s) 
        * Clean syntax s.capitalize()
    * Infix call
        * 1.to("one") 
        * 1 to "one" 
    * Operator overloading
        * listOf(1,2).contains(2)
        * 2 in listOf(1, 2)
    * Convention for the get method
        * map.get("key") 
        * map["key"] 
    * Lambda outside of parentheses
        * file.use({ f -> f.read() } ) 
        * file.use { it.read() }
    * Lambda with a receiver
        * Regular syntax
            sb.append("yes")
            sb.append("no")
        * Clean syntax
            with (sb) {
                append("yes")
                append("no")
            }
    
* dsl
    * Generally speaking, there’s no well-defined boundary between a DSL and a regular
      API
    * But one trait comes up often in DSL s and usually
      doesn’t exist in other API s: structure, or grammar
    * A typical library consists of many methods, and the client uses the library by calling
      the methods one by one. There’s no inherent structure in the sequence of calls, and
      no context is maintained between one call and the next. Such an API is sometimes
      called a command-query API .
    * As a contrast, the method calls in a DSL exist in a larger
      structure, defined by the grammar of the DSL
      * In a Kotlin DSL , structure is most com-
        monly created through the nesting of lambdas or through chained method calls
    * You
      can clearly see this in the previous SQL example: executing a query requires a combi-
      nation of method calls describing the different aspects of the required result set
    * This grammar is what allows us to call an internal DSL a language. In a natural lan-
      guage such as English, sentences are constructed out of words, and the rules of gram-
      mar govern how those words can be combined with one another. Similarly, in a DSL , a
      single operation can be composed out of multiple function calls, and the type checker
      ensures that the calls are combined in a meaningful way. In effect, the function names
      usually act as verbs ( groupBy , orderBy ), and their arguments fulfill the role of nouns
      ( Country.name )
    * assertTrue(str.startsWith("kot")) vs str should startWith("kot")
    * lambdas with
      receivers: the key feature that helps establish the grammar of DSL s
      *  In effect, you
        can give one of the parameters of the lambda the special status of a receiver, letting you
        refer to its members directly without any qualifier
      * builderAction: StringBuilder.() -> Unit // Declares a parameter of a function type with a receiver
        *  StringBuilder().builderAction() // Passes a StringBuilder as a receiver to the lambda
        val stringAction: String.() -> Unit = { println(this + "buba") }
        
        stringAction("asd")
        "viap".stringAction()
      * String.(Int, Int) -> Unit
        * receiver type: String
        * parameter types: (Int, Int)
        * Return type: Unit
    * Why an extension function type? The idea of accessing members of an external type
      without an explicit qualifier may remind you of extension functions, which allow you
      to define your own methods for classes defined elsewhere in the code. Both extension
      functions and lambdas with receivers have a receiver object, which has to be provided
      when the function is called and is available in its body. In effect, an extension function
      type describes a block of code that can be called as an extension function.
    * The way you invoke the variable also changes when you convert it from a regular
      function type to an extension function type. Instead of passing the object as an argu-
      ment, you invoke the lambda variable as if it were an extension function.
    * builderAction
      here isn’t a method declared on the StringBuilder class; it’s a parameter of a func-
      tion type that you call using the same syntax you use to call extension functions
    * Note that a lambda with a receiver looks exactly the same as a regular lambda in the
      source code. To see whether a lambda has a receiver, you need to look at the function
      to which the lambda is passed: its signature will tell you whether the lambda has a
      receiver and, if it does, what its type is.
* Using invoke to support flexible DSL syntax
    fun main() {
    
        val issues = Issues()
        issues.add("a")
        
        issues { // issues({add("abc")}) -> issues() {add("abc")} -> issues {add("abc")}
            add("abc")
        }
    }
    
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
      