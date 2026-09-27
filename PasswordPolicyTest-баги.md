# Баг-репорты

## 1. [TOO_LONG] Пароль длиной 64 символа ошибочно считается слишком длинным

### Окружение

- Проект: `demo-junit`.
- Тестируемый класс: `ru.mts.itmo.aqa.junit5.PasswordPolicy`.
- Тестовый класс: `ru.mts.itmo.aqa.junit5.PasswordPolicyTest`.
- JDK: Eclipse Temurin 21.0.8+9.
- Система сборки: Gradle 9.6.1.
- Версия с дефектом: коммит `0ca7c08` (до исправления `78bba0b`).

### Шаги воспроизведения

1. Создать экземпляр `PasswordPolicy`.
2. Передать в `check()` пароль длиной ровно 64 символа: `p2Lwh2CfhYN5xkYg7YWuT7rKR#2sBFYNTKZdpWT0i7RNK79PAw2MJsMeXWXxGmGe`.
3. Получить `PasswordPolicy.Result`.
4. Проверить `isValid()` и список `violations()`.

### Фактический результат

`isValid()` возвращает `false`, список нарушений содержит `TOO_LONG`.

### Ожидаемый результат

`isValid()` возвращает `true`, нарушение `TOO_LONG` отсутствует.

Цитата из спецификации:

> «Длина больше 64 — TOO_LONG. Длина ровно 64 — ещё допустима».

### Severity

**Major** — нарушено основное правило валидации и корректный пароль отклоняется, но пользователь может временно выбрать более короткий пароль.

### Priority

**High** — дефект воспроизводится на явно указанной в спецификации границе и должен быть исправлен до выпуска.

### Регрессионный тест

`PasswordPolicyTest.PasswordLengthPoliciesTests#tooLongBoundaryTest`, данные находятся в `tooLongBoundaryCases`.

---

## 2. [BLACKLISTED] Пароль из blacklist в верхнем регистре не получает нарушение `BLACKLISTED`

### Окружение

- Проект: `demo-junit`.
- Тестируемый класс: `ru.mts.itmo.aqa.junit5.PasswordPolicy`.
- Тестовый класс: `ru.mts.itmo.aqa.junit5.PasswordPolicyTest`.
- JDK: Eclipse Temurin 21.0.8+9.
- Система сборки: Gradle 9.6.1.
- Версия с дефектом: коммит `0ca7c08` (до исправления `78bba0b`).

### Шаги воспроизведения

1. Создать экземпляр `PasswordPolicy`.
2. Передать в `check()` пароль `PASSWORD`.
3. Получить `PasswordPolicy.Result`.
4. Проверить наличие `BLACKLISTED` в списке `violations()`.

### Фактический результат

Список нарушений не содержит `BLACKLISTED`. Проверка blacklist выполняется с учётом регистра. При этом пароль остаётся невалидным из-за других нарушений, что не отменяет дефект правила `BLACKLISTED`.

### Ожидаемый результат

Список нарушений содержит `BLACKLISTED`, поскольку сравнение должно выполняться без учёта регистра.

Цитата из спецификации:

> «Пароль совпадает с одним из простых: password, qwerty, 123456, admin, welcome, без учёта регистра — BLACKLISTED».

### Severity

**Critical** — некорректно работает правило безопасности, запрещающее использование общеизвестных простых паролей.

### Priority

**High** — дефект затрагивает требование безопасности и все элементы blacklist, записанные в другом регистре.

### Регрессионный тест

`PasswordPolicyTest.TooEasyPasswordPolicyTests#checkPasswordMatchWithPasswordFromRainbowTableTest`, данные находятся в `rainbowTableSource`.

---

## 3. [REPEATED_RUN] Серия из трёх одинаковых символов не считается нарушением

### Окружение

- Проект: `demo-junit`.
- Тестируемый класс: `ru.mts.itmo.aqa.junit5.PasswordPolicy`.
- Тестовый класс: `ru.mts.itmo.aqa.junit5.PasswordPolicyTest`.
- JDK: Eclipse Temurin 21.0.8+9.
- Система сборки: Gradle 9.6.1.
- Версия с дефектом: коммит `0ca7c08` (до исправления `78bba0b`).

### Шаги воспроизведения

1. Создать экземпляр `PasswordPolicy`.
2. Передать в `check()` пароль `aaaAbBaAbB#1`, содержащий серию из трёх символов `a`.
3. Получить `PasswordPolicy.Result`.
4. Проверить `isValid()` и наличие `REPEATED_RUN` в списке `violations()`.

### Фактический результат

`isValid()` возвращает `true`, список нарушений не содержит `REPEATED_RUN`.

### Ожидаемый результат

`isValid()` возвращает `false`, список нарушений содержит `REPEATED_RUN`.

Цитата из спецификации:

> «Три и более одинаковых символа подряд — REPEATED_RUN. „aaa“ — нарушение, „aa“ — нет».

### Severity

**Critical** — валидатор принимает пароль, нарушающий требование безопасности к повторяющимся символам.

### Priority

**High** — дефект позволяет обойти правило и воспроизводится на минимальной запрещённой длине серии.

### Регрессионный тест

`PasswordPolicyTest.SyntaxPoliciesAndRules#checkRepeatedRunTest`, данные находятся в `repeatedRunSource`.

---
