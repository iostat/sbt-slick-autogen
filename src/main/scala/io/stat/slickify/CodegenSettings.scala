package io.stat.slickify

import io.stat.slickify.TypeMagic._
import sbt._

/**
  * All the settings that the codegen needs to function
 *
  * @param destination the base directory into which code will be generated.
  *                    the file that ultimately gets generated is effectively
  *                    ``$destination/${outputPackage.replaceAll("\\.", "/")}/$outputFilename``
  * @param outputPackage the package that the generate code will be placed in
  * @param outputClassName the class/object name under which all generated code will live
  * @param outputFileName the file name of the generated code (default outputClassName + ".scala")
  * @param tableFilterPredicate A function that returns true if a Table should have Slick models generated for it
  * @param tableEntityTransformer A function that takes a table name, and returns the name of the Scala case class that
  *                               will be used to represent a row in that table. For example, for a DB that stores
  *                               customer addresses in a table called "CUSTOMER_ADDRESSES", this function should return
  *                               "CustomerAddress" "CustomerAddressRow" or something along those lines.
  * @param tableClassTransformer A function that takes a table name and returns the name of the Scala class that will
  *                              allow you to build queries against that table. Continuing the example above, if you
  *                              wanted to do something like "customerAddresses.filter(_.zipCode == 90210).first", you
  *                              would want to return "CustomerAddresses". The result of `tableValueTransformer` below
  *                              will be a value of the type that's returned for this function (i.e., you'll end up
  *                              having the equivalent of ``val customerAddresses: TableQuery[CustomerAddresses]``)
  *                              if this function returns "CustomerAddresses" and ``tableValueTransformer`` returns
  *                              "customerAddresses".
  * @param tableValueTransformer A function that returns the name of the instance of the table query used for a table.
  *                              See the description of the ``tableClassTransformer`` parameter for a better explanation
  * @param tableColumnTransformer A function that takes a table name and column name, and generates the name of the
  *                               field used in a table class as it maps to a column in the database. Following the
  *                               example in ``tableClassTransformer``, if you wanted to do the zipCode query, and the
  *                               name of the column in the database was ZIP_CODE, this function would need to return
  *                               "zipCode" when given an input of ("CUSTOMER_ADDRESSES", "ZIP_CODE")
  * @param schemaChangePredicate A predicate which executes a query and returns the Date of the last schema change
  * @param alwaysUpdate If this is true, schemaChangePredicate will be ignored and the codegen will always run
  * @param databaseConfigFile see `Keys.configurationFile`
  * @param databaseConfigKey see `Keys.configurationKey`
  * @param systemTimeOffset see `Keys.systemTimeOffset`
  * @param schemaName see `Keys.schemaName`
  */
private[slickify] sealed case class CodegenSettings(
   destination:            File,
   outputPackage:          String,
   outputClassName:        String,
   outputFileName:         String,
   tableFilterPredicate:   TableFilterPredicate,
   tableEntityTransformer: NamerFunction,
   tableClassTransformer:  NamerFunction,
   tableValueTransformer:  ValueNamerFunction,
   tableColumnTransformer: ColumnNamerFunction,
   schemaChangePredicate:  SchemaChangePredicate,
   alwaysUpdate:           Boolean,
   databaseConfigFile:     File,
   databaseConfigKey:      String,
   systemTimeOffset:       Long,
   schemaName:             String
)
