package org.slf4j.impl_fix_me_later.loggers

import sbt.Logger

/**
  * Implementation of SLF4J over SBT's logging facilities
  *
  * @author Ilya Ostrovskiy (https://github.com/iostat/) 
  */
private[impl_fix_me_later] case class SbtStreamLogger(loggerName: String, log: Logger) extends LoggerImplShared {
  override def getName: String = s"SbtStreamLogger($loggerName, $log)"

  override def error(msg: String):                                Unit = log error msg
  override def error(format: String, arg: AnyRef):                Unit = log error formatOutput(format, arg)
  override def error(format: String, arg1: AnyRef, arg2: AnyRef): Unit = log error formatOutput(format, arg1, arg2)
  override def error(format: String, arguments: AnyRef*):         Unit = log error formatOutput(format, arguments:_*)
  override def error(msg: String, t: Throwable):                  Unit = log error formatThrowable(msg, t)

  override def warn(msg: String):                                 Unit = log warn msg
  override def warn(format: String, arg: AnyRef):                 Unit = log warn formatOutput(format, arg)
  override def warn(format: String, arg1: AnyRef, arg2: AnyRef):  Unit = log warn formatOutput(format, arg1, arg2)
  override def warn(format: String, arguments: AnyRef*):          Unit = log warn formatOutput(format, arguments:_*)
  override def warn(msg: String, t: Throwable):                   Unit = log warn formatThrowable(msg, t)

  override def info(msg: String):                                 Unit = log info msg
  override def info(format: String, arg: AnyRef):                 Unit = log info formatOutput(format, arg)
  override def info(format: String, arg1: AnyRef, arg2: AnyRef):  Unit = log info formatOutput(format, arg1, arg2)
  override def info(format: String, arguments: AnyRef*):          Unit = log info formatOutput(format, arguments:_*)
  override def info(msg: String, t: Throwable):                   Unit = log info formatThrowable(msg, t)

  override def debug(msg: String):                                Unit = log debug msg
  override def debug(format: String, arg: AnyRef):                Unit = log debug formatOutput(format, arg)
  override def debug(format: String, arg1: AnyRef, arg2: AnyRef): Unit = log debug formatOutput(format, arg1, arg2)
  override def debug(format: String, arguments: AnyRef*):         Unit = log debug formatOutput(format, arguments:_*)
  override def debug(msg: String, t: Throwable):                  Unit = log debug formatThrowable(msg, t)

  override def trace(msg: String):                                Unit = log verbose msg
  override def trace(format: String, arg: AnyRef):                Unit = log verbose formatOutput(format, arg)
  override def trace(format: String, arg1: AnyRef, arg2: AnyRef): Unit = log verbose formatOutput(format, arg1, arg2)
  override def trace(format: String, arguments: AnyRef*):         Unit = log verbose formatOutput(format, arguments:_*)
  override def trace(msg: String, t: Throwable):                  Unit = log verbose formatThrowable(msg, t)
}
