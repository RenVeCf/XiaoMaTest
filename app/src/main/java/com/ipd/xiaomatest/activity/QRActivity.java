package com.ipd.xiaomatest.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.gyf.immersionbar.ImmersionBar;
import com.ipd.xiaomatest.R;
import com.ipd.xiaomatest.base.BaseActivity;
import com.ipd.xiaomatest.base.BasePresenter;
import com.ipd.xiaomatest.base.BaseView;
import com.ipd.xiaomatest.common.view.TopView;
import com.ipd.xiaomatest.utils.ApplicationUtil;
import com.ipd.xiaomatest.utils.L;
import com.ipd.xiaomatest.utils.ToastUtil;
import com.xuexiang.xqrcode.XQRCode;
import com.xuexiang.xqrcode.ui.CaptureActivity;
import com.xuexiang.xqrcode.ui.CaptureFragment;
import com.xuexiang.xqrcode.util.QRCodeAnalyzeUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ipd.xiaomatest.utils.StringUtils.identical;

/**
 * Description ：扫码
 * Author ： rmy
 * Email ： 942685687@qq.com
 * Time ： 2019/8/28.
 */
public class QRActivity extends BaseActivity {

    @BindView(R.id.tv_qr)
    TopView tvQr;
    @BindView(R.id.cb_flash)
    MaterialCheckBox cbFlash;
    @BindView(R.id.tv_flash)
    AppCompatTextView tvFlash;

    private String carNum;//扫描的车辆编号

    @Override
    public int getLayoutId() {
        return R.layout.activity_qr;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public BaseView createView() {
        return null;
    }

    @Override
    public void init() {
        //将每个Activity加入到栈中
        ApplicationUtil.getManager().addActivity(this);
        //防止状态栏和标题重叠
        ImmersionBar.setTitleBar(this, tvQr);

        CaptureFragment captureFragment = XQRCode.getCaptureFragment(R.layout.activity_custom_capture, true, 1000);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        captureFragment.setCameraInitCallBack(cameraInitCallBack);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_zxing_container, captureFragment).commit();
        //设置相机的自动聚焦间隔
        XQRCode.setAutoFocusInterval(1500L);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    /**
     * 照相机初始化监听
     */
    CaptureFragment.CameraInitCallBack cameraInitCallBack = new CaptureFragment.CameraInitCallBack() {
        @Override
        public void callBack(@Nullable Exception e) {
            if (e != null) {
                CaptureActivity.showNoPermissionTip(QRActivity.this);
            }
        }
    };

    /**
     * 二维码解析回调函数
     */
    QRCodeAnalyzeUtils.AnalyzeCallback analyzeCallback = new QRCodeAnalyzeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap bitmap, String result) {
            handleAnalyzeSuccess(bitmap, result);
        }

        @Override
        public void onAnalyzeFailed() {
            handleAnalyzeFailed();
        }
    };

    /**
     * 处理扫描成功
     *
     * @param bitmap
     * @param result
     */
    protected void handleAnalyzeSuccess(Bitmap bitmap, String result) {
        L.i("result = " + result);
        if (result.indexOf("http") != -1)
            carNum = identical(result, "imei=", "&sn").replaceAll("imei=", "").replaceAll("&", "").trim();
        else
            carNum = identical(result, "IMEI:", " ").replaceAll("IMEI:", "").trim();
        setResult(RESULT_OK, new Intent().putExtra("qr_code", carNum));
        finish();
    }

    /**
     * 处理解析失败
     */
    protected void handleAnalyzeFailed() {
        ToastUtil.showLongToast("扫描失败，请重试");
    }

    @OnClick({R.id.cb_flash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_flash:
                if (cbFlash.isChecked()) {
                    XQRCode.switchFlashLight(true);
                    tvFlash.setText("关灯");
                } else
                    try {
                        XQRCode.switchFlashLight(false);
                        tvFlash.setText("开灯");
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        ToastUtil.showLongToast("设备不支持闪光灯!");
                    }
                break;
        }
    }
}
