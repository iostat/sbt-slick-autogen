package io.stat.slickify

import java.util.Date

import sbt.{File, Logger}
import com.typesafe.config.Config
import slick.backend.DatabaseConfig
import slick.codegen.SourceCodeGenerator
import slick.driver._
import slick.jdbc.meta.MTable
import slick.profile.BasicDriver

import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag

/**
  * Created by io on 3/4/16. io is an asshole because
  * he doesn't write documentation for his code.
  *
  * @author Ilya Ostrovskiy (https://github.com/iostat/) 
  */
private[slickify] object TypeMagic {
  /**
    * A type of Slick profile that has the necessary capabilities for us to be able to use it without a
    * concrete reference to any type of driver.
    */
  type DriverWithCapabilities   = BasicDriver with JdbcDriver
  type ProfileWithCapabilities  = T forSome { type T <: JdbcProfile }
  type DatabaseWithCapabilities = ProfileWithCapabilities#Backend#Database

  /**
    * An abstract Table subtype of a subclass of SourceCodeGenerator
    */
  type OverridenTable = (T forSome { type T <: SourceCodeGenerator })#Table

  type NamerFunction         = ((String => String), String) => String
  type ValueNamerFunction    = (String, OverridenTable) => String
  type ColumnNamerFunction   = (String, OverridenTable, String) => String
  type SchemaChangePredicate =
  ((String, DriverWithCapabilities, DatabaseWithCapabilities, Logger, ExecutionContext) => Option[Date])

  type TableFilterPredicate  = MTable => Boolean

  private[slickify] type CodegenSettingsContinuation =
    ((ColumnNamerFunction, SchemaChangePredicate, Boolean, File, String, Long, String) => CodegenSettings)


  def loadReifiedConfig(confKey: String, config: Config): DatabaseConfig[DriverWithCapabilities] = {
    val driverName: String = config.getString(s"$confKey.driver")

    def reifiedConfig[T <: DriverWithCapabilities : ClassTag]: DatabaseConfig[DriverWithCapabilities] =
      DatabaseConfig.forConfig[T](confKey, config).asInstanceOf[DatabaseConfig[DriverWithCapabilities]]

    driverName match {
      case "slick.driver.DerbyDriver$"    => reifiedConfig[DerbyDriver]
      case "slick.driver.HsqldbDriver$"   => reifiedConfig[HsqldbDriver]
      case "slick.driver.JdbcDriver$"     => reifiedConfig[JdbcDriver]
      case "slick.driver.H2Driver$"       => reifiedConfig[H2Driver]
      case "slick.driver.MySQLDriver$"    => reifiedConfig[MySQLDriver]
      case "slick.driver.PostgresDriver$" => reifiedConfig[PostgresDriver]
      case "slick.driver.SQLiteDriver$"   => reifiedConfig[SQLiteDriver]
      case _ => throw new IllegalArgumentException(s"No type mapping has been defined for driver $driverName!")
    }
  }
}
