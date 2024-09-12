package com.example.login;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.login.R;
import com.example.login.camera.AnimSpring;
import com.example.login.camera.CameraPreview;
import com.example.login.camera.OverCameraView;
import com.example.login.camera.PermissionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TakePhotoActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String KEY_IMAGE_PATH = "imagePath";
    /**
     * 相机预览
     */
    private FrameLayout mPreviewLayout;
    /**
     * 拍摄按钮视图
     */
    private RelativeLayout mPhotoLayout;

    /**
     * 闪光灯
     */
    private ImageView mFlashButton;
    /**
     * 拍照按钮
     */
    private ImageView mPhotoButton;

    /**
     * 聚焦视图
     */
    private OverCameraView mOverCameraView;
    /**
     * 相机类
     */
    private Camera mCamera;
    /**
     * Handle
     */
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    /**
     * 取消按钮
     */
    private Button mCancleButton;
    /**
     * 是否开启闪光灯
     */
    private boolean isFlashing;
    /**
     * 图片流暂存
     */
    private byte[] imageData;
    /**
     * 拍照标记
     */
    private boolean isTakePhoto;
    /**
     * 是否正在聚焦
     */
    private boolean isFoucing;


    private FrameLayout cameraPreviewLayout;

    private ImageView flashButton;
    private RelativeLayout llConfirmLayout;
    private ImageView cancleSaveButton;
    private ImageView saveButton;
    private RelativeLayout llPhotoLayout;
    private Button cancleButton;
    private ImageView takePhotoButton;


    public ScheduledExecutorService scheduExec = Executors
            .newScheduledThreadPool(1);


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();

            return true;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        ActionBar actionBar = this.getSupportActionBar();

        actionBar.setTitle("舌象拍摄");

        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_take_photo);

        initView();
        setOnclickListener();




    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        CameraPreview preview = new CameraPreview(this, mCamera);
        mOverCameraView = new OverCameraView(this);
        mPreviewLayout.addView(preview);
        mPreviewLayout.addView(mOverCameraView);

        scheduExec.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        autoFocus();
                    }
                });

            }
        }, 1000 * 2, 1000 * 2, TimeUnit.MILLISECONDS);

    }




    private void setOnclickListener() {
        mCancleButton.setOnClickListener(this);

        mFlashButton.setOnClickListener(this);
        mPhotoButton.setOnClickListener(this);

    }
    private int  getWidth(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        return width;
    }
    private int getHeight(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        return height;
    }
    private void  autoFocus(){
        int with=getWidth(this);
        int height=getHeight(this);

        if (!isFoucing) {
            float x = with/2;
            float y = height/2;
            isFoucing = true;
            if (mCamera != null && !isTakePhoto) {
                mOverCameraView.setTouchFoucusRect(mCamera, autoFocusCallback, x, y);
            }
            mRunnable = () -> {

                isFoucing = false;
                mOverCameraView.setFoucuing(false);
                mOverCameraView.disDrawTouchFocusRect();
            };
            //设置聚焦超时
            mHandler.postDelayed(mRunnable, 2000);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isFoucing) {
                float x = event.getX();
                float y = event.getY();
                isFoucing = true;
                if (mCamera != null && !isTakePhoto) {
                    mOverCameraView.setTouchFoucusRect(mCamera, autoFocusCallback, x, y);
                }
                mRunnable = () -> {

                    isFoucing = false;
                    mOverCameraView.setFoucuing(false);
                    mOverCameraView.disDrawTouchFocusRect();
                };
                //设置聚焦超时
                mHandler.postDelayed(mRunnable, 3000);
            }
        }
        return super.onTouchEvent(event);
    }


    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            isFoucing = false;
            mOverCameraView.setFoucuing(false);
            mOverCameraView.disDrawTouchFocusRect();
            //停止聚焦超时回调
            mHandler.removeCallbacks(mRunnable);
        }
    };

    private void takePhoto() {
        isTakePhoto = true;

        //调用相机拍照
        mCamera.takePicture(null, null, null, (data, camera1) -> {
            imageData = data;
            mCamera.stopPreview();
            savePhoto();
            //开始预览
            mCamera.startPreview();
        });
    }

    /**

     */
    private void switchFlash() {
        isFlashing = !isFlashing;
        mFlashButton.setImageResource(isFlashing ? R.mipmap.flash_open : R.mipmap.flash_close);
        AnimSpring.getInstance(mFlashButton).startRotateAnim(120, 360);
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode(isFlashing ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            Toast.makeText(this, "该设备不支持闪光灯", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cancle_button) {
            finish();
        } else if (id == R.id.take_photo_button) {
            if (!isTakePhoto) {
                takePhoto();
            }
        } else if (id == R.id.flash_button) {
            switchFlash();
        }
    }


    /**
     * 注释：蒙版类型

     */
    public enum MongolianLayerType {

        IDCARD_POSITIVE,

    }


    /**
     * 注释：初始化视图

     */
    private void initView() {
        mCancleButton = findViewById(R.id.cancle_button);
        mPreviewLayout = findViewById(R.id.camera_preview_layout);
        mPhotoLayout = findViewById(R.id.ll_photo_layout);
        mPhotoButton = findViewById(R.id.take_photo_button);
        mFlashButton = findViewById(R.id.flash_button);
        cameraPreviewLayout = (FrameLayout) findViewById(R.id.camera_preview_layout);

        flashButton = (ImageView) findViewById(R.id.flash_button);
        llConfirmLayout = (RelativeLayout) findViewById(R.id.ll_confirm_layout);
        cancleSaveButton = (ImageView) findViewById(R.id.cancle_save_button);
        saveButton = (ImageView) findViewById(R.id.save_button);
        llPhotoLayout = (RelativeLayout) findViewById(R.id.ll_photo_layout);
        cancleButton = (Button) findViewById(R.id.cancle_button);
        takePhotoButton = (ImageView) findViewById(R.id.take_photo_button);
    }

    /**
     * 注释：保持图片
     */
    private void savePhoto() {
        FileOutputStream fos = null;
        String cameraPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "DCIM" + File.separator + "Camera";
        //相册文件夹
        File cameraFolder = new File(cameraPath);
        if (!cameraFolder.exists()) {
            cameraFolder.mkdirs();
        }
        //保存的图片文件
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String imagePath = cameraFolder.getAbsolutePath() + File.separator + "IMG_" + simpleDateFormat.format(new Date()) + ".png";
        File imageFile = new File(imagePath);
        try {
            fos = new FileOutputStream(imageFile);
            fos.write(imageData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();

                    isTakePhoto = false;
                    Intent intent=new Intent();
                    intent.putExtra("res",imagePath);
                    setResult(100,intent);
                    finish();

                } catch (IOException e) {
                    setResult(RESULT_FIRST_USER);
                    e.printStackTrace();
                }
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null!=scheduExec){
            scheduExec.shutdown();
        }
    } }