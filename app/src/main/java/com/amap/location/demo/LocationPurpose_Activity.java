package com.amap.location.demo;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.amap.location.demo.view.FeatureView;

/**
 * 场景定位功能展示
 *
 * &#064;创建时间：  2015年11月24日 下午5:49:52
 * &#064;项目名称：  AMapLocationDemo2.x
 * @author hongming.wang
 * &#064;文件名称:  LocationPurpose_Activity.java
 * &#064;类型名称:  LocationPurpose_Activity
 */
public class LocationPurpose_Activity extends AppCompatActivity
		implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
	/**
	 * Callback method to be invoked when an item in this AdapterView has
	 * been clicked.
	 * <p>
	 * Implementers can call getItemAtPosition(position) if they need
	 * to access the data associated with the selected item.
	 *
	 * @param parent   The AdapterView where the click happened.
	 * @param view     The view within the AdapterView that was clicked (this
	 *                 will be a view provided by the adapter)
	 * @param position The position of the view in the adapter.
	 * @param id       The row id of the item that was clicked.
	 */
	@Override
	public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
		DemoDetails demo = (DemoDetails) list.getAdapter().getItem(position);
		startActivity(
				new Intent(this.getApplicationContext(), demo.activityClass));
	}
	
	/**
	 * Callback method to be invoked when an item in this view has been
	 * clicked and held.
	 * Implementers can call getItemAtPosition(position) if they need to access
	 * the data associated with the selected item.
	 *
	 * @param parent   The AbsListView where the click happened
	 * @param view     The view within the AbsListView that was clicked
	 * @param position The position of the view in the list
	 * @param id       The row id of the item that was clicked
	 * @return true if the callback consumed the long click, false otherwise
	 */
	@Override
	public boolean onItemLongClick (AdapterView<?> parent, View view, int position, long id) {
		return false;
	}
	
	private static class DemoDetails {
		private final int titleId;
		private final int descriptionId;
		private final Class<? extends AppCompatActivity> activityClass;
		public DemoDetails(int titleId, int descriptionId,
				Class<? extends AppCompatActivity> activityClass) {
			super();
			this.titleId = titleId;
			this.descriptionId = descriptionId;
			this.activityClass = activityClass;
		}
	}

	private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {
		public CustomArrayAdapter(Context context, DemoDetails[] demos) {
			super(context, R.layout.feature, R.id.title, demos);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FeatureView featureView;
			if (convertView instanceof FeatureView) {
				featureView = (FeatureView) convertView;
			} else {
				featureView = new FeatureView(getContext());
			}
			DemoDetails demo = getItem(position);
			featureView.setTitleId(demo.titleId);
			featureView.setDescriptionId(demo.descriptionId);
			return featureView;
		}
	}

	private static final DemoDetails[] demos = {
			new DemoDetails(R.string.signInPurpose,
					R.string.signInPurpose_dec, Purpose_SignIn_Activity.class),
			new DemoDetails(R.string.transportPurpose,
					R.string.transportPurpose_dec, Purpose_Trancesport_Activity.class),
			new DemoDetails(R.string.sportPurpose,
					R.string.sportPurpose_dec, Purpose_Sport_Activity.class),
			};

	private ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geofence);
		setTitle(R.string.title_main);
		ListAdapter adapter = new CustomArrayAdapter(
				this.getApplicationContext(), demos);
		list = findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);
//		permChecker = new PermissionsChecker(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.exit(0);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
