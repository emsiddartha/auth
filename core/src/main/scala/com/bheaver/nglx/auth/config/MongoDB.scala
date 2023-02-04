package com.bheaver.nglx.auth.config

import com.bheaver.nglx.auth.protocol.datamodel.Patron
import com.typesafe.config.{Config, ConfigFactory}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoClient, MongoCollection}
object MongoDB {
  private val _config: Config = ConfigFactory.load()
  private val _mongoClient = MongoClient(_config.getString("database.connectionString"))
  private val _codecRegistry = fromRegistries(fromProviders(classOf[Patron]), DEFAULT_CODEC_REGISTRY )
  private val _database = _mongoClient.getDatabase(_config.getString("database.dbName")).withCodecRegistry(_codecRegistry)

  def patronCollection:MongoCollection[Patron] = _database.getCollection("patron")
}
