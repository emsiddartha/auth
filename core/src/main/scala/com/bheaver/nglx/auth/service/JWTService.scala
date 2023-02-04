package com.bheaver.nglx.auth.service

import com.bheaver.nglx.auth.protocol.datamodel.Patron

sealed trait JWTService {
  def generateToken(patron: Patron): String
}

class JWTServiceImpl extends JWTService {
  override def generateToken(patron: Patron): String = s"${patron.patron_id}..${patron.fname}"
}