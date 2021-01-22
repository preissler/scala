package implicitis

object TypeClassReview extends App {

  case class User(name: String, age: Int, email: String)

  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  object UserEqual extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.age == b.age && a.email == b.email
  }

  val user1 = User("a", 10, "t@t.com")
  val user2 = User("a", 10, "t@t.com")
  val user3 = User("a", 11, "t@t.com")

  println(UserEqual(user1, user2))
  println(UserEqual(user1, user3))

  // Adding Implicits

  implicit object UserEqual2 extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.age == b.age && a.email == b.email
  }

  object Equal {
    def apply[T](a: T, b: T)(implicit comparator: Equal[T]) = comparator(a, b)
  }
  println(Equal.apply[User](user1, user2))
  println(Equal[User](user1, user3))

}
