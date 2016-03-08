package repo

import connection.DBComponent
import models.User

import scala.concurrent.Future

/**
  * Created by akash on 8/3/16.
  */
trait UserTable {

  this: DBComponent =>
  import driver.api._

  val userTable = TableQuery[UserTable]

  class UserTable(tag: Tag) extends Table[User](tag, "student") {

    val id = column[Option[Int]]("user_id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("user_name", O.SqlType("VARCHAR(20)"))
    val email = column[String]("user_email", O.SqlType("VARCHAR(30)"))
    val password = column[String]("user_password", O.SqlType("VARCHAR(20)"))
    val mobile = column[String]("user_mobile", O.SqlType("VARCHAR(10)"))
    val isAdmin = column[Boolean]("user_isAdmin", O.SqlType("BOOLEAN"))

    def * = (name, email, password,mobile,isAdmin, id) <> (User.tupled,User.unapply)

  }

}

trait UserRepository extends UserTable {

  this: DBComponent =>
  import driver.api._

  def insert(user:User):Future[Int] = {

    val insertUser = userTable.returning(userTable.map(_.id.get)) += user
    db.run(insertUser)

  }

}
