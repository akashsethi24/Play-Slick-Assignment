package repo

import javax.inject.Inject

import models.Certificates
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

/**
  * Created by akash on 8/3/16.
  */
class CertificatesRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with CertificateTable{

    import driver.api._


    def insertCertificate(certificates: Certificates):Future[Int] = {

        val insertAction = certificateTable.returning(certificateTable.map(_.id)) += certificates
        db.run(insertAction)
    }

    def deleteCertificate(id:Int):Future[Int] = {

        val deleteAction = certificateTable.filter(_.id === id).delete
        db.run(deleteAction)
    }

    def updateCertificate(certificates: Certificates):Future[Int] = {

        val updateCertificate = for{certificate <- certificateTable if certificate.id === certificates.id }yield certificate
        val updateAction = updateCertificate.update(certificates)
        db.run(updateAction)
    }

    def getCertificatesByUser(id:Int):Future[List[Certificates]] = {

        val getCertificate = for{ certificate <- certificateTable if certificate.userId === id }yield certificate
        val getAction = db.run(getCertificate.to[List].result)
        getAction
    }
}

private[repo] trait CertificateTable {
    self: HasDatabaseConfigProvider[JdbcProfile] =>

    import driver.api._

    protected val certificateTable = TableQuery[CertificateTable]

    protected class CertificateTable(tag:Tag) extends Table[Certificates](tag,"certificate") {

        val id = column[Int]("cer_id", O.PrimaryKey, O.AutoInc)
        val userId = column[Int]("user_id")
        val name = column[String]("cer_email", O.SqlType("VARCHAR(30)"))
        val desc = column[String]("cer_desc", O.SqlType("VARCHAR(20)"))
        val year = column[Int]("cer_year")

        def * = (id, userId, name, desc, year) <>(Certificates.tupled, Certificates.unapply)
    }
}