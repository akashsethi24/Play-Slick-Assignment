package services

import com.google.inject.{ImplementedBy, Inject}
import models.Certificates

import scala.concurrent.Future

/**
  * Created by akash on 8/3/16.
  */
@ImplementedBy(classOf[CertificateServices])
trait CertificateServiceApi{

  def insertCertificate(certificate:Certificates):Future[Int]
  def deleteCertificate(id:Int):Future[Int]
  def updateCertificate(certificates: Certificates):Future[Int]
  def getCertificateByUser(id:Int):Future[List[Certificates]]
}
class CertificateServices @Inject()(certRepo:CertificateServiceApi) extends CertificateServiceApi{

  override def insertCertificate(certificate: Certificates): Future[Int] = {

    certRepo.insertCertificate(certificate)
  }

  override def deleteCertificate(id: Int): Future[Int] = {

    certRepo.deleteCertificate(id)
  }

  override def updateCertificate(certificates: Certificates): Future[Int] = {

    certRepo.updateCertificate(certificates)
  }

  override def getCertificateByUser(id: Int): Future[List[Certificates]] = {

    certRepo.getCertificateByUser(id)
  }
}
