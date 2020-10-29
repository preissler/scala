package exercises.stream

object FibonacciStreaming extends App {

  def fibonacci(first: BigInt, second: BigInt): MyStream[BigInt] = {
    new ConsStream(first, fibonacci(second, first + second))
  }

  println(fibonacci(0, 1).take(1000).toList())

}
