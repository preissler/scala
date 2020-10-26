package implicitis

import java.text.SimpleDateFormat
import java.util.Date

object Implicits extends App {
  //JSON Serialization example

  sealed trait JSONValue {
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    override def stringify: String = "\"" + value + "\""
  }

  final case class JSONNumber(value: Int) extends JSONValue {
    override def stringify: String = value.toString
  }

  final case class JSONDate(value: Date) extends JSONValue {
    override def stringify: String = "\"" + value.toString + "\""
  }

  final case class JSONArray(values: List[JSONValue]) extends JSONValue {
    override def stringify: String =
      values.map(_.stringify).mkString("[", ",", "]")
  }

  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    override def stringify: String =
      values.map {
        case (k, v) => "\"" + k + "\":" + v.stringify
      }.mkString("{", ",", "}")
  }

  trait JSONConverter[A] {
    def convert(v: A): JSONValue
  }

  implicit object StringConverter extends JSONConverter[String] {
    override def convert(v: String): JSONValue = JSONString(v)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    override def convert(v: Int): JSONValue = JSONNumber(v)
  }
  implicit object DateConverter extends JSONConverter[Date] {
    override def convert(v: Date): JSONValue = JSONDate(v)
  }
  implicit class JSONConvert[A](v: A) {
    def toJSON(implicit converter: JSONConverter[A]): JSONValue =
      converter.convert(v)
  }

  val t1 = "This is a simple test"

  println(t1.toJSON)
  println(t1.toJSON.stringify)

  // create a complex example
  case class Author(name: String, age: Int, county: String)
  case class Book(title: String, subject: String, date: Date, author: Author)

  // create the implicit converter
  implicit object AuthorConverter extends JSONConverter[Author] {
    override def convert(v: Author): JSONValue =
      JSONObject(Map("name" -> JSONString(v.name), "age" -> JSONNumber(v.age), "country" -> JSONString(v.county)))
  }

  implicit object BookConverter extends JSONConverter[Book] {
    override def convert(v: Book): JSONValue =
      JSONObject(
        Map(
          "title" -> JSONString(v.title),
          "subject" -> JSONString(v.subject),
          "date" -> JSONDate(v.date),
          "author" -> v.author.toJSON
        )
      )
  }

  // tests
  val author = Author("Martin Odersky", 50, "Germany")
  val author1 = Author("Paul Chiusano", 45, "USA")

  val dateFormat = new SimpleDateFormat("yyyy")

  val book = Book("Programming in Scala", "Programming", dateFormat.parse("2007"), author)
  val book1 = Book("Functional Programming in Scala", "Programming", dateFormat.parse("2015"), author1)

  println(book.toJSON.stringify)
  println(book1.toJSON.stringify)
}
