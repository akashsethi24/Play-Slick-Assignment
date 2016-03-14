package repo

import javax.inject.Inject

import models.ProgrammingLanguages
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.Future

/**
  * Created by akash on 8/3/16.
  */
class ProgrammingLangRepository @Inject()(
                                           protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with ProgrammingLangTable {

  import driver.api._

  def createProgrammingTable(): Boolean = {
    db.run(programmingLangTable.schema.create)
    true
  }

  def insertProgrammingLang(
                             programmingLang: ProgrammingLanguages): Future[Int] = {
    val insertAction =
      programmingLangTable
        .returning(programmingLangTable.map(_.id)) += programmingLang
    db.run(insertAction)
  }

  def deleteProgrammingLang(id: Int): Future[Int] = {
    val deleteAction = programmingLangTable.filter(_.id === id).delete
    db.run(deleteAction)
  }

  def updateProgrammingLang(
                             programmingLang: ProgrammingLanguages): Future[Int] = {
    val updateCertificate = for {
      language <- programmingLangTable if language.id === programmingLang.id
    } yield language
    val updateAction = updateCertificate.update(programmingLang)
    db.run(updateAction)
  }

  def getProgrammingLangByUser(id: Int): Future[List[ProgrammingLanguages]] = {
    val getCertificate = for {
      programmingLang <- programmingLangTable if programmingLang.userId === id
    } yield programmingLang
    val getAction = db.run(getCertificate.to[List].result)
    getAction
  }


  def getProgrammingById(id: Int): Future[Option[ProgrammingLanguages]] = {

    db.run(programmingLangTable.filter(_.id === id).result.headOption)
  }

}

private[repo] trait ProgrammingLangTable {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  protected val programmingLangTable = TableQuery[ProgrammingLangTable]

  protected class ProgrammingLangTable(tag: Tag) extends Table[ProgrammingLanguages](tag, "programminglang") {

    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    val userId = column[Int]("user_id")
    val name = column[String]("name", O.SqlType("VARCHAR(30)"))
    val skillLevel = column[String]("skill_level", O.SqlType("VARCHAR(20)"))

    def * =
      (id, userId, name, skillLevel) <>
        (ProgrammingLanguages.tupled, ProgrammingLanguages.unapply)
  }

}

