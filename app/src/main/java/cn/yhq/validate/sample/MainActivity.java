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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText1 = (EditText) this.findViewById(R.id.editText1);
        EditText editText2 = (EditText) this.findViewById(R.id.editText2);
        EditText editText3 = (EditText) this.findViewById(R.id.editText3);
        EditText editText4 = (EditText) this.findViewById(R.id.editText4);
        EditText editText5 = (EditText) this.findViewById(R.id.editText5);
        EditText editText6 = (EditText) this.findViewById(R.id.editText6);
        EditText editText7 = (EditText) this.findViewById(R.id.editText7);
        EditText editText8 = (EditText) this.findViewById(R.id.editText8);
        EditText editText9 = (EditText) this.findViewById(R.id.editText9);
        EditText editText10 = (EditText) this.findViewById(R.id.editText10);
        EditText editText11 = (EditText) this.findViewById(R.id.editText11);
        EditText editText12 = (EditText) this.findViewById(R.id.editText12);
        Button validateButton = (Button) this.findViewById(R.id.button);

        validateManager.addValidateRequiredItem(editText1, "该项为必填项，不可为空");
        validateManager.addValidateEmailItem(editText2, "请输入正确的邮箱");
        validateManager.addValidatePhoneItem(editText3, "请输入正确的手机号");
        validateManager.addValidateRegexItem(editText4, "^[1-9]\\d*$", "正则表达式不匹配（整数）");
        validateManager.addValidateMaxLengthItem(editText5, "该项的长度不可超过5个字符", 5);
        validateManager.addValidateMinLengthItem(editText6, "该项的长度不可低于5个字符", 5);
        validateManager.addValidateMaxValueItem(editText7, "该项的值不可超过100", 100);
        validateManager.addValidateMinValueItem(editText8, "该项的值不可少于100", 100);
        validateManager.addValidateEqualsItem(editText9, "该项的值和设定的值不相等", "1");
        validateManager.addValidateEqualsItem(editText10, "该项的值和上面的editText的内容不相等", editText9);
        List<String> values = new ArrayList<>();
        values.add("1");
        validateManager.addValidateUniqueItem(editText11, "输入的值已经存在，请重新输入", values);
        validateManager.addValidateItem(editText12, 0, "自定义的验证类型");

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateManager.validate()) {
                    Toast.makeText(MainActivity.this, "验证通过", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "验证不通过", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
