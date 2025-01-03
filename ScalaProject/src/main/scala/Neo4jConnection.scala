package fr.umontpellier.ig5

import org.apache.spark.sql.{SaveMode, SparkSession}

object Neo4jConnection {
  def main(args: Array[String]): Unit = {
    // Replace with the actual connection URI and credentials
    val url = "neo4j://localhost:7687"
    val username = "neo4j"
    val password = "12345678"
    val dbname = "neo4j"

    val spark = SparkSession.builder
      .config("neo4j.url", url)
      .config("neo4j.authentication.basic.username", username)
      .config("neo4j.authentication.basic.password", password)
      .config("neo4j.database", dbname)
      .appName("Spark App")
      .master("local[*]") // This sets the master to local mode using all available cores
      .getOrCreate()


    val data = spark.read.json("data/example.jsonl")

    // Write to Neo4j
    data.write
      .format("org.neo4j.spark.DataSource")
      .mode(SaveMode.Overwrite)
      .option("labels", "Person")
      .option("node.keys", "name,surname")
      .save()

    // Read from Neo4j
    val ds = spark.read
      .format("org.neo4j.spark.DataSource")
      .option("labels", "Person")
      .load()

    ds.show()
  }
}
