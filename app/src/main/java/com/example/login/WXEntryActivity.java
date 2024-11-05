package com.example.login;

import android.app.Activity;
import android.os.Bundle;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import android.util.Log;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String APP_ID = "wxb83fe7dd6b438aea";
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) resp).code; // 获取授权码
                Log.d("WXEntryActivity", "登录成功，授权码: " + code);
                // 处理登录成功后的逻辑
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.d("WXEntryActivity", "用户取消登录");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.d("WXEntryActivity", "授权失败");
                break;
            default:
                Log.d("WXEntryActivity", "未知错误，错误码: " + resp.errCode);
                break;
        }
        finish(); // 关闭该 Activity
    }

    @Override
    public void onReq(BaseReq req) {
        // 处理请求
    }
}
