package com.bheaver.nglx.auth.service

import com.bheaver.nglx.auth.protocol.datamodel.Patron
import com.bheaver.nglx.auth.protocol.{AuthenticationFailed, AuthenticationRequest, AuthenticationSuccess}
import com.bheaver.nglx.auth.repository.{PatronRepo, PatronRepoImpl}
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.Future

class AuthenticationServiceImplTest extends AsyncFlatSpec with AsyncMockFactory {


  behavior of "Authentication Service what"

  it should "Authenticate User" in {
    val patronRepo: PatronRepoImpl = mock[PatronRepoImpl]
    val jwtService: JWTServiceImpl = mock[JWTServiceImpl]
    val authenticationServiceImpl: AuthenticationServiceImpl = new AuthenticationServiceImpl(patronRepo, jwtService)

    val authenticationRequest = AuthenticationRequest("someUserName", "someEmail", "somePassword", "someLibcode")
    val patronToReturn = Patron("somePatronId",0,0,"","","")

    (patronRepo.authenticateUser _).expects(authenticationRequest.username,
      authenticationRequest.password,
      authenticationRequest.emailId,
      authenticationRequest.libcode)
      .returning(Future(patronToReturn))


    (jwtService.generateToken _).expects(patronToReturn)
      .returning("mockedJWTString")
      .once()

    authenticationServiceImpl.authenticate(authenticationRequest)
      .map{
        authSuccess =>
          assert(authSuccess != null)
          assert(authSuccess.jwtToken == "mockedJWTString")
      }

  }
}
