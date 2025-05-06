package tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class Runner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(AllTests.class);

        for (Failure failure : result.getFailures()) {
            System.err.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}