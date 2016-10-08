package com.hx.jrperson.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.utils.common.util.BitmapUtils;
import com.hx.jrperson.utils.wx.Constants;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class SharePopupwindow extends PopupWindow {

	private View mPopView;
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;
	private Context mContext;
	
	public SharePopupwindow(Activity context) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopView = inflater.inflate(R.layout.popup_share, null);

		View view = mPopView.findViewById(R.id.gray_view);
		TextView circleText = (TextView) mPopView.findViewById(R.id.share_circle_text);
		TextView wechatText = (TextView) mPopView.findViewById(R.id.share_wechat_text);

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		circleText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shareWechat(true);
			}
		});

		wechatText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shareWechat(false);
			}
		});

		setContentView(mPopView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setBackgroundDrawable(new ColorDrawable(0));
	}

	private void shareWechat(boolean isCircle) {
		dismiss();
		api = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID);
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = "http://www.zjrkeji.com/app.html";
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = "真匠人";
		msg.description = "一个新的家装维修时代，由真匠人为您开启！";
		Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_user_icon);
		if (thumb != null) {
			msg.thumbData = BitmapUtils.bmpToByteArray(thumb, true);
		}

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
        req.scene = isCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

}
