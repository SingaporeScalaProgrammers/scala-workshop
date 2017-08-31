object Loop3 extends App {

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

  println(s"Sum of even numbers from 1 to 10 is $result")

}
