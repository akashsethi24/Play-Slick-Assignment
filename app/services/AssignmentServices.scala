package services

import com.google.inject.{ImplementedBy, Inject}
import models.Assignments
import repo.AssignmentRepository

import scala.concurrent.Future

/**
  * Created by akash on 9/3/16.
  */

@ImplementedBy(classOf[AssignmentServices])
trait AssignmentServiceApi{

  def createAssignmentTable():Unit
  def insertAssignment(assignment:Assignments):Future[Int]
  def deleteAssignment(id:Int):Future[Int]
  def updateAssignment(assignments: Assignments):Future[Int]
  def getAssignmentByUser(id:Int):Future[List[Assignments]]
}
class AssignmentServices @Inject()(assignRepo:AssignmentRepository) extends AssignmentServiceApi{

  override def insertAssignment(assignment: Assignments): Future[Int] = {

    assignRepo.insertAssignment(assignment)
  }

  override def updateAssignment(assignments: Assignments): Future[Int] = {

    assignRepo.updateAssignment(assignments)
  }

  override def deleteAssignment(id: Int): Future[Int] = {

    assignRepo.deleteAssignment(id)
  }

  override def getAssignmentByUser(id: Int): Future[List[Assignments]] = {

    assignRepo.geAssignmentByUser(id)
  }

  override def createAssignmentTable(): Unit = {

    assignRepo.createAssignmentTable()
  }

}
