package mx.horace.cats.ch01
import cats._
import cats.syntax.show._
import cats.instances.int._
import cats.instances.string._
import cats.instances.option._
import cats.syntax.eq._


object TypeClassessExercise extends App {

  final case class Cat(name:String, age:Int, color:String)
  val numero= 20
  val cadena= "Cadena"
  val gato = new Cat ("Mikasa",3,"blanco")
  val gato_v2 = new Cat ("Mikasa",3,"blanco")

  /*
    toString Exercise
    type class
   */
  trait Printable[A] {
    def format(value: A) : String
  }

  /*
  Type instance
   */
  object PrintableInstances {

    implicit val stringPrintable: Printable[String] =
      new Printable[String] {
        def format(value: String ): String ={
          value
        }
      }

    implicit  val intPrintable: Printable[Int] = {
      new Printable[Int] {
        def format(value: Int): String = {
          value.toString
        }
      }
    }

    implicit val catPrintable: Printable[Cat] = {
      new Printable[Cat] {
        def format(value: Cat): String = {
          s"${value.name} is ${value.age} year-old ${value.color} cat"
        }
      }
    }

    implicit val catPrintableCats : Show[Cat] =
      Show.show(value => s"${value.name} is ${value.age} year-old ${value.color} cat (Using Scala cats Show) ")

  }

  /*
  Type instance use
   */
  object Printable {
    def format[A](value: A)(implicit f: Printable[A]) : String = f.format(value)
    def print[A](value:A)(implicit f: Printable[A]) : Unit = println(f.format(value)) //could this be a helper method?
  }
  import PrintableInstances._
  Printable.print(numero)
  Printable.print(cadena)
  Printable.print(gato)

  object PrintableSyntax {
    implicit class PrintableOps[A](value: A){
      def format(implicit f: Printable[A]) : String = f.format(value)
      def print(implicit f: Printable[A]) : Unit = println(f.format(value))
    }
  }
  println("Lo mismo pero con Option Utilities")
  import PrintableSyntax._
  numero.print
  cadena.print
  gato.print

  println(gato.show)
  println(123 === 123)
  implicit val catEq: Eq[Cat] = Eq.instance[Cat] {
    (cat1,cat2) => cat1.name === cat2.name && cat1.color === cat2.color && cat1.age === cat2.age
  }
  println(gato === gato_v2)

  val optionCat = Option(gato)
  val optionCat_v2 = Option(gato_v2)
  println(optionCat =!= optionCat_v2)

}
