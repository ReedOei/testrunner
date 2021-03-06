package com.reedoei.testrunner.runner;

import com.reedoei.testrunner.data.framework.TestFramework;
import com.reedoei.testrunner.util.MavenClassLoader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import scala.Option;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RunnerFactory {
    public static Option<Runner> from(final MavenProject project) {
        return TestFramework.testFramework(project)
                .map(framework -> create(framework, new MavenClassLoader(project).classpath(),
                        surefireEnvironment(project), project.getBasedir().toPath()));
    }

    public static Runner create(final TestFramework framework, final String classpath,
                                final Map<String, String> environment, final Path outputPath) {
        return SmartRunner.withFramework(framework, classpath, environment, outputPath);
    }

    private static <T> Stream<T> emptyIfNull(final T t) {
        return t == null ? Stream.empty() : Stream.of(t);
    }

    public static Map<String, String> surefireEnvironment(final MavenProject project) {
        return project.getBuildPlugins().stream()
                .filter(p -> p.getArtifactId().equals("maven-surefire-plugin"))
                .flatMap(p -> emptyIfNull(p.getConfiguration()))
                .flatMap(conf -> {
                    if (conf instanceof Xpp3Dom) {
                        return Stream.of((Xpp3Dom)conf);
                    } else {
                        return Stream.empty();
                    }
                })
                .flatMap(conf -> emptyIfNull(conf.getChild("environmentVariables")))
                .flatMap(envVars -> emptyIfNull(envVars.getChildren()))
                .flatMap(Arrays::stream)
                .collect(Collectors.toMap(Xpp3Dom::getName, v -> v.getValue() == null ? "" : v.getValue()));
    }
}
