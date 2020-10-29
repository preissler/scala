package exercises.stream

// Modify to avoid stackOverflow ???
object PrimeNumbersStreaming extends App {
  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] = {
    if (numbers.isEmpty) numbers
    else
      new ConsStream(numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0)))
  }
  println(eratosthenes(MyStream.from(2)(_ + 1)).take(1000).toList())
}
