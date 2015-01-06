package com.hindsightsoftware.sbt.flowtype

import sbt._
import sbt.Process
import com.typesafe.sbt.web.SbtWeb
import sbt.Keys._

object Import {

  val flowtype = TaskKey[Unit]("flowtype", "Performs type checking on JavaScript code using Flowtype.")

}

object SbtFlowtype extends AutoPlugin {

  override def requires = SbtWeb

  override def trigger = AllRequirements

  val autoImport = Import

  import SbtWeb.autoImport._
  import WebKeys._
  import autoImport._

  override def projectSettings: Seq[Setting[_]] = Seq(
    sourceDirectory in flowtype := (sourceDirectory in Assets).value,
    flowtype := checkFiles.value
  )

  def checkFiles: Def.Initialize[Task[Unit]] = Def.task {
      val sourceDir = (sourceDirectory in flowtype).value
      val s: TaskStreams = streams.value

      Process(Seq("flow", "check", "--quiet", sourceDir.getAbsolutePath), None) ! s.log match {
        case 0 => s.log.success("Flow check complete")
        case x => s.log.error("Flow check failed")
      }
  }
}
