package com.ipd.xiaomatest.contract;

import com.ipd.xiaomatest.base.BasePresenter;
import com.ipd.xiaomatest.base.BaseView;
import com.ipd.xiaomatest.bean.LockBean;

import java.util.TreeMap;

import io.reactivex.ObservableTransformer;

/**
 * Description ：MemberCenterContract  V 、P契约类
 * Author ： MengYang
 * Email ： 942685687@qq.com
 * Time ： 2019/4/2.
 */
public interface LockContract {

    interface View extends BaseView {
        //不同的Bean单独处理
        void resultBind(LockBean data);

        void resultLock(LockBean data);

        void resultBox(LockBean data);

        <T> ObservableTransformer<T, T> bindLifecycle();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void getBind(TreeMap<String, String> map, boolean isDialog, boolean cancelable);

        public abstract void getLock(TreeMap<String, String> map, boolean isDialog, boolean cancelable);

        public abstract void getBox(TreeMap<String, String> map, boolean isDialog, boolean cancelable);
    }
}