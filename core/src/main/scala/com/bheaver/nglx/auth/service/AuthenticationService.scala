package com.bheaver.nglx.auth.service

import com.bheaver.nglx.auth.protocol.{AuthenticationRequest, AuthenticationSuccess}
import com.bheaver.nglx.auth.repository.PatronRepo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

sealed trait AuthenticationService {
  def authenticate(authRequest: AuthenticationRequest): Future[AuthenticationSuccess]
}

class AuthenticationServiceImpl(patronRepo: PatronRepo, jwtService: JWTService) extends AuthenticationService {
  private val validateRequest =  (authRequest: AuthenticationRequest) => authRequest.validated()

  private val authenticate: AuthenticationRequest => Future[AuthenticationSuccess] =
    authRequest => patronRepo
    .authenticateUser(authRequest.username, authRequest.password, authRequest.emailId, authRequest.libcode)
    .map(patronVal => jwtService.generateToken(patronVal))
    .map(jwtString => AuthenticationSuccess(jwtString))
  override def authenticate(authRequest: AuthenticationRequest): Future[AuthenticationSuccess] =
    validateRequest
      .andThen(authenticate)(authRequest)

}