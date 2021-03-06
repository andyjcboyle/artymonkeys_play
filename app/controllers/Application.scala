package controllers

import controllers.email.EmailEnquiry
import model.{NewsHomeHelper, AdminHelper, VenueHelper}
import play.Routes
import play.api.mvc._


object Application extends Controller with EmailEnquiry with Secured {

  def index = SecureAction { request =>
    Ok(views.html.index(isAdminUser(request), ApplicationCake.newsHomeService.getLastNewsItems(5)))
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

  def classVenueTimes(classLoc: String) = Action { request =>
    Ok(VenueHelper.getVenueTimesHtml(classLoc))
  }

  def locationClassTimes: List[String] = {
    List("Hello1", "Hello2")
  }

  def skills = SecureAction { request =>
    Ok(views.html.skills(isAdminUser(request)))
  }

  def artyparties = SecureAction { request =>
    Ok(views.html.artyparties(isAdminUser(request)))
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
      Ok(views.html.managenews(true, ApplicationCake.newsHomeService.getLastNewsItems(20)))
    } else {
      Unauthorized(views.html.unauthorised())
    }
  }

  def managenewsDeleteItem(newsItemId: String) = SecureAction { request =>
    println("Delete: Request is " + request)
    if (isAdminUser(request)) {
      println("And deleting item : " + newsItemId)
      ApplicationCake.newsHomeService.delete(newsItemId)
      val newsItems = ApplicationCake.newsHomeService.getLastNewsItems(20)
      Ok(views.html.managenews(true, newsItems))
    } else {
      Unauthorized(views.html.unauthorised())
    }
  }

  def managenewspost = SecureAction { request =>
    if (isAdminUser(request)) {
      NewsHomeHelper.addNewsItem(request)
      Ok(views.html.managenews(true, ApplicationCake.newsHomeService.getLastNewsItems(20)))
    } else {
      Unauthorized(views.html.unauthorised())
    }
  }

  def customers = SecureAction { request =>
    if (isAdminUser(request)) {
      val allCustomers = ApplicationCake.customerService.retrieveAllCustomers()
      Ok(views.html.showCustomers(true, allCustomers))
    } else {
      Unauthorized(views.html.unauthorised())
    }
  }

  def venues = SecureAction { request =>
    if (isAdminUser(request)) {
      val allVenues = ApplicationCake.venueService.retrieveAllVenues()
      Ok(views.html.venues(true, allVenues))
    } else {
      Unauthorized(views.html.unauthorised())
    }
  }

  private def isAdminUser(request: Request[AnyContent]): Boolean = {
    val user = userIdFromHeader(request)
    AdminHelper.isAdminUser(user)
  }

}
