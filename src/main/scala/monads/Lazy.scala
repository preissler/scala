package monads

class Lazy[+A](value: => A) {
  private lazy val interner = value
  def use: A = interner
  def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(interner)
}
object Lazy {
  def apply[A](a: => A): Lazy[A] = new Lazy(a)
}
