package bootstrap

import handler._
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{ChannelFuture, ChannelInitializer, ChannelOption}
import other.Slf4jSupport

/**
  * Created by linsixin on 2019/3/23.
  */
object Server extends App with Slf4jSupport {

  def bootstrap(port:Int) = {

    val eventGroup = new NioEventLoopGroup()
    val messageEncoder = new MessageEncoder
    val messageDecoder = new MessageDecoder
    val echoHandler = new EchoHandler
    val closeHandler = new CloseHandler
    val createTabPaneHandler = new CreateTabPaneHandler

    try {
      val boot = new ServerBootstrap()
      boot.group(eventGroup)
        .channel(classOf[NioServerSocketChannel])
        .childHandler(new ChannelInitializer[SocketChannel] {
          override def initChannel(c: SocketChannel): Unit = {
            c.pipeline()
              .addLast(messageEncoder,messageDecoder)
              .addLast(createTabPaneHandler)
              .addLast(echoHandler)
              .addLast(closeHandler)
          }
        }).option(ChannelOption.SO_BACKLOG, 128 : java.lang.Integer)
        .childOption(ChannelOption.SO_KEEPALIVE, true : java.lang.Boolean)

      val bindFuture = boot.bind(port).sync()
      bindFuture.addListener((f : ChannelFuture) => {
        if(f.isSuccess){
          logger.info("绑定端口成功")
        }
      })
      bindFuture.channel()
      bindFuture.channel().closeFuture().sync() //阻塞 防止退出
    } finally {
      eventGroup.shutdownGracefully()
    }
  }

}
