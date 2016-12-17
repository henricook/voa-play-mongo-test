package controllers

import javax.inject._

import connectors.restcountries.Country
import models.UserData
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc._
import services.CountryService

import scala.concurrent.{ Await, ExecutionContext }
import scala.concurrent.duration._
import language.postfixOps

/**
  * Application Controller: Presents the application form and processes a submitted application
  */
@Singleton
class ApplicationController @Inject()(val messagesApi: MessagesApi, countryService: CountryService)(implicit exec: ExecutionContext) extends Controller with I18nSupport {

  // TODO: Await is normally a bad pattern, can this be done differently?
  val countryList: List[Country] = Await.result(countryService.getEuropeanCountries, 5 seconds)

  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "sex" -> nonEmptyText.verifying("Please select a valid sex", List("Male", "Female").contains(_)),
      "age" -> number,
      "country" -> nonEmptyText.verifying("Please select a country from the list", countryList.map(x => x.alpha3Code).contains(_))
    )(UserData.apply)(UserData.unapply)
  )

  def present = Action { Ok(views.html.application(form, countryList)) }

  def success = Action { Ok(views.html.applicationSuccess()) }

  def applicationPost = Action { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.application(formWithErrors, countryList))
      },
      contact => {
        // val contactId = Contact.save(contact)
        Redirect(routes.ApplicationController.present).flashing("success" -> "Application saved!")
      }
    )
  }
}


