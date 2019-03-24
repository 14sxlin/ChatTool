name := "ChatTool"

version := "0.1"

scalaVersion := "2.12.8"

scalaHome := Option(file("E:\\program\\scala2.12.8"))

scalacOptions += "-Ypartial-unification"

updateOptions := updateOptions.value.withCachedResolution(true)

libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"

// https://mvnrepository.com/artifact/io.netty/netty-all
libraryDependencies += "io.netty" % "netty-all" % "4.1.34.Final"

// https://mvnrepository.com/artifact/org.json4s/json4s-jackson
libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.6.5"

// https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

// https://mvnrepository.com/artifact/org.scalafx/scalafx
libraryDependencies += "org.scalafx" %% "scalafx" % "11-R16"

libraryDependencies += "org.projectlombok" % "lombok" % "1.16.16"