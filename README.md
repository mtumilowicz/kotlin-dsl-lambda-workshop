# kotlin-dsl-lambda-workshop

## lambda
* a lambda encodes a small piece of
  behavior that you can pass around as a value
* syntax: { x: Int, y: Int -> x + y }
* >>> val sum = { x: Int, y: Int -> x + y }
  >>> println(sum(1, 2)
* { println(42) }() vs run { println(42) }
    * the library function run that executes the lambda passed to it
* in Kotlin, a syntactic conven-
   tion lets you move a lambda expression out of parentheses if it’s the last argument in a
   function call.
   * people.maxBy({ p: Person -> p.age })
   * people.maxBy() { p: Person -> p.age }
   * When the lambda is the only argument to a function, you can also remove the empty
     parentheses from the call:
     * people.maxBy { p: Person -> p.age }
     * people.maxBy { p -> p.age } // Parameter type inferred
     * people.maxBy { it.age }
   * _ syntax
* If you use a lambda in a function, you can access the
  parameters of that function as well as the local variables declared before the lambda
    * One important difference between Kotlin and Java is that in Kotlin, you aren’t
      restricted to accessing final variables
    * You can also modify variables from within a
      lambda
    * You know that when you declare an anonymous inner class in a function, you can refer
      to parameters and local variables of that function from inside the class
* Note that, by default, the lifetime of a local variable is constrained by the function
  in which the variable is declared. But if it’s captured by the lambda, the code that uses
  this variable can be stored and executed later.
  * When you
    capture a final variable, its value is stored together with the lambda code that uses it
  * For non-final variables, the value is enclosed in a special wrapper that lets you change
    it, and the reference to the wrapper is stored together with the lambda
  * example: Capturing a mutable variable: implementation details
    * java: When you want to capture a mutable
            variable, you can use one of the following tricks: either declare an array of one ele-
            ment in which to store the mutable value, or create an instance of a wrapper class
            that stores the reference that can be changed
    * kotlin:
        var counter = 0
        val inc = { counter++ }
        under the hood
        class Ref<T>(var value: T)
        val counter = Ref(0)
        val inc = { counter.value++ }
* Member references
    * val getAge = { person: Person -> person.age }
    * val getAge = Person::age
    * You can have a reference to a function that’s declared at the top level (and isn’t a
      member of a class)
      * run(::salute)
    * function taking several parameters
        val action = { person: Person, message: String ->
        sendEmail(person, message)
        }
        val nextAction = ::sendEmail
    * val createPerson = ::Person // constructor reference
    * Note that you can also reference extension functions the same way:
      fun Person.isAdult() = age >= 21
      val predicate = Person::isAdult
    * A bound member reference
        >>> val dmitrysAgeFunction = p::age
        >>> println(dmitrysAgeFunction())
* Kotlin allows you to use lambdas
  when calling Java methods that take functional interfaces as parameters, ensuring that
  your Kotlin code remains clean and idiomatic
* creating an anonymous object that implements
  Runnable explicitly:
  postponeComputation(1000, object : Runnable {
    override fun run() {
    println(42)
    }
  })
  * When you explicitly declare an object, a new instance is cre-
    ated on each invocation. With a lambda, the situation is different: if the lambda
    doesn’t access any variables from the function where it’s defined, the corresponding
    anonymous class instance is reused between calls:
    postponeComputation(1000) { println(42) } // One instance of Runnable is created for the entire program
  * If the lambda captures variables from the surrounding scope, it’s no longer possible to
    reuse the same instance for every invocation. In that case, the compiler creates a new
    object for every call and stores the values of the captured variables in that object
* Lambda implementation details
    * As of Kotlin 1.0, every lambda expression is compiled into an anonymous class,
      unless it’s an inline lambda
    * The name of the class is derived by add-
      ing a suffix from the name of the function in which the lambda is declared: Handle-
      Computation$1
* From the compiler’s point of view, the lambda is a block of code, not an object, and
  you can’t refer to it as an object. The this reference in a lambda refers to a sur-
  rounding class.
* Lambdas with receivers: “with” and “apply”
    * the ability to call methods of a different object in the body of a
      lambda without any additional qualifiers. 
    * Such lambdas are called lambdas with receivers.
   
/**
 * Calls the specified function [block] and returns its result.
 *
 * For detailed usage information see the documentation for [scope functions](https://kotlinlang.org/docs/reference/scope-functions.html#run).
 */
@kotlin.internal.InlineOnly
public inline fun <R> run(block: () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block()
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its result.
 *
 * For detailed usage information see the documentation for [scope functions](https://kotlinlang.org/docs/reference/scope-functions.html#run).
 */
@kotlin.internal.InlineOnly
public inline fun <T, R> T.run(block: T.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block()
}

/**
 * Calls the specified function [block] with the given [receiver] as its receiver and returns its result.
 *
 * For detailed usage information see the documentation for [scope functions](https://kotlinlang.org/docs/reference/scope-functions.html#with).
 */
@kotlin.internal.InlineOnly
public inline fun <T, R> with(receiver: T, block: T.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return receiver.block()
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns `this` value.
 *
 * For detailed usage information see the documentation for [scope functions](https://kotlinlang.org/docs/reference/scope-functions.html#apply).
 */
@kotlin.internal.InlineOnly
public inline fun <T> T.apply(block: T.() -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block()
    return this
}

/**
 * Calls the specified function [block] with `this` value as its argument and returns `this` value.
 *
 * For detailed usage information see the documentation for [scope functions](https://kotlinlang.org/docs/reference/scope-functions.html#also).
 */
@kotlin.internal.InlineOnly
@SinceKotlin("1.1")
public inline fun <T> T.also(block: (T) -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block(this)
    return this
}

/**
 * Calls the specified function [block] with `this` value as its argument and returns its result.
 *
 * For detailed usage information see the documentation for [scope functions](https://kotlinlang.org/docs/reference/scope-functions.html#let).
 */
@kotlin.internal.InlineOnly
public inline fun <T, R> T.let(block: (T) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block(this)
}

/**
 * Returns `this` value if it satisfies the given [predicate] or `null`, if it doesn't.
 */
@kotlin.internal.InlineOnly
@SinceKotlin("1.1")
public inline fun <T> T.takeIf(predicate: (T) -> Boolean): T? {
    contract {
        callsInPlace(predicate, InvocationKind.EXACTLY_ONCE)
    }
    return if (predicate(this)) this else null
}

/**
 * Returns `this` value if it _does not_ satisfy the given [predicate] or `null`, if it does.
 */
@kotlin.internal.InlineOnly
@SinceKotlin("1.1")
public inline fun <T> T.takeUnless(predicate: (T) -> Boolean): T? {
    contract {
        callsInPlace(predicate, InvocationKind.EXACTLY_ONCE)
    }
    return if (!predicate(this)) this else null
}


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

## functions
* Under the hood, function types are declared as regular interfaces: a variable of a func-
  tion type is an implementation of a FunctionN interface
  * Kotlin standard library
    defines a series of interfaces, corresponding to different numbers of function argu-
    ments: Function0<R> (this function takes no arguments), Function1<P1, R> (this
    function takes one argument), and so on
  * Each interface defines a single invoke
    method, and calling it will execute the function
  * Java 8 lamb-
    das are automatically converted to values of function types

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
      * 
      