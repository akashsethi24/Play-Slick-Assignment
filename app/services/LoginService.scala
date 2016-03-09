package services

import com.google.inject.{ImplementedBy, Inject}
import models.User
import repo.UserRepository
import scala.concurrent.Future

/**
  * Created by akash on 8/3/16.
  */
@ImplementedBy(classOf[LoginService])
trait LoginServiceApi  {

  def getUserByEmail(email:String):Future[Option[User]]

  def isUserAdmin(user:User):Boolean

  def createUserTable():Unit

}

class LoginService @Inject()(userRepo:UserRepository) extends LoginServiceApi {

  override def getUserByEmail(email:String):Future[Option[User]] = {

    userRepo.getUser(email)
  }

  override def isUserAdmin(user:User):Boolean = {

    if(user.isAdmin) {
      true
    }
    else {
      false
    }
  }

  override def createUserTable():Unit = {
    userRepo.creteTable()
  }
}



