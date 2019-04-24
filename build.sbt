import sbt.Keys._
import sbt.Project.projectToRef

dependencyOverrides in ThisBuild += "org.webjars.npm" % "js-tokens" % "3.0.2"

// a special crossProject for configuring a JS/JVM/shared structure
lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(
    scalaVersion := Settings.versions.scala,
    libraryDependencies ++= Settings.sharedDependencies.value
  )
  // set up settings specific to the JS project
  .jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJVM = shared.jvm.settings(name := "sharedJVM")

lazy val sharedJS = shared.js.settings(name := "sharedJS")

// use eliding to drop some debug code in the production build
lazy val elideOptions = settingKey[Seq[String]]("Set limit for elidable functions")

// instantiate the JS project for SBT with some additional settings
lazy val client: Project = (project in file("client"))
  .settings ( 
      
    name := "client",
    resolvers ++= Seq (
        "jitpack" at "https://jitpack.io"/*,
        Resolver.bintrayRepo ( "freshwood", "maven" )*/
    ),
    //useYarn := true,
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= Settings.scalacOptions,
    scalaJSModuleKind := ModuleKind.CommonJSModule,
    libraryDependencies ++= Settings.scalajsDependencies.value,
    //addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.patch),
    // by default we do development build, no eliding
    elideOptions := Seq(),
    scalacOptions ++= elideOptions.value,
    emitSourceMaps in fullOptJS := true,
    emitSourceMaps in Compile := true,
    jsDependencies ++= Settings.jsDependencies.value,
    // RuntimeDOM is needed for tests
    jsDependencies += RuntimeDOM % "test",
    // yes, we want to package JS dependencies
    skip in packageJSDependencies := false,
    // use Scala.js provided launcher code to start the client app
    scalaJSUseMainModuleInitializer := true,
    scalaJSUseMainModuleInitializer in Test := false,
    skip in packageJSDependencies := false,
    //skip in packageJSDependencies := false,
    // use uTest framework for tests
    testFrameworks += new TestFramework("utest.runner.Framework"),
    //npmDependencies in Compile += "outwatch" -> "1.0.0-RC2"
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0-M4"),
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb, ScalaJSBundlerPlugin, SbtWeb )
  .dependsOn(sharedJS)

// Client projects (just one in this case)
lazy val clients = Seq(client)

/**/

// instantiate the JVM project for SBT with some additional settings
lazy val server = (project in file("server"))
  .settings(
    name := "server",
    resolvers ++= Seq (
        "jitpack" at "https://jitpack.io"/*,
        Resolver.bintrayRepo ( "freshwood", "maven" )*/
    ),
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= Settings.scalacOptions,
    PlayKeys.devSettings += "play.server.provider" -> "play.core.server.AkkaHttpServerProvider",
    libraryDependencies ++= Settings.jvmDependencies.value :+ guice :+ ws,
    commands += ReleaseCmd,
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    // connect to the client project
    scalaJSProjects := clients,
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    // compress CSS
    LessKeys.compress in Assets := true//,
    //javaOptions in run += "-Xms256M -Xmx2G"    //-XX:MaxPermSize=1024M
  )
  .enablePlugins(PlayScala, WebScalaJSBundlerPlugin)
  .disablePlugins(PlayLayoutPlugin) // use the standard directory layout instead of Play's custom
  .aggregate(clients.map(projectToRef): _*)
  .dependsOn(sharedJVM)

// Command for building a release
lazy val ReleaseCmd = Command.command("release") {
  state => "set elideOptions in client := Seq(\"-Xelide-below\", \"WARNING\")" ::
    "client/clean" ::
    "client/test" ::
    "server/clean" ::
    "server/test" ::
    "server/dist" ::
    "set elideOptions in client := Seq()" ::
    state
}

//mainClass in Compile := Some(SPAMain)

// lazy val root = (project in file(".")).aggregate(client, server)

// loads the Play server project at sbt startup
//onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
//onLoad in Global := (scala.sys.process("project server", _: State)) compose (onLoad in Global).value
onLoad in Global ~= (_ andThen ("project server" :: _))


