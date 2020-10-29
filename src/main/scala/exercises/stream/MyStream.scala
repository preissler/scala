package exercises.stream

import scala.annotation.tailrec

abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def #::[B >: A](element: B): MyStream[B]
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B]

  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A]
  def takeAsList(n: Int): List[A] = take(n).toList()

  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
    if (isEmpty) acc.reverse
    else tail.toList(head :: acc)

}
object MyStream {
  def from[A](start: A)(genereator: A => A): MyStream[A] = {
    new ConsStream(start, MyStream.from(genereator(start))(genereator))
  }
}

class ConsStream[+A](h: A, t: => MyStream[A]) extends MyStream[A] {
  override def isEmpty: Boolean = false

  override val head: A = h

  override lazy val tail: MyStream[A] = t

  override def #::[B >: A](element: B): MyStream[B] =
    new ConsStream[B](element, this)

  override def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] =
    new ConsStream(head, tail ++ anotherStream)

  override def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

  override def map[B](f: A => B): MyStream[B] =
    new ConsStream(f(head), tail.map(f))

  override def flatMap[B](f: A => MyStream[B]): MyStream[B] =
    f(head) ++ tail.flatMap(f)

  override def filter(predicate: A => Boolean): MyStream[A] =
    if (predicate(head)) new ConsStream(head, tail.filter(predicate))
    else tail.filter(predicate)

  override def take(n: Int): MyStream[A] =
    if (n <= 0) EmptyMyStream
    else if (n == 1) new ConsStream(head, EmptyMyStream)
    else new ConsStream(head, tail.take(n - 1))

}

object EmptyMyStream extends MyStream[Nothing] {
  override def isEmpty: Boolean = true

  override def head: Nothing = throw new NoSuchElementException

  override def tail: MyStream[Nothing] = throw new NoSuchElementException

  override def #::[B >: Nothing](element: B): MyStream[B] =
    new ConsStream(element, this)

  override def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] =
    anotherStream

  override def foreach(f: Nothing => Unit): Unit = ()

  override def map[B](f: Nothing => B): MyStream[B] = this

  override def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] =
    this

  override def filter(predicate: Nothing => Boolean): MyStream[Nothing] =
    this

  override def take(n: Int): MyStream[Nothing] = this

}
