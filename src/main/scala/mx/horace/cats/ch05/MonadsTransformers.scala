package mx.horace.cats.ch05

import cats.data.OptionT
import cats.data.Writer
import cats.syntax.applicative._
import cats.instances.either._


object MonadsTransformers extends App {

  // Alias Either to a type constructor with one parameter:
  type ErrorOr[A] = Either[String, A]

  // Build our final monad stack using OptionT
  type ErrorOrOption[A] = OptionT[ErrorOr, A]

  val a = 10.pure[ErrorOrOption]
  val b = 32.pure[ErrorOrOption]
  val c = a.flatMap( x => b.map(y => x+y))

  println(c.value) //unpacking

  type Logged[A] = Writer[List[String],A]

  /*
    Pattern: Methods generally  return untransformed stacks
   */
  def parseNumber(str: String): Logged[Option[Int]] =
    util.Try(str.toInt).toOption match {
      case Some(num) => Writer(List(s"Read $str"),Some(num))
      case None => Writer(List(s"Failed on $str"),None)
    }

  /*
    Consumers use monad transformers locally to simplify composition
   */
  def addAll(a: String, b:String, c:String): Logged[Option[Int]] = {
    import cats.data.OptionT
    import cats.implicits._

    val result = for {
      a <- OptionT(parseNumber(a))
      b <- OptionT(parseNumber(b))
      c <- OptionT(parseNumber(c))
    } yield a+b+c

    result.value
  }

  println(addAll("1","c","3"))
  println(addAll("1","2","b"))

}