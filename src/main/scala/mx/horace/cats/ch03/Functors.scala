package mx.horace.cats.ch03
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import cats.instances.function._ // for functor
import cats.syntax.functor._ // for map
import cats.Functor
import cats.instances.list._
import cats.instances.option._


object Functors extends App {

  val future: Future[String] = Future(123)
    .map(n => n+1)
    .map(n => n*2)
    .map(n => s"${n}!")

  println(Await.result(future,1.second)) // Cats Effect, IO helps to solve issues with Futures

  //Functions as functors -> X =>A, A => B: X => B
  val func1 : Int => Double =
    (x:Int) => x.toDouble

  val func2: Double => Double = (y: Double) => y * 2

  /* Function composition( this is sequencing) using map
    if we can pass an argument to the final function all of the operations are run in sequence.
  */

  println((func1 map func2)(1))
  println((func1 andThen  func2)(1)) // Function composition using andThen
  println(func2(func1(1))) // Function composition written out by hand This is compliant with Functor Law, Identity law and compositon law

  val func = ((x: Int ) => x.toDouble)
    .map( x => x+1)
    .map( x => x*2)
    .map( x => s"${x}!")
  println(s"Functors ${func(123)}")

  /*
    FUNCTOOOORS
   */
  val list1 = List(1,2,3)
  val list2 = Functor[List].map(list1)(_ * 2)
  println(list2)

  val option1 = Option(123)
  val option2 = Functor[Option].map(option1)(_.toString)
  println(option2)

  //Lift method converts A => B to one that operates over a functor and has type F[A] => F[B]
  val func_example= (x: Int) => x+1
  val liftedFunc = Functor[Option].lift(func_example)
  println(liftedFunc(Option(1)))
  println(Functor[List].as(list1,"As"))

  //Functors Syntax
  val func1Syntax = (a: Int) => a + 1
  val func2Syntax = (a: Int) => a * 2
  val func3Syntax = (a: Int) => s"${a}!"
  val func4Syntax = func1Syntax.map(func2Syntax).map(func3Syntax)
  println(func4Syntax(123))

  //Functor that applies a equation

  def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]): F[Int] =
    start.map(n => n+1*2)

  println(doMath(Option(20)))
  println(doMath(Option(20)))
  println(doMath(List(1,2,3)))
  println(List(1,2,3).as(20))

  //Custom types
  implicit val optionFunctor: Functor[Option] = {
    new Functor[Option] {
      def map[A,B](value: Option[A])(func: A =>B): Option[B] =
        value.map(func)
    }

  }
  val t= Option(20)
  println(Functor[Option].map(t)(n=> n*10))


}
