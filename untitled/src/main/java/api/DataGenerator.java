package api;


import com.github.javafaker.Faker;

import java.util.Locale;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));
    public static UserData generateUser() {
        return new UserData(faker.internet().emailAddress(),
                faker.internet().password(8,13, true,true,true),
                faker.name().firstName());
    }

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generatePassword() {
        return faker.internet().password(8,13, true,true,true);
    }

    public static String generateName() {
        return faker.name().firstName();
    }
}