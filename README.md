## `Статус сборки` [![Build status](https://ci.appveyor.com/api/projects/status/cdj8dyk56n91kh93?svg=true)](https://ci.appveyor.com/project/Lognestix/aqa-exercise-2-2)
## В build.gradle добавленна поддержка JUnit-Jupiter, Selenide и headless-режим.
```gradle
plugins {
    id 'java'
}

group 'ru.netology'
version '1.0-SNAPSHOT'

sourceCompatibility = 11

//Кодировка файлов (если используется русский язык в файлах)
compileJava.options.encoding = "UTF-8"
compileTestJava.options.encoding = "UTF-8"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.8.2'
    testImplementation 'com.codeborne:selenide:6.2.0'
}

test {
    useJUnitPlatform()
    //В тестах, при вызове `gradlew test -Dselenide.headless=true` будет передаватся этот параметр в JVM (где его подтянет Selenide)
    systemProperty 'selenide.headless', System.getProperty('selenide.headless')
}
```
## Авто-тесты находящиеся в этом репозитории.
```Java
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    @Test   //Задача №1
    public void shouldSuccessfulFormSubmission() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Тула");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(4)           //Текущая дата плюс 4 дня
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));     //Формат даты день.месяц.год
        $("[data-test-id=date] input").setValue(verificationDate);
        $("[data-test-id=name] input").setValue("Томас Андерсон");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $(".button").shouldHave(Condition.text("Забронировать")).click();
        $("[data-test-id=notification]")
                .shouldHave(Condition.text("Успешно! Встреча успешно забронирована на " + verificationDate),
                        Duration.ofSeconds(15));                        //Загрузка не более 15 секунд
    }

    @Test   //Задача №2
    public void shouldSuccessfulFormSubmissionAfterInteractingWithComplexElements() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Че");
        $(".menu").sendKeys(Keys.DOWN, Keys.DOWN, Keys.DOWN, Keys.ENTER);
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        String currentDate = LocalDate.now()                            //Текущая дата
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));     //Формат даты день.месяц.год
        $("[data-test-id=date] input").setValue(currentDate);
        $(".calendar").sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        $("[data-test-id=name] input").setValue("Джордани Йованович");
        $("[data-test-id=phone] input").setValue("+79014345676");
        $("[data-test-id=agreement]").click();
        $(".button").shouldHave(Condition.text("Забронировать")).click();
        String verificationDate = LocalDate.now().plusDays(7)           //Текущая дата плюс 7 дней
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));     //Формат даты день.месяц.год
        $("[data-test-id=notification]")
                .shouldHave(Condition.text("Успешно! Встреча успешно забронирована на " + verificationDate),
                        Duration.ofSeconds(15));                        //Загрузка не более 15 секунд
    }
}
```