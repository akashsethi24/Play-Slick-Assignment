package services

import com.google.inject.{Inject, ImplementedBy}
import models.ProgrammingLanguages
import repo.{ProgrammingLangRepository, AssignmentRepository}

import scala.concurrent.Future

/**
  * Created by akash on 9/3/16.
  */
@ImplementedBy(classOf[ProgrammingServices])
trait ProgrammingServiceApi {
  def createProgrammingTable(): Boolean
  def insertProgramming(programmingLang: ProgrammingLanguages): Future[Int]
  def deleteProgramming(id: Int): Future[Int]
  def updateProgramming(programmingLang: ProgrammingLanguages): Future[Int]
  def getProgrammingByUser(id: Int): Future[List[ProgrammingLanguages]]
}

class ProgrammingServices @Inject()(programmingRepo: ProgrammingLangRepository)
    extends ProgrammingServiceApi {

  override def insertProgramming(
      programmingLang: ProgrammingLanguages): Future[Int] = {
    programmingRepo.insertProgrammingLang(programmingLang)
  }

  override def getProgrammingByUser(
      id: Int): Future[List[ProgrammingLanguages]] = {
    programmingRepo.getProgrammingLangByUser(id)
  }

  override def updateProgramming(
      programmingLang: ProgrammingLanguages): Future[Int] = {
    programmingRepo.updateProgrammingLang(programmingLang)
  }

  override def deleteProgramming(id: Int): Future[Int] = {
    programmingRepo.deleteProgrammingLang(id)
  }

  override def createProgrammingTable(): Boolean = {
    programmingRepo.createProgrammingTable()
  }
}
