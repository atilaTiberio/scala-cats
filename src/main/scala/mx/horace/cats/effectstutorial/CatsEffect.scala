package mx.horace.cats.effectstutorial

import cats.effect.{IO,IOApp}

object CatsEffect extends IOApp.Simple {

  val run = IO.print("Hello, ").flatMap {_ =>
    IO.println("World ")
  }



}
