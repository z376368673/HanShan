package com.bm.tzj.fm;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.api.BaseApi;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseFragmentActivity;
import com.bm.base.adapter.ChildAdapter;
import com.bm.dialog.UtilDialog;
import com.bm.entity.Badge;
import com.bm.entity.Child;
import com.bm.entity.GrowUp;
import com.bm.entity.User;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.czzx.HonoRollAc;
import com.bm.tzj.czzx.MedalDetailAc;
import com.bm.tzj.mine.AddChildAc;
import com.bm.tzj.ts.SendGrowUpAc;
import com.bm.util.BitmapUtil;
import com.bm.util.CacheUtil;
import com.bm.util.SixPullulatePanelTwo;
import com.bm.util.Util;
import com.bm.view.CircleImageView;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.StringResult;
import com.lib.tool.SharedPreferencesHelper;
import com.lib.widget.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.richer.tzj.R;

/**
 * 
 * 成长
 * 
 * @author wangqiang
 * 
 */
@SuppressLint("NewApi")
public class PullulateFm extends Fragment implements OnClickListener {

	private Context context;
	private ListView lv_content;
	private LinearLayout ll_Msg,ll_head;
	private TextView tv_get,tv_a_num,tv_b_num,tv_name,tv_age,tv_poptip;
	private CircleImageView iv_sixview_head;
	private ImageView im_headbg;
	private RelativeLayout rl_a,rl_b,rl_c;
	private FrameLayout fm_content;
	private ImageButton ib_left;
	int strMedalId=-1;
	public static final int CLICK_STATES=1001;
	Intent intent;
	User uInfo;
	Child child; //当前选择的孩子
	String strSex="";
	public static PullulateFm instance;

	private List<Child> dataList = new ArrayList<Child>();
	private HorizontalListView hlistView;
	private PopupWindow popupWindow;
	private ChildAdapter adapter;

	public List<Badge> list = new ArrayList<Badge>();

	private ArrayList<Child> childrenList;

	private String touxiang;

