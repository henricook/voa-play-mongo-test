package controllers

import java.time.LocalDateTime
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

  /**
    * The application form
    */
  def form(countryList: List[Country]) = Form(
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
  def present = Action.async {
    countryService.getEuropeanCountries.map { countryList =>
      Ok(views.html.application(form(countryList), countryList))
    }
  }

  /**
    * Present a success message after application submission
    * @return
    */
  def success = Action { implicit request =>
    Ok(views.html.applicationSuccess())
  }

  /**
    * Handle application submission
    *
    * @return
    */
  def applicationPost = Action.async { implicit request =>
    countryService.getEuropeanCountries.flatMap { countryList =>
      form(countryList).bindFromRequest.fold(
        formWithErrors => {
          Future.successful(BadRequest(views.html.application(formWithErrors, countryList)))
        },
        application => {
          (for {
            _ <- userDataRepository.create(models.persisted.UserData.apply(application))
          } yield {
            Redirect(routes.ApplicationController.success).flashing("name" -> application.name)
          }).recoverWith {
            case ex: Throwable =>
              Logger.error("Database error when trying to write application", ex)
              Future.successful(Ok("There was a database error"))
          }
        }
      )
    }
  }
}


