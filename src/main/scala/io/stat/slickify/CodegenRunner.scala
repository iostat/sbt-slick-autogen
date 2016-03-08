package io.stat.slickify

import java.sql.Timestamp
import java.util.Date

import com.typesafe.config.ConfigFactory
import io.stat.slickify.TypeMagic.DriverWithCapabilities
import sbt.Path.richFile
import sbt.std.TaskStreams
import sbt.{File, Logger, ScopedKey}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.reflect.ClassTag


/**
  * Created by io on 3/3/16. io is an asshole because
  * he doesn't write documentation for his code.
  *
  * @author Ilya Ostrovskiy (https://github.com/iostat/) 
  */
object CodegenRunner {
  private[this] class Codegen[T <: DriverWithCapabilities : ClassTag]
    (config: CodegenSettings, driver: T, db: T#Backend#Database, log: Logger) {

    private[this] def needsUpdate(sqlDate: Option[Date]): Boolean = {
      val lastOrmChange =
        if(outFile.exists())
          Some(new Timestamp(outFile.lastModified() + config.systemTimeOffset))
        else
          None

      log.info(s"[codegen/run] lastOrmChange => $lastOrmChange")

      sqlDate.map(_.getTime) match {
        case None =>
          log.warn("[codegen/run] No timestamp received from SQL, will run codegen")
          true
        case Some(timestamp) =>
          lastOrmChange.map(_.getTime) match {
            case None =>
              log.warn("[codegen/run] No codegen'd ORM mappings exist, will run codegen.")
              true
            case Some(lastModified) =>
              val ret = lastModified < timestamp

              if (ret)
                log.warn("[codegen/run] Codegen'd ORM mappings are outdated, will run codegen.")
              else
                log.info("[codegen/run] Codegen'd ORM mappings were generated after the schema update. No update needed")

              ret
          }
      }
    }

    def execute(): Unit = {
      log.info(s"[codegen/run] profile       => ${driver.getClass.getCanonicalName}")

      implicit val ec = ExecutionContext.global

      val profileName = driver.getClass.getCanonicalName
      val profileNameCleaned =
        if (profileName.endsWith("$")) profileName.substring(0, profileName.length - 1) else profileName

      val lastSqlUpdateTime = config.schemaChangePredicate(config.schemaName, driver, db, log, ec)
      log.info(s"[codegen/run] lastSqlChange => $lastSqlUpdateTime")

      if(config.alwaysUpdate) {
        log.warn("[codegen/run] alwaysUpdate set to true, will run codegen!")
      }

      if(config.alwaysUpdate || needsUpdate(lastSqlUpdateTime)) {
        def filterTablesAndSave =
          db.run(
            driver.defaultTables
              .map(_.filter(config.tableFilterPredicate))
              .flatMap(driver.createModelBuilder(_, ignoreInvalidDefaults = true).buildModel))

        val model = Await.result(filterTablesAndSave, Duration.Inf)

        ConfigurableSourceCodeGenerator(model, config)
          .writeToFile(
            profileNameCleaned,
            config.destination.getAbsolutePath,
            config.outputPackage,
            config.outputClassName,
            config.outputFileName
          )
      } else {
        log.info("[codegen/run] No codegen run needed!")
      }
    }

    private[slickify] lazy val outFile: File = {
      config.destination / config.outputPackage.replaceAll("\\.", "/") / config.outputFileName
    }
  }


  def apply: ((CodegenSettings, TaskStreams[ScopedKey[_]]) => Seq[File]) = (settings, streams) => {
      val log = streams.log

      val loadedConfig = ConfigFactory.parseFile(settings.databaseConfigFile)
      val dbConfig     = TypeMagic.loadReifiedConfig(settings.databaseConfigKey, loadedConfig)
      val driver       = dbConfig.driver
      val db           = dbConfig.db

      val cg = new Codegen(settings, driver, db, log)

      cg.execute()

      db.close

      Seq(cg.outFile)
  }
}
