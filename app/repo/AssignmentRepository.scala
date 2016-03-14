package repo

import javax.inject.Inject

import models.Assignments
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.Future

/**
  * Created by akash on 8/3/16.
  */
class AssignmentRepository @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)
    extends HasDatabaseConfigProvider[JdbcProfile] with AssignmentTable {
  import driver.api._

  def createAssignmentTable(): Boolean = {
    db.run(assignmentTable.schema.create)
    true
  }

  def insertAssignment(assignments: Assignments): Future[Int] = {
    val insertAction =
      assignmentTable.returning(assignmentTable.map(_.id)) += assignments
    db.run(insertAction)
  }

  def deleteAssignment(id: Int): Future[Int] = {
    val deleteAction = assignmentTable.filter(_.id === id).delete
    db.run(deleteAction)
  }

  def updateAssignment(assignments: Assignments): Future[Int] = {
    val updateCertificate = for {
      assignment <- assignmentTable if assignment.id === assignments.id
    } yield assignment
    val updateAction = updateCertificate.update(assignments)
    db.run(updateAction)
  }

  def geAssignmentByUser(id: Int): Future[List[Assignments]] = {
    val getCertificate = for {
      assignment <- assignmentTable if assignment.userId === id
    } yield assignment
    val getAction = db.run(getCertificate.to[List].result)
    getAction
  }
}

private[repo] trait AssignmentTable {
  self: HasDatabaseConfigProvider[JdbcProfile] =>
  import driver.api._

  protected class AssignmentTable(tag: Tag)
      extends Table[Assignments](tag, "assignment") {
    val id = column[Int]("assign_id", O.PrimaryKey, O.AutoInc)
    val userId = column[Int]("user_id")
    val name = column[String]("assign_name", O.SqlType("VARCHAR(30)"))
    val date = column[String]("assign_date", O.SqlType("VARCHAR(20)"))
    val remark = column[String]("assign_remark", O.SqlType("VARCHAR(100)"))
    val marks = column[Int]("assign_marks")

    def * =
      (id, userId, name, date, remark, marks) <>
      (Assignments.tupled, Assignments.unapply)
  }
  protected val assignmentTable = TableQuery[AssignmentTable]
}
