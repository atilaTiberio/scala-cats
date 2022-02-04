package mx.horace.cats.ch01


/*Type classes (not the same sense as scala/Java: programming pattern originated in Haskell
 --> To extend existin libraries with new functionality without using traditional inheritance
 Pattern Matching
 Value classes
 Type Aliases
 There are three important components to the type class pattern: the type class itself, instances for particular types, and the methods that use type classes.

Type classes in Scala are implemented using implicit values and parameters, and optionally using implicit classes.
Scala language constructs correspond to the components of type classes as follows:
• traits: type classes;
• implicit values: type class instances;
• implicit parameters: type class use; and
• implicit classes: optional utilities that make type classes easier to use.
Let’s see how this works in detail.

 */

/**
 * How to package implicits
  we can package type class instances in roughly four ways:
    1. by placing them in an object such as JsonWriterInstances; -> importing them
    2. by placing them in a trait; -> inheritance
    3. by placing them in the companion object of the type class -> instances are always in implicit scope
    4. by placing them in the companion object of the parameter type -> instances are always in implicit scope
 */
object TypeClassesImplicits extends App {

  println("Type classes foundations")

  /*
    Serialize to JSON behaviour
   */
  sealed trait Json
  final case class JsObject(get: Map[String, Json]) extends Json
  final case class JsString(get: String) extends Json
  final case class JsNumber(get: Double) extends Json
  final case object JsNull extends Json

  //The serialize to JSON behaviour is encoded in this trait --> This is the "type class" and its subtypes providing supporting code
  // A companionn object can be created on this type class to package the implicits
  trait JsonWriter[A] {
    def write(value: A): Json
  }

  final case class Person(name: String, email: String)
  final case class Animal(name: String, owner: String)
  //It's time to implement type classes instances
  object JsonWriterInstances {

    implicit val stringWriter: JsonWriter[String] =
      new JsonWriter[String] {
        def write(value: String): Json =
          JsString(value) //This is the supporting code
      }

    implicit val personWriter: JsonWriter[Person] = {
      new JsonWriter[Person] {
        def write(value: Person): Json =
          JsObject(Map(
            "name" -> JsString(value.name),
            "email" -> JsString(value.email)
          ))
      }
      //These are known as implicit values
    }

    implicit  val animalWriter: JsonWriter[Animal] = {
      new JsonWriter[Animal] {
        def write(value: Animal): Json =
          JsObject( Map(
           "name" -> JsString(value.name),
           "owner" -> JsString(value.owner)
          ))
      }
    }
      //Implicit conversion: Implicit methods with non-implicit parameters forms a Scala pattern called implicit conversion (old pattern)
    implicit def optionWriter[A]( implicit writer: JsonWriter[A]): JsonWriter[Option[A]] =
      new JsonWriter[Option[A]] {
        def write(option: Option[A]) :Json =
          option match {
            case Some(aValue) => writer.write(aValue) // this will search a recursive writer
            case None => JsNull
          }
      }
  }

  //Type class use --> Implicit Parameters, Interface Objects, Interface Syntax
  // Type class use
  object Json {
    def toJson[A](value: A)(implicit w: JsonWriter[A]): Json = w.write(value)
  }

  /*
  Implicit classes have the following restrictions:
    They must be defined inside of another trait/class/object.
   Interface Syntax: extension methods to extend existing types (type enrichment, pimping)
   */
  object JsonSyntax {
    implicit class JsonWriterOps[A](value: A) { //Optional Utilities
      def toJson(implicit w: JsonWriter[A]) :Json = w.write(value)
    }
  }

  import JsonWriterInstances._
  println(Json.toJson(Person("Dave","dave@example.com"))) // this will write the implicit we need

  import JsonSyntax._
  Person("Example","InterfaceSyntax@example.com").toJson
  Animal("Tati","Horacio").toJson // a Writer should be written too for Animal

  //Next Scala method shows if the implicit we want is in the scope
  println(implicitly[JsonWriter[Animal]])

  //To check on Scala how Implicit scope works
  println(Json.toJson(Option("A string")))

}
