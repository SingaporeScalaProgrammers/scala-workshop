Introduction to Functional Programming (FP) in Scala
============

In this tutorial we will start with a trivial program, written in imperative 
style, and through a series of refactorings transform it into a functional 
program while learning core FP concepts such as:

- referential transparency,
- expression-based programming,
- recursion,
- immutability, and
- higher-order functions.

Getting Started
---------------

Open file `src/main/scala/Main.scala` in your editor of choice.
To run the program start `sbt` from the terminal and type `run` in SBT prompt.

The Challenge
-------------

Let's start with a trivial program that computes the sum of squares of the 
first even natural numbers up to 10. That would be `2*2 + 4*4 + 6*6 + 8*8 + 
10*10` which equals 220. In fact, the previous sentence arguably already 
contains the optimal program for the problem at hand – just start `sbt 
console` from the terminal and type in that arithmetic expression in to get 
the result.

Of course, as software engineers we're not happy about scalability of that 
solution. What if we need to compute the sum of squares of even numbers up to 
1000? Let's write a *real* program for that!

The "Real" Program
------------------

We'll start with almost a schoolbook solution in Java that will have loops, 
conditionals, and variables. This first example is in Java instead of Scala 
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

> Source: [Loop1.java]

This program is, on purpose, easy enough to understand. Consider however, that 
in your real programming work you might deal with several complexities at the same
time and thus a simple `for` loop with a variable can quickly get out of hand:

- iterating over some complex domain objects instead on numbers,
- iterating over two or three arrays of those domain objects simultaneously,
- multiple and / or nested conditions,
- multiple variables to keep track of running totals,
- multiple variables that all change independently based on complex conditional logic, and
- parallel loop execution (i.e. utilising multiple CPU cores).

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

> Source: [Loop2.scala]

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
def iterate(max: Int): List[Int] = ???
def filterEven(xs: List[Int]): List[Int] = ???
def square(xs: List[Int]): List[Int] = ???
def sum(xs: List[Int]): Int = ???

