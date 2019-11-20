package com.example.cloudpos.data;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.example.cloudpos.R;


//implemented by Yang Insu
//메뉴 아이템 하나가 체크 박스랑 따로 놀지 않고 전체적으로 작동하기 위한 커스텀 레이아웃

public class CheckableLinearLayout extends LinearLayout implements Checkable {
    public CheckableLinearLayout(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox)findViewById(R.id.menu_checkbox);
        if(cb.isChecked() != checked){
            cb.setChecked(checked);
        }

    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public void toggle() {
        CheckBox checkBox = (CheckBox)findViewById(R.id.menu_checkbox);
        setChecked((checkBox.isChecked()?false:true));

    }
}
