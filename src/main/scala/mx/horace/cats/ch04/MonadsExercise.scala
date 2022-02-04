package mx.horace.cats.ch04

import cats.Monad
import cats.instances.option._
import cats.instances.list._
import cats.instances.vector._
import cats.instances.future._
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import cats.syntax.applicative._
import cats.syntax.functor._
import cats.syntax.flatMap._
import cats.syntax.either._
import cats.Id

object MonadsExercise extends App {

  case class Box(name: String, size: Integer)
  /*
  trait Monad[F[_]] {
    def pure[A](value:A): F[A]

    def flatMap[A,B](value: F[A])(func: A=> F[B]) : F[B]

    def map[A,B](value:F[A])(func: A=>B): F[B] =
      flatMap(value)(a => pure(func(a)))
  }

   */

  val opt1 = Monad[Option].pure(3)
  val opt2 = Monad[Option]
    .flatMap(opt1)(a =>Some(a + 2))
  val opt3 = Monad[Option].map(opt2)(a => 100 * a)

  println(opt1)
  println(opt2)
  println(opt3)
  println("Listas")

  val list1 = Monad[List].pure(3)
  //a map call will return List(List(1, 10), List(2, 20), List(3, 30))
  val list2 = Monad[List].flatMap(List(1,2,3))(a => List(a, a * 10))
  val list3 = Monad[List].map(list2)(a => a + 123)

  println(list2)
  println(list3)

  // Also Monad provides many other methods, including all of the methods from Functor

  val vector = Monad[Vector].flatMap(Vector(1,2,3))(a => Vector(a,a * 10))
  println(vector)

  val fm = Monad[Future]

  val future = fm.flatMap(fm.pure(1))(x => fm.pure(x + 1))

  println(Await.result(future, 1.second))

  val optionPure = 1.pure[Option]
  val listPure = 1.pure[List]

  /*
    Option and List define their own explicit version of flatMap and map
   */

  println("FlatMap custom")
  def sumSquare[ F[_]: Monad](a: F[Int], b: F[Int]) : F[Int] =
    a.flatMap( x => b.map(y => x*x + y*y))

  println(sumSquare(Option(3),Option(4)))
  println(sumSquare(List(1,2,3), List(4,5)))

  /* Compiler will "do the right thing" by rewriting our comprehension in terms of flatMap and map
     and inserting the correct implicit conversions to user our monad
   */
  println("FlatMap custom for comprehension")
  def sumSquareComprehension[F[_]: Monad] (a: F[Int], b: F[Int]) =
    for {
      x <- a
      y <- b
    } yield x*x + y*y

  println(sumSquareComprehension(List(1,2,3), List(4,5)))
  /*
    This won't work with ints so that cats offers Id type
   */
  println(sumSquareComprehension(3: Id[Int], 4: Id[Int]))
  val intId: Id[Int]= 20

  /*
  Monadic Secret Identities
   */
  def pure[A](value: A): Id[A] = value

  def map[A, B](initial: Id[A])(func: A => B): Id[B] = func(initial)

  def flatMap[A, B](initial: Id[A])(func: A => Id[B]) = func(initial)

  println(flatMap(123)(_ * 2))
  println("Either")

  /*
    Either another useful monad, comes in Scala 2.12
   */
  val either_uno: Either[String,Int] = Right(10)
  val either_dos: Either[String,Int] = Right(30)

  val either_uno_syntax = 10.asRight // It's better to use cats asRight
  val either_dos_syntax = 30.asRight

  val result= for {
    a <- either_uno_syntax
    b <- either_dos
  } yield  a+b

  println(result)

  println(Either.catchOnly[NumberFormatException]("foo".toInt))
  println(Either.catchNonFatal(sys.error("Badness")))
  println(Either.fromTry(scala.util.Try("foo".toInt)))


}
