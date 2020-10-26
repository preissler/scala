package monads

object MonadCatsFuncy extends App {
  trait Monad[F[_]] {
    def pure[A](value: A): F[A]
    def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]
    def map[A, B](value: F[A])(func: A => B): F[B] =
      flatMap(value)(a => flatMap(value)(a => pure(func(a))))
  }

  import cats.Id
  def pure[A](value: A): Id[A] = value
  def map[A, B](initial: Id[A])(func: A => B): Id[B] = func(initial)
  def flatMap[A, B](initial: Id[A])(func: A => Id[B]): Id[B] = func(initial)

  println(pure(123))
  println(map(123)(_ * 2))
  println(flatMap(123)(_ * 2))
}
