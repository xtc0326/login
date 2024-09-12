package com.example.login.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.example.login.TakePhotoActivity;
import com.example.login.camera.PermissionUtils;
import com.yanzhenjie.permission.Permission;

public class UploadFileDialog extends Dialog {

    private TextView negtive;
    private View columnLine;
    private TextView positive;
    private ImageView ivImg;
    private Activity mContext;

    private String imgUrl;


    public static final String[] STORAGE = new String[]{
            Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA};

    public UploadFileDialog(Activity context) {
        super(context, R.style.CustomDialog);
        mContext=context;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        Glide.with(mContext)
                .load(imgUrl)
                .into(ivImg);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        initView();
        negtive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtils.applicationPermissions(mContext, new PermissionUtils.PermissionListener() {
                    @Override
                    public void onSuccess(Context context) {
                        Intent intent = new Intent(mContext, TakePhotoActivity.class);
                        mContext.startActivityForResult(intent,100);
                    }

                    @Override
                    public void onFailed(Context context) {

                        Toast.makeText(context, context.getString(R.string.permission_camra_storage), Toast.LENGTH_SHORT);
                    }
                }, STORAGE);
            }
        });

    }


    @Override
    public void show() {
        super.show();

    }

    private void initView() {
        negtive = (TextView) findViewById(R.id.negtive);
        columnLine = (View) findViewById(R.id.column_line);
        positive = (TextView) findViewById(R.id.positive);
        ivImg = (ImageView) findViewById(R.id.iv_img);
    }

    public interface OnClickBottomListener {
        /**
         * 点击确定按钮事件
         */
        public void onPositiveClick();

        /**
         * 点击取消按钮事件
         */
        public void onNegtiveClick();
    }
}

