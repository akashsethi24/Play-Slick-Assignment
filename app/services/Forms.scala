package services

import play.api.data.Forms._
import play.api.data._
/**
  * Created by akash on 8/3/16.
  */
object Forms {

  val loginForm = Form(
    tuple(
      "email"->email,
      "password"->nonEmptyText
    )
  )

}
