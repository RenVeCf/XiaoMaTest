package com.ipd.xiaomatest.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;

import com.ipd.xiaomatest.R;
import com.ipd.xiaomatest.base.BaseActivity;
import com.ipd.xiaomatest.bean.LockBean;
import com.ipd.xiaomatest.contract.LockContract;
import com.ipd.xiaomatest.presenter.LockPresenter;
import com.ipd.xiaomatest.utils.ApplicationUtil;
import com.ipd.xiaomatest.utils.MD5Utils;
import com.ipd.xiaomatest.utils.StringUtils;
import com.ipd.xiaomatest.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;

import static android.Manifest.permission.CAMERA;
import static com.ipd.xiaomatest.common.config.IConstants.REQUEST_CODE_90;
import static com.ipd.xiaomatest.common.config.IConstants.REQUEST_CODE_91;
import static com.ipd.xiaomatest.utils.isClickUtil.isFastClick;

public class MainActivity extends BaseActivity<LockContract.View, LockContract.Presenter> implements LockContract.View {

    @BindView(R.id.tv_car)
    SuperTextView tvCar;
    @BindView(R.id.tv_master)
    SuperTextView tvMaster;

    private long firstTime = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public LockContract.Presenter createPresenter() {
        return new LockPresenter(this);
    }

    @Override
    public LockContract.View createView() {
        return this;
    }

    @Override
    public void init() {
        //将每个Activity加入到栈中
        ApplicationUtil.getManager().addActivity(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        tvCar.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                rxPermissionCamera(REQUEST_CODE_90);
            }
        });

        tvMaster.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                rxPermissionCamera(REQUEST_CODE_91);
            }
        });
    }

    //双击退出程序
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            ToastUtil.showShortToast(getResources().getString(R.string.click_out_again));
            firstTime = secondTime;
        } else {
            ApplicationUtil.getManager().exitApp();
        }
    }

    // 相机权限
    private void rxPermissionCamera(int qrType) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(CAMERA).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) throws Exception {
                if (granted) {
                    startActivityForResult(new Intent(MainActivity.this, QRActivity.class), qrType);
                } else {
                    // 权限被拒绝
                    ToastUtil.showLongToast(R.string.permission_rejected);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case REQUEST_CODE_90:
                    tvCar.setCenterString(data.getStringExtra("qr_code"));
                    break;
                case REQUEST_CODE_91:
                    tvMaster.setCenterString(data.getStringExtra("qr_code"));
                    break;
            }
        }
    }


    @Override
    public void resultBind(LockBean data) {
        ToastUtil.showLongToast(data.getMessage());
    }

    @Override
    public void resultLock(LockBean data) {
        ToastUtil.showLongToast(data.getMessage());
    }

    @Override
    public void resultBox(LockBean data) {
        ToastUtil.showLongToast(data.getMessage());
    }

    private void lock(String lockType) {
        if (!"车辆 QR_CODE".equals(tvCar.getCenterString()) && isFastClick()) {
            TreeMap<String, String> lockMap = new TreeMap<>();
            lockMap.put("item_no", tvCar.getCenterString());
            lockMap.put("type", lockType);
            lockMap.put("timestamp", String.format("%010d", System.currentTimeMillis() / 1000));
            lockMap.put("sign", StringUtils.toLowerCase(MD5Utils.encodeMD5("secret=5c3da039f485e9f4&timestamp=" + String.format("%010d", System.currentTimeMillis() / 1000))));
            getPresenter().getLock(lockMap, false, false);
        } else {
            ToastUtil.showLongToast("请扫描车辆CODE");
        }
    }

    @Override
    public <T> ObservableTransformer<T, T> bindLifecycle() {
        return this.bindToLifecycle();
    }

    @OnClick({R.id.bt_bind, R.id.bt_open_lock, R.id.bt_close_lock, R.id.bt_open_electric_box})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_bind:
                if (!"车辆 QR_CODE".equals(tvCar.getCenterString()) && !"中控 QR_CODE".equals(tvMaster.getCenterString()) && isFastClick()) {
                    TreeMap<String, String> bindMap = new TreeMap<>();
                    bindMap.put("item_no", tvCar.getCenterString());
                    bindMap.put("imei", tvMaster.getCenterString());
                    bindMap.put("timestamp", String.format("%010d", System.currentTimeMillis() / 1000));
                    bindMap.put("sign", StringUtils.toLowerCase(MD5Utils.encodeMD5("secret=5c3da039f485e9f4&timestamp=" + String.format("%010d", System.currentTimeMillis() / 1000))));
                    getPresenter().getBind(bindMap, false, false);
                } else {
                    ToastUtil.showLongToast("请扫描CODE");
                }
                break;
            case R.id.bt_open_lock:
                lock("0");
                break;
            case R.id.bt_close_lock:
                lock("1");
                break;
            case R.id.bt_open_electric_box:
                if (!"车辆 QR_CODE".equals(tvCar.getCenterString()) && isFastClick()) {
                    TreeMap<String, String> boxMap = new TreeMap<>();
                    boxMap.put("item_no", tvCar.getCenterString());
                    boxMap.put("timestamp", String.format("%010d", System.currentTimeMillis() / 1000));
                    boxMap.put("sign", StringUtils.toLowerCase(MD5Utils.encodeMD5("secret=5c3da039f485e9f4&timestamp=" + String.format("%010d", System.currentTimeMillis() / 1000))));
                    getPresenter().getBox(boxMap, false, false);
                } else {
                    ToastUtil.showLongToast("请扫描车辆CODE");
                }
                break;
        }
    }
}
