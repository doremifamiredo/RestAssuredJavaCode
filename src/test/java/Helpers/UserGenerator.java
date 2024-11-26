package Helpers;

import net.datafaker.Faker;
import org.joda.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static Helpers.DataHelper.AddingInfo;

public class UserGenerator {
    private static final String[] SALE_STATUS = new String[]{"active_search", "on_project", "pause_search", ""};
    private static final String[] ROLES = new String[]{"admin", "user", ""};
    private static final String SPECIAL_CHARTERS = "!@#$%^&*_+=;";
    private static final String RUSSIAN_ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    public static Faker faker = new Faker();
    public static Faker fakerRU = new Faker(new Locale("ru"));
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss Z");

    public static Stream<AddingInfo> dataFakerStream() {
        return faker.<AddingInfo>stream()
                .suppliers(
                        () -> new AddingInfo(
                                new DataHelper.CustomData(faker.bool().bool(), getDate(),
                                        SALE_STATUS[faker.random().nextInt(SALE_STATUS.length)]),
                                getName(), getLastname(), getEmail(), getUsername(), getPassword(),
                                ROLES[faker.random().nextInt(ROLES.length)])
                )
                .maxLen(10)
                .build()
                .get();
    }

    private static String getName() {
        return switch (faker.random().nextInt(5)) {
            case 1 -> faker.name().firstName();
            case 2 -> fakerRU.name().firstName();
            case 3 -> faker.name().lastName() + SPECIAL_CHARTERS.charAt(faker.random().nextInt(SPECIAL_CHARTERS.length()));
            default -> "";
        };
    }

    private static String getLastname() {
        return switch (faker.random().nextInt(5)) {
            case 1 -> faker.name().lastName();
            case 2 -> fakerRU.name().lastName();
            case 3 -> faker.name().lastName() + SPECIAL_CHARTERS.charAt(faker.random().nextInt(SPECIAL_CHARTERS.length()));
            default -> "";
        };
    }

    private static String getEmail() {
        return switch (faker.random().nextInt(5)) {
            case 1 -> faker.internet().emailAddress();
            case 2 -> faker.internet().username();
            case 3 -> fakerRU.internet().username();
            default -> "";
        };
    }

    private static String getUsername() {
        return switch (faker.random().nextInt(5)) {
            case 1 -> faker.internet().username();
            case 2 -> String.join("", SPECIAL_CHARTERS);
            case 3 -> fakerRU.internet().username();
            default -> "";
        };
    }

    private static String getPassword() {
        return switch (faker.random().nextInt(5)) {
            case 1 -> faker.internet().username() + faker.random().nextInt();
            case 2 -> String.join("", SPECIAL_CHARTERS);
            case 3 -> IntStream.range(0, 10)
                    .map(i -> faker.random().nextInt(RUSSIAN_ALPHABET.length()))
                    .mapToObj(RUSSIAN_ALPHABET::charAt)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            case 4 -> faker.internet().password();
            default -> "";
        };
    }

    private static String getDate() {
        return switch (faker.random().nextInt(6)) {
            case 1 -> LocalDateTime.now().minusDays(faker.random().nextInt(300)).toString().formatted(formatter);
            case 2 -> LocalDateTime.now().plusDays(faker.random().nextInt(300)).toString().formatted(formatter);
            case 3 -> LocalDateTime.now().toString().formatted(formatter);
            case 4 -> "25.08.2024";
            default -> "";
        };
    }
}
