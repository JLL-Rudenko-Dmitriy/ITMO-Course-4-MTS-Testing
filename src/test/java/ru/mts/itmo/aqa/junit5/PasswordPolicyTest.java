package ru.mts.itmo.aqa.junit5;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordPolicyTest {
    private final PasswordPolicy passwordPolicy = new PasswordPolicy();

    @Test
    void passEqualsNullShouldThrowIllegalArgumentException() {
        String password = null;
        Allure.parameter("password", password);

        assertThrows(IllegalArgumentException.class, () -> passwordPolicy.check(password));
    }

    @ParameterizedTest(name="Test for password {0} is valid:{1}")
    @MethodSource("ru.mts.itmo.aqa.junit5.PasswordPolicyMethodSources#tooShortBoundaryCases")
    void tooShortBoundaryTest(String password, boolean expectedIsValid) {
        // Arrange
        Allure.parameter("password", password);
        Allure.parameter("expected", expectedIsValid);

        // Act
        var result = passwordPolicy.check(password);


        // Assert
        assertCheckResultEqualsExpected(
                expectedIsValid,
                List.of(PasswordPolicy.Violation.TOO_SHORT),
                result
        );
    }


    @ParameterizedTest()
    @MethodSource("ru.mts.itmo.aqa.junit5.PasswordPolicyMethodSources#tooLongBoundaryCases")
    void tooLongBoundaryTest(String password, boolean expectedIsValid) {
        // Arrange
        Allure.parameter("password", password);
        Allure.parameter("expected", expectedIsValid);

        // Act
        var result = passwordPolicy.check(password);


        // Assert
        assertCheckResultEqualsExpected(
                expectedIsValid,
                List.of(PasswordPolicy.Violation.TOO_LONG),
                result
        );
    }

    @ParameterizedTest()
    @MethodSource("ru.mts.itmo.aqa.junit5.PasswordPolicyMethodSources#pairwiseMethodSource")
    void checkWithPairwiseTestCasesShouldResultShouldBeExpectedValue(
            String password,
            boolean shouldBeValid,
            List<PasswordPolicy.Violation> expectedViolations
    ) {
        // Arrange
        Allure.parameter("password", password);
        Allure.parameter("shouldBeValid", shouldBeValid);
        Allure.parameter("expectedViolations", expectedViolations);

        // Act
        var result = passwordPolicy.check(password);

        // Assert
        assertCheckResultEqualsExpected(
                shouldBeValid,
                expectedViolations,
                result
        );
    }

    @ParameterizedTest()
    @MethodSource("ru.mts.itmo.aqa.junit5.PasswordPolicyMethodSources#whiteSpaceEquivalentClassesSource")
    void checkAllPasswordsWithWhiteSpacesShouldNotBeValid(String password, boolean shouldBeValid
    ) {
        // Arrange
        Allure.parameter("password", password);
        Allure.parameter("shouldBeValid", shouldBeValid);

        // Act
        var result = passwordPolicy.check(password);

        // Assert
        assertCheckResultEqualsExpected(
                shouldBeValid,
                List.of(PasswordPolicy.Violation.HAS_WHITESPACE),
                result
        );
    }

    @ParameterizedTest()
    @MethodSource("ru.mts.itmo.aqa.junit5.PasswordPolicyMethodSources#specialSymbolsCaseSource")
    void checkSpecialSymbolsTest(String password, boolean shouldBeValid) {
        // Arrange
        Allure.parameter("password", password);
        Allure.parameter("shouldBeValid", shouldBeValid);

        // Act
        var result = passwordPolicy.check(password);

        // Assert
        assertCheckResultEqualsExpected(
                shouldBeValid,
                List.of(PasswordPolicy.Violation.NO_SPECIAL),
                result
        );
    }

    @ParameterizedTest()
    @MethodSource("ru.mts.itmo.aqa.junit5.PasswordPolicyMethodSources#repeatedRunSource")
    void checkRepeatedRunTest(String password, boolean shouldBeValid) {
        // Arrange
        Allure.parameter("password", password);
        Allure.parameter("shouldBeValid", shouldBeValid);

        // Act
        var result = passwordPolicy.check(password);

        // Assert
        assertCheckResultEqualsExpected(
                shouldBeValid,
                List.of(PasswordPolicy.Violation.REPEATED_RUN),
                result
        );
    }

    @ParameterizedTest()
    @MethodSource("ru.mts.itmo.aqa.junit5.PasswordPolicyMethodSources#containsLoginSource")
    void checkPasswordContainsLoginTest(String password, String login, boolean shouldBeValid) {
        // Arrange
        Allure.parameter("password", password);
        Allure.parameter("login", login);
        Allure.parameter("shouldBeValid", shouldBeValid);

        // Act
        var result = passwordPolicy.check(password, login);

        // Assert
        assertCheckResultEqualsExpected(
                shouldBeValid,
                List.of(PasswordPolicy.Violation.CONTAINS_LOGIN),
                result
        );
    }

    @ParameterizedTest()
    @MethodSource("ru.mts.itmo.aqa.junit5.PasswordPolicyMethodSources#rainbowTableSource")
    void checkPasswordMatchWithPasswordFromRainbowTableTest(String password, boolean shouldBeValid) {
        // Arrange
        Allure.parameter("password", password);
        Allure.parameter("shouldBeValid", shouldBeValid);

        // Act
        var result = passwordPolicy.check(password);

        // Assert
        assertCheckResultEqualsExpected(
                shouldBeValid,
                List.of(PasswordPolicy.Violation.BLACKLISTED),
                result
        );
    }

    private static void assertCheckResultEqualsExpected(
            boolean expected,
            List<PasswordPolicy.Violation> expectedViolations,
            PasswordPolicy.Result actualResult) {

        assertEquals(expected, actualResult.isValid());
        if (!expected) {
            assertTrue(actualResult.violations().containsAll(expectedViolations));
        }
    }
}
