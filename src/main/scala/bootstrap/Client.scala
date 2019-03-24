package bootstrap

import handler.{CloseHandler, CreateTabPaneHandler, MessageDecoder, MessageEncoder}
import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.{Channel, ChannelFuture, ChannelInitializer, ChannelOption}
import org.slf4j.LoggerFactory
import other.Slf4jSupport

/**
  * Created by linsixin on 2019/3/23.
  */
object Client extends App with Slf4jSupport{



  def bootstrap(host:String, port:Int): ChannelFuture = {

    val messageEncoder = new MessageEncoder
    val messageDecoder = new MessageDecoder
    val createTabPaneHandler = new CreateTabPaneHandler
    val closeHandler = new CloseHandler
    val workerGroup = new NioEventLoopGroup
//    try {
      val b = new Bootstrap
      b.group(workerGroup)
        .channel(classOf[NioSocketChannel])
        .option(ChannelOption.SO_KEEPALIVE, true: java.lang.Boolean)
        .handler(new ChannelInitializer[SocketChannel]() {
          @throws[Exception]
          override def initChannel(ch: SocketChannel): Unit = {
            ch.pipeline.addLast(messageEncoder, messageDecoder)
              .addLast(createTabPaneHandler)
              .addLast(closeHandler)
          }
        })
      val f = b.connect(host, port).sync() // (5)

      f.addListener(echoChannelFutureHandler(s"连接端口: $port"))

      f
      //      val sysInScanner = new Scanner(System.in)
      //      while(true) {
      //        val msg = sysInScanner.nextLine()
      //        logger.info("我说: " + msg)
      //        clientChannel.writeAndFlush(Message(msg,new Date))
      //      }
      //    f.channel.closeFuture.sync
//    }
//    finally {
//      workerGroup.shutdownGracefully().sync()
//    }
  }
}
