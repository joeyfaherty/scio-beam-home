package com.home

import com.spotify.scio.{ContextAndArgs, ScioResult}
import spray.json._

/*
Cumulative withdrawal amount?

Notifies a system when suspicious activity occurs based on:
1. KYC status completion
2. Transfer amount > 30K
3. Location


 */

object AmlNotifier {

  // read json tx event from source
  // map to tx case class
  // filter on tx type, amount and kycStatus
  // retrieve state from db (previous suspicious activity)
  // notify (sink to kafka etc)

  val locationsToCheck = List("NL", "IE", "FR", "DE", "IT")

  def main(args: Array[String]): Unit = {
    val (sc, cmdOptions) = ContextAndArgs(args)

    //val exampleData = "gs://dataflow-samples/shakespeare/kinglear.txt"
    val inputJson = "src/main/resources/tx.json"
    val outputDir = "out"
    val input = cmdOptions.getOrElse("input", inputJson)
    val output = cmdOptions.getOrElse("output", outputDir)

    import TransactionProtocol._

    sc.textFile(input)
      .map(_.parseJson.convertTo[Transaction])
      .filter(p  => p.amount > 10000 && p.kycComplete && locationsToCheck.contains(p.location))
      // TODO retrieve previous state
      .saveAsTextFile(output)

    val result: ScioResult = sc.close().waitUntilFinish()
  }

}




