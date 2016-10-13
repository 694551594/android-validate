package cn.yhq.validate;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表单验证管理器
 *
 * @author Yanghuiqiang 2014-4-17
 */
public class ValidateManager {
    private Map<EditText, ValidateItems> validates;
    private static IValidateHandler mValidateHandler = new DefaultValidateHandler();

    private static Map<Integer, IValidator> validators = new HashMap<>();

    public ValidateManager() {
        validates = new LinkedHashMap<>();
    }

    public interface IValidateHandler {
        void onValidateHandler(EditText editText, String validateMessage);
    }

    public interface IValidator {
        boolean validate(int validateType, EditText editText, String text,
                         Map<String, Object> extras);
    }

    public static void setValidateHandler(IValidateHandler validateHandler) {
        mValidateHandler = validateHandler;
    }

    public static class DefaultValidateHandler implements IValidateHandler {

        @Override
        public void onValidateHandler(EditText editText, String validateMessage) {
            editText.setError(validateMessage);
        }

    }

    public static class ValidateItem {
        public int type;
        public String message;
        public Map<String, Object> extras = new HashMap<String, Object>();

        public final static String EXTRA_VALUE = "value";
        public final static String EXTRA_LENGTH = "length";
        public final static String EXTRA_TEXT = "text";
        public final static String EXTRA_REGEX = "regex";
        public final static String EXTRA_EDITTEXT = "edittext";
        public final static String EXTRA_ARRAY = "array";

        static ValidateItem createValueItem(int type, String message, int value) {
            ValidateItem item = new ValidateItem();
            item.type = type;
            item.message = message;
            item.extras.put(EXTRA_VALUE, value);
            return item;
        }

        static ValidateItem createLengthItem(int type, String message, int length) {
            ValidateItem item = new ValidateItem();
            item.type = type;
            item.message = message;
            item.extras.put(EXTRA_LENGTH, length);
            return item;
        }

        static ValidateItem createEqualsItem(int type, String message,
                                             EditText editText) {
            ValidateItem item = new ValidateItem();
            item.type = type;
            item.message = message;
            item.extras.put(EXTRA_EDITTEXT, editText);
            return item;
        }

        static ValidateItem createUniqueItem(int type, String message,
                                             List<String> array) {
            ValidateItem item = new ValidateItem();
            item.type = type;
            item.message = message;
            item.extras.put(EXTRA_ARRAY, array);
            return item;
        }

        static ValidateItem createEqualsItem(int type, String message, String text) {
            ValidateItem item = new ValidateItem();
            item.type = type;
            item.message = message;
            item.extras.put(EXTRA_TEXT, text);
            return item;
        }

        static ValidateItem createRegexItem(int type, String message, String regex) {
            ValidateItem item = new ValidateItem();
            item.type = type;
            item.message = message;
            item.extras.put(EXTRA_REGEX, regex);
            return item;
        }

        static ValidateItem createItem(int type, String message) {
            ValidateItem item = new ValidateItem();
            item.type = type;
            item.message = message;
            return item;
        }

        static ValidateItem createItem(int type, String message, Map<String, Object> extras) {
            ValidateItem item = new ValidateItem();
            item.type = type;
            item.message = message;
            item.extras = extras;
            return item;
        }

        public static ValidateItem createRequiredItem(String message) {
            return createRegexItem(ValidateType.REQUIRED, message, "");
        }

        public static ValidateItem createEmailItem(String message) {
            return createRegexItem(ValidateType.EMAIL, message, "");
        }

        public static ValidateItem createPhoneItem(String message) {
            return createRegexItem(ValidateType.PHONE, message, "");
        }
    }

    static class ValidateItems {
        public EditText editText;
        public List<ValidateItem> validateItems;
    }

    public ValidateManager addValidateItem(EditText editText, ValidateItem item) {
        ValidateItems items = validates.get(editText);
        if (items == null) {
            items = new ValidateItems();
            items.editText = editText;
            items.validateItems = new ArrayList<>();
            validates.put(editText, items);
        }
        items.validateItems.add(item);
        return this;
    }

    public ValidateManager addValidateItems(EditText editText, List<ValidateItem> items) {
        for (ValidateItem item : items) {
            addValidateItem(editText, item);
        }
        return this;
    }

    public ValidateManager addValidateRequiredItem(EditText editText, String validateMessage) {
        addValidateItem(editText, ValidateType.REQUIRED, validateMessage);
        return this;
    }

    public ValidateManager addValidateEmailItem(EditText editText, String validateMessage) {
        addValidateItem(editText, ValidateType.EMAIL, validateMessage);
        return this;
    }

    public ValidateManager addValidatePhoneItem(EditText editText, String validateMessage) {
        addValidateItem(editText, ValidateType.PHONE, validateMessage);
        return this;
    }

    public ValidateManager addValidateMinLengthItem(EditText editText, String validateMessage,
                                                    int length) {
        addValidateItem(editText,
                ValidateItem.createLengthItem(ValidateType.MIN_LENGTH, validateMessage, length));
        return this;
    }

