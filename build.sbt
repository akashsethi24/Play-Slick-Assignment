name := """Play_Slick_Assignment"""

version := "1.0-SNAPSHOT"

parallelExecution in Test := false

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  ws,
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "bootstrap" % "3.1.1-2",
  "com.typesafe.play"   %%   "play-slick"              %   "1.1.1",
  "com.typesafe.play"   %%   "play-slick-evolutions"   %   "1.1.1",
  "com.h2database"    % 	   "h2"                    %   "1.4.187" ,
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc4",
  specs2 % Test
)


resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
// Resolver is needed only for SNAPSHOT versions
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

routesGenerator := InjectedRoutesGenerator

javaOptions in Test += "-Dconfig.file=conf/application.test.conf"