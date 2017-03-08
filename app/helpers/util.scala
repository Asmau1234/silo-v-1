package helpers

import reactivemongo.bson._
import java.util.UUID

import scala.util.Random

object util {
  def random = Random.nextLong()
  def random2 = Random.nextLong()

  def generateId: Long = scala.math.abs( System.currentTimeMillis() + random.abs * random2.abs);

  def generateStringId : String  = {
    val random = java.util.UUID.randomUUID().toString
    random.split("-").foldLeft("p")(_.concat(_))
  }

  def generateLongId : Long = {
    import scala.util.Random
    math.abs(Random.nextLong() * Random.nextLong())
  }
}


object Implicits {
  implicit  def int2BSONInteger(i:Int): BSONInteger = BSONInteger(i)
}