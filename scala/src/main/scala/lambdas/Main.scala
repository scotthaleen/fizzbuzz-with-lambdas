package lambdas

object Main extends App {

  val limit:Int = args.headOption.orElse(Some("30")).get.toInt
  val emptyString:Option[String] = None

  def isaFizzBuzz(p:(Int) => Boolean, sz:String) =
    (i:Int) =>
      if (p(i)) Some(sz)
      else None

  def isFizz = isaFizzBuzz((x) => x % 3 == 0, "Fizz")
  def isBuzz = isaFizzBuzz((x) => x % 5 == 0, "Buzz")

  val conditions = List(isFizz, isBuzz)

  def combine(a:Option[String], b:Option[String]):Option[String] =
    (a,b) match {
      case (Some(s1), Some(s2)) => Some(s1 + s2)
      case (Some(_), None) => a
      case (None, Some(_)) => b
      case (_,_) => None
    }

  Stream.from(1)
    .take(limit)
    .map((i) =>
        conditions.foldLeft(emptyString) {
          (xs, fn) => combine(xs, fn(i))
        }.orElse(Some(i.toString)).get)
    .foreach(println)

}
