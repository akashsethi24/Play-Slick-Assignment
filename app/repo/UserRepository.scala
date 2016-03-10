package repo

import javax.inject.Inject
import models.User
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

/**
  * Created by akash on 8/3/16.
  */

class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with UserTable{

  import driver.api._

  def creteTable():Unit = {
    val createQuery = userTable.schema.create
    println(createQuery.statements.head)
    db.run(createQuery)
  }

  def getUser(email: String): Future[Option[User]] = {

    db.run(userTable.filter(_.email === email).result.headOption)

  }
}

private[repo] trait UserTable  {
  self: HasDatabaseConfigProvider[JdbcProfile] =>
  import driver.api._

  protected class UserTable(tag:Tag) extends Table[User](tag,"users") {

    val id = column[Option[Int]]("user_id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("user_name", O.SqlType("VARCHAR(20)"))
    val email = column[String]("user_email", O.SqlType("VARCHAR(30)"))
    val password = column[String]("user_password", O.SqlType("VARCHAR(20)"))
    val mobile = column[String]("user_mobile", O.SqlType("VARCHAR(10)"))
    val isAdmin = column[Boolean]("user_isadmin", O.SqlType("BOOLEAN"))

    def * = (name, email, password,mobile,isAdmin, id) <> (User.tupled,User.unapply)
  }
  lazy val userTable = TableQuery[UserTable]
}