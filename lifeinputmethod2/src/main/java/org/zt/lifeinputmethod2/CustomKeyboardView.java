package org.zt.lifeinputmethod2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


/**
 * 自定义键盘 View
 * @author Tao
 * @date 2024/6/23 20:51
 */
public class CustomKeyboardView extends LinearLayout {
    private static final String TAG = "CustomKeyboardView";

    // 英文 26 键盘按钮
    private static final int[] alphabetKeyButtonIds = {
            R.id.key_q, R.id.key_w, R.id.key_e, R.id.key_r, R.id.key_t,
            R.id.key_y, R.id.key_u, R.id.key_i, R.id.key_o, R.id.key_p,
            R.id.key_a, R.id.key_s, R.id.key_d, R.id.key_f, R.id.key_g,
            R.id.key_h, R.id.key_j, R.id.key_k, R.id.key_l,
            R.id.key_z, R.id.key_x, R.id.key_c, R.id.key_v, R.id.key_b,
            R.id.key_n, R.id.key_m,
    };

    // 数字键盘按钮 ids
    private static final int[] numberKeyButtonIds = {
            // 数字 10 键
            R.id.key_one, R.id.key_two, R.id.key_three, R.id.key_four, R.id.key_five,
            R.id.key_six, R.id.key_seven, R.id.key_eight, R.id.key_nine, R.id.key_zero,
            // 符号键
            R.id.key_multiply, R.id.key_division, R.id.key_add, R.id.key_sub, R.id.key_dot, R.id.key_at,
    };

    // 功能键 ids
    private static final int[] functionKeyButtonIds = {
            R.id.key_space, R.id.key_delete, R.id.key_enter, R.id.key_switch_upper, R.id.key_symbol,
    };

    private OnKeyboardActionListener listener;
    // 切换大小写输入
    private boolean isUpperCase = false;
    // 锁定大写输入
    private boolean isUpperCaseLock = false;
    private boolean isQwerty = true;
    private View customNumberKeyboard;
    private View customQwertyKeyboard;

    public interface OnKeyboardActionListener {
        void onKeyPress(String key);
    }

    public CustomKeyboardView(Context context) {
        super(context);
        init(context);
    }

    public CustomKeyboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnKeyboardActionListener(OnKeyboardActionListener listener) {
        this.listener = listener;
    }

    // 初始化
    private void init(Context context) {
        setOrientation(VERTICAL);
        // 加载不同的键盘布局
        customQwertyKeyboard = inflate(context, R.layout.custom_keyboard, null);
        customNumberKeyboard = inflate(context, R.layout.custom_number_keyboard, null);

        // 初始化键盘为英文 26 键
        addView(customNumberKeyboard);

        // 设置监听器
        setupKeyListeners(customQwertyKeyboard);
        setupKeyListeners(customNumberKeyboard);
    }

    private void setupKeyListeners(View viewGroup) {
        // 合并 keyboardView 中的所有Button，遍历设置点击事件
        int[] allButtonIds = mergeIdsArrays(alphabetKeyButtonIds, numberKeyButtonIds, functionKeyButtonIds);
        for (int id : allButtonIds) {
            View view = viewGroup.findViewById(id);
            if (view != null) {
                view.setOnClickListener(this::onClick);
            }
        }

        // 切换中文键盘按钮
        Button switchButton = viewGroup.findViewById(R.id.key_switch_number);
        if (switchButton != null) {
            switchButton.setOnClickListener(v -> switchKeyboard(customNumberKeyboard));
        }

        // 切换英文键盘按钮
        Button switchQwertyButton = viewGroup.findViewById(R.id.key_switch_qwerty);
        if (switchQwertyButton != null) {
            switchQwertyButton.setOnClickListener(v -> switchKeyboard(customQwertyKeyboard));
        }
    }

    // 合并
    public static int[] mergeIdsArrays(int[]... arrays) {
        int length = 0;
        for (int[] array : arrays) {
            length += array.length;
        }

        int[] ids = new int[length];
        int offset = 0;
        for (int[] array : arrays) {
            System.arraycopy(array, 0, ids, offset, array.length);
            offset += array.length;
        }

        return ids;
    }


    public void onClick(View v) {
        if (v != null) {
            int buttonId = v.getId();
            if (buttonId == R.id.key_switch_upper) {
                // 切换大小写
                toggleCase();
                // 更改切换大小写按键的风格
                changeStyle(v);
            } else if (buttonId == R.id.key_symbol) {
                // TODO：切换符号键盘
                Toast.makeText(getContext(), "TODO: 切换符号键盘", Toast.LENGTH_SHORT).show();
            } else {
                // 按键输入
                listener.onKeyPress(((Button) v).getText().toString());
            }
        }
    }

    // 切换英文大小写
    private void toggleCase() {
        isUpperCase = !isUpperCase;
        for (int id : alphabetKeyButtonIds) {
            Button button = findViewById(id);
            if (button != null) {
                char curKey = button.getText().charAt(0);
                char updateKey = (char) (curKey + 32);
                if (curKey >= 'a' && curKey <= 'z') {
                    updateKey = (char) (curKey - 32);
                }
                button.setText(String.valueOf(updateKey));
            }
        }
    }

    // 更改背景颜色
    private void changeStyle(View v) {
        if (isUpperCase) {
            v.setBackgroundResource(R.drawable.button_background_pressed);
            ((Button) v).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else {
            v.setBackgroundResource(R.drawable.function_key_background);
            ((Button) v).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }
    }

    // 切换键盘视图
    private void switchKeyboard(View keyboardView) {
        removeAllViews();
        addView(keyboardView);
    }
}
