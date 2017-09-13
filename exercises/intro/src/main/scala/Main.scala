object Main extends App {

  var sum = 0
  for (x <- 1 to 10) {
    if (x % 2 == 0)
      sum += x * x
  }

  println(s"Sum of even numbers from 1 to 10 is $sum")

}
