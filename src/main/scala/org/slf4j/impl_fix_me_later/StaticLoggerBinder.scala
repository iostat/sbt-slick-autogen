package org.slf4j.impl_fix_me_later

import org.slf4j.ILoggerFactory
import org.slf4j.spi.LoggerFactoryBinder

/**
  * Created by io on 3/4/16. io is an asshole because
  * he doesn't write documentation for his code.
  */
object StaticLoggerBinder extends LoggerFactoryBinder {
  override def getLoggerFactory: ILoggerFactory = SwitchableLoggerFactory
  override def getLoggerFactoryClassStr: String = SwitchableLoggerFactory.getClass.getCanonicalName
}
