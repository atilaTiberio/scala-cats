package mx.horace.cats

import cats.instances.string._
import cats.instances.int._
import cats.instances.list._
import cats.syntax.semigroup._

object Main extends App {
  var l= List(1)
  var l2= List(2)
  println("Hello " |+| "Cats!")
  println(2 |+| 1)
  println(l |+| l2)
}
