name := """pokomon-api"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  guice,
  javaWs,
  "com.github.jeromeheissler" % "play-jongo" % "master-SNAPSHOT",
  "org.rjung.util" % "gravatar" % "1.0",
  "com.auth0" % "java-jwt" % "3.8.1",
  "joda-time" % "joda-time" % "2.10.2",
  "io.sentry" % "sentry-logback" % "1.7.22",

  "org.webjars"       %%  "webjars-play"              % "2.7.0-1",
  "org.webjars.bower" %   "seiyria-bootstrap-slider"  % "10.3.1",
  "org.webjars.bower" %   "adminlte"                  % "2.3.11",
  "org.webjars.bower" %   "jquery"                    % "3.4.1",
  "org.webjars.bower" %   "d3"                        % "5.7.0",
  "org.webjars.bower" %   "c3"                        % "0.6.3",
  "org.webjars.bower" %   "moment"                    % "2.24.0"
)

// "-v" needed for more verbose output, otherwise only the number of tests is reported
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-v"))

herokuAppName in Compile := "jerome-pokomon"

herokuProcessTypes in Compile := Map(
  "web" -> "target/universal/stage/bin/pokomon-api -Dhttp.port=$PORT -Dconfig.resource=prod.conf -Dsentry.dsn=https://e3ffbb0c1aad4116b68aa3451d3c541c:a134ee6f60d24b5fb61603af15194ffe@sentry.io/147438"
)

resolvers += "jitpack" at "https://jitpack.io"

TwirlKeys.templateImports += "models._"
