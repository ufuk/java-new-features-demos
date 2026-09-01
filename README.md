# What's new in Java?

[![Java](https://img.shields.io/badge/Java-26-orange.svg)](https://openjdk.org/)
[![JUnit](https://img.shields.io/badge/JUnit-6.1.3-25A162.svg)](https://junit.org/)
[![AssertJ](https://img.shields.io/badge/AssertJ-3.27.7-blue.svg)](https://assertj.github.io/doc/)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36.svg)](https://maven.apache.org/)

This repository showcases practical code examples of new features and API enhancements introduced in Java releases
subsequent to Java 6, focusing on changes that impact a developer's daily life.

Additional examples will be incorporated with future Java releases. Moreover, existing examples will be refined and
supplemented as needed.

## Overview & Feature Matrix

| Java Version |  Type   | Key Highlights                                                                                                                                                                                                                                           | Demos                                                                      |
|:-------------|:-------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------|
| **Java 7**   | Non-LTS | Diamond operator, try-with-resources, NIO.2 File API, multi-catch, numeric underscores, Fork/Join pool                                                                                                                                                   | [`Java07Tests.java`](src/test/java/io/github/ufuk/java07/Java07Tests.java) |
| **Java 8**   | **LTS** | Lambdas, Stream API, CompletableFuture, Optional, Date/Time API (JSR-310), default & static interface methods                                                                                                                                            | [`Java08Tests.java`](src/test/java/io/github/ufuk/java08/Java08Tests.java) |
| **Java 9**   | Non-LTS | Flow API (Reactive Streams), Collection factory methods (`List.of`, `Map.ofEntries`), `CompletableFuture` timeouts, `takeWhile`/`dropWhile`, `ProcessHandle`, `InputStream.readAllBytes()`                                                               | [`Java09Tests.java`](src/test/java/io/github/ufuk/java09/Java09Tests.java) |
| **Java 10**  | Non-LTS | Local variable type inference (`var`), unmodifiable collections (`List.copyOf`)                                                                                                                                                                          | [`Java10Tests.java`](src/test/java/io/github/ufuk/java10/Java10Tests.java) |
| **Java 11**  | **LTS** | `var` in lambda parameters, String methods (`isBlank`, `lines`, `strip`), `Files.readString`/`writeString`, `HttpClient`                                                                                                                                 | [`Java11Tests.java`](src/test/java/io/github/ufuk/java11/Java11Tests.java) |
| **Java 12**  | Non-LTS | Switch expressions (preview), `Collectors.teeing()`, `CompletableFuture.exceptionallyCompose()`, String indent & transform, `Files.mismatch`, Compact Number Formatting                                                                                  | [`Java12Tests.java`](src/test/java/io/github/ufuk/java12/Java12Tests.java) |
| **Java 13**  | Non-LTS | Text blocks (preview), switch expressions with `yield` (preview)                                                                                                                                                                                         | [`Java13Tests.java`](src/test/java/io/github/ufuk/java13/Java13Tests.java) |
| **Java 14**  | Non-LTS | Records (preview), Pattern matching for `instanceof` (preview), helpful `NullPointerException`s                                                                                                                                                          | [`Java14Tests.java`](src/test/java/io/github/ufuk/java14/Java14Tests.java) |
| **Java 15**  | Non-LTS | Sealed classes (preview), text blocks (GA)                                                                                                                                                                                                               | [`Java15Tests.java`](src/test/java/io/github/ufuk/java15/Java15Tests.java) |
| **Java 16**  | Non-LTS | Records (GA), Pattern matching for `instanceof` (GA), `Stream.toList()`                                                                                                                                                                                  | [`Java16Tests.java`](src/test/java/io/github/ufuk/java16/Java16Tests.java) |
| **Java 17**  | **LTS** | Sealed classes (GA), Pattern matching for `switch` (preview)                                                                                                                                                                                             | [`Java17Tests.java`](src/test/java/io/github/ufuk/java17/Java17Tests.java) |
| **Java 18**  | Non-LTS | Simple Web Server (`jwebserver`), UTF-8 by default, code snippets in Javadoc                                                                                                                                                                             | [`Java18Tests.java`](src/test/java/io/github/ufuk/java18/Java18Tests.java) |
| **Java 19**  | Non-LTS | Virtual threads (preview), record patterns (preview), `Future.resultNow()` / `state()`                                                                                                                                                                   | [`Java19Tests.java`](src/test/java/io/github/ufuk/java19/Java19Tests.java) |
| **Java 20**  | Non-LTS | Record patterns (2nd preview), pattern matching for `switch` (4th preview)                                                                                                                                                                               | [`Java20Tests.java`](src/test/java/io/github/ufuk/java20/Java20Tests.java) |
| **Java 21**  | **LTS** | Sequenced Collections, Virtual threads (GA), Pattern matching for `switch` (GA), Record patterns (GA), Scoped values (preview), Structured Concurrency (preview), Unnamed variables (preview)                                                            | [`Java21Tests.java`](src/test/java/io/github/ufuk/java21/Java21Tests.java) |
| **Java 22**  | Non-LTS | Unnamed variables & patterns (GA), Stream Gatherers (preview), Statements before super (preview)                                                                                                                                                         | [`Java22Tests.java`](src/test/java/io/github/ufuk/java22/Java22Tests.java) |
| **Java 23**  | Non-LTS | Markdown documentation comments, Primitive types in patterns (preview), Simpler main methods update                                                                                                                                                      | [`Java23Tests.java`](src/test/java/io/github/ufuk/java23/Java23Tests.java) |
| **Java 24**  | Non-LTS | Stream Gatherers (GA), Class-File API (GA), Virtual threads unpinned in synchronized, Compact Object Headers (experimental)                                                                                                                              | [`Java24Tests.java`](src/test/java/io/github/ufuk/java24/Java24Tests.java) |
| **Java 25**  | **LTS** | Flexible constructor bodies (GA), Module import declarations (GA), Primitive types in patterns (GA), Scoped values (GA), Compact Object Headers (GA), Structured Concurrency API evolution (preview - `open` & `Joiner`), Stable Values (preview)        | [`Java25Tests.java`](src/test/java/io/github/ufuk/java25/Java25Tests.java) |
| **Java 26**  | Non-LTS | Lazy Constants & Collections (preview - JEP 526: `LazyConstant`, `List.ofLazy`, `Map.ofLazy`), Structured Concurrency Joiner API simplification (`anySuccessfulOrThrow`), Value objects & primitive classes (Valhalla preview), Leyden AOT optimizations | [`Java26Tests.java`](src/test/java/io/github/ufuk/java26/Java26Tests.java) |

## Requirements & Running Tests

- **Java Development Kit (JDK):** OpenJDK 26 (with `--enable-preview` flag enabled)
- **Build Tool:** Apache Maven (Wrapper `./mvnw` included)

### Running Tests

> [!NOTE]
> The Maven wrapper is pre-configured with `--enable-preview` for both compilation and test execution — no extra flags
needed.

```bash
# Run all test suites across all Java versions
./mvnw test

# Run tests for a specific Java release
./mvnw test -Dtest=Java21Tests

# Run a specific test method
./mvnw test -Dtest=Java21Tests#sequenced_collections
```

## Principles & Guidelines

This repository focuses on API and syntax enhancements that most directly impact a Java developer's daily workflow. The
following guidelines and commenting conventions are strictly adhered to across all test suites:

- **First Preview Placement:** Features are demonstrated under the Java version in which they were first introduced as a
  *Preview* feature (annotated with `// preview in Java X, released in Java ?`).
- **Graduation to GA Without Changes:** When a preview feature graduates to General Availability (GA) without structural
  syntax or API changes, no duplicate example is added to the GA release. Instead, the original preview test comment is
  updated from
  `released in Java ?` to `released in Java Y` (e.g., `// preview in Java 14, released in Java 16`).
- **Evolving Preview Features:** If a preview feature evolves with structural syntax or API changes in a subsequent
  release (e.g., Java Y), a new test is introduced under that version (annotated with
  `// preview in Java X, changed in Java Y, released in Java ?`).
- **Preserving Broken Historical Previews:** If evolutionary changes break compilation of older preview code against
  modern compilers, the outdated test is commented out (partially or fully) with a detailed explanatory comment block
  and a cross-reference pointing to the newer version's test method (e.g.,
  `For the updated Structured Concurrency examples, see Java25Tests#...`).
- **Withdrawn or Dropped Features:** If an experimental or preview feature is withdrawn, redesigned from scratch, or
  removed (e.g., *String Templates* withdrawn in Java 23, *Record Patterns in For-Loops* dropped in Java 21), it is
  retained as commented code with explicit JEP tracking notes (e.g.,
  `// preview in Java 20 (JEP 432), dropped in Java 21 (JEP 440)`).
- **Clean Preview Annotations:** Unless a version introduced structural API/syntax alterations, intermediate unchanged
  preview milestones are omitted from the comment tag to maintain readability (preferring
  `// preview in Java X, released in Java ?`
  over redundant intermediate `still preview in Java ...` tags).
- **Pedagogical Clarity & Console Output:** Meaningful `System.out.println` output statements are preserved alongside
  AssertJ assertions so developers running individual tests can immediately observe runtime behaviors (such as virtual
  thread carrier scheduling and task lifecycle events).
- **No Incubation Features:** Experimental/incubator modules and packages (e.g., `jdk.incubator.*`) are excluded.
