package org.slf4j.impl_fix_me_later.loggers

import org.slf4j.helpers.MarkerIgnoringBase
import org.slf4j.helpers.MessageFormatter.arrayFormat

/**
  * Created by io on 3/4/16. io is an asshole because
  * he doesn't write documentation for his code.
  *
  * @author Ilya Ostrovskiy (https://github.com/iostat/) 
  */
private[impl_fix_me_later] trait LoggerImplShared extends MarkerIgnoringBase {
  def isErrorEnabled: Boolean = true
  def isWarnEnabled:  Boolean = true
  def isInfoEnabled:  Boolean = true
  def isDebugEnabled: Boolean = true
  def isTraceEnabled: Boolean = true

  private[impl_fix_me_later] def formatOutput(format: String, args: AnyRef*): String = arrayFormat(format, args toArray).getMessage
  private[impl_fix_me_later] def formatThrowable(msg: String, t: Throwable):  String = arrayFormat(msg, Array(t)).getMessage
}