val result = sum(square(filterEven(iterate(10))))
```

Let's review the function and variable assignment Scala syntax first:

1. `def f(x: Int): List[Int]` defines function `f` that takes an 
argument `x` of type `Int` and returns a `List` of `Int`s;
2. `???` is a way to define a method stub (like a TODO),
which will allow the program to compile but throws `NotImplementedError`
when the function is called;
3. `val x = ...` defines an immutable value, once assigned the value
cannot be changed (unlike a `var`).

So how would the function implementation look?

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

> Source: [Loop3.scala]

Wow! The number of lines of code just exploded and we introduced a lot of 
duplication along the way. Let's review the function body implementation:

1. The last expression of a function body is its return value;
2. `List[Int]()` creates an empty list of integers, one alternative is to use
the equivalent factory method `List.empty[Int]` (a matter of preference);
3. `list :+ element` returns a new list made of appending `element` to `list`;

Despite explosion of code and duplication we've made our program 
composable: every function can be tested in isolation and functions can be 
combined in predictable ways as long as their type signatures are compatible.
Each function became much more reusable too: `iterate` can generate lists of 
integers of arbitrary length, `filterEven` can filter any list of integers 
and so on. Later our program will be composed of even more granular and 
universal building blocks.

Consider the expression `sum(square(filterEven(iterate(10))))`. Unlike a 
program made of statements, every sub-expression is an expression on its own:

- `10` is an expression which evaluates to 10,
- `iterate(10)` is an expression which evaluates to `List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)`,
- `filterEven (iterate(10))` is an expression which evaluates to `List(2, 4, 6, 8, 10)`,
- and so on.

Every expression can be replaced with the value it evaluates to as 
long as functions involved in the expression don't have side effects – e.g.
launch the proverbial missiles or modify variables in other parts of the 
program. In functional programming such functions are called *pure* and the 
property allowing substitution of values for expressions is called 
*referential transparency*, meaning that referring to a value via an 
indirection of a function call is transparent and doesn't change program 
execution.

Recursion
---------

Expression-based programming in pure functions makes program so much easier to 
refactor because each expression only depends on its sub-expressions so 
expressions can be rearranged freely. It relies on referential transparency 
though and referential transparency doesn't stand mutable variables.

To be clear, mutable variables are useful for performance optimization on 
micro-level. That's the reason Scala supports them. We need however to learn 
to program without mutable variables to fully embrace functional programming 
style. So let's take a look at our `iterate` function and consider how to get
rid of mutability there:

```scala
def iterate(max: Int): List[Int] = {
  var result = List[Int]()

  for (x <- 1 to max)
    result = result :+ x    // <- mutation

  result
}
```

The reason we need a mutable variable in this case is to keep track of the 
result of the previous iteration as we compute the current iteration. How can
we keep track of the previous result without a variable? It turns out that in
functional programming we use functions for everything. We will let a 
function call itself with the result of the previous iteration and return the
current iteration's result. This is called *recursion*:

```scala
def iterate(max: Int): List[Int] = {

  @tailrec
  def loop(result: List[Int], max: Int): List[Int] = max match {
    case 0 => result
    case _ => loop(max :: result, max - 1)    // <- recursion
  }

  loop(Nil, max)
}
```

First, let's clear the new Scala syntax out of the way:

1. Functions can be defined inside the body of other functions, this is not 
recursion though;
2. `@tailrec` annotation checks at compile time that our recursive function 
`loop` will not blow the call stack with large inputs;
3. We use *pattern matching* to match on integer `max`. It looks like `switch` 
statement in other programming languages, but is an expression instead of a 
statement because every `case` branch has to evaluate to the same type;
4. `case _` matches any value (i.e. catchall);
5. `element :: list` creates a new list with `element` prepended to `list`;
6. `Nil` is an empty list.

Recursive pattern is to pass the intermediary result along with the reduced 
input to the `loop` function itself. The recursive function breaks out of 
recursion when the input is fully reduced and the result is fully computed. 
In this case `iterate` defines an internal recursive helper function which is
then called with the original input and empty result.

Similarly, we rewrite other functions using recursion:

```scala
def filterEven(xs: List[Int]): List[Int] = {

  @tailrec
  def loop(result: List[Int], xs: List[Int]): List[Int] = xs match {
    case Nil => result
    case x :: tail =>
      val next = if (x % 2 == 0) List(x) else Nil
      loop(next ++ result, tail)
  }

  loop(Nil, xs)
}

def square(xs: List[Int]): List[Int] = {

  @tailrec
  def loop(result: List[Int], xs: List[Int]): List[Int] = xs match {
    case Nil => result
    case x :: tail =>
      val next = List(x * x)
      loop(next ++ result, tail)
  }

  loop(Nil, xs)
}

def sum(xs: List[Int]): Int = {

  @tailrec
  def loop(result: Int, xs: List[Int]): Int = xs match {
    case Nil => result
    case x :: tail =>
      val next = x
      loop(next + result, tail)
  }

  loop(0, xs)
}
```

> Source: [Loop4.scala]<br>
> Diff: [Loop3.scala => Loop4.scala]

Here we pattern match on a list instead of integer. Note how we used the same
list construction syntax in the `case` expression to capture list's head `x` 
and its `tail`. `++` concatenates two lists.

Now we have a completely referentially transparent program composed of pure 
functions! It is still far from perfect due to extensive code duplication 
though. We'll deal with that next.

Higher-Order Functions
----------------------

Consider functions `filterEven` and `square`. They look almost identical: 
declare a recursive helper function `loop` that enumerates the list and 
builds up the output list one element at a time. The only difference is in 
how `filterEven` and `square` define the next element of the output list from
the element of the input list.

In object-oriented programming code duplication like this could be resolved 
using the [template method design pattern](https://en.wikipedia
.org/wiki/Template_method_pattern) which involves subclassing and is quite 
heavy-weight. In functional programming functions can be passed around as 
arguments and be return values of other functions. Functions that take other 
functions as arguments or return functions are called *higher-order* 
functions. Let's see how we can refactor `filterEven` and `square` by 
extracting a new higher-order function that we will call `flatMap`:

```scala
def flatMap(xs: List[Int], f: Int => List[Int]): List[Int] = {

  @tailrec
  def loop(xs: List[Int], result: List[Int]): List[Int] = xs match {
    case Nil => result
    case x :: tail =>
      loop(tail, f(x) ++ result)
  }

  loop(xs, Nil)
}

