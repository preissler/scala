package akka.streams

import akka.NotUsed
import akka.stream.scaladsl.{Balance, GraphDSL, Merge}
import akka.stream.{Graph, Inlet, Outlet, Shape}

case class Distribute[T](override val inlets: List[Inlet[T]], override val outlets: List[Outlet[T]]) extends Shape {
  override def deepCopy(): Shape = {
    Distribute(inlets.map(_.carbonCopy()), outlets.map(_.carbonCopy()))
  }
}
object Distribute {
  def apply[T](inletn: Int, outletn: Int): Graph[Distribute[T], NotUsed] =
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._
      val merge = builder.add(Merge[T](inletn))
      val balance = builder.add(Balance[T](outletn))
      merge ~> balance

      Distribute(merge.inlets.toList, balance.outlets.toList)
    }

}
