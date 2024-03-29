package com.ipd.xiaomatest.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.xuexiang.xui.XUI;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

/**
 * Application类 初始化各种配置
 */
public class ApplicationUtil extends Application {

    private static Context mContext; //全局上下文对象
    private static ApplicationUtil sManager;
    private Stack<WeakReference<Activity>> mActivityStack;

    @Override
    public void onCreate() {
        super.onCreate();
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
//        MobSDK.init(this);//http://www.mob.com/ 使用这个公司的分享功能，具体参照官网提示，页面分享功能初始化
//        CrashReport.initCrashReport(getApplicationContext());//Bugly
        mContext = getApplicationContext();

//        Map<String, Object> checkVersionMap = new HashMap<>();
//        checkVersionMap.put("type", "1");
//        checkVersionMap.put("sign", StringUtils.toUpperCase(MD5Utils.encodeMD5(checkVersionMap.toString().replaceAll(" ", "") + "F9A75BB045D75998E1509B75ED3A5225")));

//        XUpdate.get()
//                .debug(true)
//                .isWifiOnly(false)                                               //默认设置只在wifi下检查版本更新
//                .isGet(true)                                                    //默认设置使用get请求检查版本
//                .isAutoMode(false)
//                .params(checkVersionMap)//默认设置非自动模式，可根据具体使用配置
//                .setOnUpdateFailureListener(new OnUpdateFailureListener() {     //设置版本更新出错的监听
//                    @Override
//                    public void onFailure(UpdateError error) {
//                        if (error.getCode() != CHECK_NO_NEW_VERSION) {          //对不同错误进行处理
//                            ToastUtil.showShortToast(error.toString());
//                        }
//                    }
//                })
//                .supportSilentInstall(true)                                     //设置是否支持静默安装，默认是true
//                .setIUpdateHttpService(new OKHttpUpdateHttpService())           //这个必须设置！实现网络请求功能。
//                .init(this);
    }

    public static Context getContext() {
        return mContext;
    }

    public static ApplicationUtil getManager() {
        if (sManager == null) {
            synchronized (ApplicationUtil.class) {
                if (sManager == null) {
                    sManager = new ApplicationUtil();
                }
            }
        }
        return sManager;
    }

    /**
     * 添加Activity到栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(new WeakReference<>(activity));
    }

    /**
     * 检查弱引用是否释放，若释放，则从栈中清理掉该元素
     */
    public void checkWeakReference() {
        if (mActivityStack != null) {
            // 使用迭代器进行安全删除
            for (Iterator<WeakReference<Activity>> it = mActivityStack.iterator(); it.hasNext(); ) {
                WeakReference<Activity> activityReference = it.next();
                Activity temp = activityReference.get();
                if (temp == null) {
                    it.remove();
                }
            }
        }
    }

    /**
     * 获取当前Activity（栈中最后一个压入的）
     *
     * @return
     */
    public Activity currentActivity() {
        checkWeakReference();
        if (mActivityStack != null && !mActivityStack.isEmpty()) {
            return mActivityStack.lastElement().get();
        }
        return null;
    }

    /**
     * 关闭当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = currentActivity();
        if (activity != null) {
            finishActivity(activity);
        }
    }

    /**
     * 关闭指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && mActivityStack != null) {
            // 使用迭代器进行安全删除
            for (Iterator<WeakReference<Activity>> it = mActivityStack.iterator(); it.hasNext(); ) {
                WeakReference<Activity> activityReference = it.next();
                Activity temp = activityReference.get();
                // 清理掉已经释放的activity
                if (temp == null) {
                    it.remove();
                    continue;
                }
                if (temp == activity) {
                    it.remove();
                }
            }
            activity.finish();
        }
    }

    /**
     * 关闭指定类名的所有Activity
     *
     * @param cls
     */
    public void finishActivity(Class<?> cls) {
        if (mActivityStack != null) {
            // 使用迭代器进行安全删除
            for (Iterator<WeakReference<Activity>> it = mActivityStack.iterator(); it.hasNext(); ) {
                WeakReference<Activity> activityReference = it.next();
                Activity activity = activityReference.get();
                // 清理掉已经释放的activity
                if (activity == null) {
                    it.remove();
                    continue;
                }
                if (activity.getClass().equals(cls)) {
                    it.remove();
                    activity.finish();
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (mActivityStack != null) {
            for (WeakReference<Activity> activityReference : mActivityStack) {
                Activity activity = activityReference.get();
                if (activity != null) {
                    activity.finish();
                }
            }
            mActivityStack.clear();
        }
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            finishAllActivity();
            // 退出JVM,释放所占内存资源,0表示正常退出
            System.exit(0);
            // 从系统中kill掉应用程序
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
