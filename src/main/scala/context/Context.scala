package context

import java.util.concurrent.locks.ReentrantReadWriteLock

import cats.data.OptionT
import cats.implicits._
import io.netty.channel.Channel
import other.Slf4jSupport

import scala.util.Try

/**
  * Created by linsixin on 2019/3/23.
  */
object Context extends Slf4jSupport{

  type ThrowableEither[A] = Either[Throwable,A]

  private [this] val map = collection.mutable.HashMap[String,Channel]()
  private [this] val lock = new ReentrantReadWriteLock()
  private [this] val readLock = lock.readLock()
  private [this] val writeLock = lock.writeLock()
  private [this] var current = none[String]

  val noCurrentException = new IllegalArgumentException("当前没有连接的对象")

  def currentChannel: ThrowableEither[Option[Channel]] = {
    current.map { id =>
      Try{
        readLock.lock()
        val res = map.get(id)
        readLock.unlock()
        res
      }.toEither
    }.getOrElse(Left(noCurrentException))
  }

  def addChannel(id:String, channel:Channel): Either[Throwable,Unit] = {
    Try {
      writeLock.lock()
      map += id -> channel
      current = Some(id)
      writeLock.unlock()
    }.toEither
  }

  def getChannel(id:String): OptionT[ThrowableEither,Channel] = {
    OptionT(Try{
      readLock.lock()
      val res = map.get(id)
      readLock.unlock()
      res
    }.toEither)
  }

  def closeChannel(id:String): Either[Throwable, Unit] = {
    Try {
      writeLock.lock()
      map -= id
      writeLock.unlock()

    }.toEither
  }

}
