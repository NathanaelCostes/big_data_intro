package fr.umontpellier.ig5

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import play.api.libs.json._

// Define a case class for the CVE data
case class CVEData(
                    ID: String,
                    Description: String,
                    baseScore: Option[Double],
                    baseSeverity: Option[String],
                    exploitabilityScore: Option[Double],
                    impactScore: Option[Double]
                  )

object CVEProcessorLocal {
  def main(args: Array[String]): Unit = {
    // Step 1: Initialize Spark Session
    val spark = SparkSession.builder()
      .appName("CVE Processor Local")
      .master("local[*]") // Run Spark locally
      .getOrCreate()

    import spark.implicits._

    // Step 2: Define JSON file URLs for years 2002-2024
    val startYear = 2002
    val endYear = 2024
    val basePath = "data/cve/input/"
    val files = (startYear to endYear).map(year => s"$basePath/nvdcve-1.1-$year.json")

    files.foreach { file =>
      // Extract the year from the filename
      val year = file.split("-")(2).split("\\.")(0).toInt
      println(s"Processing JSON for year: $year")

      // Step 3: Read the JSON file into Spark DataFrame
      val data = spark.read
        .format("json")
        .option("inferSchema", "true")
        .json(file)

      // Step 4: Extract relevant fields
      // Extract the relevant data
      val extractedData = data
        .select(explode($"CVE_Items").as("CVE_Item")) // Explode the CVE_Items array
        .select(
          $"CVE_Item.cve.CVE_data_meta.ID".as("ID"),
          $"CVE_Item.cve.description.description_data".getItem(0).getField("value").as("Description"),
          $"CVE_Item.impact.baseMetricV3.cvssV3.baseScore".as("baseScore"),
          $"CVE_Item.impact.baseMetricV3.cvssV3.baseSeverity".as("baseSeverity"),
          $"CVE_Item.impact.baseMetricV3.exploitabilityScore".as("exploitabilityScore"),
          $"CVE_Item.impact.baseMetricV3.impactScore".as("impactScore")
        )

      extractedData.show(false)

      // Step 5: Write extracted data to a local JSON file
      val outputPath = s"dat/cve/output/cve_$year.json"
      extractedData.write
        .format("json")
        .mode("overwrite")
        .save(outputPath)

      println(s"Extracted data for year $year written to $outputPath")
    }

    // Stop Spark Session
    spark.stop()
  }
}

