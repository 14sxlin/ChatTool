import gui.GUIContext
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene

/**
  * Created by linsixin on 2019/3/23.
  */
object MainGUI extends JFXApp {

  stage = new PrimaryStage {
    title.value = "局域网聊天室"
    width = 600
    height = 450
    minWidth = 300
    minHeight = 300
    scene = new Scene {
      content = GUIContext.tabPane
      GUIContext.tabPane.prefWidth <== width
      GUIContext.tabPane.prefHeight <== height
    }
  }


}
