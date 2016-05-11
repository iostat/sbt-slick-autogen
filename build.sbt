name := "sbt-slick-autogen"

organization := "io.stat.slickify"

version := "1.1"

sbtPlugin := true

addMavenResolverPlugin

resolvers ++= Seq(
  "Bintray JCenter" at "https://jcenter.bintray.com/"
)

libraryDependencies ++= Seq(
  //"org.slf4j"           %  "slf4j-api"          % "1.5.5", // #justsbtthingz
  "com.typesafe.slick"  %% "slick"                % "3.1.1",
  "com.typesafe.slick"  %% "slick-hikaricp"       % "3.1.1",
  "com.typesafe.slick"  %% "slick-codegen"        % "3.1.1",
  "mysql"               %  "mysql-connector-java" % "5.1.31"
  // todo: add JDBC drivers for everything else slick supports thats not MySQL
)

scalacOptions in ThisBuild ++= Seq(
  "-feature",
  "-language:postfixOps",
  "-language:reflectiveCalls",
  "-language:existentials"
)
