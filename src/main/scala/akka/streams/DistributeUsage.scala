package akka.streams

import akka.actor.ActorSystem
import akka.stream.ClosedShape
import akka.stream.scaladsl.{GraphDSL, Merge, RunnableGraph, Sink, Source}

import scala.concurrent.duration._

object DistributeUsage extends App {

  implicit val system = ActorSystem("GraphShapesGeneric")

  val fixShape = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._
    val source1 = Source(Stream.from(1)).throttle(1, 1 second)
    val source2 = Source(Stream.from(1)).throttle(2, 1 second)
    val source3 = Source(Stream.from(1)).throttle(2, 1 second)

    def createSink(index: Int) =
      Sink.fold(0)((count: Int, element: Int) => {
        println(s"[sink $index] Element $element, count is $count")
        count + 1
      })

    val sink1 = builder.add(createSink(1))
    val sink2 = builder.add(createSink(2))
    val sink3 = builder.add(createSink(3))

    val distribute3_3 = builder.add(Distribute[Int](3, 3))

    source1 ~> distribute3_3.inlets(0)
    source2 ~> distribute3_3.inlets(1)
    source3 ~> distribute3_3.inlets(2)

    distribute3_3.outlets(0) ~> sink1
    distribute3_3.outlets(1) ~> sink2
    distribute3_3.outlets(2) ~> sink3

    ClosedShape
  })
  fixShape.run()

}
