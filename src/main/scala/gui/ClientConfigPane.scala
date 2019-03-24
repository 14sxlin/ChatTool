package gui

import bootstrap.Client
import cats._
import cats.data.Chain
import cats.implicits._
import context.Context
import entity.{Message, NonEmptyString}
import io.netty.channel.ChannelFuture
import other.Slf4jSupport
import scalafx.beans.binding.NumberBinding
import scalafx.scene.control.{Button, Label, TextField}

import scala.util.Try

/**
  * Created by linsixin on 2019/3/23.
  */
class ClientConfigPane(_width: NumberBinding, _height: NumberBinding) extends CommonFlowPane(_width, _height)
  with Slf4jSupport {

  val ip = new TextField()
  val ipLabel: Label = centerAlignLabel("IP:")
  val port = new TextField()
  val portLabel: Label = centerAlignLabel("Port:")
  val connectBtn = new Button("连接")
  val result = new Label("连接结果")

  leftNodes += ipLabel
  leftNodes += portLabel
  rightNodes += ip
  rightNodes += port
  bottomNodes += connectBtn
  bottomNodes += result

  init()

  connectBtn.onMouseClicked = _ => {
    val channelFutureWriter = for {
      hostOp <- eitherToWriter[NonEmptyString](NonEmptyString.fromString(ip.text.value))
//      host <- hostOp.get.writer(Chain("主机号: " + hostOp))
      portNumOp <- tryToWriter(Try { port.text.value.toInt }, "端口号解析成功" )
//      portNum <- portNumOp.get.writer(Chain("使用端口号: " + portNumOp.get))
      channelFutureOp <- tryToWriter(Try { Client.bootstrap(hostOp.get.value, portNumOp.get) }, "客户端引导成功" )
//      channel <- channelFutureOp.get.writer(Chain.empty)
    } yield channelFutureOp

    val (chain, channelFutureOp) = channelFutureWriter.run
    channelFutureOp.foreach { channelFuture =>
      if(channelFuture.isSuccess) {
        Context.addChannel(s"${ip.text.value}:${port.text.value}", channelFuture.channel())
        channelFuture.channel().writeAndFlush(Message("连接成功"))
      } else {
        val cs = channelFuture.cause()
        logger.error("连接失败, "  + cs.getMessage,cs)
      }
    }
    logNonEmpty(chain)

  }
}
