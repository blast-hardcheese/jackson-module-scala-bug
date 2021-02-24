package regression

import scala.util.Try

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.core.`type`.TypeReference

// Minimal reproducing class for the functional case.
// The `apply` methods have different _parameter names_, which does not exhibit the undesirable behaviour.
class PositiveLong private (val value: Long) {
  override def toString() = s"PositiveLong($value)"
}
object PositiveLong {
  @JsonCreator
  def apply(long: Long): PositiveLong = new PositiveLong(long)
  @JsonCreator
  def apply(str: String): PositiveLong = new PositiveLong(str.toLong)
}

// Minimal reproducing class for the first failure case.
// The `apply` methods have the same _parameter names_, which causes:
//   Conflicting property-based creators: already had explicitly marked creator [method regression.ConflictingJsonCreator#apply(long)],
//   encountered another: [method regression.ConflictingJsonCreator#apply(java.lang.String)]
class ConflictingJsonCreator private (val value: Long) {
  override def toString() = s"ConflictingJsonCreator($value)"
}
object ConflictingJsonCreator {
  @JsonCreator
  def apply(value: Long): ConflictingJsonCreator = new ConflictingJsonCreator(value)
  @JsonCreator
  def apply(value: String): ConflictingJsonCreator = new ConflictingJsonCreator(value.toLong)
}

// Minimal reproducing class for the second failure case.
// The `apply` method has the same parameter name as the value class's _member_, which causes:
//   Cannot construct instance of `regression.ConflictingMember` (although at least one Creator exists):
//   no int/Int-argument constructor/factory method to deserialize from Number value (10)
class ConflictingMember private (val value: Long) {
  override def toString() = s"ConflictingMember($value)"
}
object ConflictingMember {
  @JsonCreator
  def apply(value: Long): ConflictingMember = new ConflictingMember(value)
}

object Bug extends App {
  implicit val mapper: ObjectMapper = new ObjectMapper()
    .registerModule(DefaultScalaModule) // When this line is present, convertValue below fails. When it is absent, no errors.

  val node: com.fasterxml.jackson.databind.JsonNode = mapper.valueToTree[com.fasterxml.jackson.databind.node.IntNode](10)

  println(Try(mapper.convertValue(node, new com.fasterxml.jackson.core.`type`.TypeReference[PositiveLong] {})))
  println(Try(mapper.convertValue(node, new com.fasterxml.jackson.core.`type`.TypeReference[ConflictingJsonCreator] {})))
  println(Try(mapper.convertValue(node, new com.fasterxml.jackson.core.`type`.TypeReference[ConflictingMember] {})))
}
