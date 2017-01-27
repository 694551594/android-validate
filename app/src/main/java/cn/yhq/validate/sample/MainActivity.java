package cn.yhq.validate.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.yhq.validate.Email;
import cn.yhq.validate.EqualsEditText;
import cn.yhq.validate.EqualsValue;
import cn.yhq.validate.MaxLength;
import cn.yhq.validate.MaxValue;
import cn.yhq.validate.MinLength;
import cn.yhq.validate.MinValue;
import cn.yhq.validate.Phone;
import cn.yhq.validate.Regex;
import cn.yhq.validate.Required;
import cn.yhq.validate.Unique;
import cn.yhq.validate.ValidateManager;

public class MainActivity extends AppCompatActivity {
    private ValidateManager validateManager = new ValidateManager();

    static {
        ValidateManager.setValidateHandler(new ValidateManager.IValidateHandler() {
            @Override
            public void onValidateHandler(EditText editText, String validateMessage) {
                Toast.makeText(editText.getContext(), validateMessage, Toast.LENGTH_LONG).show();
            }
        });
        ValidateManager.register(0, new ValidateManager.IValidator() {
            @Override
            public boolean validate(int validateType, EditText editText, String text, Map<String, Object> extras) {
                return false;
            }
        });
    }

    private List<String> arrays = new ArrayList<>();

    @Required(message = "该项为必填项，不可为空")
    private EditText editText1;
    @Email(message = "请输入正确的邮箱")
    private EditText editText2;
    @Phone(message = "请输入正确的手机号")
    private EditText editText3;
    @Regex(regex = "^[1-9]\\d*$", message = "正则表达式不匹配（整数）")
    private EditText editText4;
    @MaxLength(message = "该项的长度不可超过5个字符", maxLength = 5)
    private EditText editText5;
    @MinLength(message = "该项的长度不可低于5个字符", minLength = 5)
    private EditText editText6;
    @MaxValue(message = "该项的值不可超过100", maxValue = 100)
    private EditText editText7;
    @MinValue(message = "该项的值不可少于100", minValue = 100)
    private EditText editText8;
    @EqualsValue(value = "1", message = "该项的值和设定的值不相等")
    private EditText editText9;
    @EqualsEditText(editext = "editText9", message = "该项的值和上面的editText的内容不相等")
    private EditText editText10;
    @Unique(array = "arrays", message = "输入的值已经存在，请重新输入")
    private EditText editText11;
    private EditText editText12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrays.add("1");
        editText1 = (EditText) this.findViewById(R.id.editText1);
        editText2 = (EditText) this.findViewById(R.id.editText2);
        editText3 = (EditText) this.findViewById(R.id.editText3);
        editText4 = (EditText) this.findViewById(R.id.editText4);
        editText5 = (EditText) this.findViewById(R.id.editText5);
        editText6 = (EditText) this.findViewById(R.id.editText6);
        editText7 = (EditText) this.findViewById(R.id.editText7);
        editText8 = (EditText) this.findViewById(R.id.editText8);
        editText9 = (EditText) this.findViewById(R.id.editText9);
        editText10 = (EditText) this.findViewById(R.id.editText10);
        editText11 = (EditText) this.findViewById(R.id.editText11);
        editText12 = (EditText) this.findViewById(R.id.editText12);
        Button validateButton = (Button) this.findViewById(R.id.button);

        // validateManager.addValidateRequiredItem(editText1, "该项为必填项，不可为空");
//        validateManager.addValidateEmailItem(editText2, "请输入正确的邮箱");
//        validateManager.addValidatePhoneItem(editText3, "请输入正确的手机号");
//        validateManager.addValidateRegexItem(editText4, "^[1-9]\\d*$", "正则表达式不匹配（整数）");
//        validateManager.addValidateMaxLengthItem(editText5, "该项的长度不可超过5个字符", 5);
//        validateManager.addValidateMinLengthItem(editText6, "该项的长度不可低于5个字符", 5);
//        validateManager.addValidateMaxValueItem(editText7, "该项的值不可超过100", 100);
//        validateManager.addValidateMinValueItem(editText8, "该项的值不可少于100", 100);
//        validateManager.addValidateEqualsItem(editText9, "该项的值和设定的值不相等", "1");
//        validateManager.addValidateEqualsItem(editText10, "该项的值和上面的editText的内容不相等", editText9);
//        List<String> values = new ArrayList<>();
//        values.add("1");
      //  validateManager.addValidateUniqueItem(editText11, "输入的值已经存在，请重新输入", values);
        validateManager.addValidateItem(editText12, 0, "自定义的验证类型");

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateManager.validate(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "验证通过", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "验证不通过", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
