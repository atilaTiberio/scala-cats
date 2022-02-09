package mx.horace.cats.ch04

import cats.data.{Reader, Writer}
import cats.instances.vector._
import cats.syntax.applicative._
import cats.syntax.writer._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object CatsDataTypes extends App {


  type Logged[A] = Writer[Vector[String], A]

  def slowly[A](body: => A) =
    try body finally Thread.sleep(100)

  def factorial(n: Int): Logged[Int] =
    for {
      ans <- if(n==0) {
          1.pure[Logged]
      } else {
        slowly(factorial(n-1).map(_ * n))
      }
      _ <- Vector(s"fact $n $ans").tell
    } yield ans

  val (log,res) = factorial(5).run
  println(s"$log --> $res")

 val r= Await.result(Future.sequence(Vector(
    Future(factorial(5)),
    Future(factorial(6))
  )).map(_.map(_.written)),5.seconds)

  /*
    Readers
   */
  final case class  Cat(name: String, favoriteFood: String)

  val catName: Reader[Cat, String] = Reader(cat => cat.name)

  val greetKitty: Reader[Cat, String] = catName.map(name => s"Hello ${name}")
  println(greetKitty.run(Cat("Garfield","lasagna")))
  val feedKitty: Reader[Cat, String] = Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")

  val greetAndFeed: Reader[Cat, String] =
    for {
      greet <- greetKitty
      feed <- feedKitty
    } yield s"$greet $feed"

  println(greetAndFeed(Cat("Mikasa","croquetas de perro ")))



}