/**
 * 
 */
package com.amap.location.demo;

import androidx.appcompat.app.AppCompatActivity;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 最后位置功能演示
 * &#064;创建时间：2016年1月7日  下午4:36:22
 * &#064;项目名称：  AMapLocationDemo2.x
 * @author hongming.wang
 * &#064;文件名称：LastLocation_Activity.java
 * &#064;类型名称：LastLocation_Activity
 * @since 2.3.0
 */
public class LastLocation_Activity extends AppCompatActivity {
	private AMapLocationClient locationClient = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lastlocation);
		setTitle(R.string.title_lastLocation);
		TextView tvReult = findViewById(R.id.tv_result);
		try {
			locationClient = new AMapLocationClient(this.getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		findViewById(R.id.bt_lastLoc).setOnClickListener(v -> {
			try {
				AMapLocation location = locationClient.getLastKnownLocation();
				String result = Utils.getLocationStr(location);
				tvReult.setText(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != locationClient) {
			locationClient.onDestroy();
			locationClient = null;
		}
	}
}
