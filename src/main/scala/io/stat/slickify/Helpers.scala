package io.stat.slickify

import java.sql.Timestamp
import java.util.TimeZone

import io.stat.slickify.TypeMagic._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by io on 3/7/16. io is an asshole because
  * he doesn't write documentation for his code.
  *
  * @author Ilya Ostrovskiy (https://github.com/iostat/) 
  */
object Helpers {
  /** Ripped from slick.codegen.AbstractGenerator **/
  /** Slick code generator string extension methods. (Warning: Not unicode-safe, uses String#apply) */
  implicit class StringExtensions(val str: String){
    /** Lowercases the first (16 bit) character. (Warning: Not unicode-safe, uses String#apply) */
    final def uncapitalize: String = str(0).toString.toLowerCase + str.tail

    /**
      * Capitalizes the first (16 bit) character of each word separated by one or more '_'. Lower cases all other characters.
      * Removes one '_' from each sequence of one or more subsequent '_' (to avoid collision).
      * (Warning: Not unicode-safe, uses String#apply)
      */
    final def toCamelCase: String
    = str.toLowerCase
      .split("_")
      .map{ case "" => "_" case s => s } // avoid possible collisions caused by multiple '_'
      .map(_.capitalize)
      .mkString("")
  }


  object tableFilters {
    def all: TableFilterPredicate = _ => true
    def only(included: String*): TableFilterPredicate = mt => included.contains(mt.name.name)
    def except(excluded: String*): TableFilterPredicate = mt => !excluded.contains(mt.name.name)
  }

  object namers {
    def defaultNamer: NamerFunction = (originalMethod, input) => originalMethod(input)
    def noChange:     NamerFunction = (_, input)              => input

    def defaultEntityNamer:  NamerFunction = (originalMethod, input) => originalMethod(input)
    def noChangeEntityNamer: NamerFunction = (_, input)              => input + "Row"

    def defaultValueNamer:  ValueNamerFunction = (_,table) => table.model.name.table.toCamelCase.uncapitalize
    def noChangeValueNamer: ValueNamerFunction = (_,table) => table.model.name.table.uncapitalize

    def defaultColumnNamer: ColumnNamerFunction  = (_,_,_,columnModelName) => columnModelName.toCamelCase.uncapitalize
    def noChangeColumnNamer: ColumnNamerFunction = (originalValue,_,_,_)   => originalValue
    def camelCaseNormalizingColumnNamer: ColumnNamerFunction = (_,_,_,columnModelName) => {
      columnModelName.contains("_") match {
        case true  => columnModelName.toCamelCase.uncapitalize
        case false => columnModelName.uncapitalize
      }
    }
  }

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
          GROUP BY
            `TABLE_SCHEMA`
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
