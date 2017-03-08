name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += jdbc
libraryDependencies += cache
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test


libraryDependencies ++= Seq(
  specs2 % Test,
  "org.webjars" % "angularjs" % "1.4.6",
  "org.webjars" % "bootstrap" % "3.3.5",
  "org.webjars" % "jquery" % "2.1.1",
  "org.webjars" % "satellizer" % "0.12.5",
  "org.webjars" % "angular-ui-router" % "0.2.15",
  "org.webjars" % "angular-toastr" % "1.5.0",
  "org.webjars" % "underscorejs" % "1.8.3",
  "org.webjars" % "ngDialog" % "0.3.12-1",
  "org.webjars" % "angular-chart.js" % "0.7.1",
  "org.webjars.bower" % "ng-file-upload" % "9.0.12",
  "org.webjars.bower" % "angular-utils-pagination" % "0.9.1",
  "org.webjars.bower" % "angular-loading-bar" % "0.8.0",
  "org.webjars" % "angular-bootstrap-datetimepicker" % "0.3.8",
  "org.webjars" % "momentjs" % "2.12.0",
  "com.pauldijou" %% "jwt-play" % "0.4.1",
  "com.github.nscala-time" %% "nscala-time" % "2.4.0",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.webjars.bower" % "ngstorage" % "0.3.11"
)

libraryDependencies += "com.google.maps" % "google-maps-services" % "0.1.6"
libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.11.14"

routesGenerator := InjectedRoutesGenerator
