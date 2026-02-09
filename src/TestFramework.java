import java.util.*;
import java.lang.reflect.*;

/**
 * Unit Test Framework for VirtualXander
 * Simple testing utilities for test creation
 * Part of Phase 4: Code Quality
 */
public class TestFramework {
    
    private static TestFramework instance;
    private int totalTests;
    private int passedTests;
    private int failedTests;
    private List<String> failureMessages;
    private long startTime;
    
    private TestFramework() {
        this.totalTests = 0;
        this.passedTests = 0;
        this.failedTests = 0;
        this.failureMessages = new ArrayList<>();
    }
    
    /**
     * Gets singleton instance
     */
    public static synchronized TestFramework getInstance() {
        if (instance == null) {
            instance = new TestFramework();
        }
        return instance;
    }
    
    /**
     * Asserts that a condition is true
     */
    public void assertTrue(String message, boolean condition) {
        totalTests++;
        if (condition) {
            passedTests++;
        } else {
            failedTests++;
            failureMessages.add("FAILED: " + message + " - Expected true but was false");
        }
    }
    
    /**
     * Asserts that a condition is false
     */
    public void assertFalse(String message, boolean condition) {
        totalTests++;
        if (!condition) {
            passedTests++;
        } else {
            failedTests++;
            failureMessages.add("FAILED: " + message + " - Expected false but was true");
        }
    }
    
    /**
     * Asserts that two objects are equal
     */
    public void assertEquals(String message, Object expected, Object actual) {
        totalTests++;
        if (Objects.equals(expected, actual)) {
            passedTests++;
        } else {
            failedTests++;
            failureMessages.add("FAILED: " + message + " - Expected [" + expected + "] but was [" + actual + "]");
        }
    }
    
    /**
     * Asserts that two strings are equal (case insensitive)
     */
    public void assertEqualsIgnoreCase(String message, String expected, String actual) {
        totalTests++;
        if (expected == null && actual == null) {
            passedTests++;
        } else if (expected != null && expected.equalsIgnoreCase(actual)) {
            passedTests++;
        } else {
            failedTests++;
            failureMessages.add("FAILED: " + message + " - Expected [" + expected + "] but was [" + actual + "] (case insensitive)");
        }
    }
    
    /**
     * Asserts that an object is null
     */
    public void assertNull(String message, Object object) {
        totalTests++;
        if (object == null) {
            passedTests++;
        } else {
            failedTests++;
            failureMessages.add("FAILED: " + message + " - Expected null but was [" + object + "]");
        }
    }
    
    /**
     * Asserts that an object is not null
     */
    public void assertNotNull(String message, Object object) {
        totalTests++;
        if (object != null) {
            passedTests++;
        } else {
            failedTests++;
            failureMessages.add("FAILED: " + message + " - Expected not null but was null");
        }
    }
    
    /**
     * Asserts that a collection is not empty
     */
    public void assertNotEmpty(String message, Collection<?> collection) {
        totalTests++;
        if (collection != null && !collection.isEmpty()) {
            passedTests++;
        } else {
            failedTests++;
            failureMessages.add("FAILED: " + message + " - Expected non-empty collection");
        }
    }
    
    /**
     * Asserts that an exception is thrown
     */
    public void assertThrows(String message, Class<? extends Throwable> expectedException, Runnable code) {
        totalTests++;
        try {
            code.run();
            failedTests++;
            failureMessages.add("FAILED: " + message + " - Expected " + expectedException.getSimpleName() + " but no exception was thrown");
        } catch (Throwable e) {
            if (expectedException.isInstance(e)) {
                passedTests++;
            } else {
                failedTests++;
                failureMessages.add("FAILED: " + message + " - Expected " + expectedException.getSimpleName() + 
                    " but got " + e.getClass().getSimpleName());
            }
        }
    }
    
    /**
     * Asserts that a value is greater than another
     */
    public <T extends Comparable<T>> void assertGreaterThan(String message, T expected, T actual) {
        totalTests++;
        if (expected != null && actual != null && actual.compareTo(expected) > 0) {
            passedTests++;
        } else {
            failedTests++;
            failureMessages.add("FAILED: " + message + " - Expected [" + actual + "] to be greater than [" + expected + "]");
        }
    }
    
