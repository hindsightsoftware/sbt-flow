package com.hindsightsoftware.sbt.flowtype

import sbt._
import sbt.Process
import com.typesafe.sbt.web.SbtWeb
import sbt.Keys._

object Import {

  val flow = TaskKey[Unit]("flow", "Performs type checking on JavaScript code using Flow.")
  val check = TaskKey[Unit]("check", "Performs type checking on JavaScript code using Flow.")
  val init = TaskKey[Unit]("init", "Initializes the 'sourceDirectory' to be used as a flow root directory")
  val start = TaskKey[Unit]("start", "Start the Flow server for incremental typechecking")
  val status = TaskKey[Unit]("status", "Typecheck recent changes made to JavaScript files")
  val stop = TaskKey[Unit]("stop", "Stop the flow server")

  object FlowKeys {
    val allFiles = SettingKey[Boolean]("allFiles", "Typecheck all files, not just files with the @flow annotation")
    val weakInference = SettingKey[Boolean]("weakInference", "Typecheck with weak inference, assuming dynamic types by default")
    val interfacePaths = SettingKey[Seq[String]]("interfacePaths", "Specify one or more library paths for interfaces and declarations")
  }

}

object SbtFlow extends AutoPlugin {

  override def requires = SbtWeb

  override def trigger = AllRequirements

  val autoImport = Import

  import SbtWeb.autoImport._
  import autoImport._
  import FlowKeys._

  override def projectSettings: Seq[Setting[_]] = Seq(
    sourceDirectory in flow := (sourceDirectory in Assets).value,
    allFiles := false,
    weakInference := false,
    interfacePaths := Seq.empty,
    check in flow := flowCommand("check").value,
    init in flow := flowSimpleCommand("status").value,
    start in flow := flowCommand("start").value,
    status in flow := flowSimpleCommand("status").value,
    stop in flow := flowSimpleCommand("stop").value,
    flow := status in flow
  )

  def flowCommand(subCommand: String, args: Seq[String] = Seq.empty): Def.Initialize[Task[Unit]] = Def.task {
    val sourceDir = (sourceDirectory in flow).value
    val s: TaskStreams = streams.value

    val flags = Map("--all" -> (allFiles in flow).value,
                    "--weak" -> (weakInference in flow).value).filter( e => e._2 ).keys
    val paths = Map("--lib" -> (interfacePaths in flow).value).filter( e => ! e._2.isEmpty).flatMap( v => Seq( v._1, v._2 mkString(",")))

    val command: Seq[String] = Seq("flow", subCommand) ++ args ++ flags ++ paths ++ Seq(sourceDir.getAbsolutePath)
    s.log.debug(command mkString(" "))
    Process(command, None) ! s.log
  }

  def flowSimpleCommand(subCommand: String, args: Seq[String] = Seq.empty): Def.Initialize[Task[Unit]] = Def.task {
    val sourceDir = (sourceDirectory in flow).value
    val s: TaskStreams = streams.value

    val command: Seq[String] = Seq("flow", subCommand) ++ args ++ Seq(sourceDir.getAbsolutePath)
    s.log.debug(command mkString(" "))
    Process(command, None) ! s.log
  }
}
