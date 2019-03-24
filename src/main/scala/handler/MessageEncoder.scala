package handler

import entity.Message
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.slf4j.LoggerFactory

/**
  * Created by linsixin on 2019/3/23.
  */
@Sharable
class MessageEncoder extends MessageToByteEncoder[Message] {

  val logger = LoggerFactory.getLogger(getClass)

  import org.json4s._
  import org.json4s.jackson.Serialization
  implicit private val formats: DefaultFormats.type = org.json4s.DefaultFormats

  override def encode(channelHandlerContext: ChannelHandlerContext, msg: Message, byteBuf: ByteBuf): Unit = {
    val message = Serialization.write(msg)
    logger.info("传输消息: " + message)
    message.getBytes.foreach(byteBuf.writeByte(_))
  }

}
