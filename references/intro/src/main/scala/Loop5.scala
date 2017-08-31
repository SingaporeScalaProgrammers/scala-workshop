import scala.annotation.tailrec

object Loop5 extends App {

  def iterate(max: Int): List[Int] = {
    var result = List[Int]()

    for (x <- 1 to max)
      result = result :+ x

    result
  }

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

  val result = sum(square(filterEven(iterate(10))))

  println(s"Sum of even numbers from 1 to 10 is $result")

}
