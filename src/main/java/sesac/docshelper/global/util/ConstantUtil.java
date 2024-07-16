package sesac.docshelper.global.util;

public class ConstantUtil {
    public static final String EXCEPTION_ATTRIBUTE = "exception";

    // 인스턴스화 방지
    private ConstantUtil() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }
}
