package io.stat.slickify

import java.sql.Timestamp
import java.util.TimeZone

import io.stat.slickify.TypeMagic.{OverridenTable, SchemaChangePredicate}
import slick.jdbc.meta.MTable

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by io on 3/7/16. io is an asshole because
  * he doesn't write documentation for his code.
  *
  * @author Ilya Ostrovskiy (https://github.com/iostat/) 
  */
object Helpers {

  def all: MTable => Boolean = _ => true

  def copyName: String => String = identity
  def columnIdentity: (String, String) => String = (_, c) => c

  def defaultNamer(originalMethod: (String => String), input: String): String = originalMethod(input)
  def defaultValueNamer(originalValue: String, table: OverridenTable): String = originalValue
  def defaultColumnNamer(originalValue: String, table: OverridenTable, columnModelName: String): String = originalValue

  object offsets {
    def none: Long = 0
    def systemTimeToUTC: Long = -TimeZone.getDefault.getRawOffset
  }

  object queries {
    def alwaysNeedsUpdate: SchemaChangePredicate = (_,_,_,l,_) => {
      l.warn("Using ALWAYS_NEEDS_UPDATE SchemaChangePredicate. Probably not what you want!")
      None
    }

    def mysqlUpdatePredicate: SchemaChangePredicate = (schemaName, driver, db, logger, ec) => {
      import driver.api.actionBasedSQLInterpolation
      val query =
        sql"""
          SELECT
             	GREATEST(MAX(COALESCE(CREATE_TIME, 0)), MAX(COALESCE(UPDATE_TIME, 0))) AS `LATEST_UPDATE`,
             	`TABLE_SCHEMA`
          FROM
            `information_schema`.`TABLES`
          WHERE
            `TABLE_SCHEMA` = $schemaName
          GROUP BY `TABLE_SCHEMA`
          ORDER BY
            1, 2
            DESC
          """.as[(Timestamp, String)]

      implicitly(ec)
      val result = Await.result(db.run(query), Duration(2, "min"))(0)
      Some(result._1)
    }
  }
}
