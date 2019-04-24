package services

import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import spray.json.{DefaultJsonProtocol, JsBoolean, JsNull, JsNumber, JsObject, JsString, JsValue, NullOptions, RootJsonFormat}
import spray.json._

object JsonEitherSpec extends /*Specification with*/ SprayJsonSupport with DefaultJsonProtocol {
  object Data {

    case class SuccessJson[A](result: A)

    object SuccessJson {

      implicit def successFormat[A](implicit format: JsonFormat[A]) = new RootJsonFormat[SuccessJson[A]] {

        override def write(value: SuccessJson[A]): JsValue = {
          JsObject("ok" -> JsBoolean(true), "result" -> format.write(value.result))
        }

        override def read(json: JsValue): SuccessJson[A] = {
          val root = json.asJsObject
          (root.fields.get("ok"), root.fields.get("result")) match {
            case (Some(JsTrue), Some(jsValue)) => SuccessJson(format.read(jsValue))
            case _ => deserializationError("JSON not a Success")
          }
        }
      }
    }

    case class FailureJson(reason: String)

    object FailureJson {

      implicit object failureFormat extends RootJsonFormat[FailureJson] {

        override def write(value: FailureJson): JsValue = {
          JsObject("ok" -> JsBoolean(false), "error" -> JsString(value.reason))
        }

        override def read(json: JsValue): FailureJson = {
          val root = json.asJsObject
          (root.fields.get("ok"), root.fields.get("error")) match {
            case (Some(JsFalse), Some(JsString(reason))) => FailureJson(reason)

            case _ => deserializationError("JSON not a Failure")
          }
        }
      }
    }

    type Result[ A ] = Either[ FailureJson, SuccessJson[A] ]

    implicit def rootEitherFormat[A : RootJsonFormat, B : RootJsonFormat] = new RootJsonFormat[Either[A, B]] {
      val format = DefaultJsonProtocol.eitherFormat[A, B]

      def write(either: Either[A, B]) = format.write(either)

      def read(value: JsValue) = format.read(value)
    }

  }

  /*"Example Unmarshalling JSON Response as Either[Failure, Success]" >> {
    import Data._

    "unmarshalling a Success[String] json response" in {
      val result: Result[String] =  Right(Success("Success!"))
      val jsonResponse = HttpResponse(status = StatusCodes.OK, entity = marshalUnsafe(result))
      jsonResponse.as[Result[String]] must beRight(Right(Success("Success!")))
    }

    "unmarshalling a Failure json response" in {
      val result: Result[String] = Left(Failure("Failure!"))
      val jsonResponse = HttpResponse(status = StatusCodes.OK, entity = marshalUnsafe(result))
      jsonResponse.as[Result[String]] must beRight(Left(Failure("Failure!")))
    }
  }*/
}