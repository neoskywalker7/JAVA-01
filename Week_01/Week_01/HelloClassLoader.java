package Week_01;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException,
        InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> helloClass = new HelloClassLoader().findClass("Hello");
        Object helloObject = helloClass.getDeclaredConstructor().newInstance();
        Method helloMethod = helloClass.getMethod("hello");
        helloMethod.invoke(helloObject);
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        byte[] bytes;
        InputStream is = getClass().getClassLoader().getResourceAsStream(className.replace(".", File.pathSeparator) + ".xlass");
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int next;
            while ((next = is.read()) != -1) {
                byteArrayOutputStream.write(next);
            }
            byteArrayOutputStream.close();
            bytes = byteArrayOutputStream.toByteArray();
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException(".xlass file not found");
        }
        return super.defineClass(className, bytes, 0, bytes.length);
    }
}