package com.zhenggzh.dream.retrofitandrxjavademo.netutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;


import com.zhenggzh.dream.retrofitandrxjavademo.utils.CompressUtils;

import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by 眼神 on 2018/3/27.
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理   成功时 通过result是否等于1分别回调onSuccess和onFault，默认处理了401错误转登录。
 * 回调结果为String，需要手动序列化
 */

public class OnSuccessAndFaultSub extends Subscriber<ResponseBody> implements ProgressCancelListener {
    /**
     * 是否需要显示默认Loading
     */
    private boolean showProgress = true;
    private OnSuccessAndFaultListener mOnSuccessAndFaultListener;

    private Context context;
    private ProgressDialog progressDialog;


    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
    }


    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     * @param context                    上下文
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, Context context) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     * @param context                    上下文
     * @param showProgress               是否需要显示默认Loading
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, Context context, boolean showProgress) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        this.showProgress = showProgress;
    }

    private void showProgressDialog() {
        if (showProgress && null != progressDialog) {
            progressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (showProgress && null != progressDialog) {
            progressDialog.dismiss();
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
        progressDialog = null;
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        try {

            if (e instanceof SocketTimeoutException) {//请求超时
            } else if (e instanceof ConnectException) {//网络连接超时
//                ToastManager.showShortToast("网络连接超时");
                mOnSuccessAndFaultListener.onFault("网络连接超时");
            } else if (e instanceof SSLHandshakeException) {//安全证书异常
//                ToastManager.showShortToast("安全证书异常");
                mOnSuccessAndFaultListener.onFault("安全证书异常");
            } else if (e instanceof HttpException) {//请求的地址不存在
                int code = ((HttpException) e).code();
                if (code == 504) {
//                    ToastManager.showShortToast("网络异常，请检查您的网络状态");
                    mOnSuccessAndFaultListener.onFault("网络异常，请检查您的网络状态");
                } else if (code == 404) {
//                    ToastManager.showShortToast("请求的地址不存在");
                    mOnSuccessAndFaultListener.onFault("请求的地址不存在");
                } else {
//                    ToastManager.showShortToast("请求失败");
                    mOnSuccessAndFaultListener.onFault("请求失败");
                }
            } else if (e instanceof UnknownHostException) {//域名解析失败
//                ToastManager.showShortToast("域名解析失败");
                mOnSuccessAndFaultListener.onFault("域名解析失败");
            } else {
//                ToastManager.showShortToast("error:" + e.getMessage());
                mOnSuccessAndFaultListener.onFault("error:" + e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            Log.e("OnSuccessAndFaultSub", "error:" + e.getMessage());
//            mOnSuccessAndFaultListener.onFault("error:" + e.getMessage());
            dismissProgressDialog();
            progressDialog = null;

        }

    }

    /**
     * 当result等于1回调给调用者，否则自动显示错误信息，若错误信息为401跳转登录页面。
     */
    @Override
    public void onNext(ResponseBody body) {
        try {
            final String result = CompressUtils.decompress(body.byteStream());
            Log.e("body", result);
            JSONObject jsonObject = new JSONObject(result);
            int resultCode = jsonObject.getInt("ErrorCode");
            if (resultCode == 1) {
                mOnSuccessAndFaultListener.onSuccess(result);
            } else {
                String errorMsg = jsonObject.getString("ErrorMessage");
                mOnSuccessAndFaultListener.onFault(errorMsg);
                Log.e("OnSuccessAndFaultSub", "errorMsg: " + errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
