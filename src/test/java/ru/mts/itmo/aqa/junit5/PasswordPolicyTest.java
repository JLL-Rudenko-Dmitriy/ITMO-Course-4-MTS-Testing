package ru.mts.itmo.aqa.junit5;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[Password] policies")
public class PasswordPolicyTest {
    private final PasswordPolicy passwordPolicy = new PasswordPolicy();

    @Nested
    @DisplayName("[Password]: Null password throw IllegalArgumentException")
    class NullPasswordThrowIllegalArgumentExceptionTest {
        @Test
        @DisplayName("[Password] Null password should throw IllegalArgumentException")
        void passEqualsNullShouldThrowIllegalArgumentException() {
            // Arrange
            String password = null;
            Allure.parameter("password", password);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> passwordPolicy.check(password));
        }

        @Test
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
    @DisplayName("[Password]: Length policies")
    class PasswordLengthPoliciesTests {

        @DisplayName("[Password] TOO_SHORT if password < 8")
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
    @DisplayName("[Password] Syntax policies and rules")
    class SyntaxPoliciesAndRules {

        @DisplayName("[Password] Should contains at least one digit else return NO_DIGIT")
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
    @DisplayName("[Password] Contains login policies")
    class ContainsLoginPolicyTests {
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
    @DisplayName("[Password] password in balack list policy")
    class TooEasyPasswordPolicyTests {
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