    public ValidateManager addValidateMaxLengthItem(EditText editText, String validateMessage,
                                                    int value) {
        addValidateItem(editText,
                ValidateItem.createLengthItem(ValidateType.MAX_LENGTH, validateMessage, value));
        return this;
    }

    public ValidateManager addValidateMinValueItem(EditText editText, String validateMessage,
                                                   int value) {
        addValidateItem(editText,
                ValidateItem.createValueItem(ValidateType.MIN_VALUE, validateMessage, value));
        return this;
    }

    public ValidateManager addValidateUniqueItem(EditText editText, String validateMessage,
                                                 List<String> array) {
        addValidateItem(editText,
                ValidateItem.createUniqueItem(ValidateType.UNIQUE, validateMessage, array));
        return this;
    }

    public ValidateManager addValidateMaxValueItem(EditText editText, String validateMessage,
                                                   int value) {
        addValidateItem(editText,
                ValidateItem.createValueItem(ValidateType.MAX_VALUE, validateMessage, value));
        return this;
    }

    public ValidateManager addValidateEqualsItem(EditText editText1, String validateMessage,
                                                 EditText editText2) {
        addValidateItem(editText1,
                ValidateItem.createEqualsItem(ValidateType.EQUALS_EDITTEXT, validateMessage, editText2));
        return this;
    }

    public ValidateManager addValidateEqualsItem(EditText editText, String validateMessage,
                                                 String text) {
        addValidateItem(editText,
                ValidateItem.createEqualsItem(ValidateType.EQUALS_STRING, validateMessage, text));
        return this;
    }

    /**
     * 设置验证项
     *
     * @param editText
     * @param type
     * @param validateMessage
     */
    public ValidateManager addValidateItem(EditText editText, int type,
                                           String validateMessage) {
        addValidateItem(editText, ValidateItem.createItem(type, validateMessage));
        return this;
    }

    public ValidateManager addValidateItem(EditText editText, int type,
                                           String validateMessage, Map<String, Object> extras) {
        addValidateItem(editText, ValidateItem.createItem(type, validateMessage, extras));
        return this;
    }

    public ValidateManager addValidateRegexItem(EditText editText, String validateRegex,
                                                String validateMessage) {
        addValidateItem(editText, ValidateItem.createRegexItem(ValidateType.REGEX, validateMessage, validateRegex));
        return this;
    }

    /**
     * 验证类型
     *
     * @author Yanghuiqiang 2014-4-17
     */
    public interface ValidateType {
        int REQUIRED = -100; // 是否为空
        int EMAIL = -101; // 邮箱
        int PHONE = -102; // 手机号
        int REGEX = -103; // 正则表达式
        int MAX_LENGTH = -104; // 最大长度
        int MIN_LENGTH = -105; // 最小长度
        int MAX_VALUE = -106; // 最大值
        int MIN_VALUE = -107; // 最小值
        int EQUALS_STRING = -108; // 字符串相等
        int EQUALS_EDITTEXT = -109; // edittext内容相等
        int UNIQUE = -110; // 唯一性
    }

    public void clear() {
        this.validates.clear();
    }

    private void requestFocus(EditText editText) {
        editText.requestFocus();
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
    }

    static {
        validators.put(ValidateType.REQUIRED, new RequiredValidator());
        validators.put(ValidateType.EMAIL, new EmailValidator());
        validators.put(ValidateType.EQUALS_EDITTEXT, new EqualsEditTextValidator());
        validators.put(ValidateType.EQUALS_STRING, new EqualsStringValidator());
        validators.put(ValidateType.MAX_LENGTH, new MaxLengthValidator());
        validators.put(ValidateType.MAX_VALUE, new MaxValueValidator());
        validators.put(ValidateType.MIN_LENGTH, new MinLengthValidator());
        validators.put(ValidateType.MIN_VALUE, new MinValueValidator());
        validators.put(ValidateType.PHONE, new PhoneValidator());
        validators.put(ValidateType.REGEX, new RegexValidator());
        validators.put(ValidateType.UNIQUE, new UniqueValidator());
    }

    public static void register(int validateType, IValidator validator) {
        validators.put(validateType, validator);
    }

    static class UniqueValidator implements IValidator {

        @Override
        public boolean validate(int validateType, EditText editText, String text,
                                Map<String, Object> extras) {
            if (!unique(text, (List<String>) extras.get(ValidateItem.EXTRA_ARRAY))) {
                return false;
            }
            return true;
        }

    }

    static class RequiredValidator implements IValidator {

        @Override
        public boolean validate(int validateType, EditText editText, String text,
                                Map<String, Object> extras) {
            if (isBlank(text)) {
                return false;
            }
            return true;
        }

    }

    static class EmailValidator implements IValidator {

        @Override
        public boolean validate(int validateType, EditText editText, String text,
                                Map<String, Object> extras) {
            if (!isEmail(text)) {
                return false;
            }
            return true;
        }

    }