def filterEven(xs: List[Int]): List[Int] =
  flatMap(xs, x => if (x % 2 == 0) List(x) else Nil)

def square(xs: List[Int]): List[Int] =
  flatMap(xs, x => List(x * x))
```

> Source: [Loop5.scala]<br>
> Diff: [Loop4.scala => Loop5.scala]

1. `flatMap` takes function `f` as its second argument;
2. `A => B` is type annotation for a function that takes an argument of type 
`A` and returns a value of type `B`;
3. `x => List(x * x)` is a lambda expression – a functional literal that 
takes an argument `x` and returns a list with a value of `x` squared.

`flatMap` is called this way because it maps each element of the input list 
to a new value and, because the new value is a list, it flattens the 
resulting list of lists to return a simple list. `flatMap` is more universal 
than just `map` because it allows filtering to be expressed in terms of 
itself. Imagine how would you implement `filterEven` in terms of `map` 
instead of `flatMap`.

Folding Lists
-------------

Now consider the `sum` function: It also does enumerate the input list and 
build up the result based on the current element of the loop, however the 
result type is different – it's `Int` instead of a list. What if we could 
abstract away the type and let the passed in function argument decide the 
type of the result? Let's try combining higher-order functions with generics 
and define an even more fundamental function – `foldLeft`:

```scala
def foldLeft[A, R](xs: List[A])(zero: R)(combine: (R, A) => R): R = {

  @tailrec
  def loop(xs: List[A], result: R): R = xs match {
    case Nil => result
    case x :: tail =>
      loop(tail, combine(result, x))
  }

  loop(xs, zero)
}

def flatMap(xs: List[Int], f: Int => List[Int]): List[Int] =
  foldLeft(xs)(List[Int]())((acc, x) => f(x) ++ acc)

def filterEven(xs: List[Int]): List[Int] =
  flatMap(xs, x => if (x % 2 == 0) List(x) else Nil)

def square(xs: List[Int]): List[Int] =
  flatMap(xs, x => List(x * x))

def sum(xs: List[Int]): Int =
  foldLeft(xs)(0)((acc, x) => acc + x)
```

> Source: [Loop6.scala]<br>
> Diff: [Loop5.scala => Loop6.scala]

1. `foldLeft[A, R]` is parametrized on type of the input list `A` as well as 
the type of its return value `R` thus `foldLeft` is a *generic* function;
2. Scala functions can have multiple parameter lists. Here it's useful for 
type inference: by knowing type of `xs` and `zero` Scala compiler doesn't 
require explicit type annotations on `combine`.

Function `foldLeft` takes a `zero` result (which is returned if the input list 
is empty) and a `combine` function which will be applied to the intermediate 
result (starting with `zero`) and to each element of the input list. In case 
of `flatMap`, `zero` is an empty list and the combining function is list 
concatenation. In case of `sum`, `zero` is integer 0 and combining function 
is integer addition.

We can implement `flatMap` in terms of `foldLeft` but not the other way 
around, which means that `foldLeft` is a more powerful function. If something 
can be implemented using more powerful functions, it doesn't mean it has to 
be – less powerful functions are often more readable as they raise the level 
of abstraction. They can also be faster as they can be optimized for a 
narrower use case. Write programs according to the principle of the least 
powerful abstraction.

More Combinators
----------------

Now `filterEven` and `square` can also be expressed in terms of more common 
list combinators:

```scala
def filter[A](xs: List[A])(f: A => Boolean): List[A] =
  flatMap(xs)(x => if (f(x)) List(x) else Nil)

