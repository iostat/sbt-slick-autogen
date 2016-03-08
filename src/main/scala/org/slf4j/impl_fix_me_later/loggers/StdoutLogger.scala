package org.slf4j.impl_fix_me_later.loggers

private[impl_fix_me_later] object StdoutLogger extends LoggerImplShared {
  override def getName: String = s"StdoutLogger"

  override def error(msg: String):                                Unit = println("[error] " +  msg)
  override def error(format: String, arg: AnyRef):                Unit = println("[error] " +  formatOutput(format, arg))
  override def error(format: String, arg1: AnyRef, arg2: AnyRef): Unit = println("[error] " +  formatOutput(format, arg1, arg2))
  override def error(format: String, arguments: AnyRef*):         Unit = println("[error] " +  formatOutput(format, arguments:_*))
  override def error(msg: String, t: Throwable):                  Unit = println("[error] " +  formatThrowable(msg, t))

  override def warn(msg: String):                                 Unit = println("[warn] " +  msg)
  override def warn(format: String, arg: AnyRef):                 Unit = println("[warn] " +  formatOutput(format, arg))
  override def warn(format: String, arg1: AnyRef, arg2: AnyRef):  Unit = println("[warn] " +  formatOutput(format, arg1, arg2))
  override def warn(format: String, arguments: AnyRef*):          Unit = println("[warn] " +  formatOutput(format, arguments:_*))
  override def warn(msg: String, t: Throwable):                   Unit = println("[warn] " +  formatThrowable(msg, t))

  override def info(msg: String):                                 Unit = println("[info] " +  msg)
  override def info(format: String, arg: AnyRef):                 Unit = println("[info] " +  formatOutput(format, arg))
  override def info(format: String, arg1: AnyRef, arg2: AnyRef):  Unit = println("[info] " +  formatOutput(format, arg1, arg2))
  override def info(format: String, arguments: AnyRef*):          Unit = println("[info] " +  formatOutput(format, arguments:_*))
  override def info(msg: String, t: Throwable):                   Unit = println("[info] " +  formatThrowable(msg, t))

  override def debug(msg: String):                                Unit = println("[debug] " +  msg)
  override def debug(format: String, arg: AnyRef):                Unit = println("[debug] " +  formatOutput(format, arg))
  override def debug(format: String, arg1: AnyRef, arg2: AnyRef): Unit = println("[debug] " +  formatOutput(format, arg1, arg2))
  override def debug(format: String, arguments: AnyRef*):         Unit = println("[debug] " +  formatOutput(format, arguments:_*))
  override def debug(msg: String, t: Throwable):                  Unit = println("[debug] " +  formatThrowable(msg, t))

  override def trace(msg: String):                                Unit = println("[trace] " +  msg)
  override def trace(format: String, arg: AnyRef):                Unit = println("[trace] " +  formatOutput(format, arg))
  override def trace(format: String, arg1: AnyRef, arg2: AnyRef): Unit = println("[trace] " +  formatOutput(format, arg1, arg2))
  override def trace(format: String, arguments: AnyRef*):         Unit = println("[trace] " +  formatOutput(format, arguments:_*))
  override def trace(msg: String, t: Throwable):                  Unit = println("[trace] " +  formatThrowable(msg, t))
}
