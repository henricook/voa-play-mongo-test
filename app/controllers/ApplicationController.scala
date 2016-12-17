package controllers

import javax.inject._

import connectors.restcountries.Country
import models.UserData
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc._
import repositories.UserdataRepository
import services.CountryService

import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.concurrent.duration._
import language.postfixOps

/**
  * Application Controller: Presents the application form and processes a submitted application
  */
@Singleton
class ApplicationController @Inject()(val messagesApi: MessagesApi,
                                      countryService: CountryService,
                                      userDataRepository: UserdataRepository)(implicit exec: ExecutionContext) extends Controller with I18nSupport {

  // TODO: Await is normally a bad pattern, can this be done differently?
  val countryList: List[Country] = Await.result(countryService.getEuropeanCountries, 5 seconds)

  /**
    * The application form
    */
  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "sex" -> nonEmptyText.verifying("Please select a valid sex", List("Male", "Female").contains(_)),
      "age" -> number,
      "country" -> nonEmptyText.verifying("Please select a country from the list", countryList.map(x => x.alpha3Code).contains(_))
    )(UserData.apply)(UserData.unapply)
  )

  /**
    * Present the main page
    * @return
    */
  def present = Action { Ok(views.html.application(form, countryList)) }

  /**
    * Present a success message after application submission
    * @return
    */
  def success = Action { implicit request =>
    Ok(views.html.applicationSuccess())
  }

  /**
    * Handle application submission
    * @return
    */
  def applicationPost = Action.async { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.application(formWithErrors, countryList)))
      },
      application => {
        userDataRepository.create(application).map { _ =>
          Redirect(routes.ApplicationController.success).flashing("name" -> application.name)
        }.recoverWith {
          case ex: Throwable =>
            Logger.error("Database error when trying to write application", ex)
            Future.successful(Ok("There was a database error"))
        }
      }
    )
  }
}


