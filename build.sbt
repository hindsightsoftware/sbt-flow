import bintray.Keys._
import com.typesafe.sbt.SbtGit._

sbtPlugin := true

name := "sbt-flow"

organization := "com.hindsightsoftware.sbt"

scalaVersion := "2.10.4"

addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.0.0")

versionWithGit

git.baseVersion := "0.1"

bintrayPublishSettings

repository in bintray := "sbt-plugins"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

bintrayOrganization in bintray := Some("hindsightsoftware")

publishMavenStyle := false

