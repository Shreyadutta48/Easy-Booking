/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    @Test public void appHasAGreeting() {
        AppTest classUnderTest = new AppTest();
        assertNotNull(classUnderTest.getGreeting(), "App should have a greeting");
    }
    public Object getGreeting(){
        System.out.println("Welcome to IntelliJ!");
        return null;
    }
}
