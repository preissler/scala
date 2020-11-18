package akka.streams

import java.text.SimpleDateFormat

import akka.actor.ActorSystem
import akka.stream.ClosedShape
import akka.stream.scaladsl.{GraphDSL, RunnableGraph, Sink, Source, ZipWith}

import scala.io.{Source => SSource}

object StreamsConversions extends App {

  /**
   * Problem :
   * Stream A contains stock values placed in a currency X
   * Stream B contains currency exchange rates for X to  Y
   * Goal: Create a stream C where the stock values from stream A are using Y
   *
   */
  case class ExchangeRates(currency_a: String, value_a: BigDecimal, currency_b: String, value_b: BigDecimal)

  case class Stock(code: String, value: BigDecimal, currency: String)

  implicit val system = ActorSystem("AkkaStreams")

  val format = new SimpleDateFormat("yyyy-mmm-dd:hh:mm:ss")
  val stockSource = Source(
    SSource
      .fromFile("src/resources/stocks.txt")
      .getLines
      .map(a => {
        val s = a.split(",")
        Stock(s(0), BigDecimal(s(1)), "Real")
      })
      .toStream
  )

  stockSource.to(Sink.foreach(println)).run()

  val currency_rates = Source(
    SSource
      .fromFile("src/resources/dollar_real_rate.txt")
      .getLines
      .map(c => {
        val s = c.split(",")
        ExchangeRates("Real", BigDecimal(s(0)), "Dollar", BigDecimal(s(1)))
      })
      .toStream
  )

  val simpleSink = Sink.foreach[Stock](println)

//  currency_rates.to(Sink.foreach(println)).run()

  val convertCurrencyGraph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._
    val stks = builder.add(stockSource)
    val rate = builder.add(currency_rates)
    val zip = builder.add(ZipWith[ExchangeRates, Stock, Stock]((ex, s) => {
      Stock(s.code, s.value * ex.value_b, ex.currency_b)
    }))

    val sink = builder.add(simpleSink)

    rate ~> zip.in0
    stks ~> zip.in1
    zip.out ~> sink

    ClosedShape
  })
  convertCurrencyGraph.run()

}
