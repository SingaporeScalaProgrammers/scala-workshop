import scala.annotation.tailrec

object Loop6 extends App {

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

  def flatMap(xs: List[Int], f: Int => List[Int]): List[Int] =
    foldLeft(xs)(List[Int]())((acc, x) => f(x) ++ acc)

  def filterEven(xs: List[Int]): List[Int] =
    flatMap(xs, x => if (x % 2 == 0) List(x) else Nil)

  def square(xs: List[Int]): List[Int] =
    flatMap(xs, x => List(x * x))

  def sum(xs: List[Int]): Int =
    foldLeft(xs)(0)((acc, x) => acc + x)

  val result = sum(square(filterEven(iterate(10))))

  println(s"Sum of even numbers from 1 to 10 is $result")

}
