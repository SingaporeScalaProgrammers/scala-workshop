object Loop8 extends App {

  def isEven(x: Int): Boolean = x % 2 == 0

  def square(x: Int): Int = x * x

  def sumOfEvenSquares(max: Int): Int = {
    (1 to max)
      .filter(isEven)
      .map(square)
      .sum
  }

  val result = sumOfEvenSquares(10)

  println(s"Sum of even numbers from 1 to 10 is $result")

}