    /**
     * Asserts that a value is less than another
     */
    public <T extends Comparable<T>> void assertLessThan(String message, T expected, T actual) {
        totalTests++;
        if (expected != null && actual != null && actual.compareTo(expected) < 0) {
            passedTests++;
        } else {
            failedTests++;
            failureMessages.add("FAILED: " + message + " - Expected [" + actual + "] to be less than [" + expected + "]");
        }
    }
    
    /**
     * Asserts that a string contains a substring
     */
    public void assertContains(String message, String container, String substring) {
        totalTests++;
        if (container != null && container.contains(substring)) {
            passedTests++;
        } else {
            failedTests++;
            failureMessages.add("FAILED: " + message + " - Expected [" + container + "] to contain [" + substring + "]");
        }
    }
    
    /**
     * Runs a test method
     */
    public void runTest(String testName, Runnable testCode) {
        System.out.println("Running test: " + testName);
        try {
            testCode.run();
        } catch (Throwable e) {
            failedTests++;
            failureMessages.add("FAILED: " + testName + " - Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Runs all tests in a class
     */
    public void runTestClass(Object testClass) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Running tests for: " + testClass.getClass().getSimpleName());
        System.out.println("=".repeat(50));
        
        startTime = System.currentTimeMillis();
        
        for (Method method : testClass.getClass().getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                try {
                    method.invoke(testClass);
                } catch (InvocationTargetException e) {
                    failedTests++;
                    failureMessages.add("FAILED: " + method.getName() + " - Exception: " + e.getCause().getMessage());
                } catch (IllegalAccessException e) {
                    failedTests++;
                    failureMessages.add("FAILED: " + method.getName() + " - Cannot access method");
                }
            }
        }
    }
    
    /**
     * Prints test results
     */
    public void printResults() {
        long duration = System.currentTimeMillis() - startTime;
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST RESULTS");
        System.out.println("=".repeat(50));
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + failedTests);
        System.out.println("Duration: " + duration + "ms");
        System.out.println("=".repeat(50));
        
        if (!failureMessages.isEmpty()) {
            System.out.println("\nFAILURES (" + failureMessages.size() + "):");
            for (String failure : failureMessages) {
                System.out.println("  - " + failure);
            }
        }
        
        if (failedTests == 0) {
            System.out.println("\n✓ ALL TESTS PASSED!");
        } else {
            System.out.println("\n✗ SOME TESTS FAILED!");
        }
    }
    
    /**
     * Resets test framework
     */
    public void reset() {
        totalTests = 0;
        passedTests = 0;
        failedTests = 0;
        failureMessages.clear();
    }
    
    /**
     * Gets test statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", totalTests);
        stats.put("passed", passedTests);
        stats.put("failed", failedTests);
        stats.put("passRate", totalTests > 0 ? (double) passedTests / totalTests * 100 : 0);
        return stats;
    }
    
    /**
     * Gets failure messages
     */
    public List<String> getFailureMessages() {
        return new ArrayList<>(failureMessages);
    }
    
    /**
     * Checks if all tests passed
     */
    public boolean allTestsPassed() {
        return failedTests == 0;
    }
    
    /**
     * Creates a mock object for testing
     */
    public <T> T createMock(Class<T> interfaceClass) {
        // Simple mock creation using Proxy
        return (T) Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class<?>[] { interfaceClass },
            (proxy, method, args) -> {
                // Default mock behavior: return null for object, 0 for primitives, empty for collections
                Class<?> returnType = method.getReturnType();
                if (returnType == void.class) {
                    return null;
                } else if (returnType == boolean.class) {
                    return false;
                } else if (returnType == int.class) {
                    return 0;
                } else if (returnType == long.class) {
                    return 0L;
                } else if (returnType == double.class) {
                    return 0.0;
                } else if (returnType == String.class) {
                    return "";
                } else if (Collection.class.isAssignableFrom(returnType)) {
                    return Collections.emptyList();
                } else if (Map.class.isAssignableFrom(returnType)) {
                    return Collections.emptyMap();
                }
                return null;
            }
        );
    }
    
    /**
     * Test utilities
     */
    
    public static class TestUtils {
        
        /**
         * Generates random test string
         */
        public static String randomString(int length) {
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            return sb.toString();
        }
        
        /**
         * Generates random test email
         */
        public static String randomEmail() {
            return randomString(8) + "@test.com";
        }
        
        /**
         * Waits for specified milliseconds
         */
        public static void sleep(int milliseconds) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

