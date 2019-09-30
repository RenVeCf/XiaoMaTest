package com.ipd.xiaomatest.presenter;

import android.content.Context;

import com.ipd.xiaomatest.bean.LockBean;
import com.ipd.xiaomatest.contract.LockContract;
import com.ipd.xiaomatest.model.LockModel;
import com.ipd.xiaomatest.progress.ObserverResponseListener;
import com.ipd.xiaomatest.utils.ExceptionHandle;
import com.ipd.xiaomatest.utils.ToastUtil;

import java.util.TreeMap;

/**
 * Description ：
 * Author ： MengYang
 * Email ： 942685687@qq.com
 * Time ： 2018/8/26.
 */
public class LockPresenter extends LockContract.Presenter {

    private LockModel model;
    private Context context;

    public LockPresenter(Context context) {
        this.model = new LockModel();
        this.context = context;
    }

    @Override
    public void getBind(TreeMap<String, String> map, boolean isDialog, boolean cancelable) {
        model.getBind(context, map, isDialog, cancelable, getView().bindLifecycle(), new ObserverResponseListener() {
            @Override
            public void onNext(Object o) {
                //这一步是必须的，判断view是否已经被销毁
                if (getView() != null) {
                    getView().resultBind((LockBean) o);
                }
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                if (getView() != null) {
                    //// TODO: 2017/12/28 自定义处理异常
                    ToastUtil.showShortToast(ExceptionHandle.handleException(e).message);
                }
            }
        });
    }

    @Override
    public void getLock(TreeMap<String, String> map, boolean isDialog, boolean cancelable) {
        model.getLock(context, map, isDialog, cancelable, getView().bindLifecycle(), new ObserverResponseListener() {
            @Override
            public void onNext(Object o) {
                //这一步是必须的，判断view是否已经被销毁
                if (getView() != null) {
                    getView().resultLock((LockBean) o);
                }
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                if (getView() != null) {
                    //// TODO: 2017/12/28 自定义处理异常
                    ToastUtil.showShortToast(ExceptionHandle.handleException(e).message);
                }
            }
        });
    }

    @Override
    public void getBox(TreeMap<String, String> map, boolean isDialog, boolean cancelable) {
        model.getBox(context, map, isDialog, cancelable, getView().bindLifecycle(), new ObserverResponseListener() {
            @Override
            public void onNext(Object o) {
                //这一步是必须的，判断view是否已经被销毁
                if (getView() != null) {
                    getView().resultBox((LockBean) o);
                }
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                if (getView() != null) {
                    //// TODO: 2017/12/28 自定义处理异常
                    ToastUtil.showShortToast(ExceptionHandle.handleException(e).message);
                }
            }
        });
    }
}