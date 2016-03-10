package services

import com.google.inject.{ImplementedBy, Inject}
import models.Languages
import repo.LanguageRepository
import scala.concurrent.Future

/**
  * Created by akash on 9/3/16.
  */
@ImplementedBy(classOf[LanguageServices])
trait LanguageServiceApi{

  def createLanguageTable():Unit
  def insertLanguage(language:Languages):Future[Int]
  def deleteLanguage(id:Int):Future[Int]
  def updateLanguage(languages: Languages):Future[Int]
  def getLanguageByUser(id:Int):Future[List[Languages]]
}

class LanguageServices @Inject()(langRepo:LanguageRepository) extends LanguageServiceApi{

  override def insertLanguage(language: Languages): Future[Int] = {

    langRepo.insertLanguage(language)
  }

  override def deleteLanguage(id: Int): Future[Int] = {

    langRepo.deleteLanguage(id)
  }

  override def updateLanguage(languages: Languages): Future[Int] = {

    langRepo.updateLanguage(languages)
  }

  override def getLanguageByUser(id: Int): Future[List[Languages]] = {

    langRepo.getLanguageByUser(id)
  }

  override def createLanguageTable(): Unit = {

    langRepo.createLanguageTable()
  }
}