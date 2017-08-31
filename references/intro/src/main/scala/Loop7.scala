import scala.annotation.tailrec

object Loop7 extends App {

  def iterate(max: Int): List[Int] = {
    var result = List[Int]()

    for (x <- 1 to max)
      result = result :+ x

    result
  }

  def foldLeft[A, R](xs: List[A])(zero: R)(combine: (R, A) => R): R = {

    @tailrec
    def loop(xs: List[A], result: R): R = xs match {
      case Nil => result
      case x :: tail =>
        loop(tail, combine(result, x))
    }

    loop(xs, zero)
  }

  def flatMap[A, B](xs: List[A])(f: A => List[B]): List[B] =
    foldLeft(xs)(List[B]())((acc, x) => f(x) ++ acc)

  def filter[A](xs: List[A])(f: A => Boolean): List[A] =
    flatMap(xs)(x => if (f(x)) List(x) else Nil)

  def map[A, B](xs: List[A])(f: A => B): List[B] =
    flatMap(xs)(x => List(f(x)))

  def isEven(x: Int): Boolean = x % 2 == 0

  def square(x: Int): Int = x * x

  def sum(xs: List[Int]): Int =
    foldLeft(xs)(0)((acc, x) => acc + x)

  def sumOfEvenSquares(max: Int): Int = {
    sum(map(filter(iterate(max))(isEven))(square))
  }

  val result = sumOfEvenSquares(10)

  println(s"Sum of even numbers from 1 to 10 is $result")

}
