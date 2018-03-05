package com.nebeek.newsstand.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nebeek.newsstand.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmDialog extends Dialog {

    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.caption)
    TextView caption;

    private String captionText;
    private String confirmText;
    private String cancelText;
    private View.OnClickListener confirmOnClickListener;
    private View.OnClickListener cancelOnClickListener;

    public ConfirmDialog(@NonNull Context context, String captionText, String confirmText, String cancelText, View.OnClickListener confirmOnClickListener, View.OnClickListener cancelOnClickListener) {
        super(context);
        this.captionText = captionText;
        this.confirmText = confirmText;
        this.cancelText = cancelText;
        this.confirmOnClickListener = confirmOnClickListener;
        this.cancelOnClickListener = cancelOnClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        ButterKnife.bind(this);
        getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        init();
    }

    private void init() {
        caption.setText(captionText);
        confirm.setText(confirmText);
        cancel.setText(cancelText);
        confirm.setOnClickListener(view -> {
            confirmOnClickListener.onClick(view);
            dismiss();
        });
        cancel.setOnClickListener(view -> {
            cancelOnClickListener.onClick(view);
            dismiss();
        });
    }
}
