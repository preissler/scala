package monads

import org.scalatest.{Matchers, WordSpec}

class LazySpec extends WordSpec with Matchers {
  "The Lazy object" should {
    val myLazy = Lazy[Int](10)
    val otherLazy = Lazy[Int](20)
    "It should not evaluate " in {
      val lazyExc = Lazy {
        new RuntimeException("Something was wrong")
      }
      1 === 1
    }
    "It should be able to compute the values" in {
      val res = myLazy.flatMap(a => otherLazy.flatMap(b => Lazy(a * b)))
      res.use === 200
    }
  }
}
