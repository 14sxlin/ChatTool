package gui

import entity.NonEmptyString
import handler.{AppendRecMsgToTextAreaHandler, AppendSendMsgToTextArea}
import io.netty.channel.Channel
import other.Slf4jSupport
import scalafx.scene.control.{Tab, TabPane}
import scalafx.scene.layout.Pane

/**
  * Created by linsixin on 2019/3/23.
  */
object GUIContext extends Slf4jSupport{

  val tabPane: TabPane = new TabPane() {
    tabs = Seq(
      tabFromPane(new ServerConfigPane(width + 0, height + 0)) { _.text = "主机" },
      tabFromPane(new ClientConfigPane(width + 0, height + 0)) { _.text = "连接" },
    )
  }

  def tabFromPane(pane: Pane)(apply: Tab => Unit): Tab = {
    val tab = new Tab {
      content = pane
      closable = false
    }
    apply(tab)
    tab
  }

  def createNewChatPane(title:NonEmptyString, channel:Channel): Unit = {
    val chatPane = new ChatPane(title.value, channel,tabPane.prefWidth + 0,tabPane.prefHeight + 0)
    val newPane = tabFromPane(chatPane){ _.text = title.value }
    channel.pipeline()
      .addLast(new AppendSendMsgToTextArea(chatPane.messageArea))
      .addLast(new AppendRecMsgToTextAreaHandler(chatPane.messageArea))
    runLater {
      tabPane.tabs.add(newPane)
    }
  }
}
