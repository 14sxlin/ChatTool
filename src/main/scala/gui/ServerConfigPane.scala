package gui

import java.util.concurrent.Executors

import cats.implicits._
import bootstrap.Server
import cats.data.Chain
import other.Slf4jSupport
import scalafx.beans.binding.NumberBinding
import scalafx.scene.control.{Button, Label, TextField, ToggleButton}

import scala.util.Try

/**
  * Created by linsixin on 2019/3/23.
  */
class ServerConfigPane(_width: NumberBinding, _height: NumberBinding) extends CommonFlowPane(_width, _height)
 with Slf4jSupport {

  val port = new TextField()
  val portLabel: Label = centerAlignLabel("端口")
  val connectBtn = new Button("开启服务器")
  val result: Label = new Label("连接结果")

  leftNodes += portLabel
  rightNodes += port

  bottomNodes += connectBtn
  bottomNodes += result

  init()

  connectBtn.onMouseClicked = _ => {
    Executors.newSingleThreadExecutor().submit(new Runnable {
      override def run(): Unit = {
        logger.info("开始绑定服务器端口..")
        val channelFuture = for {
          portNumOp <- tryToWriter(Try { port.text.value.toInt }, "端口号解析成功")
          port <- portNumOp.map(p => p.writer(Chain("端口号: "))).get
          bindFuture <- Server.bootstrap(port).writer(Chain("启动服务器"))
        } yield bindFuture
        val (chain, bindFuture) = channelFuture.run
        bindFuture.addListener(echoChannelFutureHandler("绑定服务器"))
        logNonEmpty(chain) //todo 在线程外的话, 会阻塞的, 而且会报错 MessageEncoder不能是 Sharable, 为什么里面不会报错
      }
    })
  }

}
