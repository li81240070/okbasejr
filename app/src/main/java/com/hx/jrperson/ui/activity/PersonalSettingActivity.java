package com.hx.jrperson.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.PersonalInforEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.JrController;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.WaittingDiaolog;
import com.hx.jrperson.views.baseView.BaseActivity;
import com.hx.jrperson.views.widget.CircleImageView;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人信息页面
 * Created by ge on 2016/3/2.
 */
public class PersonalSettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout  personal_nick_nameRL, personal_phone_numberRL, personal_signtureRL;
    private CircleImageView personal_head_imgIV;
    private TextView personal_nick_nameTV, personal_phone_numberTV, personal_signtureTV;
    private Button pop_window_head_photeBT, pop_window_head_audioBT, pop_window_head_cancleBT;
    private Dialog dialog;
    private Bitmap headPhoto;
    private String imgPath;
    private Uri imgUri;
    private PersonalInforEntity entity;
    private WaittingDiaolog waitDiaolog;
    private Handler handler;
    ///////////////////////////////////////////
    private RelativeLayout backButtonInMyView;
    private ImageView backbuttonInMyView;
    private TextView  personal_headRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        showToolBar("个人信息", true, this, false);
        EventBus.getDefault().register(this);
        initView();
        initData();
        setListener();
        backButtonInMyView= (RelativeLayout) findViewById(R.id.backButtonInMyView);
        backButtonInMyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalSettingActivity.this.finish();
            }
        });
        backbuttonInMyView= (ImageView) findViewById(R.id.backbuttonInMyView);
        backbuttonInMyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalSettingActivity.this.finish();
            }
        });

    }

    @Override
    protected void initView() {
        personal_headRL = (TextView) findViewById(R.id.personal_headRL);
        personal_nick_nameRL = (RelativeLayout) findViewById(R.id.personal_nick_nameRL);
        personal_phone_numberRL = (RelativeLayout) findViewById(R.id.personal_phone_numberRL);
        personal_signtureRL = (RelativeLayout) findViewById(R.id.personal_signtureRL);
        personal_head_imgIV = (CircleImageView) findViewById(R.id.personal_head_imgIV);
        personal_nick_nameTV = (TextView) findViewById(R.id.personal_nick_nameTV);
        personal_phone_numberTV = (TextView) findViewById(R.id.personal_phone_numberTV);
        personal_signtureTV = (TextView) findViewById(R.id.personal_signtureTV);
    }

    @Override
    protected void initData() {
        handler = new Handler();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        entity = (PersonalInforEntity) bundle.get("personal");
        if (entity != null) {
            personal_nick_nameTV.setText(entity.getDataMap().getNick_name());
            personal_phone_numberTV.setText(entity.getDataMap().getMobile());
            personal_signtureTV.setText(entity.getDataMap().getCustom_sign());
            if (null != entity.getDataMap().getAvatar() && !"".equals(entity.getDataMap().getAvatar())){
                final String avaterUrl = API.AVATER + entity.getDataMap().getAvatar() + "_200.jpg";
                new Thread(){
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
            }else {
                personal_head_imgIV.setImageResource(R.mipmap.ic_personal_head_img);
            }
        }
    }

    public void getAvater(String avatarUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JrController.setCertificates(PersonalSettingActivity.this, client, PersonalSettingActivity.this.getAssets().open("zhenjren.cer"));
        try {
            Request request = new Request.Builder().url(avatarUrl).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                InputStream is = response.body().byteStream();
                final Bitmap bm = BitmapFactory.decodeStream(is);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        personal_head_imgIV.setImageBitmap(bm);
                    }
                });
            }else if (response.code() == 401){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PersonalSettingActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setListener() {
        personal_headRL.setOnClickListener(this);
        personal_nick_nameRL.setOnClickListener(this);
        personal_phone_numberRL.setOnClickListener(this);
        personal_signtureRL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal_headRL://头像
                dialog = creatDialog(this, R.style.MyDialogStyleBottom);
                dialog.show();
                break;
            case R.id.personal_nick_nameRL://昵称
                clickAlterBtn(1);
                break;
            case R.id.personal_phone_numberRL://手机号

                break;
            case R.id.personal_signtureRL://个性签名
                clickAlterBtn(2);
                break;
        }
    }

    /**
     * 跳转到修改昵称和签名页面
     * i=1跳转到修改签名页面
     * i=2跳转到修改签名页面
     * **/
    private void clickAlterBtn(int i) {
        Intent intent = new Intent(this, AlterNameAndSignActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("staute", i);
        bundle.putSerializable("infor", entity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //创建底部菜单dialog
    private Dialog creatDialog(Context context, int stytle) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.buttom_popupwindow_head, null);
        pop_window_head_photeBT = (Button) dialogView.findViewById(R.id.pop_window_head_photeBT);
        pop_window_head_audioBT = (Button) dialogView.findViewById(R.id.pop_window_head_audioBT);
        pop_window_head_cancleBT = (Button) dialogView.findViewById(R.id.pop_window_head_cancleBT);
        popWindowListener();

        final Dialog customDialog = new Dialog(context, stytle);

        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        localLayoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        localLayoutParams.x = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.y = WindowManager.LayoutParams.MATCH_PARENT;
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        dialogView.setMinimumWidth(screenWidth);

        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        customDialog.setCancelable(true);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setContentView(dialogView, localLayoutParams);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                customDialog.show();
            }
        }
        return customDialog;
    }


    private void popWindowListener() {
        //拍照
        pop_window_head_photeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    imgPath = Environment.getExternalStorageDirectory().getPath() + "/jr_ordor";
                    File dir = new File(imgPath);//图片路径
                    if (!dir.exists()) {//判断文件夹是否存在
                        dir.mkdir();
                    }
                    //取时间为文件名
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddssSSS");
                    String fileName = "JR" + (dateFormat.format(new Date())) + ".jpg";
                    imgPath = imgPath + "/" + fileName;//文件路径
                    imgUri = Uri.fromFile(new File(imgPath));
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);//照完照片的输出路径更改
                    startActivityForResult(takePhotoIntent, 1);
                    dialog.dismiss();
                } else {
                    Toast.makeText(PersonalSettingActivity.this, "找不到SD卡", Toast.LENGTH_LONG).show();
                }
            }
        });
        //相册
        pop_window_head_audioBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                dialog.dismiss();
            }
        });
        //取消
        pop_window_head_cancleBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //订单发布成功
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(final JSONArray array) {
        if (array != null){
            waitDiaolog = new WaittingDiaolog(PersonalSettingActivity.this);
            waitDiaolog.show();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    updataImg(array);
                }
            }.start();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPersonalInfor();
    }

    //获取个人信息
    public void getPersonalInfor() {
        String url = API.DETAIL;
        String phone = PreferencesUtils.getString(this, Consts.PHONE_PF);
        if (phone != null && !phone.equals("")){
            Map<String, String> map = new HashMap<>();
            map.put(Consts.USER_ID, phone);
            url = JrUtils.appendParams(url, map);
            NetLoader.getInstance(PersonalSettingActivity.this).loadGetData(PersonalSettingActivity.this, url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int code) {
                    if (code == 200){
                        Gson gson = new Gson();
                        entity = gson.fromJson(resultString, PersonalInforEntity.class);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (entity != null){
                                    personal_nick_nameTV.setText(entity.getDataMap().getNick_name());
                                    personal_phone_numberTV.setText(entity.getDataMap().getMobile());
                                    personal_signtureTV.setText(entity.getDataMap().getCustom_sign());
                                    final String avater = API.AVATER + entity.getDataMap().getAvatar() + "_200.jpg";
                                    new Thread(){
                                        @Override
                                        public void run() {
                                            super.run();
                                            try {
                                                getAvater(avater);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();

                                }
                            }
                        });
                    }else if (code == 401){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PersonalSettingActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0){
            return;
        }
        Uri path = null;
        ContentResolver contentResolver = getContentResolver();
        Bitmap bitmap = null;
        if (resultCode == 0){
            return;
        }
        InputStream inputStream = null;
            switch (requestCode) {
            case 1:
                try {
                    inputStream = contentResolver.openInputStream(imgUri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = 2;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    Bitmap upBitmap = JrUtils.compressImage(bitmap);
//                    Picasso.with(PersonalSettingActivity.this).load(imgUri).error(R.mipmap.ic_personal_head_img).into(personal_head_imgIV);
                    final JSONArray array = JrUtils.bitmapToBase64(upBitmap);
                    EventBus.getDefault().post(array);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                Uri uri = data.getData();
                try {
                    inputStream = contentResolver.openInputStream(uri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = 2;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
//                    Picasso.with(PersonalSettingActivity.this).load(uri).error(R.mipmap.ic_personal_head_img).into(personal_head_imgIV);
                    Bitmap upBitmap = JrUtils.compressImage(bitmap);
                    final JSONArray array1 = JrUtils.bitmapToBase64(upBitmap);
                    EventBus.getDefault().post(array1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //上传头像
    private void updataImg(JSONArray imageBytes) {
        String phone = PreferencesUtils.getString(this, Consts.PHONE_PF);
        String token = PreferencesUtils.getString(this, Consts.TOKEN);
        JSONObject objectv = new JSONObject();
        try {
            objectv.put(Consts.IMGDATA, imageBytes.toString());
            objectv.put(Consts.TOKEN, token);
            objectv.put(Consts.CUSTOMERID, phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClient httpClient = new OkHttpClient();
        try {
            JrController.setCertificates(PersonalSettingActivity.this, httpClient, PersonalSettingActivity.this.getAssets().open("zhenjren.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = API.ALTER_IMG;
        RequestBody body = RequestBody.create(JSON, objectv.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("token", token)
                .tag(this)
                .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.code() != 401){
                if (response.isSuccessful()){
                    waitDiaolog.dismiss();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            getPersonalInfor();
                            Toast.makeText(PersonalSettingActivity.this, "头像修改成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    waitDiaolog.dismiss();
                }
            }else if (response.code() == 401){
                waitDiaolog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PersonalSettingActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().post(3333);//登陆成功 发送给mainctivity
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
