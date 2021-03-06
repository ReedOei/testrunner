package com.reedoei.testrunner.data.results;

import com.google.gson.Gson;
import edu.illinois.cs.dt.tools.diagnosis.DiffContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestRunResult {
    public static TestRunResult empty(final String id) {
        return new TestRunResult(id, new ArrayList<>(), new HashMap<>(), new HashMap<>());
    }

    private final String id;
    private final List<String> testOrder;
    private final Map<String, TestResult> results;
    private final Map<String, DiffContainer> diffs;

    public TestRunResult(final String id, final List<String> testOrder,
                         final Map<String, TestResult> results, final Map<String, DiffContainer> diffs) {
        this.id = id;
        this.testOrder = testOrder;
        this.results = results;
        this.diffs = diffs;
    }

    public String id() {
        return id;
    }

    public List<String> testOrder() {
        return testOrder;
    }

    public Map<String, TestResult> results() {
        return results;
    }

    public Map<String, DiffContainer> diffs() {
        return diffs;
    }

    private PrintStream outputStream(final String outputPath) throws IOException {
        if (outputPath == null || outputPath.equals("stdout")) {
            return System.out;
        } else {
            final File f = new File(outputPath);
            Files.createDirectories(f.toPath().getParent());
            return new PrintStream(new FileOutputStream(new File(outputPath)));
        }
    }

    public void writeTo(final String outputPath) {
        try (final PrintStream p = outputStream(outputPath)) {
            p.print(toString());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
