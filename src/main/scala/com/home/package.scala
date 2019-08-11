package com

import spray.json.DefaultJsonProtocol

package object home {

  case class Transaction(id: String,
                         transactionType: String,
                         amount: BigDecimal,
                         currency: String,
                         exRate: Int,
                         kycComplete: Boolean,
                         location: String)

  object TransactionProtocol extends DefaultJsonProtocol {
    implicit val txFormat = jsonFormat7(Transaction)
  }

}
