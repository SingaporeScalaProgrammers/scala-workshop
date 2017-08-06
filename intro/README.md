Introduction to Functional Programming in Scala
============

In this tutorial we will start with a trivial program, written in imperative 
style, and through a series of refactorings transform it into a functional 
program while learning the core FP concepts like referential transparency, 
expression-based programming, recursion, immutability and higher-order 
functions.

Getting Started
-------

Open file `src/main/scala/Main.scala` in your editor of choice. To run the 
program start `sbt` from the terminal and type `run` in SBT prompt.

The Challenge
-------------

Let's start with a trivial program that computes the sum of squares of the 
first even natural numbers up to 10. That would be `2*2 + 4*4 + 6*6 + 8*8 + 
10*10` which equals 220. In fact, the previous sentence arguably already 
contains the optimal program for the problem at hand – just start `sbt 
console` from the terminal and type in that arithmetic expression in to get 
the result.

Of course, as software engineers we're not happy about scalability of that 
solution. What if need to compute the sum of squares of even numbers up to 
1000? Let's write a *real* program for that!

The "Real" Program
------------------

We'll start with almost a schoolbook solution in Java that will have loops, 
conditionals and variables. This first example is in Java instead of Scala 
so that it's immediately familiar to anyone who did some programming in 
procedural imperative language before:

```java
public class Loop {
    public static void main(String[] args) {
        int sum = 0;

        for (int x = 1; x <= 10; x++) {
            if (x % 2 == 0)
                sum += x * x;
        }

        System.out.println("Sum of even numbers from 1 to 10 is " + sum);
    }
}
```

This program is on purpose easy enough to understand. Consider however that 
in your real programming work you would iterate over some complex domain 
objects instead on numbers, maybe over two or three arrays of those 
simultaneously, there would be many more conditions and they would be nested,
there would be multiple variables to keep track of running totals and they 
would all change independently of each other based on complex conditional 
logic. And what about running this loop on multiple CPU cores in parallel so 
that it can finish faster? You see how a simple `for` loop with a variable can 
quickly get out of hand?

Two cornerstones of imperative programming languages contribute to the 
problem here:

- *Mutable variables* are named memory places, that any part of the program 
can change at will, make keeping track of what changes where and why 
particularly tricky.
- *Statement-based programs* are made of imperative instructions for the 
computer to execute in a given order. In conjunction with mutable variables 
imperative statements can not be easily composed into larger programs because
 each statement can potentially alter variables and thus break other statements.

Of course, imperative languages provide larger units of composition (e.g. 
procedures and classes) to deal with increasing complexity. And still 100 
lines-long procedures and 100 methods-large classes that are hard to refactor
 suggest that there might be a better way.

Next we will translate our program to Scala and refactor it to functional 
style guided by principles of immutable data and expression-based programming.

Meet Scala
----------

Let's see how our program could look like written in Scala and learn some of 
the Scala syntax:

```scala
object Main extends App {    // 1.

  var sum = 0                // 2.
  for (x <- 1 to 10) {       // 3. and 4.
    if (x % 2 == 0)
      sum += x * x
  }

  println(s"Sum of even numbers from 1 to 10 is $sum")    // 5.
}
```

First you notice that Scala is less verbose: no semicolons needed, no `public
 static void main`, no explicit reassignment of `x` in `for` loop. Here are 
 other notable differences:

1. `object` keyword defines a singleton value initialized by the block of code 
inside it. Extending `App` makes it an entrypoint of an application;
2. `var` keyword defines a variable that can be reassigned later. Type of the
 variable is inferred from the right-hand side of the assignment, `Int` in 
 this case;
3. `1 to 10` defines a inclusive `Range` – a sequence that can be enumerated;
4. `for (x <- seq) { ...x... }` enumerates elements of `seq` executing the 
code block in curly braces with `x` representing each element in scope;
5. `s"...$x..."` does string interpolation of value `x`.

Decomplecting Functions
-----------------------

Now let's look at that program again and consider how many distinct functions
 are interleaved together:

```scala
var sum = 0             // sum
for (x <- 1 to 10) {    // iterate
  if (x % 2 == 0)       // filter even
    sum += x * x        // square and sum
}
```

There are 4 distinct functions that are entangled in this statement-based 
program. No single function can be tested in isolation. Let's pull them apart:

```scala
def iterate(max: Int): List[Int] = {
  var result = List[Int]()

  for (x <- 1 to max)
    result = result :+ x

  result
}

def filterEven(xs: List[Int]): List[Int] = {
  var result = List[Int]()

  for (x <- xs)
    if (x % 2 == 0)
      result = result :+ x

  result
}

def square(xs: List[Int]): List[Int] = {
  var result = List[Int]()

  for (x <- xs)
    result = result :+ (x * x)

  result
}

def sum(xs: List[Int]): Int = {
  var result = 0

  for (x <- xs)
    result += x

  result
}

val result = sum(square(filterEven(iterate(10))))
```

Wow! The number of lines of code just exploded and we introduced a lot of 
duplication along the way. Let's review new Scala syntax first:

1. `def f(x: Int): List[Int] = {...}` defines function `f` that takes an 
argument `x` of type `Int` and returns a `List` of `Int`s;
2. The last expression of a function body is its return value;
3. `List[Int]()` creates an empty list of integers;
4. `list :+ element` returns a new list made of appending `element` to `list`;
5. `val x = ...` defines an immutable value that cannot be changed.

Despite explosion of code and duplication we've made our program 
composable: every function can be tested in isolation and functions can be 
combined in predictable ways as long as their type signatures are compatible.

Consider the expression `sum(square(filterEven(iterate(10))))`. Unlike a 
program made of statements, every sub-expression is an expression on its own:
 `10` is an expression which evaluates to 10, `iterate(10)` is an expression 
which evaluates to `List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)`, `filterEven 
(iterate(10))` is an expression which evaluates to `List(2, 4, 6, 8, 10)` 
and so on. Every expression can be replaced with the value it evaluates to as 
long as functions involved in the expression don't have side effects – e.g.
launch the proverbial missiles or modify variables in other parts of the 
program. In functional programming such functions are called *pure* and the 
property allowing substitution of values for expressions is called 
*referential transparency* meaning that referring to a value via an 
indirection of a function call is transparent and doesn't change program 
execution.
