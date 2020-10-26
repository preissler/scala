package monads

import scala.util.Success

object Monads extends App {
  // playing

  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  case class Fail(throwable: Throwable) extends Attempt[Nothing] {
    override def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  case class Success[+A](value: A) extends Attempt[A] {
    override def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e => Fail(e)
      }
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e => Fail(e)
      }
  }

  val attemptFail = Attempt {
    throw new RuntimeException("Exception")
  }
  println(attemptFail)

}
