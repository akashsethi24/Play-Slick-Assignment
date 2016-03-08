package models

/**
  * Created by akash on 8/3/16.
  */
case class User(id:Int,email:String,password:String,mobile:Long,isAdmin:Boolean)

case class Certificates(id:Int,userId:Int,name:String,description:String,year:Int)

case class Languages(id:Int,userId:Int,name:String,fluency:String)

case class Assignments(id:Int,userId:Int,name:String,date:String,remark:String,marks:Int)

case class ProgrammingLanguages(id:Int,userId:Int,name:String,fluency:String)