    static class PhoneValidator implements IValidator {

        @Override
        public boolean validate(int validateType, EditText editText, String text,
                                Map<String, Object> extras) {
            if (!isPhone(text)) {
                return false;
            }
            return true;
        }

    }

    static class MinLengthValidator implements IValidator {

        @Override
        public boolean validate(int validateType, EditText editText, String text,
                                Map<String, Object> extras) {
            if (!minLength(text, (int) extras.get(ValidateItem.EXTRA_LENGTH))) {
                return false;
            }
            return true;
        }

    }

    static class MaxLengthValidator implements IValidator {

        @Override
        public boolean validate(int validateType, EditText editText, String text,
                                Map<String, Object> extras) {
            if (!maxLength(text, (int) extras.get(ValidateItem.EXTRA_LENGTH))) {
                return false;
            }
            return true;
        }

    }

    static class MinValueValidator implements IValidator {

        @Override
        public boolean validate(int validateType, EditText editText, String text,
                                Map<String, Object> extras) {
            if (!minValue(text, (int) extras.get(ValidateItem.EXTRA_VALUE))) {
                return false;
            }
            return true;
        }

    }

    static class MaxValueValidator implements IValidator {

        @Override
        public boolean validate(int validateType, EditText editText, String text,
                                Map<String, Object> extras) {
            if (!maxValue(text, (int) extras.get(ValidateItem.EXTRA_VALUE))) {
                return false;
            }
            return true;
        }

    }

    static class EqualsStringValidator implements IValidator {

        @Override
        public boolean validate(int validateType, EditText editText, String text,
                                Map<String, Object> extras) {
            if (!equalsString(text, (String) extras.get(ValidateItem.EXTRA_TEXT))) {
                return false;
            }
            return true;
        }

    }

    static class EqualsEditTextValidator implements IValidator {

        @Override
        public boolean validate(int validateType, EditText editText, String text,
                                Map<String, Object> extras) {
            if (!equalsString(text,
                    ((EditText) extras.get(ValidateItem.EXTRA_EDITTEXT)).getText().toString())) {
                return false;
            }
            return true;
        }

    }

    static class RegexValidator implements IValidator {

        @Override
        public boolean validate(int validateType, EditText editText, String text,
                                Map<String, Object> extras) {
            if (!regex((String) extras.get(ValidateItem.EXTRA_REGEX), text)) {
                return false;
            }
            return true;
        }

    }

    public boolean validate(EditText editText) {
        boolean v = true;
        ValidateItems items = validates.get(editText);
        List<ValidateItem> list = items.validateItems;
        for (int i = 0; i < list.size(); i++) {
            ValidateItem item = list.get(i);
            int type = item.type;
            String validateMessage = item.message;
            String text = editText.getText().toString();

            IValidator validator = validators.get(type);
            if (!validator.validate(type, editText, text, item.extras)) {
                mValidateHandler.onValidateHandler(editText, validateMessage);
                requestFocus(editText);
                v = false;
            }
        }

        return v;
    }

    /**
     * 验证
     *
     * @return
     */
    public boolean validate() {
        boolean v = true;
        for (Map.Entry<EditText, ValidateItems> entry : validates.entrySet()) {
            v = v & validate(entry.getKey());
            if (!v) {
                break;
            }
        }
        return v;
    }


    /**
     * 验证是否为空
     *
     * @return
     */
    public static boolean isBlank(String text) {
        return text == null || text.trim().equals("");
    }

    /**
     * 手机验证
     *
     * @return
     */
    public static boolean isPhone(String text) {
        return isPhone(null, text);
    }

    /**
     * 手机验证
     *
     * @return
     */
    public static boolean isPhone(String regex, String text) {
        if (regex == null) {
            regex = "^1[3|4|5|8][0-9]\\d{4,8}$";
        }
        return regex(regex, text);
    }

    /**
     * email验证
     *
     * @return
     */
    public static boolean isEmail(String text) {
        return isEmail(null, text);

    }

    /**
     * email验证
     *
     * @return
     */
    public static boolean isEmail(String regex, String text) {
        if (regex == null) {
            regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        }
        return regex(regex, text);
    }

    public static boolean equalsString(String text1, String text2) {
        return text1.equals(text2);
    }

    public static boolean minValue(String text, int value) {
        try {
            int _value = Integer.valueOf(text);
            return _value >= value;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean maxValue(String text, int value) {
        try {
            int _value = Integer.valueOf(text);
            return _value <= value;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean minLength(String text, int length) {
        return text.length() >= length;
    }

    public static boolean maxLength(String text, int length) {
        return text.length() <= length;
    }

    public static boolean unique(String text, List<String> array) {
        return array.indexOf(text) == -1;
    }

    /**
     * 正则验证
     *
     * @param regex
     * @param text
     * @return
     */
    public static boolean regex(String regex, String text) {
        if (isBlank(text)) {
            return true;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        return m.find();
    }

}
