package fr.umontpellier.ig5

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.sql.functions._

object StackOverflowParser {
  def main(args: Array[String]): Unit = {

    val data = "data/stackoverflow.csv"

    // Step 1: Initialize the SparkSession
    val spark = SparkSession.builder()
      .appName("CSV Line Counter")
      .master("local[*]") // Use local mode; adjust as necessary for your cluster
      .getOrCreate()

    val schema = new StructType()
      .add("postTypeId", IntegerType, nullable = true)
      .add("id", IntegerType, nullable = true)
      .add("acceptedAnswer", StringType, nullable = true)
      .add("parentId", IntegerType, nullable = true)
      .add("score", IntegerType, nullable = true)
      .add("tag", StringType, nullable = true)

    // Step 2: Load the CSV file into a DataFrame
    val df = spark.read
      .option("header", "false")
      .schema(schema)
      .csv(data)
      .drop("acceptedAnswer")

    // Step 3: Count the rows (i.e., lines in the CSV)
    val lineCount = df.count()

    println(s"Number of lines: $lineCount")


    df.printSchema()

    df.show(5)

    // count nb of null values
    val postTypeCounts = df.groupBy("postTypeId").count()
    postTypeCounts.show()

    // Step 4: Count the number of rows for each language
    val topTags = df.groupBy("tag").count().orderBy(desc("count"))
    topTags.show()

    val top5Score =
      df.groupBy("tag")
        .agg(avg("score").as("avg_score"))
        .orderBy(desc("avg_score"))
        .limit(5)
    top5Score.show()

    df.createOrReplaceTempView("stackoverflow")
    val sqlDF = spark.sql("SELECT * FROM stackoverflow WHERE score > 100")
    sqlDF.show()

    spark.stop()
  }
}
