package handler

import entity.Message
import io.netty.channel.{ChannelHandlerContext, ChannelOutboundHandlerAdapter, ChannelPromise}
import other.Slf4jSupport
import scalafx.scene.control.TextArea

/**
  * Created by linsixin on 2019/3/23.
  */
class AppendSendMsgToTextArea(textArea: TextArea) extends ChannelOutboundHandlerAdapter with Slf4jSupport {
  override def write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise): Unit = {
    msg match {
      case Message(content,time) =>
        textArea.text = textArea.text.value + ("我说: " + content + " \r\n ---" + simpleDateFormat.format(time) + "\r\n" ) //todo 这里是不是有更好的处理方式?
      case _ =>
        logger.info("unknown msg: " + msg.toString)
    }
    super.write(ctx, msg, promise)
  }
}
