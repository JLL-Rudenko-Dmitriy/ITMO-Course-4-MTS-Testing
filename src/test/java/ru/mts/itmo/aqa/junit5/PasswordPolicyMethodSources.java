package ru.mts.itmo.aqa.junit5;

import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class PasswordPolicyMethodSources {

    static Stream<Arguments> tooShortBoundaryCases() {
        return Stream.of(
                Arguments.of("aA1@1Aa", false),
                Arguments.of("#aA1@1Aa", true),
                Arguments.of("#aA1@1Aa#", true)
        );
    }

    static Stream<Arguments> tooLongBoundaryCases() {
        return Stream.of(
                Arguments.of("p2Lwh2CfhYN5xkYg7YWuT7rKR#2sBFYNTKZdpWT0i7RNK79PAw2MJsMeXWXxGmG", true),
                Arguments.of("p2Lwh2CfhYN5xkYg7YWuT7rKR#2sBFYNTKZdpWT0i7RNK79PAw2MJsMeXWXxGmGe", true),
                Arguments.of("p2Lwh2CfhYN5xkYg7YWuT7rKR#2sBFYNTKZdpWT0i7RNK79PAw2MJsMeXWXxGmGee", false)
        );
    }

    static Stream<Arguments> digitCaseSource() {
        return Stream.of(
                Arguments.of("Abcdefg@", false),
                Arguments.of("Abcdef1@", true)
        );
    }

    static Stream<Arguments> upperCaseLatinLettersSource() {
        return Stream.of(
                Arguments.of("abcdef1@", false),
                Arguments.of("Abcdef1@", true)
        );
    }

    static Stream<Arguments> whiteSpaceEquivalentClassesSource() {
        return Stream.of(
                Arguments.of("Itmo_Test Passw0rd", false),
                Arguments.of("Itmo_Test\tPassw0rd", false),
                Arguments.of("Itmo_Test_Passw0rd", true)
        );
    }

    static Stream<Arguments> specialSymbolsCaseSource() {
        return Stream.of(
                Arguments.of("1Aab22baA1", false),
                Arguments.of("1Aab{]baA1", false),
                Arguments.of("1Aab@#baA1", true)
        );
    }

    static Stream<Arguments> lowerCaseLatinLettersSource() {
        return Stream.of(
                Arguments.of("ABC1@DEF", false),
                Arguments.of("ABC1@DEf", true)
        );
    }

    static Stream<Arguments> pairwiseMethodSource() {
        return Stream.of(
                Arguments.of("1sS@Ss1", false, List.of(
                        PasswordPolicy.Violation.TOO_SHORT
                )),
                Arguments.of("s S@sss", false, Arrays.asList(
                        PasswordPolicy.Violation.TOO_SHORT,
                        PasswordPolicy.Violation.HAS_WHITESPACE,
                        PasswordPolicy.Violation.REPEATED_RUN
                )),
                Arguments.of("1sd@ @ds1", false, Arrays.asList(
                        PasswordPolicy.Violation.NO_UPPER,
                        PasswordPolicy.Violation.HAS_WHITESPACE
                )),
                Arguments.of("Ssd@@sss", false, Arrays.asList(
                        PasswordPolicy.Violation.NO_DIGIT,
                        PasswordPolicy.Violation.REPEATED_RUN
                )),
                Arguments.of("s1Sd  fss", false, Arrays.asList(
                        PasswordPolicy.Violation.HAS_WHITESPACE,
                        PasswordPolicy.Violation.NO_SPECIAL
                )),
                Arguments.of("asdf@kiii", false, Arrays.asList(
                        PasswordPolicy.Violation.NO_UPPER,
                        PasswordPolicy.Violation.NO_DIGIT,
                        PasswordPolicy.Violation.REPEATED_RUN
                )),
                Arguments.of("iA#_?_#1-BBBb>7ngLxjGDDbXNVov@", false, List.of(
                        PasswordPolicy.Violation.REPEATED_RUN
                )),
                Arguments.of("iaiohoiccbbdfgfnglxjgddbxmvo f", false, Arrays.asList(
                        PasswordPolicy.Violation.NO_DIGIT,
                        PasswordPolicy.Violation.NO_UPPER,
                        PasswordPolicy.Violation.NO_SPECIAL,
                        PasswordPolicy.Violation.HAS_WHITESPACE
                )),
                Arguments.of(
                        "Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!",
                        true,
                        List.of()
                ),
                Arguments.of(
                        "aaa bcdefghijklmnopqrstuvwxyzbcdefghijklmnopqrstuvwxyzbcdefghijk",
                        false,
                        List.of(
                                PasswordPolicy.Violation.NO_DIGIT,
                                PasswordPolicy.Violation.NO_UPPER,
                                PasswordPolicy.Violation.NO_SPECIAL,
                                PasswordPolicy.Violation.HAS_WHITESPACE,
                                PasswordPolicy.Violation.REPEATED_RUN
                        )
                ),
                Arguments.of(
                        "Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!C",
                        false,
                        List.of(
                                PasswordPolicy.Violation.TOO_LONG
                        )
                ),
                Arguments.of(
                        "aaa bcdefghijklmnopqrstuvwxyzbcdefghijklmnopqrstuvwxyzbcdefghijkl",
                        false,
                        List.of(
                                PasswordPolicy.Violation.TOO_LONG,
                                PasswordPolicy.Violation.NO_DIGIT,
                                PasswordPolicy.Violation.NO_UPPER,
                                PasswordPolicy.Violation.NO_SPECIAL,
                                PasswordPolicy.Violation.HAS_WHITESPACE,
                                PasswordPolicy.Violation.REPEATED_RUN
                        )
                )
        );
    }

    static Stream<Arguments> repeatedRunSource() {
        return Stream.of(
                Arguments.of("aAbBaAbBaAbBaAbB1#1", true),
                Arguments.of("aaabBaAbB#1", false),
                Arguments.of("aaaAbBaAbB#1", false),
                Arguments.of("aaaaAbBaAbB#1", false),
                Arguments.of("1#Aaaaaaaaaaaaaaaaaaaa", false),
                Arguments.of("1#aAAAAAAACCCAAAAAAAAA", false),
                Arguments.of("aaAbbBaaAbbBaaAbbBaaAbbB1#1", true)
        );
    }

    static Stream<Arguments> containsLoginSource() {
        return Stream.of(
                Arguments.of("superUser+22", "superUser", false),
                Arguments.of("super+22User", "superUser", true),
                Arguments.of("SuperUser0sdf_+", "superUser", false),
                Arguments.of("superUser01++", "", true),
                Arguments.of("superUser01++", null, true)
        );
    }

    static Stream<Arguments> rainbowTableSource() {
        return Stream.of(
                Arguments.of("password", false),
                Arguments.of("qwerty", false),
                Arguments.of("123456", false),
                Arguments.of("admin", false),
                Arguments.of("welcome", false),
                Arguments.of("PASSWORD", false),
                Arguments.of("QWERTY", false),
                Arguments.of("123456", false),
                Arguments.of("ADMIN", false),
                Arguments.of("WELCOME", false),
                Arguments.of("Password1@", true)
        );
    }
}
