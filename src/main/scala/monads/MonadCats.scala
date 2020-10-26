package monads

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

object MonadCats extends App {

  val numberList = List(1, 2, 3)
  val charList = List('a', 'b', 'c')
  val numberOption = Option(2)
  val charOption = Option('d')
  val numberFuture = Future(42)
  val charFuture = Future('Z')

  import cats.Monad
  import cats.instances.option._
  val optionMonad = Monad[Option]
  val anOption = optionMonad.pure(4)
  val affineTransformOption =
    optionMonad.flatMap(anOption)(x => if (x % 3 == 0) Some(x + 1) else None)
  println(affineTransformOption)

  import cats.instances.list._
  val listMonad = Monad[List]

  val aList = listMonad.pure(3)
  val aTransformedList = listMonad.flatMap(aList)(x => List(x, x + 1))
  println(aTransformedList)

  import cats.instances.future._
  val futureMonad = Monad[Future]
  val aFuture = futureMonad.pure(10)
  val aTransformedFuture = futureMonad.flatMap(aFuture)(x => Future(150 + x))
  aTransformedFuture.foreach(println)

  def getPairs[M[_], A, B](ma: M[A], mb: M[B])(
    implicit
    monad: Monad[M]
  ): M[(A, B)] =
    monad.flatMap(ma)(a => monad.map(mb)(b => (a, b)))

  println(getPairs(numberList, charList))
  println(getPairs(numberOption, charOption))
  getPairs(numberFuture, charFuture).foreach(println)

  //import the flatMap and Map
  import cats.syntax.flatMap._
  import cats.syntax.functor._
  def getPairs2[M[_], A, B](ma: M[A], mb: M[B])(
    implicit
    monad: Monad[M]
  ): M[(A, B)] =
    for {
      a <- ma
      b <- mb
    } yield (a, b)

  //forcing to have a Monad in scope
  def getPairs3[M[_]: Monad, A, B](ma: M[A], mb: M[B]): M[(A, B)] =
    ma.flatMap(a => mb.map(b => (a, b)))

  println(getPairs2(numberList, charList))

  println(getPairs2(numberOption, charOption))
  getPairs2(numberFuture, charFuture).foreach(println)

}
