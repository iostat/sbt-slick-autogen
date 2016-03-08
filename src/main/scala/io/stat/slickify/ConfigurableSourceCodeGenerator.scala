package io.stat.slickify

import slick.codegen.SourceCodeGenerator
import slick.model.Model

/**
  * Created by io on 3/7/16. io is an asshole because
  * he doesn't write documentation for his code.
  *
  * @author Ilya Ostrovskiy (https://github.com/iostat/) 
  */
case class ConfigurableSourceCodeGenerator(model: Model, config: CodegenSettings)
  extends SourceCodeGenerator(model) {

  override def tableName  = config.tableClassTransformer(super.tableName, _)
  override def entityName = config.tableEntityTransformer(super.entityName, _)

  override def Table = new Table(_) { table =>
    override def TableValue = new TableValue {
      override def rawName = config.tableValueTransformer(super.rawName, table)
    }

    override def Column = new Column(_) {
      override def rawName = config.tableColumnTransformer(super.rawName, table, this.model, this.model.name)
    }
  }
}
