//package com.bm.tzj.fm;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.animation.AnimationUtils;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.bm.api.UserManager;
//import com.bm.app.App;
//import com.bm.base.adapter.CourseGalleryAdapter;
//import com.bm.base.adapter.CourseListAdapter;
//import com.bm.base.adapter.ImagePagerAdapters280;
//import com.bm.entity.Advertisement;
//import com.bm.entity.Child;
//import com.bm.entity.HotGoods;
//import com.bm.entity.Storelist;
//import com.bm.entity.User;
//import com.bm.tzj.activity.AccouterIndexAc;
//import com.bm.tzj.activity.MainAc;
//import com.bm.tzj.activity.MainAc.OnTabActivityResultListener;
//import com.bm.tzj.city.Activity01;
//import com.bm.tzj.city.City;
//import com.bm.tzj.kc.CourseDetailAc;
//import com.bm.tzj.kc.CourseListAc;
//import com.bm.tzj.mine.AddChildAc;
//import com.bm.tzj.mine.MyMessageAc;
//import com.bm.util.CacheUtil;
//import com.bm.util.Util;
//import com.lib.http.ServiceCallback;
//import com.lib.http.result.CommonListResult;
//import com.lib.http.result.CommonResult;
//import com.lib.tool.Pager;
//import com.lib.tool.SharedPreferencesHelper;
//import com.lib.tool.UITools;
//import com.lib.widget.HorizontalListView;
//import com.lib.widget.refush.NsRefreshLayout;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.richer.tzj.R;
//
///**
// *
// * 课程
// *
// * @author wangqiang
// *
// */
//@SuppressLint("NewApi")
//public class CourseFm extends Fragment implements OnClickListener,
//		OnTabActivityResultListener, NsRefreshLayout.NsRefreshLayoutController, NsRefreshLayout.NsRefreshLayoutListener{
//	private ImageView iv_sixview_head;
//
//	private PopupWindow popupWindow;
//
//	private ArrayList<Child> childrenList;
//
//	private Context context;
//	private ImageView iv_yj, iv_six_a, iv_six_b, iv_six_c, iv_six_d,img_read,img_default;
//	private LinearLayout ll_search,ll_default;
//	private FrameLayout fl;
//	private HorizontalListView h_list;
//	private CourseGalleryAdapter adapter;
//	private ListView lv_content;
//	List<Storelist> prolist = new ArrayList<Storelist>();
//	private CourseListAdapter listAdapter;
//	public List<HotGoods> list = new ArrayList<HotGoods>();
////	BannerView banner;
//	private TextView tv_location, tv_poptip;
////	 private BGARefreshLayout mRefreshLayout;
//
//	private NsRefreshLayout mRefreshLayout;
//	City city  =null;
//	/** 分页器 */
//	public Pager pager = new Pager(10);
//	private List<Advertisement> listAdvment = new ArrayList<Advertisement>();
//
//	private ScrollView sv;
//	private ViewPager vp_ads;
//	private LinearLayout ll_vp_indicator;
//	private FrameLayout fm_image;
//	String[] imagesSrc = new String[] {
//			"http://images.hisupplier.com/var/userImages/201312/25/145025923484_s.jpg",
//			"http://images.hisupplier.com/var/userImages/201403%2F20%2F211154839619.jpg",
//			"http://ww1.sinaimg.cn/mw600/d1946ec0jw1e5nntcqu71j21kw11xnek.jpg" };
//
//
//	private int index=-1;//判断是单广告还是多广告
//	private ArrayList<Advertisement> advertisements=new ArrayList<Advertisement>();
// 	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View messageLayout = inflater.inflate(R.layout.fm_indexs, container,
//				false);
//		context = this.getActivity();
//		city  =App.getInstance().getCityCode();
//		initView(messageLayout);
//		// App.toast("HealthRecordFm");
//		return messageLayout;
//	}
//
//	/**
//	 * 初始化数据
//	 *
//	 * @param v
//	 */
//	private void initView(View v) {
//		iv_sixview_head = (ImageView)v.findViewById(R.id.iv_sixview_head);
//
//		img_default = (ImageView) v.findViewById(R.id.img_default);
//		sv = (ScrollView) v.findViewById(R.id.sv);
//		lv_content = (ListView) v.findViewById(R.id.lv_content);
//		iv_yj = (ImageView) v.findViewById(R.id.iv_yj);
//		iv_yj.setOnClickListener(this);
//		fl = (FrameLayout) v.findViewById(R.id.fl);
//		h_list = (HorizontalListView) v.findViewById(R.id.h_list);
//		img_read=(ImageView) v.findViewById(R.id.img_read);
//		tv_location = (TextView) v.findViewById(R.id.tv_location);
//		 mRefreshLayout = (NsRefreshLayout) v.findViewById(R.id.swipyrefreshlayout);
//		 mRefreshLayout.setRefreshLayoutController(this);
//		 mRefreshLayout.setRefreshLayoutListener(this);
//
//		vp_ads = (ViewPager) v.findViewById(R.id.vp_ads);
//		ll_vp_indicator = (LinearLayout) v.findViewById(R.id.ll_vp_indicator);
//		fm_image = (FrameLayout)v.findViewById(R.id.fm_image);
//
//		tv_poptip=(TextView) v.findViewById(R.id.tv_poptip);
//
//		if(null!=city&&!TextUtils.isEmpty(city.cityName)){
//			String strCity =city.cityName;
//			tv_location.setText(city.cityName+"");//城市名称
//
//			//包含市 则截取
//			strCity=strCity.substring(strCity.length()-1,strCity.length());
//			if(strCity.equals("市")){
//				tv_location.setText(city.cityName.substring(0,city.cityName.length()-1));
//			}
//		}else{
//			tv_location.setText("西安");//城市名称
//		}
//
//		initH(v);
//		init();
//		setFoucs();
//		getIsMessage();
//		getAdvertlist();
//
//	}
//
//	/**
//	 * 设置焦点
//	 *
//	 */
//	public void setFoucs() {
//		sv.smoothScrollTo(0, 0);
//		lv_content.setFocusable(false);
//	}
//
//	@SuppressWarnings("deprecation")
//	private void initH(View v) {
//		iv_six_a = (ImageView) v.findViewById(R.id.iv_six_a);
//		iv_six_b = (ImageView) v.findViewById(R.id.iv_six_b);
//		iv_six_c = (ImageView) v.findViewById(R.id.iv_six_c);
//		iv_six_d = (ImageView) v.findViewById(R.id.iv_six_d);
//
//		ll_search = (LinearLayout) v.findViewById(R.id.ll_search);
//
//		iv_six_a.setOnClickListener(this);
//		iv_six_b.setOnClickListener(this);
//		iv_six_c.setOnClickListener(this);
//		iv_six_d.setOnClickListener(this);
//		ll_search.setOnClickListener(this);
//		tv_location.setOnClickListener(this);
//		MainAc.intance.setOnTabActivityResultListener(this);
//
//		vp_ads.setOnPageChangeListener(new OnPageChangeListener() {
//			@Override
//			public void onPageSelected(int pos) {
//				// 更新指示器
//				for (int i = 0; i < vp_ads.getAdapter().getCount(); i++) {
//					ll_vp_indicator.getChildAt(i).setSelected(i == pos);
//				}
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//			}
//		});
//
//	}
//
//
//	private void popTip()
//	{
////		View contentView = LayoutInflater.from(context).inflate(R.layout.pop_tip_course, null);
////		final PopupWindow popupWindow = new PopupWindow(contentView,
////                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
////		contentView.setOnTouchListener(new View.OnTouchListener(){
////			@Override
////			public boolean onTouch(View v, MotionEvent event) {
//////				popupWindow.dismiss();
////				return false;
////			}});
////		popupWindow.showAsDropDown(this.iv_sixview_head);
////		contentView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.baidong));
//
//		tv_poptip.setVisibility(View.VISIBLE);
//		tv_poptip.setAnimation(AnimationUtils.loadAnimation(context, R.anim.baidong));
//	}
//
//
//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(android.os.Message msg) {
//			switch(msg.what){
//			case 1:
//				break;
//			}
//		};
//	};
//
//	/**
//	 * 每隔6秒钟切换到下一页
//	 */
//	private Runnable nextPage = new Runnable() {
//		@Override
//		public void run() {
//			int curpos = vp_ads.getCurrentItem();
//			curpos += 1;
//
//			if (vp_ads.getAdapter() != null) {
//				if (curpos == vp_ads.getAdapter().getCount()) {
//					curpos = 0;
//				}
//				ll_vp_indicator.getChildAt(curpos).setSelected(true);
//				vp_ads.setCurrentItem(curpos, true);
//				handler.postDelayed(nextPage, 3000);
//			}
//		}
//	};
//
//	private ImagePagerAdapters280 imgAdapter;
//
//	/**
//	 *
//	 *
//	 * 加载首页广告图片
//	 *
//	 * adpositionidType  1 内部富文本
//	 * adpositionidType  2 外边链接
//	 * @param pictures
//	 */
//	private void addImgToViewPager(String[] pictures){
//		handler.removeCallbacks(nextPage);
//		ll_vp_indicator.removeAllViews();
//		for (int i = 0; i < pictures.length; i++) {
//			// 添加一个小圆点
//			LinearLayout dot = new LinearLayout(context);
//			LayoutParams lp = new LinearLayout.LayoutParams(
//					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
//			dot.setBackgroundResource(R.drawable.dot_item_with_view_pager);
//			int m = UITools.dip2px(6);
//			lp.setMargins(m, m, m, m);
//			dot.setLayoutParams(lp);
//			ll_vp_indicator.addView(dot, lp);
//		}
//		// 为了在OnPageChangeListener中引用ViewPager
//		ll_vp_indicator.getChildAt(0).setSelected(true);
//
//		imgAdapter = new ImagePagerAdapters280(context, pictures);
//		vp_ads.setAdapter(imgAdapter);
//		handler.postDelayed(nextPage, 6000);
//
//		imgAdapter.setOnItemClickListener(new ImagePagerAdapters280.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(ImageView view, int position) {
//				System.out.println(advertisements.size()+"#"+position+"wanghaiyan1#"+advertisements.get(position).boardId);
//				if("0".equals(advertisements.get(position).boardType)){//0 单个广告类型 1多个广告类型
//					getGood(advertisements.get(position).boardId);
//				}else{
//					Intent intent = new Intent(context,CourseListAc.class);
//					intent.putExtra("boardId", advertisements.get(position).boardId);
//					intent.putExtra("tag", "002");
//					context.startActivity(intent);
//				}
//			}
//		});
//
//
//	}
//
//
//	// 初始化课程
//	public void init() {
//		list.clear();
//		listAdapter = new CourseListAdapter(context, list);
//		lv_content.setAdapter(listAdapter);
//
//		getHotGoods();
//		getStorelist();
//
//		getChildrenList();
//
//		updateView();
//		iv_sixview_head.setOnClickListener(this);
//	}
//
//
//	//弹出孩子选择框
//	private void showPopupWindow()
//	{
//		if(popupWindow == null)
//		{
//			View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_course_mychildren, null);
//			popupWindow = new PopupWindow(contentView,
//	                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
//			contentView.setOnTouchListener(new View.OnTouchListener() {
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					popupWindow.dismiss();
//					return false;
//				}
//			});
//			contentView.findViewById(R.id.ll_addchild).setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					popupWindow.dismiss();
//					//添加孩子
//					context.startActivity(new Intent(context, AddChildAc.class));
//				}
//			});
//		}
//
//		LinearLayout ll_mychildren = (LinearLayout)popupWindow.getContentView().findViewById(R.id.ll_mychildren);
//		ll_mychildren.removeAllViews();
//		if(childrenList != null && childrenList.size() > 0)
//		{
//			int[] ids = {R.id.tv_child1,R.id.tv_child2,R.id.tv_child3,R.id.tv_child4};
//			for(int i = 0; i<= childrenList.size()/4; i++)
//			{
//				View v = LayoutInflater.from(context).inflate(R.layout.view_mychildren_row, ll_mychildren, false);
//
//				int j = 0;
//				for(j = 0; j < 4; j++)
//				{
//					if(i*4+j >= childrenList.size())
//						break;
//					final Child child = childrenList.get(i*4+j);
//					((TextView)v.findViewById(ids[j])).setText(child.realName);
//					v.findViewById(ids[j]).setVisibility(View.VISIBLE);
//					v.findViewById(ids[j]).setOnClickListener(new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							User user = App.getInstance().getUser();
//							if(user != null)
//							{
//								App.getInstance().setChild(child);
//								App.toast("选择孩子："+child.realName);
//								popupWindow.dismiss();
//								refresh();
//							}
//						}
//					});
//				}
//
//				if(j>0)
//					ll_mychildren.addView(v);
//			}
//		}
//
//		popupWindow.setAnimationStyle(android.R.style.Animation);
//		popupWindow.showAsDropDown(iv_sixview_head, 0, -Util.dpToPx(46, getResources()));
//	}
//
//	/**
//	 * 获取未读消息
//	 */
//	public void getIsMessage(){
//
//		final HashMap<String, String> map = new HashMap<String, String>();
//		if(null != App.getInstance().getUser()){
//			map.put("userId", App.getInstance().getUser().userid);
//		}else{
//			map.put("userId", "0");
//		}
//		if(null!=city&&!TextUtils.isEmpty(city.cityName)){
//			map.put("regionName", city.cityName);//城市名称
//		}else{
//			map.put("regionName", "西安市");//城市名称
//		}
//		MainAc.intance.showProgressDialog();
//		UserManager.getInstance().getTzjmessageIsunreadmessage(context, map, new ServiceCallback<CommonResult<User>>() {
//
//			final String CACHEKEY = "CourseFm_getIsMessage";
//
//			@Override
//			public void error(String msg) {
//				//从缓存中读取数据
//				CommonResult<User> obj = new CommonResult<User>(){};
//				Type type = obj.getClass().getGenericSuperclass();
//				obj = (CommonResult<User>)CacheUtil.read(context, CACHEKEY, map, type);
//				if(obj != null)
//				{
//					doResult(obj);
//					return;
//				}
//
//				//没有缓存时执行下面的逻辑
//				MainAc.intance.hideProgressDialog();
//				MainAc.intance.dialogToast(msg);
//			}
//
//			@Override
//			public void done(int what, CommonResult<User> obj) {
//				doResult(obj);
//
//				//缓存结果
//				CacheUtil.save(context, CACHEKEY,map, obj);
//			}
//
//			private void doResult(CommonResult<User> obj)
//			{
//				MainAc.intance.hideProgressDialog();
//				if(null !=obj.data){
//					if("1".equals(obj.data.isMessage)){
//						img_read.setVisibility(View.VISIBLE);
//					}else if("0".equals(obj.data.isMessage)){
//						img_read.setVisibility(View.GONE);
//					}
//				}
//			}
//		});
//	}
//
//	/**
//	 * 获取广告位
//	 */
//	public void getAdvertlist(){
//
//		final HashMap<String, String> map = new HashMap<String, String>();
////		if(null != App.getInstance().getUser()){
////			map.put("babyAge", App.getInstance().getUser().babyAge+"");
////		}
//		if(null!=city&&!TextUtils.isEmpty(city.cityName)){
//			map.put("regionName", city.cityName);//城市名称
//		}else{
//			map.put("regionName", "西安市");//城市名称
//		}
//		UserManager.getInstance().getTzjadvertAdvertlist(context, map, new ServiceCallback<CommonListResult<Advertisement>>() {
//
//			final String CACHEKEY = "CourseFm_getAdvertlist";
//
//			@Override
//			public void error(String msg) {
//				//从缓存中读取数据
//				CommonListResult<Advertisement> obj = new CommonListResult<Advertisement>(){};
//				Type type = obj.getClass().getGenericSuperclass();
//				obj = (CommonListResult<Advertisement>)CacheUtil.read(context, CACHEKEY, map, type);
//				if(obj != null)
//				{
//					doResult(obj);
//					return;
//				}
//
//				//没有缓存时执行下面的逻辑
//				MainAc.intance.dialogToast(msg);
//			}
//
//			@Override
//			public void done(int what, CommonListResult<Advertisement> obj) {
//				doResult(obj);
//
//				//缓存结果
//				CacheUtil.save(context, CACHEKEY, map, obj);
//			}
//
//			private void doResult(CommonListResult<Advertisement> obj)
//			{
//				if(null!=obj.data&&obj.data.size()>0){
//					listAdvment.clear();
//					advertisements.clear();
//					listAdvment.addAll(obj.data);
//					if(obj.data.size()>0){
//						img_default.setVisibility(View.GONE);
//						fm_image.setVisibility(View.VISIBLE);
//						imagesSrc = new String[listAdvment.size()];
//						for(int i = 0;i<listAdvment.size();i++){
//							imagesSrc[i] = listAdvment.get(i).titleMultiUrl;
//						}
//						advertisements.addAll(obj.data);
//						addImgToViewPager(imagesSrc);
//
//					}
//				}else{
//					img_default.setVisibility(View.VISIBLE);
//					fm_image.setVisibility(View.GONE);
//				}
//
//				if(imgAdapter !=null){
//					imgAdapter.notifyDataSetChanged();
//				}
//			}
//		});
//	}
//	/**
//	 * 获取门店
//	 */
//	public void getStorelist(){
//		prolist.clear();
//		final HashMap<String, String> map = new HashMap<String, String>();
//		if(null!=city&&!TextUtils.isEmpty(city.cityName)){
//			map.put("regionName", city.cityName);//城市名称
//		}else{
//			map.put("regionName", "西安市");//城市名称
//		}
//
//		UserManager.getInstance().getTzjstoreStorelist(context, map, new ServiceCallback<CommonListResult<Storelist>>() {
//
//			final String CACHEKEY = "CourseFm_getStorelist";
//
//			@Override
//			public void error(String msg) {
//				//从缓存中读取数据
//				CommonListResult<Storelist> obj = new CommonListResult<Storelist>(){};
//				Type type = obj.getClass().getGenericSuperclass();
//				obj = (CommonListResult<Storelist>)CacheUtil.read(context, CACHEKEY,map, type);
//				if(obj != null)
//				{
//					doResult(obj);
//					return;
//				}
//
//				//没有缓存时执行下面的逻辑
//				MainAc.intance.dialogToast(msg);
//			}
//
//			@Override
//			public void done(int what, CommonListResult<Storelist> obj) {
//				doResult(obj);
//
//				//缓存结果
//				CacheUtil.save(context, CACHEKEY, map, obj);
//			}
//
//			private void doResult(CommonListResult<Storelist> obj)
//			{
//				if(null!=obj.data){
//					prolist=obj.data;
//					if (prolist.size() > 0) {
//						fl.setVisibility(View.VISIBLE);
//						h_list.setVisibility(View.VISIBLE);
//					}
//					adapter = new CourseGalleryAdapter(context, prolist);
//					h_list.setAdapter(adapter);
//					// gallery.setSelection(1);
//
//					// 画廊点击事件
//					h_list.setOnItemClickListener(new OnItemClickListener() {
//
//						@Override
//						public void onItemClick(AdapterView<?> arg0, View arg1,
//								int position, long arg3) {
//							Intent intent = new Intent(context, CourseListAc.class);
//							intent.putExtra("title", prolist.get(position).storeName);
//							intent.putExtra("tag", "001");//表示是点击门店进去的
//							intent.putExtra("storeId", prolist.get(position).storeId);//门店ID
//							intent.putExtra("lon", prolist.get(position).lon);
//							intent.putExtra("lat", prolist.get(position).lat);
//							startActivity(intent);
//						}
//					});
//				}
//			}
//		});
//	}
//	/**
//	 * 获取推荐课程
//	 */
//	public void getHotGoods(){
//		final HashMap<String, String> map = new HashMap<String, String>();
//		if(null!=city&&!TextUtils.isEmpty(city.cityName)){
//			map.put("regionName", city.cityName);//城市名称
//		}else{
//			map.put("regionName", "西安市");//城市名称
//		}
//
//		if(null != App.getInstance().getUser() && App.getInstance().getChild() != null){
//			map.put("babyAge", App.getInstance().getChild().age+"");
//		}
//		map.put("pageNum", pager.nextPageToStr());//查询页数
//		map.put("pageCount", "10");//每页展示条数
////		map.put("goodsType", value);//类别
//
//		UserManager.getInstance().getTzjgoodsHotgoods(context, map, new ServiceCallback<CommonListResult<HotGoods>>() {
//
//			final String CACHEKEY = "CourseFm_getHotGoods";
//
//			@Override
//			public void error(String msg) {
//				//从缓存中读取数据
//				CommonListResult<HotGoods> obj = new CommonListResult<HotGoods>(){};
//				Type type = obj.getClass().getGenericSuperclass();
//				obj = (CommonListResult<HotGoods>)CacheUtil.read(context, CACHEKEY, map, type);
//				if(obj != null)
//				{
//					doResult(obj);
//					return;
//				}
//
//				//没有缓存时执行下面的逻辑
//				MainAc.intance.dialogToast(msg);
//				MainAc.intance.hideProgressDialog();
//				mRefreshLayout.finishPullLoad();
//			}
//
//			@Override
//			public void done(int what, CommonListResult<HotGoods> obj) {
//				doResult(obj);
//
//				//缓存结果
//				CacheUtil.save(context, CACHEKEY, map, obj);
//			}
//
//			private void doResult(CommonListResult<HotGoods> obj)
//			{
//				MainAc.intance.hideProgressDialog();
//				if(obj.data.size()>0){
//					pager.setCurrentPage(pager.nextPage(),list.size());
//					list.addAll(obj.data);
//
//					// 推荐课程点击事件
//					lv_content.setOnItemClickListener(new OnItemClickListener() {
//						@Override
//						public void onItemClick(AdapterView<?> arg0, View arg1,
//								int position, long arg3) {
//							Intent intent = new Intent(context, CourseDetailAc.class);
//							intent.putExtra("degree", list.get(position).goodsType);
//							intent.putExtra("goodsId", list.get(position).goodsId);
//							intent.putExtra("pageType", "CourseFm");
//							intent.putExtra("pageTag", "1");//pageTag 1基础数据（首页推荐课程，广告位选择，类别搜索）  2商品信息
//							startActivity(intent);
//						}
//					});
//				}
//				listAdapter.notifyDataSetChanged();
//				mRefreshLayout.finishPullLoad();
//			}
//		});
//	}
//
//	@Override
//	public void onClick(View v) {
//		Intent intent;
//		switch (v.getId()) {
//		case R.id.iv_yj:
//			intent = new Intent(context, MyMessageAc.class);
//			startActivity(intent);
//			break;
//		case R.id.iv_six_a: // 城市营地
//			intent = new Intent(context, CourseListAc.class);
//			intent.putExtra("title", "城市营地");
//			startActivity(intent);
//			break;
//		case R.id.iv_six_b: // 户外俱乐部
//			intent = new Intent(context, CourseListAc.class);
//			intent.putExtra("title", "周末户外");
//			startActivity(intent);
//			break;
//		case R.id.iv_six_c: // 暑期大露营
//			intent = new Intent(context, CourseListAc.class);
//			intent.putExtra("title", "暑期大露营");
//			startActivity(intent);
//			break;
//		case R.id.iv_six_d:  //卖商品
////			 intent = new Intent(context, CourseListAc.class);
////			 intent.putExtra("title", "搜索");
////			 startActivity(intent);
//			intent = new Intent(context, AccouterIndexAc.class);
//			startActivity(intent);
//			 break;
//		case R.id.ll_search: //导航栏搜索
//			 intent = new Intent(context, CourseListAc.class);
//			 intent.putExtra("title", "搜索");
//			 startActivity(intent);
//			 break;
//		case R.id.tv_location:
//			intent = new Intent(context, Activity01.class);
//			MainAc.intance.startActivityForResult(intent, 1);
//			// startActivity(intent);
//			break;
//		case R.id.iv_sixview_head:  //弹出孩子选择框
//			if(MainAc.intance.tag ==2){
//				showPopupWindow();
//				tv_poptip.setAnimation(null);
//				tv_poptip.setVisibility(View.GONE);
//				SharedPreferencesHelper.saveBoolean("CourseTip", true);
//			}else{
////				isLogin();
//			}
//			break;
//		default:
//			break;
//		}
//	}
//	@Override
//	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
//		if (5 == resultCode) {
//			String cityName = data.getStringExtra("cityName"); // 城市名称
//			tv_location.setText(cityName);
//			String strCity = cityName.substring(cityName.length()-1,cityName.length());
//			if(strCity.equals("市")){
//				tv_location.setText(cityName.substring(0,cityName.length()-1));
//			}
//
//			City cInfo = App.getInstance().getCityCode();
//			if(null != cInfo){
//				cInfo.cityName = cityName;
//				App.getInstance().saveCityCode(cInfo);
//				city  =App.getInstance().getCityCode();
//			}
//
////			list.clear();
////			getStorelist();
////			getAdvertlist();
////			getIsMessage();
//		}
//	}
//
////	@Override
////	public void onItemClick(ImageView view, int position) {
////		if("0".equals(advertisements.get(position).boardType)){//0 单个广告类型 1多个广告类型
////			getGood(advertisements.get(position).boardId);
////		}else{
////			Intent intent = new Intent(context,CourseListAc.class);
////			intent.putExtra("boardId", advertisements.get(position).boardId);
////			intent.putExtra("tag", "002");
////			context.startActivity(intent);
////		}
////	}
//
//	/**
//	 * 通过广告ID获取商品id
//	 */
//	public void getGood(String boardId){
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("boardId", boardId);
//		if(null != App.getInstance().getUser()){
//			map.put("userId", App.getInstance().getUser().userid);
//		}else{
//			map.put("userId", "0");
//		}
//		if(null != App.getInstance().getChild())
//			map.put("babyId", App.getInstance().getChild().babyId);
//		MainAc.intance.showProgressDialog();
//		UserManager.getInstance().getTzjgoodsGoodsdetailbyboard(context, map, new ServiceCallback<CommonResult<HotGoods>>() {
//
//			@Override
//			public void error(String msg) {
//				MainAc.intance.hideProgressDialog();
//				MainAc.intance.dialogToast(msg);
//			}
//
//			@Override
//			public void done(int what, CommonResult<HotGoods> obj) {
//				MainAc.intance.hideProgressDialog();
//				if(null !=obj.data){
//					Intent intent = new Intent(context,CourseDetailAc.class);
//					intent.putExtra("goodsId", obj.data.goodsId);
//					intent.putExtra("degree",  obj.data.goodsType);
//					intent.putExtra("pageTag", "1");//pageTag 1基础数据（首页推荐课程，广告位选择，类别搜索）  2商品信息
//
//					context.startActivity(intent);
//				}
//			}
//		});
//	}
//
//	@Override
//	public void onRefresh() {
//
//		mRefreshLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            	refresh();
//            }
//        }, 3000);
//
//	}
//
//	public void refresh()
//	{
//		MainAc.intance.showProgressDialog();
//  		 pager.setFirstPage();
//  		 list.clear();
//  		listAdapter.notifyDataSetChanged();
//  		 getHotGoods();
//  		getAdvertlist();
//  		getStorelist();
//  		getIsMessage();
//  		 mRefreshLayout.finishPullRefresh();
//  		updateView();
//  		getChildrenList();
//	}
//
//	private void getChildrenList()
//	{
//		//查询孩子列表
//		HashMap<String, String> hmap = new HashMap<String, String>();
//		if(null == App.getInstance().getUser()){
//			hmap.put("userId", "0");
//		}else{
//			hmap.put("userId", App.getInstance().getUser().userid);
//		}
//		UserManager.getInstance().getChildrenlist(context, hmap, new ServiceCallback<CommonListResult<Child>>(){
//			@Override
//			public void done(int what, CommonListResult<Child> obj) {
//				childrenList = obj.data;
////				SharedPreferencesHelper.saveBoolean("CourseTip", false);
//				if(CourseFm.this.isVisible() && childrenList!=null && childrenList.size()>1 && !SharedPreferencesHelper.getBoolean("CourseTip"))
//				{
//					popTip();
//				}
//			}
//			@Override
//			public void error(String msg) {
////						App.toast(msg);
//			}});
//	}
//
//	public void updateView()
//	{
//		Child child = App.getInstance().getChild();
//		if(child != null)
//			ImageLoader.getInstance().displayImage(child.avatar, iv_sixview_head, App.getInstance().getheadImage());
//
////		SharedPreferencesHelper.saveBoolean("CourseTip", false);
//		if(CourseFm.this.isVisible() && childrenList!=null && childrenList.size()>1 && !SharedPreferencesHelper.getBoolean("CourseTip"))
//		{
//			popTip();
//		}
//	}
//
//	@Override
//	public void onLoadMore() {
//
//		mRefreshLayout.postDelayed(new Runnable() {
//	          @Override
//	          public void run() {
//	        	  getHotGoods();
//
//	          }
//	      }, 2000);
//
//
//	}
//
//	@Override
//	public boolean isPullRefreshEnable() {
//		return true;
//	}
//
//	@Override
//	public boolean isPullLoadEnable() {
//		return true;
//	}
//
//
//}
