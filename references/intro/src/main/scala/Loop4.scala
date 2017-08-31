import scala.annotation.tailrec

object Loop4 extends App {

  def iterate(max: Int): List[Int] = {
    var result = List[Int]()

    for (x <- 1 to max)
      result = result :+ x

    result
  }

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

  val result = sum(square(filterEven(iterate(10))))

  println(s"Sum of even numbers from 1 to 10 is $result")

}
