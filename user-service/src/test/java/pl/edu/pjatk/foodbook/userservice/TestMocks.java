package pl.edu.pjatk.foodbook.userservice;

import org.junit.platform.commons.util.ReflectionUtils;
import pl.edu.pjatk.foodbook.userservice.repository.model.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class TestMocks {

    /**
     * @param <T> desired class type to be returned
     * @return desired mocked object of given class type with all fields prefilled with mocked data
     */
    @SuppressWarnings("unchecked")
    public static <T> T getMock(Class<T> classToBeMocked) {
        Method method = ReflectionUtils.findMethod(TestMocks.class, "get" + classToBeMocked.getSimpleName()).orElseThrow();
        try {
            Object mockedObject = method.invoke(null);
            return (T) mockedObject;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static User getUser() {
        return User.builder()
                   .realName("Firstname Lastname")
                   .username("username")
                   .email("user@mail.com")
                   .build();
    }
}
