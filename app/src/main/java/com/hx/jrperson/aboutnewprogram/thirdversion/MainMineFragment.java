package com.hx.jrperson.aboutnewprogram.thirdversion;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.PersonalInforEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.JrController;
import com.hx.jrperson.aboutnewprogram.mywollet.MyWollet;
import com.hx.jrperson.aboutnewprogram.mywollet.SelectMoneyClass;
import com.hx.jrperson.aboutnewprogram.preferential.MyPostCardActivity;
import com.hx.jrperson.aboutnewprogram.preferential.PostCardClass;
import com.hx.jrperson.ui.activity.PersonalSettingActivity;
import com.hx.jrperson.ui.activity.SettingActivity;
import com.hx.jrperson.ui.activity.StandardActivity;
import com.hx.jrperson.utils.CircleImage;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/23.
 * 主页面碎片文件中显示"我的"的碎片部分,整体嵌入在mainactivity中
 */
public class MainMineFragment extends Fragment {
    //联系文件
    private Context context;
    //在我的中的余额/优惠券/折扣卡的包含模块,分别对应firstModel/secondModel/ThirdModel
    private RelativeLayout thirdModel, secondModel, firstModel;
    //联系客服的组件
    private TextView callUs;
    //显示自己头像的组件
    private CircleImage goChangeMine;
    //个人信息数据类
    private PersonalInforEntity entity;
    private Handler handler;
    //获取当前个人信息的地址
    private String avaterUrl;
    //对应昵称和个性签名组件
    private TextView yourName, yourSay;
    //对应查询我的钱包/查询我的优惠券/查询我的订单/查询历史记录模块
    private RelativeLayout goMyWollet, goMyPostCard, goMyOrder, goMyServerState;
    //获取当前余额的网址
    private String urlForSelect;
    private TextView howManyIHave;
    //查询当前可用优惠券及折扣卡数量
    private String url;
    //记录当前优惠券数量及折扣卡数量
    private int postNum = 0, valueNum = 0;
    //显示优惠券和折扣卡数量的模块
    private TextView postCardNum, valueCardNum;
    //绑定由跳转到设置页面的组件
    private ImageView goMySetting;
    //用户登陆时产生的唯一身份标志
    private int user_id;
    //卡券选择结果传值
    private MySendReciver mysendreciver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.myhomepage, null);
        //本地拿到用户userid
        SharedPreferences getSp = getActivity().getSharedPreferences("TrineaAndroidCommon", context.MODE_PRIVATE);
        user_id = getSp.getInt("user_id", 0);
        //查询当前余额的地址
        urlForSelect = "http://123.57.185.241:8180/ZhenjiangrenManagement/api/v1/charge/remain?userId=" + user_id;
        //查询当前卡券数据的地址
        url = "http://123.57.185.241:8180/ZhenjiangrenManagement/api/v1/coupon/all?userId=" + user_id;
        //创建新的广播接收者
        mysendreciver = new MySendReciver();
        //相当于注册页面的操作
        IntentFilter intentFilter = new IntentFilter();
        //里面放的是自定义的内容
        intentFilter.addAction("com.example.dllo.broadcast.sendPrice");
        //与接收系统的一样
        getActivity().registerReceiver(mysendreciver, intentFilter);
        //获取个人信息的封装动作
        getPersonalInfor();
        handler = new Handler();
        //动态加载组件大小
        firstModel = (RelativeLayout) view.findViewById(R.id.firstModel);
        secondModel = (RelativeLayout) view.findViewById(R.id.secondModel);
        thirdModel = (RelativeLayout) view.findViewById(R.id.thirdModel);
        //绑定打电话功能组件
        callUs = (TextView) view.findViewById(R.id.callUs);
        //实现联系客服功能
        callUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "041184542809"));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                getActivity().startActivity(callIntent);
            }
        });
        //绑定个人信息按钮
        goChangeMine = (CircleImage) view.findViewById(R.id.goChangeMine);
        //点击个人按钮进入设置页面
        goChangeMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalSettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("personal", entity);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //实现数据的请求功能
        yourName = (TextView) view.findViewById(R.id.yourName);
        yourSay = (TextView) view.findViewById(R.id.yourSay);
        //实现进入钱包功能
        goMyWollet = (RelativeLayout) view.findViewById(R.id.goMyWollet);
        goMyWollet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyWollet.class);
                startActivity(intent);
            }
        });
        //实现进入我的卡券功能
        goMyPostCard = (RelativeLayout) view.findViewById(R.id.goMyPostCard);
        goMyPostCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), MyPostCardActivity.class);
                startActivity(intent2);
            }
        });
        //实现滑动到指定
        goMyOrder = (RelativeLayout) view.findViewById(R.id.goMyOrder);
        goMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.dllo.broadcast.GoSecondPager");
                //往广播里面放内容
                intent.putExtra("heihei", "走你");
                //进入广播启动项
                getActivity().sendBroadcast(intent);
            }
        });
        //实现服务标准的跳转功能
        goMyServerState = (RelativeLayout) view.findViewById(R.id.goMyServerState);
        goMyServerState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StandardActivity.class);
                startActivity(intent);
            }
        });

        //实现进入设置功能
        goMySetting = (ImageView) view.findViewById(R.id.goMySetting);
        goMySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent set_intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(set_intent);
            }
        });
        //获取当前屏幕大小,动态加载钱包/优惠券/折扣卡的相关组件的大小
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams Params = (LinearLayout.LayoutParams) firstModel.getLayoutParams();
        LinearLayout.LayoutParams Params2 = (LinearLayout.LayoutParams) secondModel.getLayoutParams();
        LinearLayout.LayoutParams Params3 = (LinearLayout.LayoutParams) thirdModel.getLayoutParams();
        Params.width = width / 3;
        Params2.width = width / 3;
        Params3.width = width / 3;
        //钱包组件
        firstModel.setLayoutParams(Params);
        //优惠券组件
        secondModel.setLayoutParams(Params2);
        //折扣卡组件
        thirdModel.setLayoutParams(Params3);
        //获取当前余额的网络申请
        howManyIHave = (TextView) view.findViewById(R.id.howManyIHave);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest2 = new StringRequest(urlForSelect, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                SelectMoneyClass selectMoneyClass = gson.fromJson(response, SelectMoneyClass.class);
                howManyIHave.setText("余额" + selectMoneyClass.getDataMap().getData().getRemine_amount() + "元");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "余额请求错误", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest2);
        //获取可用优惠券和折扣卡相关数据
        //网络请求数据
        postCardNum = (TextView) view.findViewById(R.id.postCardNum);
        valueCardNum = (TextView) view.findViewById(R.id.valueCardNum);
        RequestQueue queue2 = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //两个数据分别记录当前拥有的优惠券和折扣卡的数量数据
                postNum = 0;
                valueNum = 0;
                Gson gson = new Gson();
                PostCardClass postCardClass = gson.fromJson(response, PostCardClass.class);
                Log.i("bbbbbb", postCardClass.getDataMap().getCoupons().size() + "****");
                //进行请求后的数据筛选,选出优惠券和折扣卡的相关数量
                for (int i = 0; i < postCardClass.getDataMap().getCoupons().size(); i++) {
                    if (postCardClass.getDataMap().getCoupons().get(i).getUse_state() == 1 && postCardClass.getDataMap().getCoupons().get(i).getCoupon_kind() == 1) {
                        postNum++;
                    } else {
                        if (postCardClass.getDataMap().getCoupons().get(i).getUse_state() == 1 && postCardClass.getDataMap().getCoupons().get(i).getCoupon_kind() == 2) {
                            valueNum++;
                        }
                    }
                }
                //在页面中进行相关显示
                postCardNum.setText("抵值券" + postNum + "张");
                valueCardNum.setText("折扣卡" + valueNum + "张");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue2.add(stringRequest);
        return view;
    }

    //获取个人信息
    public void getPersonalInfor() {
        String url = API.DETAIL;
        String PREFERENCE_NAME = "TrineaAndroidCommon";
        SharedPreferences settings = getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String phone = settings.getString("phone", "");

        if (phone != null && !phone.equals("")) {
            Map<String, String> map = new HashMap<>();
            map.put(Consts.USER_ID, phone);
            url = JrUtils.appendParams(url, map);
            NetLoader.getInstance(getContext()).loadGetData(getActivity(), url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int code) {
                    if (code != 401 && code != 404 && code == 200) {
                        Gson gson = new Gson();
                        entity = gson.fromJson(resultString, PersonalInforEntity.class);
                        avaterUrl = API.AVATER + entity.getDataMap().getAvatar() + "_200.jpg";
                        //拉取网络昵称
                        yourName.setText(entity.getDataMap().getNick_name());
                        yourSay.setText(entity.getDataMap().getCustom_sign());
                        Log.i("DDDDDD", avaterUrl + "*********");
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    getAvater(avaterUrl);
                                    Log.i("geanwen头像url", avaterUrl);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        EventBus.getDefault().post(entity);
                    } else if (code == 401) {
                    }
                }

                @Override
                public void fail(String failString, Exception e) {
                    Log.i("geanwen", failString);
                }
            });
        }
    }

    public void getAvater(String avatarUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JrController.setCertificates(getActivity(), client, getActivity().getAssets().open("zhenjren.cer"));
        try {
            Request request = new Request.Builder().url(avatarUrl).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream is = response.body().byteStream();
                final Bitmap bm = BitmapFactory.decodeStream(is);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        goChangeMine.setImageBitmap(bm);
                    }
                });
            } else if (response.code() == 401) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //接收卡券选择页面的传值结果
    //带值广播接收者
    class MySendReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String data = intent.getStringExtra("id");
            String data2 = intent.getStringExtra("price");
        }
    }

    //销毁广播注册
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mysendreciver);
    }
}
