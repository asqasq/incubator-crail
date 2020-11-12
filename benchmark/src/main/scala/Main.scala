import org.apache.spark._
import org.apache.spark.sql._
import org.apache.hadoop.fs.{FileSystem, Path}

import scala.concurrent.{Await, Future, future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

object Main {
  def main(args: Array[String]): Unit = {

    //readChar()
    //val conf = new SparkConf().setSparkHome("/Users/bro/repos/spark-2.4.7-bin-hadoop2.7").setAppName("NetworkWordCount")

    import java.io.File

    import org.apache.spark.sql.{Row, SaveMode, SparkSession}

    case class Record(key: Int, value: String)

    // warehouseLocation points to the default location for managed databases and tables
    //val spark_home = sys.env("SPARK_HOME")
    val spark_home = "/Users/bro/repos/spark-2.4.7-bin-hadoop2.7"
    val warehouseLocation = new File(spark_home ++ "/spark-warehouse").getAbsolutePath

    println("Warehouse location " ++ warehouseLocation)

    val spark = SparkSession
      .builder()
      .appName("Spark Hive Example")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .config("hive.metastore.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    //spark.conf.set("spark.sql.parquet.compression.codec", "gzip")
    println(spark.conf.get("spark.sql.warehouse.dir"))

    import spark.implicits._
    import spark.sql

    sql("show databases").show()
    sql("use tpcds").show()



    val query = args(0)

    println("Running with TPC_DS query" + query)

    val query_text = scala.io.Source.fromFile("sql_queries/query" + query + ".sql").mkString.split(";")(0)

    var run = 1

    while(run < 10) {

      println("Performing run " + run)

      val parallel_tasks = if(run % 2 == 0) {
        1
      } else {
        5
      }

      val tasks = for (i <- 1 to parallel_tasks) yield future {
        val res = sql(query_text)
        val res_rows = res.count()
        println("Executed task " + i + " with " + res_rows + " rows")
        res_rows
      }

      val aggregated = Future.sequence(tasks)

      val res = Await.result(aggregated, 1000.seconds)

      println(res)
      run = run + 1
    }



    spark.stop()


  }
}

