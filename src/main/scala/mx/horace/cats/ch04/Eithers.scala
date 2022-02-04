package mx.horace.cats.ch04

import cats.syntax.either._

object Eithers extends App {

  println("Either")

  /*
    Either another useful monad, comes in Scala 2.12
   */
  val either_uno: Either[String,Int] = Right(10)
  val either_dos: Either[String,Int] = Right(30)

  val either_uno_syntax = 10.asRight // It's better to use cats asRight
  val either_dos_syntax = 30.asRight

  /*
    Before Scala 2.12 there was a need to add calls to right on for-comprehension
    cats support it on scala 2.11

   */
  val result= for {
    a <- either_uno_syntax
    b <- either_dos_syntax
  } yield  a+b

  def countPositive(num: List[Int]) =
    num.foldLeft(0.asRight[String]) {
      (accumulator, num) =>
        if(num> 0 ){
          accumulator.map(_ + 1)
        } else {
          Left("Negative. Stopping!")
        }
    }

  println(countPositive(List(1,2,3,-4)))

  println(result)

  println(Either.catchOnly[NumberFormatException]("foo".toInt))
  println(Either.catchNonFatal(sys.error("Badness")))
  println(Either.fromTry(scala.util.Try("foo".toInt)))
  println(Either.fromOption[String,Int](None,"Badness"))

  println("Error".asLeft[Int].getOrElse(0)) //Plain value
  println("Error".asLeft[Int].orElse(2.asRight[String])) // Either value
  //ensure

  println((-1).asRight[String].ensure("Must be non-negative")(_ > 0))

  //for futures
  "error".asLeft[Int].recover {
    case _ : String => -1
  }

  "error".asLeft[Int].recoverWith{
    case _ : String => Right(-1)
  }

  println("foo".asLeft[Int].leftMap(_.reverse))
  println("rab".asLeft[Int].bimap(_.reverse, _ * 7 ))
  println(123.asRight[String].swap)
  /*
    toOption, toTry, toValidated..
   */



}
