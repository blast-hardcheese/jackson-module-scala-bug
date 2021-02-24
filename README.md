Repo to show `ObjectMapper` with `DefaultScalaModule` 2.12.0+ causes some new and creative runtime errors.

### First issue

In the first case, methods annotated with `@JsonCreator` with the same name but different types now require that the _parameter names_ be different, otherwise 

    Conflicting property-based creators: already had explicitly marked creator [method regression.ConflictingJsonCreator#apply(long)],
    encountered another: [method regression.ConflictingJsonCreator#apply(java.lang.String)]

### Second issue

In the second case, all `@JsonCreator`'s must have different parameter names from the _member of the value class_, otherwise:

    Cannot construct instance of `regression.ConflictingMember` (although at least one Creator exists):
    no int/Int-argument constructor/factory method to deserialize from Number value (10)
