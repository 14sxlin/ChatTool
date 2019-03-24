package handler

import entity.Message
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}

/**
  * Created by linsixin on 2019/3/23.
  */
@Sharable
class CloseHandler extends ChannelInboundHandlerAdapter {

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    msg match {
      case Message(Message.CLOSE_CONTENT,_) => ctx.close()
      case _ => super.channelRead(ctx,msg)
    }
  }
}
