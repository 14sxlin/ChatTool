package handler

import entity.Message
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import other.Slf4jSupport
import scalafx.scene.control.TextArea

/**
  * Created by linsixin on 2019/3/23.
  */
class AppendRecMsgToTextAreaHandler(textArea:TextArea) extends ChannelInboundHandlerAdapter with Slf4jSupport{

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    msg match {
      case Message(content,time) =>
        textArea.text = textArea.text.value + ("对方说: " + content + " \r\n ---" + simpleDateFormat.format(time) + "\r\n" ) //todo 这里是不是有更好的处理方式?
      case _ =>
        logger.info("unknown msg: " + msg.toString)
    }
//    super.channelRead(ctx, msg)
  }

}
