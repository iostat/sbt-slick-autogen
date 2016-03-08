package org.slf4j.impl_fix_me_later.loggers

import org.slf4j.Logger

/**
  * Created by io on 3/4/16. io is an asshole because
  * he doesn't write documentation for his code.
  *
  * @author Ilya Ostrovskiy (https://github.com/iostat/) 
  */
private[impl_fix_me_later] case class SwitchableLogger(loggerName: String, loggerRef: ThreadLocal[Logger]) extends LoggerImplShared {
  private[this] def log(): Logger = loggerRef get

  override def error(msg: String):                                Unit = log error msg
  override def error(format: String, arg: AnyRef):                Unit = log error(format, arg)
  override def error(format: String, arg1: Any, arg2: Any):       Unit = log error(format, arg1, arg2)
  override def error(format: String, arguments: AnyRef*):         Unit = log error(format, arguments:_*)
  override def error(msg: String, t: Throwable):                  Unit = log error(msg, t)

  override def warn(msg: String):                                 Unit = log warn msg
  override def warn(format: String, arg: AnyRef):                 Unit = log warn(format, arg)
  override def warn(format: String, arg1: Any, arg2: Any):        Unit = log error(format, arg1, arg2)
  override def warn(format: String, arguments: AnyRef*):          Unit = log warn(format, arguments:_*)
  override def warn(msg: String, t: Throwable):                   Unit = log warn(msg, t)

  override def info(msg: String):                                 Unit = log info msg
  override def info(format: String, arg: AnyRef):                 Unit = log info(format, arg)
  override def info(format: String, arg1: Any, arg2: Any):        Unit = log error(format, arg1, arg2)
  override def info(format: String, arguments: AnyRef*):          Unit = log info(format, arguments:_*)
  override def info(msg: String, t: Throwable):                   Unit = log info(msg, t)

  override def debug(msg: String):                                Unit = log debug msg
  override def debug(format: String, arg: AnyRef):                Unit = log debug(format, arg)
  override def debug(format: String, arg1: Any, arg2: Any):       Unit = log error(format, arg1, arg2)
  override def debug(format: String, arguments: AnyRef*):         Unit = log debug(format, arguments:_*)
  override def debug(msg: String, t: Throwable):                  Unit = log debug(msg, t)

  override def trace(msg: String):                                Unit = log trace msg
  override def trace(format: String, arg: AnyRef):                Unit = log trace(format, arg)
  override def trace(format: String, arg1: Any, arg2: Any):       Unit = log error(format, arg1, arg2)
  override def trace(format: String, arguments: AnyRef*):         Unit = log trace(format, arguments:_*)
  override def trace(msg: String, t: Throwable):                  Unit = log trace(msg, t)
}
