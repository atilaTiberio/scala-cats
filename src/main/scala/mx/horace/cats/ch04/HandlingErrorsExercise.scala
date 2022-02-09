package mx.horace.cats.ch04

import cats.syntax.either._
import cats.{Eval}

object HandlingErrorsExercise extends App {

  /*
    Errors may occur in our program
   */

  object wrapper {
    sealed  trait LoginError extends Product with Serializable

    final case class UserNotFound(username: String) extends LoginError

    final case class PasswordIncorrect(username: String) extends LoginError

    case object UnexpectedError extends LoginError
  }; import wrapper._

  case class User(username: String, password: String)

  type LoginResult = Either[LoginError, User] // Alias

  def handleError( error: LoginError): Unit =
    error match {
      case UserNotFound(u) => println(s"User not found : $u")
      case PasswordIncorrect(u) => println(s"Password incorrect: $u")
      case UnexpectedError => println("Unexpected error") //this is why case object UnexpectedError
    }

  val validUser: LoginResult = User("hiturbe","igarcia").asRight
  val userNotFound: LoginResult = UserNotFound("hiturbe").asLeft

  validUser.fold(handleError,println)
  userNotFound.fold(handleError,println)

  val ans = for {
     a <- Eval.now { println("Calculating A"); 40 }
     b <- Eval.always { println("Calculating B"); 2}
  } yield {
      println("Adding A and B")
    a + b
  }
  ans.value
}
