package ru.mts.itmo.aqa.junit5;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Epic("PasswordPolicy chek")
@DisplayName("[Password] policies")
public class PasswordPolicyTest {
    private final PasswordPolicy passwordPolicy = new PasswordPolicy();

    @Nested
    @DisplayName("[Password]: Null password throw IllegalArgumentException")
    class NullPasswordThrowIllegalArgumentExceptionTest {
        @Test
        @Story("Null password throw IllegalArgumentException")
        @Severity(SeverityLevel.NORMAL)
        @DisplayName("[Password] Null password should throw IllegalArgumentException")
        void passEqualsNullShouldThrowIllegalArgumentException() {
            // Arrange
            String password = null;
            Allure.parameter("password", password);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> passwordPolicy.check(password));
        }

        @Test
        @Story("Password is not null should no throw IllegalArgumentException")
        @Severity(SeverityLevel.BLOCKER)
        @DisplayName("[Password] Not null password should not throw IllegalArgumentException")
        void passNotEqualsNullShouldNotThrowException() {
            // Arrange
            String password = "Abcdef1@";
            Allure.parameter("password", password);

            // Act & Assert
            assertDoesNotThrow(() -> passwordPolicy.check(password));
        }
    }

    @Nested
    @Feature("Password length")
    @DisplayName("[Password]: Length policies")
    class PasswordLengthPoliciesTests {

        @DisplayName("[Password] TOO_SHORT if password < 8")
        @Story("TOO_SHORT")
        @Severity(SeverityLevel.CRITICAL)
        @ParameterizedTest(name="Password {0} length >= 8 is {1}")
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


        @DisplayName("[Password] TOO_LONG if password > 64")
        @Story("TOO_LONG")
        @Severity(SeverityLevel.CRITICAL)
        @ParameterizedTest(name="Password {0} length <= 64 is {1}")
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
    }


    @Nested
    @Feature("Password syntax")
    @DisplayName("[Password] Syntax policies and rules")
    class SyntaxPoliciesAndRules {

        @DisplayName("[Password] Should contains at least one digit else return NO_DIGIT")
        @Story("NO_DIGIT")
        @Severity(SeverityLevel.CRITICAL)
        @ParameterizedTest(name="Password = {0} has at least one digit: {1}")
        @MethodSource("ru.mts.itmo.aqa.junit5.PasswordPolicyMethodSources#digitCaseSource")
        void checkDigitsTest(String password, boolean shouldBeValid) {
            // Arrange
            Allure.parameter("password", password);
            Allure.parameter("shouldBeValid", shouldBeValid);

            // Act
            var result = passwordPolicy.check(password);

            // Assert
            assertCheckResultEqualsExpected(
                    shouldBeValid,
                    List.of(PasswordPolicy.Violation.NO_DIGIT),
                    result
            );
        }

        @DisplayName("[Password] Should contains at least one upper case symbol else return NO_UPPER")
        @Story("NO_UPPER")
        @Severity(SeverityLevel.CRITICAL)
        @ParameterizedTest(name="Password = {0} has at least one uppercase symbol: {1}")
        @MethodSource("ru.mts.itmo.aqa.junit5.PasswordPolicyMethodSources#upperCaseLatinLettersSource")
        void checkUpperCaseLatinLettersTest(String password, boolean shouldBeValid) {
            // Arrange
            Allure.parameter("password", password);
            Allure.parameter("shouldBeValid", shouldBeValid);

            // Act
            var result = passwordPolicy.check(password);

            // Assert
            assertCheckResultEqualsExpected(
                    shouldBeValid,
                    List.of(PasswordPolicy.Violation.NO_UPPER),
                    result
            );
        }

        @DisplayName("[Password] Should not have whitespace symbols else return HAS_WHITESPACE")
        @Story("HAS_WHITESPACE")
        @Severity(SeverityLevel.NORMAL)
        @ParameterizedTest(name="Password = {0} does not has whitespaces or tabs: {1}")
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

        @DisplayName("[Password] Should contains at least one special symbols from {!,@,#,$,%,^,&,*,(,),-,_,=,+} else return NO_SPECIAL")
        @Story("NO_SPECIAL")
        @Severity(SeverityLevel.CRITICAL)
        @ParameterizedTest(name="Password = {0} contains allowed speical symbol: {1}")
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

        @DisplayName("[Password] should contains at least one lower case symbol else returns NO_LOWER")
        @Story("NO_LOWER")
        @Severity(SeverityLevel.CRITICAL)
        @ParameterizedTest(name="Password = {0} has at least one lowercase symbol {1}")
        @MethodSource("ru.mts.itmo.aqa.junit5.PasswordPolicyMethodSources#lowerCaseLatinLettersSource")
        void checkLowerCaseLatinLettersTest(String password, boolean shouldBeValid) {
            // Arrange
            Allure.parameter("password", password);
            Allure.parameter("shouldBeValid", shouldBeValid);

            // Act
            var result = passwordPolicy.check(password);

            // Assert
            assertCheckResultEqualsExpected(
                    shouldBeValid,
                    List.of(PasswordPolicy.Violation.NO_LOWER),
                    result
            );
        }

        @DisplayName("[Password] should not contains repeated characters")
        @Story("REPEATED_RUN")
        @Severity(SeverityLevel.CRITICAL)
        @ParameterizedTest(name = "Password = {0} hasn\'t repeated characters = {1}")
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
    }

    @Nested
    @Feature("Login validation")
    @DisplayName("[Password] Contains login policies")
    class ContainsLoginPolicyTests {
        @Story("CONTAINS_LOGIN")
        @Severity(SeverityLevel.CRITICAL)
        @ParameterizedTest(name= "password = {0} contains loging = {1} should be valid = {2}")
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
    }

    @Nested
    @Feature("Blacklist validation")
    @DisplayName("[Password] password in blacklist policy")
    class TooEasyPasswordPolicyTests {
        @Story("BLACKLISTED")
        @Severity(SeverityLevel.CRITICAL)
        @ParameterizedTest(name="password = {0} is in black list should be valid {1}")
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
    }

    @Nested
    @DisplayName("[Password] Pairwise tests")
    class PairwiseTests {

        @Story("Pairwise testing")
        @Severity(SeverityLevel.NORMAL)
        @ParameterizedTest(name="password={0} with errors: {2} is valid {1}")
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
    }

    @Step("Check validity and list of violations")
    private static void assertCheckResultEqualsExpected(
            boolean expected,
            List<PasswordPolicy.Violation> expectedViolations,
            PasswordPolicy.Result actualResult) {

        attachResult(actualResult);

        assertEquals(expected, actualResult.isValid());
        if (!expected) {
            assertTrue(actualResult.violations().containsAll(expectedViolations));
        }
    }

    @Attachment(value = "Actual result", type = "text/plain")
    private static String attachResult(PasswordPolicy.Result actualResult) {
        return "isValid=" + actualResult.isValid()
                + System.lineSeparator()
                + "violations=" + actualResult.violations();
    }
}
