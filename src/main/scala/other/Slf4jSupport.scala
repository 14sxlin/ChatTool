package other

import java.text.SimpleDateFormat

import cats.data.{Chain, Writer}
import cats.implicits.{none, _}
import io.netty.channel.ChannelFuture
import io.netty.util.concurrent.GenericFutureListener
import org.slf4j.{Logger, LoggerFactory}

import scala.util.{Failure, Success, Try}
/**
  * Created by linsixin on 2019/3/23.
  */
trait Slf4jSupport {
  protected val logger: Logger = LoggerFactory.getLogger(getClass)

  val simpleDateFormat = new SimpleDateFormat("HH:mm:ss")

  def logException(e : Throwable): Unit = logger.error(e.getMessage,e)

  def eitherToWriter[T](either:Either[Throwable,T]): Writer[Chain[String], Option[T]] = {
    either match {
      case Left(e) =>
        logException(e)
        none[T].writer(Chain(e.getMessage))
      case Right(value) => value.some.writer(Chain())
    }
  }

  def tryToWriter[T](tryDo :Try[T], successMsg:String): Writer[Chain[String], Option[T]] = {
    tryDo match {
      case Success(value) =>
        value.some.writer(Chain(successMsg))
      case Failure(e) =>
        none[T].writer(Chain(e.getMessage))
    }
  }

  def logNonEmpty(chain: Chain[String]): Unit = {
    if(chain != null && chain.nonEmpty) {
      logger.error(chain.toString())
    }
  }

  val successChannelFutureHandler :  (ChannelFuture => Unit) => GenericFutureListener[ChannelFuture] = successDo => (f : ChannelFuture) => {
    if(f.isSuccess) {
      successDo(f)
    } else {
      val cause = f.cause()
      logger.error(cause.getMessage,cause)
    }
  }

  val echoChannelFutureHandler: String => GenericFutureListener[ChannelFuture] = (msg:String) => (f:ChannelFuture) => {
    if(f.isSuccess) {
      logger.info(msg + " 成功")
    }else {
      val cause = f.cause()
      logger.error(cause.getMessage,cause)
    }
  }

}
