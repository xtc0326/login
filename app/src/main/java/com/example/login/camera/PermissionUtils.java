package com.example.login.camera;

import android.content.Context;
import android.os.Build;
import androidx.fragment.app.Fragment;


import com.yanzhenjie.permission.AndPermission;

/**
 * 注释: Android权限申请工具类
 */
public class PermissionUtils {

    /**
     * 注释：权限申请回调接口，用于处理权限申请的结果
     */
    public interface PermissionListener {
        /**
         * 注释：权限申请成功时调用的方法
         * @param context 上下文对象
         */
        void onSuccess(Context context);

        /**
         * 注释：权限申请失败时调用的方法
         * @param context 上下文对象
         */
        void onFailed(Context context);
    }

    /**
     * 注释：申请一组权限
     * @param context 上下文对象
     * @param listener 权限申请回调接口
     * @param permissions 需要申请的权限组
     */
    public static void applicationPermissions(Context context, PermissionListener listener, String[]... permissions) {
        // 检查当前系统版本是否大于Android 6.0 (API 23)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            // 检查是否已经拥有这些权限
            if (!AndPermission.hasPermissions(context, permissions)) {
                // 如果没有权限，则申请权限
                AndPermission.with(context)
                        .runtime()
                        .permission(permissions)  // 需要申请的权限
                        .rationale((mContext, data, executor) -> {
                            // 显示权限申请理由提示框
                            executor.execute();  // 用户同意后继续执行权限申请
                        })
                        .onGranted((permission) -> {
                            // 权限申请成功时调用
                            listener.onSuccess(context);
                        })
                        .onDenied((permission) -> {
                            // 权限申请失败时调用
                            listener.onFailed(context);
                        })
                        .start();
            } else {
                // 如果已经有权限，直接调用成功回调
                listener.onSuccess(context);
            }
        } else {
            // 如果系统版本低于Android 6.0，直接调用成功回调
            listener.onSuccess(context);
        }
    }

    /**
     * 注释：申请单个权限
     * @param context 上下文对象
     * @param listener 权限申请回调接口
     * @param permissions 需要申请的权限
     */
    public static void applicationPermissions(Context context, PermissionListener listener, String... permissions) {
        // 检查当前系统版本是否大于Android 5.1 (API 22)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            // 检查是否已经拥有这些权限
            if (!AndPermission.hasPermissions(context, permissions)) {
                // 如果没有权限，则申请权限
                AndPermission.with(context)
                        .runtime()
                        .permission(permissions)  // 需要申请的权限
                        .rationale((mContext, data, executor) -> {
                            // 显示权限申请理由提示框
                            executor.execute();  // 用户同意后继续执行权限申请
                        })
                        .onGranted((permission) -> {
                            // 权限申请成功时调用
                            listener.onSuccess(context);
                        })
                        .onDenied((permission) -> {
                            // 权限申请失败时调用
                            listener.onFailed(context);
                        })
                        .start();
            } else {
                // 如果已经有权限，直接调用成功回调
                listener.onSuccess(context);
            }
        } else {
            // 如果系统版本低于Android 5.1，直接调用成功回调
            listener.onSuccess(context);
        }
    }
}
