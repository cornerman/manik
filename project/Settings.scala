import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

/**Application settings. Configure the build for your application here.
 * You normally don't have to touch the actual build definition after this.  */
object Settings {

  val name = "manik"    /** Adquirir materiales */

  val version = "1.1.5"     /** The version of your application */

  val scalacOptions = Seq(  /** Options for the scala compiler */
    "-Xlint",
    "-unchecked",
    "-deprecation",
    "-feature",
    "-Ypartial-unification"
  )

  object versions {         /** Declare global dependency versions here to avoid mismatches in multi part dependencies */
    val scala = "2.12.6" //"2.12.4"

    val scalaCSS = "0.5.5"  //"0.5.3"

    val log4js = "1.4.10"
    val autowire = "0.2.6"
    val booPickle = "1.2.6"
    val uTest = "0.4.7"

    //val bootstrap = "3.3.6"  //"3.3.7-1" "3.3.6" "4.1.1"

    val scalajsScripts = "1.1.2"

    val outWatch = "0.4.0"
  }

  /**These dependencies are shared between JS and JVM projects
   * the special %%% function selects the correct version for each project*/
  val sharedDependencies = Def.setting(Seq(
    "com.lihaoyi" %%% "autowire" % versions.autowire,
    "io.suzaku" %%% "boopickle" % versions.booPickle,
    "org.typelevel" %% "cats-core" % "1.4.0",
    "com.github.marklister" %% "base64" % "0.2.5",
    "com.typesafe.akka" %% "akka-actor" % "2.5.19"
  ))

  /** Dependencies only used by the JVM project */
  val jvmDependencies = Def.setting( Seq ( 
  "com.vmunier" %% "scalajs-scripts" % versions.scalajsScripts,
    "org.webjars" % "font-awesome" % "4.3.0-1" % Provided, //"5.1.0" % Provided,
    "com.lihaoyi" %% "utest" % versions.uTest % Test,
    //Slick
    "com.typesafe.play" %% "play-slick" % "3.0.3",
    "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
    "io.underscore" %% "slickless" % "0.3.3",
    //Fin de Slick
    "javax.inject" % "javax.inject" % "1",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.7",
    "com.github.cornerman.mycelium" %%% "mycelium" % "44906c6"
    //"com.typesafe.akka" %% "akka-actor" % "2.5.19"
  ))

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting ( Seq (
      "com.lihaoyi" %%% "utest" % versions.uTest % Test,
      "org.akka-js" %%% "akkajsactor" % "1.2.5.19",
      "com.lihaoyi" %%% "scalatags" % "0.6.7",
      "com.github.cornerman" % "outwatch" % "bb2dbc58",//"com.github.outwatch" % "outwatch" % "master-SNAPSHOT",
      "com.clovellytech" %%% "outwatch-router" % "0.0.6",
      "org.webjars.npm" % "base64-js" % "1.3.0",
      "com.github.cornerman.mycelium" %%% "mycelium" % "44906c6"
      //"com.github.marklister" %% "base64" % "0.2.5"
  )
  )

  /** Dependencies for external JS libs that are bundled into a single .js file according to dependency order */
  val jsDependencies = Def.setting ( Seq (
      "org.webjars" % "log4javascript" % versions.log4js / "js/log4javascript_uncompressed.js" minified "js/log4javascript.js"
    )
  )

}
