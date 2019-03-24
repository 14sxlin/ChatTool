package entity

import cats.implicits._
/**
  * Created by linsixin on 2019/3/23.
  */
case class NonEmptyString private (value:String)

object NonEmptyString {
  def fromString(s:String): Either[IllegalArgumentException, NonEmptyString] = s match {
    case null | "" => Left(new IllegalArgumentException("不允许空值"))
    case _ => Right(NonEmptyString(s))
  }

  def option(s:String): Option[NonEmptyString] = fromString(s).toOption

  def optionOrElse(s:String, default:String): NonEmptyString = {
    (fromString(s).toOption <+> option(default)).getOrElse(NonEmptyString("default should not be empty"))
  }
}


