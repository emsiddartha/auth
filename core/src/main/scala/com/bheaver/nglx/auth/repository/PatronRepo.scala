package com.bheaver.nglx.auth.repository

import com.bheaver.nglx.auth.config.MongoDB._
import com.bheaver.nglx.auth.protocol.datamodel.Patron
import com.bheaver.nglx.auth.protocol.exception.AuthenticationFailedException
import org.mongodb.scala.model.Filters._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait PatronRepo {
  def authenticateUser(username: String,
                       password: String,
                       emailId: String,
                       libcode: String): Future[Patron]
}

class PatronRepoImpl extends PatronRepo {
  override def authenticateUser(username: String,
                                password: String,
                                emailId: String,
                                libcode: String): Future[Patron] = {

    patronCollection.find(
      and(
        or(equal("patron_id", username),
          equal("email", emailId)),
        equal("library_id", libcode),
        equal("user_password", password)
      )
    )
      .first()
      .toFutureOption()
      .map {
        case Some(patron) => patron
        case None => throw new AuthenticationFailedException("Authentication failed")
      }
  }
}
