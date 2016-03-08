package io.stat.slickify

import sbt._

object SBTPlugin extends AutoPlugin {
  override def requires = plugins.JvmPlugin
  override def trigger  = noTrigger

  override val projectSettings: Seq[Setting[_]] =
    Defaults.projectSettings ++ Defaults.slickCodegenScopedSettings
}
