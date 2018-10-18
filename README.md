# Test Runner

This plugin allows you to create tools that operate on Java (JUnit) tests with fine-grained control (e.g., running them in arbitrary orders).

# Installation

If you want the latest version of this plugin (or for some reason you can't get if off Maven central), you can run:

```bash
git clone https://github.com/ReedOei/testrunner
cd testrunner-maven-plugin
mvn install

git clone https://github.com/ReedOei/dt-fixing-tools
cd dt-fixing-tools
mvn install
```

You can generate documentation using:
```
mvn javadoc:javadoc scala:doc
```

# Quickstart

Make sure both *testrunner* and *dt-fixing-tools* have been installed from either Maven Central or Github

## Automatic

First, download both PomFile.java and modify-project.sh from [this link](https://github.com/ReedOei/dt-fixing-tools/tree/master/scripts/docker/pom-modify) into any directory. Run:

```bash
bash ./modify-project.sh ABSOLUTE_PATH_TO_WORKING_DIRECTORY
```

Then, change into your working directory and run:

```bash
{ time -p mvn test -fn |& tee mvn-test.log ;} 2> mvn-test-time.log
```

Then run:

```bash
mvn testrunner:testplugin
```

## Manual

First, create a class that extends the following abstract class:

```java
public abstract class TestPlugin {
    public TestPlugin() {}

    public abstract void execute(final MavenProject project);
}
```

Then, add the following plugin to the `pom.xml` of the Maven project you wish to run your `TestPlugin` on.

```xml
<plugin>
	<groupId>com.reedoei</groupId>
	<artifactId>testrunner-maven-plugin</artifactId>
	<version>1.0-SNAPSHOT</version>
	<configuration>
		<className>FULLY_QUALIFIED_CLASS_NAME_GOES_HERE</className>
	</configuration>
</plugin>
```

Then run:

```bash
mvn testrunner:testplugin
```

# Documentation
See the [wiki](https://github.com/ReedOei/testrunner/wiki).