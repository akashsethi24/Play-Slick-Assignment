package services

import models.{Assignments, Certificates, Languages, ProgrammingLanguages}
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
  * Created by akash on 8/3/16.
  */
object Forms {
  val loginForm = Form(
      tuple(
          "email" -> email,
          "password" -> nonEmptyText
      )
  )

  val addCertificates = Form {
    mapping(
        "id" -> number,
        "userId" -> number,
        "name" -> nonEmptyText,
        "description" -> nonEmptyText,
        "year" -> number
    )(Certificates.apply)(Certificates.unapply)
  }

  val addLanguages = Form {
    mapping(
        "id" -> number,
        "userId" -> number,
        "name" -> nonEmptyText,
        "fluency" -> nonEmptyText
    )(Languages.apply)(Languages.unapply)
  }

  val addAssignments = Form {
    mapping(
        "id" -> number,
        "userId" -> number,
        "name" -> nonEmptyText,
        "date" -> nonEmptyText,
        "remarks" -> nonEmptyText,
        "marks" -> number
    )(Assignments.apply)(Assignments.unapply)
  }

  val addProgrammingLanguages = Form {
    mapping(
        "id" -> number,
        "userId" -> number,
        "name" -> nonEmptyText,
        "fluency" -> nonEmptyText
    )(ProgrammingLanguages.apply)(ProgrammingLanguages.unapply)
  }
}
