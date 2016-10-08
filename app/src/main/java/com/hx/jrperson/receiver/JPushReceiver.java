package com.hx.jrperson.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.ui.activity.MainActivity;
import com.hx.jrperson.ui.activity.NegotiatePriceActivity;
import com.hx.jrperson.bean.entity.OrderEntity;
import com.hx.jrperson.bean.entity.PushOrder;
import com.hx.jrperson.utils.BadgeUtil;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.AlterPriceDialog;
import com.hx.jrperson.views.FinishOrderDialog;
import com.hx.jrperson.views.OrderPopDialog;
import com.hx.jrperson.views.baseView.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 接收极光推送消息的服务
 * Created by ge on 2016/3/14.
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    private PushOrder order;

    private OrderPopDialog orderPopDialog;
    private AlterPriceDialog alterPriceDialog;
    private String title;
    private FinishOrderDialog finishOrderDialog;
    private Handler handler;


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
    private int numberMessage = 0;//消息数量
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!BaseActivity.isActivity){
            numberMessage = PreferencesUtils.getInt(context, Consts.NUMBERMESSAGE);
            numberMessage++;
            PreferencesUtils.putInt(context, Consts.NUMBERMESSAGE, numberMessage);
            BadgeUtil.setBadgeCount(context, numberMessage);
        }
        final Bundle bundle = intent.getExtras();
        boolean isShow = BaseActivity.isActivity;
        handler = new Handler();
        Log.d(TAG, "[MyReceiver1] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (bundle.getString("cn.jpush.android.EXTRA") != null){
            Gson gson = new Gson();
            order = gson.fromJson(bundle.getString("cn.jpush.android.EXTRA"), PushOrder.class);
            if (order != null){
                if (isShow){//app在前台运行
                    JPushInterface.clearAllNotifications(context);
                    if ("2".equals(order.getOrderStatus())){//抢单消息
                        orderMessage(order, context);
                    }else if ("3".equals(order.getOrderStatus())){//改价格状态
                        title = bundle.getString("cn.jpush.android.ALERT");
                        alterPriceMessage(order, context);
                    }else if ("5".equals(order.getOrderStatus())){//结束任务状态
                        title = bundle.getString("cn.jpush.android.ALERT");
                        finishOrderMessage(order, context, bundle);
                    }else if ("56".equals(order.getOrderStatus())){//我家升级装修监控
                        title = bundle.getString("cn.jpush.android.ALERT");
                        myHomeUpdate(context);
                    }
                }
            }
        }
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver2] 接收Registration Id : " + regId);
            // send the Registration Id to your server...
            //如果是第一次向激光注册 则会在本回调方法中返回registrationId 并保存到缓存中
            PreferencesUtils.putString(context, Consts.REGISTRATIONID, regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver3] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver4] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver5] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver6] 用户点击打开了通知");
            final boolean isShowing = BaseActivity.isActivity;//app是否在前台运行
            //当点击了通知 先请求下订单状态 如果一样再继续操作 如果不一样就不进行操作
            String url = API.ORDER_DETAIL;
            final String orderid = order.getOrderId();
            final String staute = order.getOrderStatus();
            Map<String, String> map = new HashMap<>();
            map.put(Consts.ORDER_ID, orderid);
            url = JrUtils.appendParams(url, map);
            NetLoader.getInstance(context).loadGetData(context, url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int code) {
                    if (401 != code){
                        Gson gson = new Gson();
                        OrderEntity entity = gson.fromJson(resultString, OrderEntity.class);
                        String newstaute = String.valueOf(entity.getDataMap().getOrder_status());
                        if (newstaute.equals(staute)) {
                            if (!isShowing) {//不在当前app
                                //打开自定义的Activity
                                if ("2".equals(order.getOrderStatus())) {//抢单
                                    clickMessageOrder(context);
                                } else if ("3".equals(order.getOrderStatus())) {//改价格
                                    clickMessageAlterPrice(context, bundle);
                                } else if ("5".equals(order.getOrderStatus())){//完成
                                    clickMessageFinish(context, bundle);
                                } else if ("56".equals(order.getOrderStatus())){//我家升级 装修监控
                                    clickMyHomeUpdata(context);
                                }
                            } else {//在app中
                                //打开自定义的Activity
                                if ("2".equals(order.getOrderStatus())) {//抢单
                                    clickMessageOrder(context);
                                } else if ("3".equals(order.getOrderStatus())) {//改价格
                                    clickMessageAlterPrice(context, bundle);
                                } else if ("5".equals(order.getOrderStatus())){
                                    clickMessageFinish(context, bundle);
                                }
                            }
                        }
                    }
                }

                @Override
                public void fail(String failString, Exception e) {
                    Log.i("geanwen", failString);
                }
            });

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver7] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver8]" + intent.getAction() +" connected state change to "+connected);
        } else {
            Log.d(TAG, "[MyReceiver9] Unhandled intent - " + intent.getAction());
        }
    }

    /**
     * 订单状态56
     * 我家升级或装修监控
     * 不在app中点击notifycation
     * **/
    private void clickMyHomeUpdata(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        if (finishOrderDialog != null){
            return;
        }else {
            finishOrderDialog = new FinishOrderDialog(context, title);
            finishOrderDialog.show();
            finishOrderDialog.setOnClickFinishListener(new FinishOrderDialog.OnClickFinishListener() {
                @Override
                public void onClickFinish(View view) {
                    finishOrderDialog.dismiss();
                }
            });
        }
    }


    /**
     * 订单状态56
     * 我家升级或装修监控
     * **/
    private void myHomeUpdate(final Context context) {
        if (finishOrderDialog != null){
//            finishOrderDialog.dismiss();
//            finishOrderDialog = null;
            return;
        }
        finishOrderDialog = new FinishOrderDialog(context, title);
        finishOrderDialog.show();
        finishOrderDialog.setOnClickFinishListener(new FinishOrderDialog.OnClickFinishListener() {
            @Override
            public void onClickFinish(View view) {
                finishOrderDialog.dismiss();
            }
        });
    }


    /**
     * 订单已完成
     * 在后台运行
     * **/
    private void clickMessageFinish(final Context context, Bundle bundle) {
        title = bundle.getString("cn.jpush.android.ALERT");
        Intent i = new Intent(context, MainActivity.class);
//        i.putExtras(bundle);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        if (NegotiatePriceActivity.insance == null){
//            NegotiatePriceActivity.insance.finish();
            if (finishOrderDialog != null){
                finishOrderDialog.dismiss();
                finishOrderDialog = null;
            }
            finishOrderDialog = new FinishOrderDialog(context, title);
            finishOrderDialog.show();
            finishOrderDialog.setOnClickFinishListener(new FinishOrderDialog.OnClickFinishListener() {
                @Override
                public void onClickFinish(View view) {
                    finishOrderDialog.dismiss();
//                                            if (context != NegotiatePriceActivity.insance){
                    Intent intent1 = new Intent(context, NegotiatePriceActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("orderid", order.getOrderId());
                    bundle1.putString("staute", "5");
                    intent1.putExtras(bundle1);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            });
        }else {
            if (finishOrderDialog != null){
                finishOrderDialog.dismiss();
                finishOrderDialog = null;
            }
            finishOrderDialog = new FinishOrderDialog(context, title);
            finishOrderDialog.show();
            finishOrderDialog.setOnClickFinishListener(new FinishOrderDialog.OnClickFinishListener() {
                @Override
                public void onClickFinish(View view) {
                    finishOrderDialog.dismiss();
                    EventBus.getDefault().post(order);
                }
            });
        }
    }


    /**
     * 订单已完成
     * 通知 在app中
     * **/
    private void finishOrderMessage(final PushOrder order, final Context context, Bundle bundle) {
        title = bundle.getString("cn.jpush.android.ALERT");
        if (NegotiatePriceActivity.insance == null){
            if (finishOrderDialog != null){
                finishOrderDialog.dismiss();
                finishOrderDialog = null;
            }
            finishOrderDialog = new FinishOrderDialog(context, title);
            finishOrderDialog.show();
            finishOrderDialog.setOnClickFinishListener(new FinishOrderDialog.OnClickFinishListener() {
                @Override
                public void onClickFinish(View view) {
                    finishOrderDialog.dismiss();
                    Intent intent1 = new Intent(context, NegotiatePriceActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("orderid", order.getOrderId());
                    bundle1.putString("staute", "5");
                    intent1.putExtras(bundle1);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            });
        }else {
            if (finishOrderDialog != null){
                finishOrderDialog.dismiss();
                finishOrderDialog = null;
            }
            finishOrderDialog = new FinishOrderDialog(context, title);
            finishOrderDialog.show();
            finishOrderDialog.setOnClickFinishListener(new FinishOrderDialog.OnClickFinishListener() {
                @Override
                public void onClickFinish(View view) {
                    finishOrderDialog.dismiss();
                    EventBus.getDefault().post(order);
                }
            });
        }
    }


    /**
     * 点击通知
     * 改价格状态的通知
     * **/
    private void clickMessageAlterPrice(final Context context, Bundle bundle) {
        title = bundle.getString("cn.jpush.android.ALERT");
        Intent i = new Intent(context, MainActivity.class);
//        i.putExtras(bundle);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        if (finishOrderDialog != null){
            finishOrderDialog.dismiss();
            finishOrderDialog = null;
        }
        finishOrderDialog = new FinishOrderDialog(context, title);
        finishOrderDialog.show();
        finishOrderDialog.setOnClickFinishListener(new FinishOrderDialog.OnClickFinishListener() {
            @Override
            public void onClickFinish(View view) {
                finishOrderDialog.dismiss();
                Intent intent1 = new Intent(context, NegotiatePriceActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("orderid", order.getOrderId());
                bundle1.putString("staute", "3");
                intent1.putExtras(bundle1);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
        });
    }


    /**
     * 点击通知
     * 抢单状态得通知
     * **/
    private void clickMessageOrder(final Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        String url = API.ORDER_DETAIL;
        String orderid = order.getOrderId();
        Map<String, String> map = new HashMap<>();
        map.put(Consts.ORDER_ID, orderid);
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(context).loadGetData(context, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                if (code == 200) {
                    Gson gson = new Gson();
                    final OrderEntity entity = gson.fromJson(resultString, OrderEntity.class);
                    int wid = PreferencesUtils.getInt(context, Consts.WID);
                    int hei = PreferencesUtils.getInt(context, Consts.HEI);
                    if (orderPopDialog != null) {
//                        return;
                        orderPopDialog.dismiss();
                        orderPopDialog = null;
                        orderPopDialog = new OrderPopDialog(context, wid, hei, entity);
                        orderPopDialog.show();
                        orderPopDialog.setOnClickWorkerHeadListener(new OrderPopDialog.OnClickWorkerHeadListener() {
                            @Override
                            public void onClickWorkerHead(View view, OrderEntity orderEntity) {
                                orderPopDialog.dismiss();
                                Intent intent1 = new Intent(context, NegotiatePriceActivity.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("orderid", order.getOrderId());
                                bundle1.putSerializable("jupsh", orderEntity);
                                bundle1.putString("staute", "2");
                                intent1.putExtras(bundle1);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent1);
                            }
                        });
                    } else {
                        orderPopDialog = new OrderPopDialog(context, wid, hei, entity);
                        orderPopDialog.show();
                        orderPopDialog.setOnClickWorkerHeadListener(new OrderPopDialog.OnClickWorkerHeadListener() {
                            @Override
                            public void onClickWorkerHead(View view, OrderEntity orderEntity) {
                                orderPopDialog.dismiss();
                                Intent intent1 = new Intent(context, NegotiatePriceActivity.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("orderid", order.getOrderId());
                                bundle1.putSerializable("jupsh", orderEntity);
                                bundle1.putString("staute", "2");
                                intent1.putExtras(bundle1);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent1);
                            }
                        });
                    }
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {
                Log.i("geanwen", failString);
            }
        });
    }


    /**
     * app在前台运行
     * 当有改价格得消息时
     * **/
    private void alterPriceMessage(final PushOrder order, final Context context) {
        if (NegotiatePriceActivity.insance == null){
            if (finishOrderDialog != null){
//                alterPriceDialog.dismiss();
//                alterPriceDialog = null;
                return;
            }else {
                finishOrderDialog = new FinishOrderDialog(context, title);
                finishOrderDialog.show();
                finishOrderDialog.setOnClickFinishListener(new FinishOrderDialog.OnClickFinishListener() {
                    @Override
                    public void onClickFinish(View view) {
                        finishOrderDialog.dismiss();
                        Intent intent1 = new Intent(context, NegotiatePriceActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("orderid", order.getOrderId());
                        bundle1.putString("staute", "3");
                        intent1.putExtras(bundle1);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
                    }
                });
            }
        }else {
            if (finishOrderDialog != null){
//                alterPriceDialog.dismiss();
//                alterPriceDialog = null;
                return;
            }
            finishOrderDialog = new FinishOrderDialog(context, title);
            finishOrderDialog.show();
            finishOrderDialog.setOnClickFinishListener(new FinishOrderDialog.OnClickFinishListener() {
                @Override
                public void onClickFinish(View view) {
                    finishOrderDialog.dismiss();
                    EventBus.getDefault().post(order);
                }
            });
        }
    }


    /**
     * app在前台运行时
     * 有抢单消息
     * 最上层弹出提示窗口
     * **/
    private void orderMessage(final PushOrder order, final Context context) {
        String url = API.ORDER_DETAIL;
        String orderid = order.getOrderId();
        Map<String, String> map = new HashMap<>();
        map.put(Consts.ORDER_ID, orderid);
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(context).loadGetData(context, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                if (code == 200){
                    Gson gson = new Gson();
                    final OrderEntity entity = gson.fromJson(resultString, OrderEntity.class);
                    int wid = PreferencesUtils.getInt(context, Consts.WID);
                    int hei = PreferencesUtils.getInt(context, Consts.HEI);
                    if (NegotiatePriceActivity.insance == null){
                        orderPopDialog = new OrderPopDialog(context, wid, hei, entity);
                        orderPopDialog.show();
                        orderPopDialog.setOnClickWorkerHeadListener(new OrderPopDialog.OnClickWorkerHeadListener() {
                            @Override
                            public void onClickWorkerHead(View view, OrderEntity orderEntity) {
                                switch (view.getId()){
                                    case R.id.popupOrderLL:
                                        orderPopDialog.dismiss();
                                        Intent intent1 = new Intent(context, NegotiatePriceActivity.class);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putString("orderid", order.getOrderId());
                                        bundle1.putSerializable("jupsh", orderEntity);
                                        bundle1.putString("staute", "2");
                                        intent1.putExtras(bundle1);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent1);
                                        break;
                                    case R.id.call_jr_phoneIV:
//                                        orderPopDialog.dismiss();
                                        clickCallPhone(context, orderEntity.getDataMap().getWorker_mobile());
                                        break;
                                }

                            }
                        });
                    }else {
                        orderPopDialog = new OrderPopDialog(context, wid, hei, entity);
                        orderPopDialog.show();
                        orderPopDialog.setOnClickWorkerHeadListener(new OrderPopDialog.OnClickWorkerHeadListener() {
                            @Override
                            public void onClickWorkerHead(View view, OrderEntity orderEntity) {
                                orderPopDialog.dismiss();
                                EventBus.getDefault().post(order);
                            }
                        });
                    }
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void fail(String failString, Exception e) {
                Log.i("geanwen", failString);
            }
        });

    }


    //给匠人打电话
    private void clickCallPhone(Context context, String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(callIntent);
    }

    // 发消息到指定的activity
//    private void processCustomMessage(Context context, Bundle bundle) {
//        if (HomeActivity.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (null != extraJson && extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            context.sendBroadcast(msgIntent);
//        }
//    }
}
