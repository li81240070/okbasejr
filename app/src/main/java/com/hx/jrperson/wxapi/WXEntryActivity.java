package com.hx.jrperson.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hx.jrperson.R;
import com.hx.jrperson.utils.wx.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq baseReq) {

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onResp(BaseResp baseResp) {
		int result = 0;
		switch (baseResp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = R.string.share_success;
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = R.string.share_cancel;
				break;
			case BaseResp.ErrCode.ERR_SENT_FAILED:
				result = R.string.share_fail;
				break;
			default:
				result = R.string.share_unknow;
				break;
		}

		Toast.makeText(getApplicationContext(), getString(result), Toast.LENGTH_SHORT).show();
		this.finish();
	}
}