	private int type = 0;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater
				.inflate(R.layout.fm_pullulate, container, false);
		// App.toast("CaseFm");
		instance = this;
		context = this.getActivity();
		initView(messageLayout);
		return messageLayout;
	}

	/**
	 * 初始化数据
	 * @param v
	 */
	private void initView(View v) {
		ll_head = (LinearLayout) v.findViewById(R.id.ll_head);
		ll_head.setOnClickListener(this);
		ib_left = (ImageButton) v.findViewById(R.id.ib_left);
		ib_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		fm_content = (FrameLayout) v.findViewById(R.id.fm_content);
		tv_name=(TextView) v.findViewById(R.id.tv_name);
		tv_age=(TextView) v.findViewById(R.id.tv_age);

		tv_a_num=(TextView) v.findViewById(R.id.tv_a_num);
		tv_b_num=(TextView) v.findViewById(R.id.tv_b_num);
		tv_poptip=(TextView) v.findViewById(R.id.tv_poptip);

		rl_a=(RelativeLayout) v.findViewById(R.id.rl_a);
		rl_b=(RelativeLayout) v.findViewById(R.id.rl_b);
		rl_c=(RelativeLayout) v.findViewById(R.id.rl_c);

		tv_get=(TextView) v.findViewById(R.id.tv_get);
		tv_get.setOnClickListener(this);

		rl_a.setOnClickListener(this);
		rl_b.setOnClickListener(this);
		rl_c.setOnClickListener(this);
		iv_sixview_head = (CircleImageView)v.findViewById(R.id.iv_sixview_head);
		im_headbg = (ImageView)v.findViewById(R.id.im_headbg);
		getDate();


		//切换服务器地址用的
		final SharedPreferences sp =this.context.getSharedPreferences("ssspathss", Context.MODE_PRIVATE);
		v.findViewById(R.id.tv_center).setOnClickListener(new View.OnClickListener() {
			private int i=0;
			@Override
			public void onClick(View v) {
				i++;
				if(i>6)
				{
					final EditText et = new EditText(context);
					et.setText("http://59.110.62.10:8888/tongZiJun/api/");
					new AlertDialog.Builder(context)
					.setMessage("选择服务地址")
					.setNegativeButton(BaseApi.API_HOST2.replaceAll("http://", ""), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							BaseApi.API_URL_PRE = BaseApi.API_HOST2 + "/tongZiJun/api/";
							sp.edit().putString("path", BaseApi.API_URL_PRE).commit();
						}
					})
					.setPositiveButton(BaseApi.API_HOST1.replaceAll("http://", ""), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							BaseApi.API_URL_PRE = BaseApi.API_HOST1 + "/tongZiJun/api/";
							sp.edit().putString("path", BaseApi.API_URL_PRE).commit();
						}
					})
					.setNeutralButton("手动输入的", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							BaseApi.API_URL_PRE = et.getEditableText().toString();
							sp.edit().putString("path", BaseApi.API_URL_PRE).commit();
						}
					})
					.setView(et).setCancelable(false)
					.show();
					i=0;
				}
			}
		});
	}



	public void updateView()
	{
		uInfo = App.getInstance().getUser();
		if(null == uInfo){
			return;
		}
		child = App.getInstance().getChild();
		if(child == null)
			return;

		tv_name.setText(getNullData(child.realName));//宝贝姓名
		strSex = child.gender;
		if("1".equals(strSex)){
			strSex="男";
		}else if("2".equals(strSex)){
			strSex="女";
		}else {
			strSex="未知";
		}
		String address = getNullData(uInfo.regionName);
		String strAgeAddress ="";
		if (!TextUtils.isEmpty(getNullData(child.age))) {
			strAgeAddress = getNullData(child.age)==""?"0岁":child.age+"岁     |   "+address;
		}
		tv_age.setText(strSex+" "+strAgeAddress);//宝贝性别 年龄地址 省市区
		tv_a_num.setText(uInfo.medalNum==null?"0":uInfo.medalNum);//宝贝勋章
		tv_b_num.setText(uInfo.honourSort==null?"0":"NO."+uInfo.honourSort);//宝贝勋章
		if(child.avatar == null || !child.avatar.equals(touxiang))
		{
			touxiang = child.avatar;
			ImageLoader.getInstance().displayImage(child.avatar, iv_sixview_head, App.getInstance().getheadImage());
			//			ImageLoader.getInstance().displayImage(child.avatar, im_headbg, App.getInstance().getheadImage());
			im_headbg.setImageBitmap(null);
			ImageLoader.getInstance().loadImage(child.avatar, App.getInstance().getheadImage(), new ImageLoadingListener(){
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
				}
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap bm) {
					if(bm != null)
						im_headbg.setImageBitmap(BitmapUtil.fastblur(bm, 100));
				}
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				}
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
				}});
		}


		//		SharedPreferencesHelper.saveBoolean("PullulateTip", false);
		if(this.isVisible() && childrenList!=null && childrenList.size()>1 && !SharedPreferencesHelper.getBoolean("PullulateTip"))
		{
			popTip();
		}
	}

	public void getDate() {
		list.clear();
		uInfo = App.getInstance().getUser();
		if(null == uInfo){
			return;
		}
		child = App.getInstance().getChild();

		updateView();

		final HashMap<String, String> map = new HashMap<String, String>();
		map.put("userId", uInfo.userid);//用户ID
		if(null != child)
			map.put("babyId", child.babyId);
		MainAc.intance.showProgressDialog();
		UserManager.getInstance().getTzjmedalMedallist(context, map,new ServiceCallback<CommonListResult<Badge>>() {

			final String CACHEKEY = "PullulateFm_cache";

			@Override
			public void error(String msg) {

				//从缓存中读取数据
				CommonListResult<Badge> obj = new CommonListResult<Badge>(){}; 
				Type type = obj.getClass().getGenericSuperclass();
				obj = (CommonListResult<Badge>)CacheUtil.read(context, CACHEKEY, map, type);
				if(obj != null)
				{
					doResult(obj);
					return;
				}

				//没有缓存时执行下面的逻辑
				App.toast(msg);
				MainAc.intance.hideProgressDialog();
			}

			@Override
			public void done(int what, CommonListResult<Badge> obj) {
				doResult(obj);

				//缓存结果
				CacheUtil.save(context, CACHEKEY, map, obj);
			}

			private void doResult(CommonListResult<Badge> obj)
			{
				MainAc.intance.hideProgressDialog();
				if(fm_content.getChildCount()>0){
					fm_content.removeAllViews();
				}
				if(obj.data.size()>0){
					//					for (int i = 0; i < obj.data.size(); i++) {
					//						list.add(obj.data.get(i));
					//					}
					list.addAll(obj.data);
					if(list.size()%4==1){
						list.add(new Badge());
					}else if(list.size()%4==3){
						list.add(new Badge());
					} 

					SixPullulatePanelTwo.setViews(fm_content, list, context,handler);
				}
			}
		});


		//查询孩子列表
		loadChildData();
	}


	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CLICK_STATES:
				strMedalId = msg.arg1;
				Badge bInfo = (Badge) msg.obj;
				intent = new Intent(context, MedalDetailAc.class);
				intent.putExtra("medalInfo", bInfo);
				startActivity(intent);
				break;
			}
		}
	};

	private void popTip()
	{
		//		View contentView = LayoutInflater.from(context).inflate(R.layout.pop_tip_pullulate, null);
		//		final PopupWindow popupWindow = new PopupWindow(contentView,
		//                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		////		popupWindow.setOutsideTouchable(true);
		//		contentView.setOnTouchListener(new View.OnTouchListener(){
		//			@Override
		//			public boolean onTouch(View v, MotionEvent event) {
		////				popupWindow.dismiss();
		//				return false;
		//			}});
		//		popupWindow.showAsDropDown(this.iv_sixview_head);
		//		popupWindow.setAnimationStyle(R.anim.baidong);
		//		contentView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.baidong));

		tv_poptip.setVisibility(View.VISIBLE);
		tv_poptip.setAnimation(AnimationUtils.loadAnimation(context, R.anim.baidong));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_get:
			intent = new Intent(context, MedalDetailAc.class);
			startActivity(intent);
			break;
		case R.id.rl_a:
			break;
		case R.id.rl_b:
			break;
		case R.id.rl_c:
			intent = new Intent(context, HonoRollAc.class);
			startActivity(intent);
			break;
		case R.id.ll_head://头像跳转个人信息
			//			intent = new Intent(context, PersonalInformation.class);
			//			intent.putExtra("pageType", "PullulateFm");
			//			startActivity(intent);
			showPopupWindow(v);
			tv_poptip.setAnimation(null);
			tv_poptip.setVisibility(View.GONE);
			SharedPreferencesHelper.saveBoolean("PullulateTip", true);
			break;
		default:
			break;
		}

	}

	/**
	 *   * 判断值是否为空   * @param arg   * @return  
	 */
	public static String getNullData(String arg) {
		return arg == null ? "" : arg;
	}


	private void showPopupWindow(View view) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];
		popupWindow.showAtLocation(view, Gravity.TOP, x-(view.getWidth()/2), y);
	}
	private void loadChildData()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		if(null == App.getInstance().getUser()){
			map.put("userId", "0");
		}else{
			map.put("userId", App.getInstance().getUser().userid);

		}
		((BaseFragmentActivity) getActivity()).showProgressDialog();
		UserManager.getInstance().getChildrenlist(getActivity(), map, new ServiceCallback<CommonListResult<Child>>(){
			@Override
			public void done(int what, CommonListResult<Child> obj) {
				((BaseFragmentActivity) getActivity()).hideProgressDialog();
				dataList.clear();
				dataList.addAll(obj.data);
				makePopWindow();
			}

			@Override
			public void error(String msg) {
				((BaseFragmentActivity) getActivity()).hideProgressDialog();
				((BaseFragmentActivity) getActivity()).dialogToast(msg);
			}});
	}

	protected void makePopWindow() {
		popupWindow = new PopupWindow(getActivity());
		popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
		View popLayout = LayoutInflater.from(getActivity()).inflate(com.richer.tzj.R.layout.layout_popupwindow_child,null);
		hlistView =(HorizontalListView)popLayout.findViewById(R.id.hlistview);
		adapter = new ChildAdapter(getActivity(), dataList);
		hlistView.setAdapter(adapter);
		popupWindow.setContentView(popLayout);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);

		popLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});

		hlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				popupWindow.dismiss();
				if (position == dataList.size()-1) {
					//跳转到添加孩子
					context.startActivity(new Intent(context, AddChildAc.class));
				}else{
					//更换孩子
					changeChild(position);
				}
			}
		});
	}


	public void changeChild(int positon){
		if (dataList != null &&dataList.size()>0) {
			Child child = dataList.get(positon);
			App.getInstance().setChild(child);
			updateView();
		}
	}
}
