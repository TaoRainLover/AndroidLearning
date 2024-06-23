package org.zt.lifeinputmethod2;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/**
 * @author Tao
 * @date 2024/6/21 22:24
 * @desc
 */
public class LifeInputMethodService extends InputMethodService implements CustomKeyboardView.OnKeyboardActionListener {

    private CustomKeyboardView inflate;

    @Override
    public View onCreateInputView() {
        inflate = (CustomKeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view_layout, null);
        CustomKeyboardView keyboardView = inflate;
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }

    // 实现按钮操作接口
    @Override
    public void onKeyPress(String key) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            switch (key) {
                case "space":
                    ic.commitText(" ", 1);
                    break;
                case "Del":
                    ic.deleteSurroundingText(1, 0);
                    break;
                case "Search":
                    ic.performEditorAction(EditorInfo.IME_ACTION_DONE);
                    break;
                default:
                    ic.commitText(key, 1);
                    break;
            }
        }
    }
}
