package mx.horace.cats.ch03

import cats.Contravariant
import cats.Show
import cats.instances.string._

object FunctorsInCats extends App {

// This codes the use of Contravariant and Invariant in Cats

  val showString = Show[String]
  //showSymbol won't be needed using syntax
  val showSymbol = Contravariant[Show].contramap(showString)( (sym: Symbol) => s"'${sym.name}'")

  println(showSymbol.show(Symbol("dave")))
  import cats.syntax.contravariant._
  showString
    .contramap[Symbol](sym => s"'${sym.name}'")
    .show(Symbol("Contravariant with Syntax"))

  /*
    Invariant
   */
  import cats.Monoid
  import cats.instances.string._ // for Monoid
  import cats.syntax.invariant._ // for imap
  import cats.syntax.semigroup._ // for |+|
  implicit val symbolMonoid: Monoid[Symbol] = Monoid[String].imap(Symbol.apply)(_.name)
  Monoid[Symbol].empty
  Symbol("a") |+| Symbol("few") |+| Symbol("words")

  val either: Either[String,Int] = Right(123)
  println(either)
}
