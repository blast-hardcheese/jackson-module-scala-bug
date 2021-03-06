name := "regression"

// Last known good version: 2.11.4
// Tested versions:
//   Pass:
//   - 2.11.4
//   Fail:
//   - 2.12.0
//   - 2.12.1
val jacksonVersion         = "2.12.1"

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core"     %  "jackson-core"            % jacksonVersion,
  "com.fasterxml.jackson.core"     %  "jackson-databind"        % jacksonVersion,
  "com.fasterxml.jackson.core"     %  "jackson-annotations"     % jacksonVersion,
  "com.fasterxml.jackson.datatype" %  "jackson-datatype-jsr310" % jacksonVersion,
  "com.fasterxml.jackson.module"   %% "jackson-module-scala"    % jacksonVersion,
)
