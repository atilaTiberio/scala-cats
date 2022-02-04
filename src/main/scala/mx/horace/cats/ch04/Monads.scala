package mx.horace.cats.ch04



object Monads extends App {

  /*

   */
  def parseInt(str: String): Option[Int] =
    scala.util.Try(str.toInt).toOption

  def divide(a: Int, b: Int): Option[Int] =
    if(b==0) None else Some(a/b)

  def stringDivideBy(aStr: String, bStr: String): Option[Int] =
    parseInt(aStr).flatMap {
      aNum => parseInt(bStr).flatMap {
        bNum => divide(aNum,bNum)
      }
    }

  def stringDivideByForComprehension(aStr: String, bStr: String): Option[Int] =
  for {
    aNum <- parseInt(aStr)
    bNum <- parseInt(bStr)
    ans  <- divide(aNum, bNum)
  } yield ans

  println(stringDivideBy("20","2"))

}
