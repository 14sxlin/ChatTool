package gui

import cats.data.Chain
import entity.{Message, NonEmptyString}
import io.netty.channel.{Channel, ChannelFuture}
import other.Slf4jSupport
import scalafx.beans.binding.NumberBinding
import scalafx.scene.control.{Button, Label, TextArea, TextField}
import scalafx.scene.layout.{BorderPane, FlowPane}
import cats.implicits._
import scalafx.scene.input.KeyCode

/**
  * Created by linsixin on 2019/3/23.
  */
class ChatPane(title: String,
               channel: Channel,
               _width: NumberBinding,
               _height: NumberBinding,
               _hgap: Int = 10,
               _vgap: Int = 10) extends BorderPane with Slf4jSupport {
  prefWidth <== _width
  prefHeight <== _height

  private val inputTextWidth = (_width - _hgap) * 2 / 3
  private val sendButtonWidth = _width - _hgap - 1 - inputTextWidth

  val inputTextField: TextField = new TextField() {
    prefWidth <== inputTextWidth
  }
  val messageArea = new TextArea()
  val infoLabel = new Label("用户信息: " + title)
  val userInfo: FlowPane = new FlowPane() {
    children = Seq(
      infoLabel
    )
  }
  private val sendButton = new Button("发送") {
    prefWidth <== sendButtonWidth
  }


  top = new FlowPane {
    children = Seq(
      inputTextField, sendButton
    )
    vgap = _vgap
    hgap = _hgap
  }
  center = messageArea
  bottom = userInfo


  private def sendMessage = {
    val channelFutureWriter = for {
      content <- eitherToWriter(NonEmptyString.fromString(inputTextField.text.value))
      future <- channel.writeAndFlush(Message(content.get.value)).writer(Chain("发送消息成功"))
    } yield future
    val (chain, future) = channelFutureWriter.run
    logger.info(chain.toString)
  }

  inputTextField.onKeyPressed = keyEvent => {
    if(keyEvent.getCode.name() == KeyCode.Enter.name) {
      sendMessage
    }
  }
  sendButton.onAction = _ => {
    sendMessage
  }

}
