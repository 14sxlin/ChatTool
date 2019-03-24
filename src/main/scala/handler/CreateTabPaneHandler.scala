package handler

import entity.NonEmptyString
import gui.GUIContext
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import other.Slf4jSupport

/**
  * Created by linsixin on 2019/3/23.
  */
@Sharable
class CreateTabPaneHandler extends ChannelInboundHandlerAdapter with Slf4jSupport{

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    logger.info("创建新的tab")
    GUIContext.createNewChatPane(NonEmptyString.optionOrElse(ctx.channel().id().toString,"unknown"),ctx.channel())
    ctx.fireChannelActive()
  }

}
