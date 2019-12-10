This is a copy of https://github.com/SonarSource/sonar-custom-rules-examples/tree/master/java-custom-rules

Custom Sonar rule for checking logger declaration in all class, except which are marked with special annotation like @Nologger.

Examples:
#### Noncompliant Code Example
<pre>
    public class Foo {
        Foo() {...}
    }
</pre>
#### Compliant Solution
<pre>
    public class Foo {
        private static final Logger LOGGER = ... // continue e.g.: LoggerFactory.getLogger(Foo.class)
        Foo() {...}
    }
</pre>
I you want to ignore class the scan, you can use @NoLogger (or any other custom) annotation, for example:
<pre>
    @NoLogger
    public class Foo {
        Foo() {...}
    }
</pre>

#### Todos:
* create tests for rule