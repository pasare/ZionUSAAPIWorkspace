package org.zionusa.management.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.zionusa.management.util.StringUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class MicrosoftGraphServiceTest {

    @InjectMocks
    private MicrosoftGraphService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    public void generateEmailAddress() {
//        // Arrange
//        UserRegistration userRegistration = new UserRegistration();
//        userRegistration.setFirstName("John");
//        userRegistration.setLastName("Doe");
//        // Act
//        String actual = service.generateEmailAddress(userRegistration);
//        // Assert
//        assertThat(actual).isEqualTo("John.Doe@zionusa.org");
//    }
//
//    @Test
//    public void generateEmailAddressWithTwoFirstNames() {
//        // Arrange
//        UserRegistration userRegistration = new UserRegistration();
//        userRegistration.setFirstName("John James");
//        userRegistration.setLastName("Doe");
//        // Act
//        String actual = service.generateEmailAddress(userRegistration);
//        // Assert
//        assertThat(actual).isEqualTo("John.Doe@zionusa.org");
//    }
//
//    @Test
//    public void generateEmailAddressWithTwoLastNames() {
//        // Arrange
//        UserRegistration userRegistration = new UserRegistration();
//        userRegistration.setFirstName("John");
//        userRegistration.setLastName("Doe Smith");
//        // Act
//        String actual = service.generateEmailAddress(userRegistration);
//        // Assert
//        assertThat(actual).isEqualTo("John.Doe-Smith@zionusa.org");
//    }
//
//    @Test
//    public void generateEmailAddressRemovesAccents() {
//        // Arrange
//        UserRegistration userRegistration = new UserRegistration();
//        userRegistration.setFirstName("Juán");
//        userRegistration.setLastName("Pañilla");
//        // Act
//        String actual = service.generateEmailAddress(userRegistration);
//        // Assert
//        assertThat(actual).isEqualTo("Juan.Panilla@zionusa.org");
//    }
//
//    @Test
//    public void generateEmailAddressTrimsSpaces() {
//        // Arrange
//        UserRegistration userRegistration = new UserRegistration();
//        userRegistration.setFirstName(" John James ");
//        userRegistration.setLastName(" Doe Smith  ");
//        // Act
//        String actual = service.generateEmailAddress(userRegistration);
//        // Assert
//        assertThat(actual).isEqualTo("John.Doe-Smith@zionusa.org");
//    }

    @Test
    public void removeAccents() {
        // Arrange
        String s = ".@ÁÀÂÃÄÅAáàâãäåaBbÇCçcDdÉÈÊËEéèêëeFfGgHhÍÌÎÏIíìîïiJjKkLlMmÑNñnMMmmÓÒÔÕÖOóòôõöoPpQqRrSsTt" +
                "ÚÙÛÜUúùûüuVvWwXxÝŸYýÿyZz";
        // Act
        String actual = StringUtil.sanitizeString(s);
        // Assert
        String expected = ".@AAAAAAAaaaaaaaBbCCccDdEEEEEeeeeeFfGgHhIIIIIiiiiiJjKkLlMmNNnnMMmmOOOOOOooooooPpQqRrSsTt" +
                "UUUUUuuuuuVvWwXxYYYyyyZz";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void removeAccentsWithNullString() {
        // Arrange
        String s = null;
        // Act
        String actual = StringUtil.sanitizeString(s);
        // Assert
        assertThat(actual).isEqualTo("");
    }
}
