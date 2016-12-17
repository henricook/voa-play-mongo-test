# voa-play-mongo-test

# About me

Thanks for considering this test. I'm a contractor already working within HMRC with Scala/Mongo/Play so have extensive experience with the MDTP. 
I believe the VOA is linked with the tax/MDTP ecosystem which means you can checkout my work to date from within the organisation (@henri-cook on internal github) or
by looking at my current team's open source contributions (@henricook on github.com) under the HMRC organisation.

You can also contact me on hmrcdigital slack, @henri.cook

# To Run

sbt run

# About the test

This was implemented in around an hour and a half.

## Base

I contemplated using HMRC's Play2.5 template for this (https://github.com/hmrc/example-play-25-microservice) as it would have been more like "real life" as an MDTP citizen. In the end however i've opted for a vanilla play2.5 template (via activator) to keep the code free from any unnecessary complexity. HMRC's Play2.5 template is also relatively new, and still generates some compile warnings due to the need to be backwards compatible.

## Scaling

Of course in a real deployment where scale was a concern, this would be split into at least frontend and backend micro services, if not eventually several domain-oriented
backend microservices.

## Validation

I've used exclusively server-side validation as it's the most reliable, given time client side could be added so the user gets less jarring validation messages.

There are no constraints on the mongo collection, and a person with identical details can apply multiple times. This could be easily remedied as further work.

## Front-end

Given more time, i would have added a base template for views. I would also have considered not using flashing() to pass the user's firstname for the success screen
in favour of a GET parameter (most reliable) or pulling the latest inserted record from the database (fine for this example, but a bad idea in general).

Flashing also leads to some unsatisfactory error handling in applicationSuccess.scala.html

## Architecture

For such a straightforward spec, I could legitimately be accused of overengineering this solution. I've tried to use repository, service and connector patterns to 
demonstrate my knowledge. But if this spec was the entire scope of the project it's likely I would have done much more within the ApplicationController.

I would not normally call repositories directly from a controller as I have here, i'd insert an interim service, in my experience that helps testability.

## Storage of UserData

I've used primitive types for storage, notably only storing the Country's three letter code as a string. I contemplated storing the country as a more complex object
but deemed it unnecessary with the current scope of requirements.

## Datetimes

It has been assumed that the server running the application would be in UTC, as is good practice. LocalDateTime has been use of ZonedDateTime.

## External Dependencies

As required i've used the restcountries.eu endpoint for Europe data, in a real world situation I wouldn't have depended on this third party unless it was a firm
requirement to do so, i would more likely have kept a copy for the application locally where it could be accessed quickly and reliably, and refreshed it as necessary.

I have developed the app to cache this endpoint at startup time so that your first page load is fast, in a more enterprise setting this would likely be considered
bad practice, as an app starting and trying to cache tens or hundreds of data sources simultaneously would likely be sluggish. I'd more likely do a lazy cache or, as mentioned above
bring the data in locally.

## JSON

Is parsed using play-json and case classes from the server-side. Again this could be done client side with an AJAX call, as a potential future improvement.

## Mongo

I have used Reactive Mongo, a reactive, asynchronous and non-blocking Scala driver for MongoDB.

## Testing

Connectors are untested, they could be as a future enhancement with something like wiremock or by mocking WSClient.

Repositories and controllers are integration tested

Other types of class are unit tested

# Requirements

Please complete the following problem to determine your competency with Scala
and Play. Use Scala, Play and MongoDB. Place your final solution in a GitHub
repository. Provide a readme that explains how you have implemented your solution
and how to execute it. Provide the GitHub repository name to your recruitment
contact on completion.

Requirements:

1. Create a web application that allows a user to enter the details shown in the
screen mockups below.

2. Use the json endpoint https://restcountries.eu/rest/v1/region/Europe to populate
the Country drop down.

3. Validate all user input. All inputs are mandatory. Display an appropriate error
message when invalid data is entered.

4. Display the user’s name in the message shown on the second page.

5. Save the validated data to a MongoDB collection. The MongoDB database name
should be nodetest and the collection name should be userdata. The document
properties should be name, sex, age, country and dateCreated.

6. Write tests that test the retrieval of country data and your persistence mechanism.
