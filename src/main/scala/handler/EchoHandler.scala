package handler

import entity.Message
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import other.Slf4jSupport

/**
  * Created by linsixin on 2019/3/23.
  */
@Sharable
class EchoHandler extends ChannelInboundHandlerAdapter with Slf4jSupport{
  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    msg match {
      case message : Message =>
        logger.info("收到消息: " + message)
        ctx.fireChannelRead(msg)
      case _ =>
    }
  }
}
