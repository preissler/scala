package monads

import java.util.concurrent.Executors

import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.{ExecutionContext, Future}
import scala.math.BigInt.int2bigInt

class SafeValueSpec extends WordSpec with Matchers {
  implicit val ec: ExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  "The SafeValue object" should {

    "avoid concurrency issue " in {
      val n = 100000
      var s = SafeValue[Int](n)
      for (_ <- 1 to n)
        Future(s = s.flatMap(x => SafeValue(x - 1)))
      s.get === 0
    }
  }

}
