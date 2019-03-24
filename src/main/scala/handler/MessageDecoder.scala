package handler

import java.util

import entity.Message
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.util.CharsetUtil
import other.Slf4jSupport

import scala.collection.mutable.ArrayBuffer

/**
  * Created by linsixin on 2019/3/23.
  */
class MessageDecoder extends ByteToMessageDecoder with Slf4jSupport {

  import org.json4s._
  import org.json4s.jackson.Serialization.read
  implicit private val formats: DefaultFormats.type = org.json4s.DefaultFormats

  override def decode(channelHandlerContext: ChannelHandlerContext, byteBuf: ByteBuf, list: util.List[AnyRef]): Unit = {
    //todo 这个东西这样复制到 jvm 是不是有问题, 有没有更好的方法
    val byteBuffer = ArrayBuffer[Byte]()
    while(byteBuf.readableBytes() > 0){
      byteBuffer += byteBuf.readByte()
    }


    val json = new String(byteBuffer.toArray, CharsetUtil.UTF_8)
    list.add(read[Message](json))
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    logger.error(cause.getMessage,cause)
    ctx.close() // todo 这里直接close 是不是有问题
  }

}
