/**
 *
 */
package com.amap.location.demo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 继承了Activity，实现Android6.0的运行时权限检测
 * 需要进行运行时权限检测的Activity可以继承这个类
 *
 * @author hongming.wang
 * @创建时间：2016年5月27日 下午3:01:31
 * @项目名称： AMapLocationDemo
 * @文件名称：PermissionsChecker.java
 * @类型名称：PermissionsChecker
 * @since 2.5.0
 */
public class CheckPermissionsActivity extends AppCompatActivity {
	//是否需要检测后台定位权限，设置为true时，如果用户没有给予后台定位权限会弹窗提示
	private boolean needCheckBackLocation = false;
	//如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
	private static String BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
	/**
	 * 需要进行检测的权限数组
	 */
	protected String[] needPermissions = {
			Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,};
	
	private static final int PERMISSON_REQUESTCODE = 0;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		needPermissions = new String[] {
				Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
				BACKGROUND_LOCATION_PERMISSION};
	}
	
	/**
	 * 判断是否需要检测，防止不停的弹框
	 */
	private boolean isNeedCheck = true;
	
	@Override
	protected void onResume () {
		super.onResume();
		if (isNeedCheck) {
			checkPermissions(needPermissions);
		}
	}
	
	private void checkPermissions (String... permissions) {
		try {
			List<String> needRequestPermissonList = findDeniedPermissions(permissions);
			if (! needRequestPermissonList.isEmpty()) {
				String[] array = needRequestPermissonList.toArray(new String[0]);
				Method method = getClass().getMethod("requestPermissions", new Class[] {
						String[].class, int.class});
				
				method.invoke(this, array, PERMISSON_REQUESTCODE);
			}
		}
		catch (Throwable ignored) {
		}
	}
	
	/**
	 * 获取权限集中需要申请权限的列表
	 */
	private List<String> findDeniedPermissions (String[] permissions) {
		List<String> needRequestPermissonList = new ArrayList<>();
		try {
			for (String perm : permissions) {
				Method checkSelfMethod = getClass().getMethod("checkSelfPermission", String.class);
				Method shouldShowRequestPermissionRationaleMethod = getClass().getMethod(
						"shouldShowRequestPermissionRationale", String.class);
				if ((Integer) checkSelfMethod.invoke(this, perm) != PackageManager.PERMISSION_GRANTED ||
					(Boolean) shouldShowRequestPermissionRationaleMethod.invoke(this, perm)) {
					if (! needCheckBackLocation && BACKGROUND_LOCATION_PERMISSION.equals(perm)) {
						continue;
					}
					needRequestPermissonList.add(perm);
				}
			}
		}
		catch (Throwable ignored) {
		
		}
		return needRequestPermissonList;
	}
	
	/**
	 * 检测是否所有的权限都已经授权
	 */
	private boolean verifyPermissions (int[] grantResults) {
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}
	
	public void onRequestPermissionsResult (int requestCode, @NotNull String[] permissions,
											@NotNull int[] paramArrayOfInt) {
		super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
		if (requestCode == PERMISSON_REQUESTCODE) {
			if (! verifyPermissions(paramArrayOfInt)) {
				showMissingPermissionDialog();
				isNeedCheck = false;
			}
		}
	}
	
	/**
	 * 显示提示信息
	 */
	private void showMissingPermissionDialog () {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.notifyTitle);
		builder.setMessage(R.string.notifyMsg);
		
		// 拒绝, 退出应用
		builder.setNegativeButton(R.string.cancel, (dialog, which) -> finish());
		
		builder.setPositiveButton(R.string.setting, (dialog, which) -> startAppSettings());
		
		builder.setCancelable(false);
		
		builder.show();
	}
	
	/**
	 * 启动应用的设置
	 */
	private void startAppSettings () {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
