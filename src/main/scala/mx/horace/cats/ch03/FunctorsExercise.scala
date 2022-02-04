package mx.horace.cats.ch03

import cats.Functor
//import cats.instances.function._ // for functor
import cats.syntax.functor._ // for map

object FunctorsExercise extends App {


  sealed trait Tree[+A]
  final case class Branch[A](left: Tree[A],right: Tree[A]) extends Tree[A]
  final case class Leaf[A](value: A) extends Tree[A]

  implicit val treeFunctor: Functor[Tree] = {
    new Functor[Tree] {
      def map[A, B](tree: Tree[A])(func: A => B): Tree[B] =
        tree match {
          case Branch(left,right) => Branch(map(left)(func),map(right)(func)) // This needs recursion
          case Leaf(value) => Leaf(func(value))
        }
    }
  }
  // Branch(Leaf(10),Leaf(20)).map() This is no working because the implicit is for tree no for the branch
  println(Functor[Tree].map(Branch(Leaf(10),Leaf(20)))(_ *2)) // this works
  object Tree {
    def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left,right)
    def leaf[A](value: A): Tree[A] = Leaf(value)
  }
  println(Tree.leaf(100).map(_ * 2))
  println(Tree.branch(Tree.leaf(100),Tree.leaf(100)).map(_ * 2))
  println(Tree.branch(Tree.branch(Tree.leaf(100),Tree.leaf(200)),Tree.branch(Tree.leaf(300),Tree.leaf(400))).map(_ * 3))

  // Contramap function F[B] => F[A]
  trait Printable[A] { self =>
    def format(value: A): String

    def contramap[B](func: B => A): Printable[B] =
      new Printable[B] {
        def format(value: B): String =
          self.format(func(value))
      }
  }
  def format[A](value: A)(implicit p:Printable[A]): String = p.format(value)
  final case class Box[A](value: A)
  implicit val stringPrintable: Printable[String] =
    new Printable[String] {
      def format(value: String): String =
        s"'${value}'"
    }

  implicit val booleanPrintable: Printable[Boolean] =
    new Printable[Boolean] {
      def format(value: Boolean): String =
        if(value) "yes" else "no"
    }

  implicit val intPrintable: Printable[Int] =
    new Printable[Int] {
      def format(value: Int): String = value.toString
    }

  /*
  implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] =
    new Printable[Box[A]] {
      def format(box: Box[A]) : String =
        p.format(box.value)
    }
   */

  implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] = p.contramap[Box[A]](_.value)

  println(format("Hello"))
  println(format(Box("20")))
  println(format(Box(true)))
  println(format(Box(20)))

  // Invariant functors imap informally equivalent to a combination of map and contramap
  trait Codec[A] { self =>
    def encode(value: A): String
    def decode(value: String): A

    def imap[B](dec: A =>B, enc: B => A): Codec[B] = {
      new Codec[B] {
        def encode(value: B): String = self.encode(enc(value))
        def decode(value: String ): B = dec(self.decode(value))
      }
    }

  }

  def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)
  def decode[A](value: String)(implicit c: Codec[A]): A = c.decode(value)

  implicit val stringCodec: Codec[String] =
    new Codec[String] {
      def encode(value: String): String = s"'${value}'"
      def decode(value: String): String = value
    }

  implicit val intCodec: Codec[Int] =
    stringCodec.imap(_.toInt, _.toString)

  implicit val booleanCodec: Codec[Boolean] =
    stringCodec.imap(_.toBoolean, _.toString)

  implicit val doubleCodec: Codec[Double] =
    stringCodec.imap(_.toDouble, _.toString)

  implicit def boxCodec[A](implicit c: Codec[A]): Codec[Box[A]] = c.imap[Box[A]](Box(_), _.value)

  println("\tCodec")
  println(encode(123))
  println(encode(123.4))
  println("\tDecode")
  println(decode[Double]("123"))
  println(encode(Box(123.4)))
  println(decode[Box[Double]]("123.4"))

  /*
    Contravariant: F[A], B => A, F[B]
    Invariant: F[A] to F[B] via A => B and viceversa B => A
   */

}
