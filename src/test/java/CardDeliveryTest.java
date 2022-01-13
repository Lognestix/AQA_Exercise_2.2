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