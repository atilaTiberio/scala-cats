package mx.horace.cats.ch02

import cats._
import cats.implicits._

/*import  cats.syntax.semigroup._
import cats.instances.string._
import cats.instances.int._
import cats.instances.option._
*/

object MonoidAndSemigroup extends App {
  /*
    Monoids and Semigroups are used for combining values
      1.- Type class
      2.- Instances
      3.- interface
   */
  println(Monoid[String].combine("a","b"))
  println("a" |+| "b")
  println(1 |+| 2 |+| Monoid[Int].empty)

  def add (items: List[Int]): Int =
    items.foldLeft(Monoid[Int].empty)( _ |+| _) // items.foldLeft(Monoid.apply[Int].empty)( _ |+| _)

  /*This is the way how to design generics with cats, that implicit should be in scope*/
  def addGeneric[A](items: List[A])(implicit monoid: Monoid[A]) : A =
    items.foldLeft(monoid.empty)( _ |+| _)

  def addContextBound[A: Monoid](items: List[A]): A =
    items.foldLeft(Monoid[A].empty)(_ |+| _)

  println(addGeneric(List(1,2,3)))
  println(addGeneric(List("1","2","3")))
  println(addContextBound(List(5,6,7)))
  case class Order(totalCost: Double, quantity: Double)

  implicit  val monoid: Monoid[Order] = new Monoid[Order] {
    def combine(o1: Order, o2: Order) : Order =
      Order (
        o1.totalCost + o2.totalCost,
        o1.quantity + o2.quantity
      )
    def empty = Order(0,0)
  }

  println(addContextBound(List(Order(1,2),Order(4,5))))



}
