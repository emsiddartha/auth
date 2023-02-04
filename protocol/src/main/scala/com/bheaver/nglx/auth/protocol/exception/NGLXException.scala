package com.bheaver.nglx.auth.protocol.exception

sealed abstract class NGLXException(message: String) extends RuntimeException(message) {
  def httpStatus:Int
}

class AuthenticationFailedException(message: String) extends NGLXException(message) {
  override def httpStatus: Int = 401
}

class BadRequestException(message: String) extends NGLXException(message) {
  override def httpStatus: Int = 400
}