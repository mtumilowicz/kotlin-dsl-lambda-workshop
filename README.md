# kotlin-dsl-lambda-workshop

* In an infix call, the method name is placed immediately between the target object
  name and the parameter, with no extra separators
    * 1.to("one") 
    * 1 to "one" 
* To allow a function to be called using the infix notation, you need to mark it with the infix modifier.

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
    