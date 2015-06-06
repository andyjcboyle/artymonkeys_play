package controllers

import controllers.dao.{NewsHomeDao, CustomerDao, UserDao}
import controllers.email.{EmailEnquiry, EmailInterestRegistered}
import play.Routes
import play.api.mvc._


object Application extends Controller
with EmailInterestRegistered with EmailEnquiry with Secured {

  val usersDao = new UserDao()
  val customersDao = new CustomerDao()
  val newsHomeDao = new NewsHomeDao()

  def index = SecureAction { request =>
    Ok(views.html.index(isAdminUser(request), newsHomeDao.getLastNewsItems()))
  }

  def about = SecureAction { request =>
    Ok(views.html.about(isAdminUser(request)))
  }

  def classes = SecureAction { request =>
    Ok(views.html.classes(isAdminUser(request)))
  }

  def contacts = SecureAction { request =>
    Ok(views.html.contacts(isAdminUser(request)))
  }

  //  def locationClassTimes(classLocation: String) = SecureAction { request =>
  //    val validClassTimesForLocation = List("Hello1", "Hello2", classLocation)
  //    Ok(views.html.contacts(!isAdminUser(request), validClassTimesForLocation))
  //  }

//  def ajaxTest() = SecureAction { request =>
  def ajaxTest(classLoc: String) = Action { request =>
    if (classLoc == "baristas") {
      Ok(
        """
          |  <div id="monkeyclasstimesdiv" class="formelement">
          |  Time:
          |     <select id="monkeyclasstimes" class="howdidyouhear" name="time" required>
          |     <option selected disabled hidden value=''></option>
          |     <option value='9to10'>9am to 10am</option>
          |     <option value='10to11'>10am to 11am</option>
          |     <option value='11to12'>11am to 12noon</option>
          |     <option value='12to13'>12noon to 1pm</option>
          |     </select>
          |  </div>
        """.stripMargin)
    } else {
      Ok(
        """
          |  <div id="monkeyclasstimesdiv" class="formelement">
          |  Time:
          |     <select id="monkeyclasstimes" class="howdidyouhear" name="time" required>
          |     <option selected disabled hidden value=''></option>
          |     <option value='10to11'>10am to 11am</option>
          |     </select>
          |  </div>
        """.stripMargin)
    }
  }

  def locationClassTimes: List[String] = {
    List("Hello1", "Hello2")
  }

  def skills = SecureAction { request =>
    Ok(views.html.skills(isAdminUser(request)))
  }

  def monkeynews = SecureAction { request =>
    Ok(views.html.monkeynews(isAdminUser(request)))
  }

  def oauth2call = SecureAction { request =>
    Ok(views.html.oauth())
  }

  def loginSuccess = SecureAction { request =>
    if (isAdminUser(request)) {
      Ok(views.html.loginsuccess(isAdminUser(request)))
    } else {
      Unauthorized(views.html.unauthorised())
    }
  }

  def logoutSuccess = SecureAction {
    Redirect(routes.Application.index()).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

  def login = SecureAction { implicit request =>
    Ok(views.html.login())
  }

  def logout = SecureAction {
    Redirect(routes.Application.index()).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

  def unauthorised = SecureAction {
    Ok(views.html.unauthorised())
  }

  def managenews = SecureAction { request =>
    if (isAdminUser(request)) {
      Ok(views.html.managenews(true, newsHomeDao.getLastNewsItems()))
    } else {
      Unauthorized(views.html.unauthorised())
    }
  }

  def managenewspost = SecureAction { request =>
    if (isAdminUser(request)) {
      Ok(views.html.managenews(true, newsHomeDao.getLastNewsItems()))
    } else {
      Unauthorized(views.html.unauthorised())
    }
  }

  def customers = SecureAction { request =>
    if (isAdminUser(request)) {
      val allCustomers = customersDao.retrieveAllCustomers()
      Ok(views.html.showCustomers(true, allCustomers))
    } else {
      Unauthorized(views.html.unauthorised())
    }
  }

  private def isAdminUser(request: Request[AnyContent]): Boolean = {
    val user = userIdFromHeader(request)
    val users = usersDao.retrieveAllUsers()
    if (user.isDefined && users.exists(theuser => theuser.id == user.get.toString.replace("\"", ""))) {
      true
    } else {
      false
    }
  }

}
