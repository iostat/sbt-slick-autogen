package org.slf4j.impl_fix_me_later

import java.util.function.Supplier

import org.slf4j.impl_fix_me_later.loggers.{SbtStreamLogger, StdoutLogger, SwitchableLogger}
import org.slf4j.{ILoggerFactory, Logger}

/**
  * Created by io on 3/4/16. io is an asshole because
  * he doesn't write documentation for his code.
  *
  * @author Ilya Ostrovskiy (https://github.com/iostat/) 
  */
object SwitchableLoggerFactory extends ILoggerFactory {
  private[this] val stdoutLoggerSupplier: Supplier[Logger] = new Supplier[Logger] {
    override def get(): Logger = StdoutLogger
  }
  private[this] val backingLogger: ThreadLocal[Logger] = ThreadLocal.withInitial(stdoutLoggerSupplier)

  override def getLogger(name: String): Logger = SwitchableLogger(name, backingLogger)

  // todo: make this thread-safe? (e.g., using ThreadLocal instances of Logger or w/e)
  def withLogger[T](function: (() => T))(implicit sbtLogger: sbt.Logger): T = withLogger("<no name>", function)
  def withLogger[T](name: String, function: (() => T))(implicit sbtLogger: sbt.Logger): T = {
    try {
      backingLogger.set(SbtStreamLogger(name, sbtLogger))
      function()
    } finally {
      backingLogger.set(stdoutLoggerSupplier.get)
    }
  }
}
