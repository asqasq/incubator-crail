name := "spark-sql"

version := "0.1"

scalaVersion := "2.11.0"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.7"
libraryDependencies += "org.apache.spark" % "spark-hive_2.11" % "2.4.7"
libraryDependencies += "org.xerial.snappy" % "snappy-java" % "1.1.7.5"

excludeDependencies += "org.xerial.snappy" % "snappy-java" % "1.0.4.1"