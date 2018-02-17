enablePlugins(GatlingPlugin)

name := "InContactPerformance"

version := "1.0"

scalaVersion := "2.11.8"


scalacOptions := Seq(
  "-encoding", "UTF-8", "-target:jvm-1.8", "-deprecation",
  "-feature", "-unchecked", "-language:implicitConversions", "-language:postfixOps")


libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.0" % "test,it"
libraryDependencies += "io.gatling"            % "gatling-test-framework"    % "2.2.0" % "test,it"

libraryDependencies += "org.apache.commons" % "commons-email" % "1.5"
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.1"
libraryDependencies += "org.zeroturnaround" % "zt-zip" % "1.12"
libraryDependencies += "com.relevantcodes" % "extentreports" % "2.40.2"
libraryDependencies += "commons-io" % "commons-io" % "2.5"
libraryDependencies += "com.opencsv" % "opencsv" % "4.1"


