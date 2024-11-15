package fr.umontpellier.ig5

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

object PersonCsvParser {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("Simple Application").master("local[*]").getOrCreate()
    val logData = spark.read.textFile("data/persons.csv").cache()
    logData.show()

    val schema = new StructType()
      .add("id", IntegerType, nullable = true)
      .add("name", StringType, nullable = true)
      .add("age", IntegerType, nullable = true)
      .add("city", StringType, nullable = true)

    val df = spark.read.option("header", "true").schema(schema).csv("data/persons.csv")

    df.show()

    val filterByAge25AndAbove = df.filter("age >= 25")

    filterByAge25AndAbove.show()

    // Extract names and cities
    val name = df.select("name").distinct()

    name.show()

    val city = df.select("city").distinct()

    city.show()

    // SHow users grouped by city
    val groupByCity = df.groupBy("city").count()

    groupByCity.show()

    spark.stop()
  }

}
