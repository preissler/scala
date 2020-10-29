package exercises.stream

object NaturalsStreaming extends App {
  val naturals = MyStream.from(1)(_ + 1)
  println(naturals.head)
  println(naturals.tail.head)
  println(naturals.tail.tail.head)
  println(naturals.tail.tail.tail.head)

  val n = -10 #:: naturals
  println(n.head)
  println(n.tail.head)
  println(n.tail.tail.head)

  naturals.take(10000).foreach(println)

  println(naturals.map(_ * 2).take(10).toList())

  println(
    naturals
      .flatMap(x => new ConsStream(1, new ConsStream(x + 10, EmptyMyStream)))
      .take(10)
      .toList()
  )

  println(naturals.take(100).filter(_ < 10).toList())
  println(naturals.filter(_ < 10).take(2).toList())

}
