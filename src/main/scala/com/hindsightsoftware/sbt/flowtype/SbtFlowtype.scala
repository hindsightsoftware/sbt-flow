package com.hindsightsoftware.sbt.flowtype

import sbt._
import sbt.Process
import com.typesafe.sbt.web.SbtWeb
import sbt.Keys._

object Import {

  val flowtype = TaskKey[Unit]("flowtype", "Performs type checking on JavaScript code using Flowtype.")
  val allFiles = SettingKey[Boolean]("allFiles", "Typecheck all files, not just files with the @flow annotation")
  val weakInference = SettingKey[Boolean]("weakInference", "Typecheck with weak inference, assuming dynamic types by default")

}

object SbtFlowtype extends AutoPlugin {

  override def requires = SbtWeb

  override def trigger = AllRequirements

  val autoImport = Import

  import SbtWeb.autoImport._
  import autoImport._

  override def projectSettings: Seq[Setting[_]] = Seq(
    sourceDirectory in flowtype := (sourceDirectory in Assets).value,
    allFiles in flowtype := false,
    weakInference in flowtype := false,
    flowtype := checkFiles.value
  )

  def checkFiles: Def.Initialize[Task[Unit]] = Def.task {
      val sourceDir = (sourceDirectory in flowtype).value
      val s: TaskStreams = streams.value

      val flags = Map("--all" -> (allFiles in flowtype).value,
                      "--weak" -> (weakInference in flowtype).value).filter( e => e._2 ).keys

      Process(Seq("flow", "check", "--quiet") ++ flags ++ Seq(sourceDir.getAbsolutePath), None) ! s.log match {
        case 0 => s.log.success("Flow check complete")
        case x => s.log.error("Flow check failed")
      }
  }
}
