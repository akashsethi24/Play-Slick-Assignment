name := """Play_Slick_Assignment"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  ws,
  "com.adrianhurt" %% "play-bootstrap" % "1.0-P24-B3-SNAPSHOT" exclude("org.webjars", "jquery"),
  "org.webjars" % "jquery" % "2.1.0",
  "com.typesafe.play"   %%   "play-slick"              %   "1.1.1",
  "com.typesafe.play"   %%   "play-slick-evolutions"   %   "1.1.1",
  "com.h2database"    % 	   "h2"                    %   "1.4.187" ,
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc4",
  specs2 % Test
)


resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"


routesGenerator := InjectedRoutesGenerator
