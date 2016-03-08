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
    codegenDestination <<= (sourceManaged in Compile) (_ / "slick-codegen"),
    codegenPackage      := "io.stat.slickify.generated",

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
    codegenClassName     := "Tables",
    codegenClassFileName := codegenClassName.value + ".scala",

    alwaysUpdate          := false,
    schemaChangePredicate := Helpers.queries.alwaysNeedsUpdate,

    includedTables   := Helpers.all,
    entityNamer      := Helpers.defaultNamer,
    classNamer       := Helpers.defaultNamer,
    valueNamer       := Helpers.defaultValueNamer,
    columnNamer      := Helpers.defaultColumnNamer,
    systemTimeOffset := Helpers.offsets.systemTimeToUTC,

    codegenSettingsPart1 <<= (
      codegenDestination, codegenPackage, codegenClassName, codegenClassFileName,
      includedTables, entityNamer, classNamer, valueNamer, streams
    ) map ((a, b, c, d, e, f, g, h, s) => {
      s.log.info(s"[codegen/set] directory     => $a")
      s.log.info(s"[codegen/set] out/file      => $d")
      s.log.info(s"[codegen/set] package       => $b")
      s.log.info(s"[codegen/set] class         => $c")
      (i, j, k, l, m, n, o) => CodegenSettings(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)
    }),

    codegenSettings <<= (
        codegenSettingsPart1, columnNamer, schemaChangePredicate, alwaysUpdate,
        configurationFile, configurationKey, systemTimeOffset, schemaName, streams
      ) map ((csp1, cn, scq, au, cf, ck, sto, sn, s) => {
      s.log.info(s"[codegen/set] alwaysUpdate  => $au")
      s.log.info(s"[codegen/set] config/file   => $cf")
      s.log.info(s"[codegen/set] config/key    => $ck")
      s.log.info(s"[codegen/set] config/schema => $sn")
      s.log.info(s"[codegen/set] config/offset => $sto")
        csp1(cn, scq, au, cf, ck, sto, sn)
      }),

    codegen <<= (codegenSettings, streams) map CodegenRunner.apply
  ))
}

