package com.bheaver.nglx.auth.protocol

import com.bheaver.nglx.auth.protocol.exception.BadRequestException

trait Authentication
case class AuthenticationRequest(username: String,
                                 emailId: String,
                                 password: String,
                                 libcode: String) extends Authentication {
  def validated() = {
    if(("".equals(username) && "".equals(emailId)) || "".equals(password) || "".equals(libcode)) {
      throw  new BadRequestException("Invalid Authentication Request")
    }
    this
  }
}

case class AuthenticationSuccess(jwtToken: String) extends Authentication

case class AuthenticationFailed(reason: String) extends Authentication