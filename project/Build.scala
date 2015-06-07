import sbt._
import sbt.Keys._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.scalajs.sbtplugin.cross.CrossProject
import spray.revolver.RevolverPlugin._
import com.typesafe.sbt.web.SbtWeb
import com.typesafe.sbt.web.Import._
import sass.Import._

object Build extends sbt.Build {
  val outPath = new File("web")
  val jsPath = outPath / "js"
  val cssPath = outPath / "css"
  val fontsPath = outPath / "fonts"

  val deploy   = System.getenv("DEPLOY") == "true"
  val port     = if (deploy) 80 else 8080
  val optLevel =
    if (deploy) {
      println("[info] Compiling in production mode")
      fullOptJS
    } else {
      println("[info] Compiling in development mode")
      fastOptJS
    }

  val copyFontsTask = {
    val webJars = WebKeys.webJarsDirectory in Assets
    webJars.map { path =>
      val paths = Seq(
        path / "lib" / "font-awesome" / "fonts"
      , path / "lib" / "bootstrap-sass" / "fonts" / "bootstrap"
      )

      paths.flatMap { fontPath =>
        fontPath.listFiles().map { src =>
          val tgt = fontsPath / src.getName
          IO.copyFile(src, tgt)
          tgt
        }
      }
    }
  }.dependsOn(WebKeys.webJars in Assets)

  lazy val crossProject = CrossProject("server", "client", file("."), CrossType.Full)
    .enablePlugins(SbtWeb)
    .settings(
      name := "skeleton",
      version := "0.1-SNAPSHOT",
      organization := "io.widok",
      scalaVersion := "2.11.6",
      scalacOptions := Seq(
        "-unchecked"
      , "-deprecation"
      , "-encoding", "utf8"
      , "-Xelide-below", annotation.elidable.ALL.toString
      )
    )
    .jvmSettings(
      Revolver.settings: _*
    )
    .jvmSettings(
       libraryDependencies ++= Seq(
        "io.spray" %% "spray-can" % "1.3.1",
        "io.spray" %% "spray-routing" % "1.3.1",
        "com.typesafe.akka" %% "akka-actor" % "2.3.2",
        "com.lihaoyi" %% "upickle" % "0.2.8",
        "com.lihaoyi" %% "autowire" % "0.2.5",
        "net.sf.extjwnl" % "extjwnl" % "1.8.1",
        "net.sf.extjwnl" % "extjwnl-data-wn31" % "1.2"
    )
    , mainClass in Revolver.reStart := Some("io.widok.server.Server")
    )
    .jsSettings(
      libraryDependencies ++= Seq(
        "io.github.widok" %%% "widok" % "0.2.1" withSources() withJavadoc(),
        "com.lihaoyi" %%% "upickle" % "0.2.8",
        "com.lihaoyi" %%% "autowire" % "0.2.5",
        "org.webjars" % "bootstrap-sass" % "3.3.1",
        "org.webjars" % "font-awesome" % "4.3.0-1"
      )
    , persistLauncher := true
    , skip in packageJSDependencies := false
    , artifactPath in (Compile, packageScalaJSLauncher) := jsPath / "launcher.js"
    , artifactPath in (Compile, packageJSDependencies) := jsPath / "deps.js"
    , artifactPath in (Compile, optLevel) := jsPath / "application.js"
    , resourceManaged in sass in Assets := cssPath
    , sourceGenerators in Assets <+= copyFontsTask
    )

  lazy val js = crossProject.js

  lazy val jvm = crossProject.jvm.settings(
    baseDirectory in Revolver.reStart := new File(".") // defaults to jvm/
  , Revolver.reStart <<= Revolver.reStart dependsOn (optLevel in (js, Compile))
  , Revolver.reStartArgs := Seq(port.toString)
  )
}
