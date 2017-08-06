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

        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0)
                sum += i * i;
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
