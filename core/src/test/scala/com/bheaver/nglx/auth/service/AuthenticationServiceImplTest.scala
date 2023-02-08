package com.bheaver.nglx.auth.service

import com.bheaver.nglx.auth.protocol.AuthenticationRequest
import com.bheaver.nglx.auth.protocol.datamodel.Patron
import com.bheaver.nglx.auth.protocol.exception.{AuthenticationFailedException, BadRequestException}
import com.bheaver.nglx.auth.repository.PatronRepoImpl
import org.mockito.ArgumentMatchers.anyString
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.Future
import org.mockito.Mockito._

class AuthenticationServiceImplTest extends AsyncFlatSpec with MockitoSugar with TableDrivenPropertyChecks {
  val patronRepo: PatronRepoImpl = mock[PatronRepoImpl]
  val jwtService: JWTServiceImpl = mock[JWTServiceImpl]
  val authenticationServiceImpl: AuthenticationServiceImpl = new AuthenticationServiceImpl(patronRepo, jwtService)

  "Authentication Service what" should "Authenticate User" in {
    val testData = Table(
      "authRequest",
      AuthenticationRequest("someUserName", "someEmail", "somePassword", "someLibcode"),
      AuthenticationRequest("", "someEmail", "somePassword", "someLibcode"),
      AuthenticationRequest("someUserName", "", "somePassword", "someLibcode"),
      AuthenticationRequest(null, "someEmail", "somePassword", "someLibcode"),
      AuthenticationRequest("someUserName", null, "somePassword", "someLibcode")
    )

    forAll(testData) {
      authenticationRequest =>
        val patronToReturn = Patron("somePatronId", 0, 0, "", "", "")

        when(jwtService.generateToken(patronToReturn)).thenReturn("mockedJWTString")
        when(patronRepo.authenticateUser(authenticationRequest.username,
          authenticationRequest.password,
          authenticationRequest.emailId,
          authenticationRequest.libcode)).thenReturn(Future(patronToReturn))

        authenticationServiceImpl.authenticate(authenticationRequest)
          .map {
            authSuccess =>
              assert(authSuccess != null)
              assert(authSuccess.jwtToken == "mockedJWTString")
          }
    }
  }

  it should "Handle Errors from Repo" in {
    val authenticationRequest = AuthenticationRequest("someUserName", "someEmail", "somePassword", "someLibcode")
    when(patronRepo.authenticateUser(anyString(),
      anyString(),
      anyString(),
      anyString())).thenThrow(new AuthenticationFailedException("Authentication failed"))

    val caught = intercept[AuthenticationFailedException] {
      authenticationServiceImpl.authenticate(authenticationRequest)
    }

    assert(caught.getMessage == "Authentication failed")
  }

  it should "Validate the request" in {
    val testData = Table(
      "authenticationRequest",
      AuthenticationRequest("", "", "somePassword", "someLibcode"),
      AuthenticationRequest("someid", "someemail", "", "someLibcode"),
      AuthenticationRequest("someid", "someemail", "somepassword", ""),
      AuthenticationRequest("", "", "", "")
    )
    forAll(testData) {
      authenticationRequest =>
        val caught = intercept[BadRequestException] {
          authenticationServiceImpl.authenticate(authenticationRequest)
        }

        assert(caught.getMessage == "Invalid Authentication Request")
    }
  }
}
