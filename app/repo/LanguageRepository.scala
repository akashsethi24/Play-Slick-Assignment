package repo

import javax.inject.Inject
import models.Languages
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.Future

/**
  * Created by akash on 8/3/16.
  */
class LanguageRepository @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)
    extends HasDatabaseConfigProvider[JdbcProfile] with LanguageTable {
  import driver.api._

  def createLanguageTable(): Unit = {
    db.run(languageTable.schema.create)
  }

  def insertLanguage(languages: Languages): Future[Int] = {
    val insertAction =
      languageTable.returning(languageTable.map(_.id)) += languages
    db.run(insertAction)
  }

  def deleteLanguage(id: Int): Future[Int] = {
    val deleteAction = languageTable.filter(_.id === id).delete
    db.run(deleteAction)
  }

  def updateLanguage(languages: Languages): Future[Int] = {
    val updateCertificate = for {
      language <- languageTable if language.id === languages.id
    } yield language
    val updateAction = updateCertificate.update(languages)
    db.run(updateAction)
  }

  def getLanguageByUser(id: Int): Future[List[Languages]] = {
    val getCertificate = for {
      language <- languageTable if language.userId === id
    } yield language
    val getAction = db.run(getCertificate.to[List].result)
    getAction
  }
}

private [repo] trait LanguageTable {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  protected val languageTable = TableQuery[LanguageTable]

  protected class LanguageTable(tag: Tag)
      extends Table[Languages](tag, "language") {
    val id = column[Int]("lang_id", O.PrimaryKey, O.AutoInc)
    val userId = column[Int]("user_id")
    val name = column[String]("language", O.SqlType("VARCHAR(30)"))
    val fluency = column[String]("lang_fluency", O.SqlType("VARCHAR(20)"))

    def * =
      (id, userId, name, fluency) <> (Languages.tupled, Languages.unapply)
  }
}