def map[A, B](xs: List[A])(f: A => B): List[B] =
  flatMap(xs)(x => List(f(x)))

def isEven(x: Int): Boolean = x % 2 == 0

def square(x: Int): Int = x * x

def sumOfEvenSquares(max: Int): Int = {
  sum(map(filter(iterate(max))(isEven))(square))
}
```

> Source: [Loop7.scala]<br>
> Diff: [Loop6.scala => Loop7.scala]

Here, `square` really does only what it says and doesn't mess with lists 
anymore, which is now the responsibility of `map`.

Collection API
--------------

"By a lucky coincidence", Scala standard library collection API already provides
most of the functions that we've just discovered. It would be a shame not to
use them:

```scala
def isEven(x: Int): Boolean = x % 2 == 0

def square(x: Int): Int = x * x

def sumOfEvenSquares(max: Int): Int = {
  (1 to max)
    .filter(isEven)
    .map(square)
    .sum
}
```

> Source: [Loop8.scala]<br>
> Diff: [Loop7.scala => Loop8.scala]

We use `1 to 10` directly instead of our own `iterate` function. `filter`, 
`map`, `sum` as well as `flatMap`, `foldLeft` and many more functions are 
available as methods on any Scala collection. This allows using the fluid API 
style avoiding nested function calls for improved readability.

Summary
-------

The resulting program in functional style roughly the same size as the 
imperative Java program we wrote in the beginning. It's arguably much more 
readable and easy to maintain and change because it doesn't contain any 
mutable variables and imperative statements. It's very modular – we can 
change the order of operations in predictable ways and we can add more 
transformations without changing the existing ones.

<!-- Links and notes -->
[Loop1.java]: https://github.com/SingaporeScalaProgrammers/scala-workshop/blob/master/references/intro/src/main/java/Loop1.java
[Loop2.scala]: https://github.com/SingaporeScalaProgrammers/scala-workshop/blob/master/references/intro/src/main/scala/Loop2.scala
[Loop3.scala]: https://github.com/SingaporeScalaProgrammers/scala-workshop/blob/master/references/intro/src/main/scala/Loop3.scala
[Loop3.scala => Loop4.scala]: https://rawgit.com/SingaporeScalaProgrammers/scala-workshop/master/references/intro/diff/Loop3-Loop4.html
[Loop4.scala]: https://github.com/SingaporeScalaProgrammers/scala-workshop/blob/master/references/intro/src/main/scala/Loop4.scala
[Loop4.scala => Loop5.scala]: https://rawgit.com/SingaporeScalaProgrammers/scala-workshop/master/references/intro/diff/Loop4-Loop5.html
[Loop5.scala]: https://github.com/SingaporeScalaProgrammers/scala-workshop/blob/master/references/intro/src/main/scala/Loop5.scala
[Loop5.scala => Loop6.scala]: https://rawgit.com/SingaporeScalaProgrammers/scala-workshop/master/references/intro/diff/Loop5-Loop6.html
[Loop6.scala]: https://github.com/SingaporeScalaProgrammers/scala-workshop/blob/master/references/intro/src/main/scala/Loop6.scala
[Loop6.scala => Loop7.scala]: https://rawgit.com/SingaporeScalaProgrammers/scala-workshop/master/references/intro/diff/Loop6-Loop7.html
[Loop7.scala]: https://github.com/SingaporeScalaProgrammers/scala-workshop/blob/master/references/intro/src/main/scala/Loop7.scala
[Loop7.scala => Loop8.scala]: https://rawgit.com/SingaporeScalaProgrammers/scala-workshop/master/references/intro/diff/Loop7-Loop8.html
[Loop8.scala]: https://github.com/SingaporeScalaProgrammers/scala-workshop/blob/master/references/intro/src/main/scala/Loop8.scala
