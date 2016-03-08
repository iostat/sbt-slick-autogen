package io.stat.slickify

import io.stat.slickify.TypeMagic._
import sbt._
import slick.jdbc.meta.MTable

object Keys {
  val Slick: Configuration = config("slick")
  val codegen          = TaskKey[Seq[File]]("codegen", "Run the generated codegen")

  val slickVersion     = SettingKey[String]("slick-version",             "Which version of slick the codegen will use")
  val slickDriverClass = SettingKey[String]("slick-driver",              "Driver class to use")

  val codegenDestination   = SettingKey[File]("codegen-destination",         "Where generated files will go")
  val codegenPackage       = SettingKey[String]("codegen-package",           "What package the generated code will reside in")
  val codegenClassName     = SettingKey[String]("codegen-class-name",        "What the class name of the generated class will be")
  val codegenClassFileName = SettingKey[String]("codegen-class-file-name",   "What the file name where the generated class resides will be")

  val configurationFile = TaskKey[File]("configuration-file",             "Your application.conf, or where your database is")
  val configurationKey  = SettingKey[String]("configuration-key",         "The key of your database object in configurationFile")
  val schemaName        = SettingKey[String]("schema-name", "The name of the schema you're working against. This is a temporary hack, probably")

  val includedTables    = SettingKey[MTable => Boolean]("included-tables",   "See `CodegenSettings.tablePredicateFunction`")
  val entityNamer       = SettingKey[NamerFunction]("table-entity-namer", "See `CodegenSettings.tableEntityTransformer`")
  val classNamer        = SettingKey[NamerFunction]("table-class-namer",  "See `CodegenSettings.tableClassTransformer`")
  val valueNamer        = SettingKey[ValueNamerFunction]("table-value-namer",  "See `CodegenSettings.tableValueTransformer`")
  val columnNamer       = SettingKey[ColumnNamerFunction]("table-column-namer", "See `CodegenSettings.tableColumnTransformer`")

  val alwaysUpdate      = SettingKey[Boolean]("always-update", "Whether or not the codegen should run on every build")
  val schemaChangePredicate = SettingKey[SchemaChangePredicate]("schema-change-query", "A query that determines if the database schema has changed")
  val systemTimeOffset  = SettingKey[Long]("system-time-offset", "How many seconds to add to the current system timestamp to get the same current time as on the server")

  private[slickify] val codegenSettingsPart1 = TaskKey[CodegenSettingsContinuation]("codegen-settings-part-1", "#justSBTthingz")
  private[slickify] val codegenSettings = TaskKey[CodegenSettings]("codegen-settings", "Don't worry about this value, you can't change it anyway")
}
