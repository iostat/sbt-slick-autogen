package io.stat.slickify

import sbt.Keys._
import sbt._

import io.stat.slickify.Keys._

/**
  * Created by io on 3/3/16. io is an asshole because
  * he doesn't write documentation for his code.
  *
  * @author Ilya Ostrovskiy (https://github.com/iostat/) 
  */
object Defaults {
  lazy val projectSettings: Seq[Setting[_]] = Seq(
    sourceGenerators in Compile <+= (codegen in Slick),
    cleanFiles <+= (codegenDestination in Slick),
    managedSourceDirectories in Compile <+= (codegenDestination in Slick)
  )

  lazy val slickCodegenScopedSettings = inConfig(Slick)(Seq(
    version := "3.1.1",

    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick"         % (version in Slick).value,
      "com.typesafe.slick" %% "slick-codegen" % (version in Slick).value
    ),

    resourceDirectory    := (resourceDirectory in Compile).value,
    configurationFile    := (resourceDirectory map (_ / "application.conf")).value,
    codegenPackage       := "slick.generated",
    codegenClassName     := "Tables",
    codegenClassFileName := codegenClassName.value + ".scala",
    codegenDestination  <<= (sourceManaged in Compile)(_ / "slick-codegen"),

    alwaysUpdate          := false,
    schemaChangePredicate := Helpers.queries.alwaysNeedsUpdate,

    includedTables   := Helpers.tableFilters.all,
    classNamer       := Helpers.namers.defaultNamer,
    entityNamer      := Helpers.namers.defaultNamer,
    valueNamer       := Helpers.namers.defaultValueNamer,
    columnNamer      := Helpers.namers.defaultColumnNamer,
    systemTimeOffset := Helpers.offsets.systemTimeToUTC,

    codegenSettingsPart1 <<= (
      codegenDestination, codegenPackage, codegenClassName, codegenClassFileName,
      includedTables, entityNamer, classNamer, valueNamer, streams
    ) map ((a, b, c, d, e, f, g, h, s) => {
      (i, j, k, l, m, n, o, logger) => CodegenSettings(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, logger)
    }),

    codegenSettings <<= (
        codegenSettingsPart1, columnNamer, schemaChangePredicate, alwaysUpdate,
        configurationFile, configurationKey, systemTimeOffset, schemaName, streams
      ) map ((csp1, cn, scq, au, cf, ck, sto, sn, s) => {
        csp1(cn, scq, au, cf, ck, sto, sn, s.log)
      }),

    codegen <<= (codegenSettings, streams) map ((cs, st) => CodegenRunner.apply(cs, st.log))
  ))
}

