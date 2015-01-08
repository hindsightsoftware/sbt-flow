package com.hindsightsoftware.sbt.flowtype

import sbt._
import sbt.Process
import com.typesafe.sbt.web.SbtWeb
import sbt.Keys._

object Import {

  val flow = TaskKey[Unit]("flow-check", "Performs type checking on JavaScript code using Flowtype.")

  object FlowKeys {
    val allFiles = SettingKey[Boolean]("allFiles", "Typecheck all files, not just files with the @flow annotation")
    val weakInference = SettingKey[Boolean]("weakInference", "Typecheck with weak inference, assuming dynamic types by default")
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
    flow := checkFiles.value
  )

  def checkFiles: Def.Initialize[Task[Unit]] = Def.task {
      val sourceDir = (sourceDirectory in flow).value
      val s: TaskStreams = streams.value

      val flags = Map("--all" -> allFiles.value,
                      "--weak" -> weakInference.value).filter( e => e._2 ).keys

      Process(Seq("flow", "check", "--quiet") ++ flags ++ Seq(sourceDir.getAbsolutePath), None) ! s.log match {
        case 0 => s.log.success("Flow check complete")
        case x => s.log.error("Flow check failed")
      }
  }
}
