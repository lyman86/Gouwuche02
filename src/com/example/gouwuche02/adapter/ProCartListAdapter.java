package com.example.gouwuche02.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gouwuche02.R;
import com.example.gouwuche02.activity.ProCartListActivity;
import com.example.gouwuche02.bean.ProCartListBean;
import com.example.gouwuche02.config.BaseUrl;
import com.example.gouwuche02.dialog.MyAlertDialog;
import com.example.gouwuche02.utils.StringUtils;
import com.example.gouwuche02.utils.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ProCartListAdapter extends CommonAdapter<ProCartListBean> {
	private ProCartListActivity mActivity;
	// 每个商品数量的集合
	private List<Integer> numbers;

	// 每个商品的数量是否改变
	private List<Boolean> isNumbersChanged;

	// 被选中商品 的数量
	private int selectCount = 0;

	ProCartListBean bean;

	private onSelectListener mListener;
	private DecimalFormat decimalFormat;

	private boolean isClick = false;

	// 商品的总价格
	private float all = 0.0f;
	private long secondTime;

	private final int ADD = 1;
	
	private onDeleteListener mOnDeleteListener;
	public interface onDeleteListener{
		void onDelete(int pos,String item);
	}
	
	public void setOnDeleteListener(onDeleteListener mOnDeleteListener){
		this.mOnDeleteListener = mOnDeleteListener;
	}
	public interface onSelectListener {
		void onSelect(Float allPrice);
	}

	public void setOnSelectListener(onSelectListener mListener) {
		this.mListener = mListener;
	}

	public ProCartListAdapter(ProCartListActivity context, List<ProCartListBean> list,
			int layoutId) {
		super(context, list, layoutId);
		mActivity = context;
		// 初始化数据
		initDatas();
		new MyThread().start();

	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
		// 初始化ImageLoader类
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
		List<String> subTotals = new ArrayList<String>();
		List<Boolean> isSelects = new ArrayList<Boolean>();
		// 初始化每个商品的数量
		numbers = new ArrayList<Integer>();
		isNumbersChanged = new ArrayList<Boolean>();
		for (int i = 0; i < list.size(); i++) {
			numbers.add(Integer.parseInt(list.get(i).number));
			subTotals.add(String.valueOf(Float.parseFloat(list.get(i).price)
					* Integer.parseInt(list.get(i).number)));
			isNumbersChanged.add(false);
			isSelects.add(false);
		}
		bean = new ProCartListBean();
		bean.setSubTotals(subTotals);
		bean.setIsSelects(isSelects);
		
		decimalFormat = new DecimalFormat("0.00");
	}

	public ProCartListAdapter() {}

	@Override
	public void convert(final ViewHolder viewHolder, final ProCartListBean t) {
		//每个种类商品的总价，保留两位小数
		Float price = Float.parseFloat(t.price)* numbers.get(viewHolder.getPosition());
		String subTotal = decimalFormat.format(price);
		
	  viewHolder.setText(R.id.tv_title, t.title)
				.setText(R.id.tv_price, "批发价：" + t.price + "￥")
				.setText(R.id.tv_pinpai, "品牌：" + t.brand)
				.setText(R.id.tv_item, "订货号：" + t.item)
				.setText(R.id.tv_xinghao, "型号：" + t.model)
				.setText(R.id.tv_pro_num,String.valueOf(numbers.get(viewHolder.getPosition())))
				.setText(R.id.tv_item_price, "小计: " + subTotal);
		
		//设置加减按钮的监听
		//setAddAndSubListener(viewHolder, t);
		//加载图片
		loadImage(viewHolder,t);
		//设置删除按钮的监听
		setDeleteListener(viewHolder,t);
		
		final CheckBox checkBox = viewHolder.getView(R.id.cb_select);

		//设置选项的监听
		setCheckBoxListener(viewHolder,t,checkBox);
		
		boolean isSelect = bean.getIsSelects().get(viewHolder.getPosition());
		
		checkBox.setChecked(isSelect);
		
	}
	
	
	/**
	 * 设置选项的监听
	 * @param viewHolder
	 * @param t
	 */
	private void setCheckBoxListener(final ViewHolder viewHolder, ProCartListBean t,final CheckBox checkBox) {
		 
		checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String subTotal = bean.getSubTotals().get(viewHolder.getPosition());
				boolean isSelect = bean.getIsSelects().get(viewHolder.getPosition());
				if (isSelect) {
					all -= Float.valueOf(subTotal);
					selectCount--;
					
				} else {
					all += Float.valueOf(subTotal);
					selectCount++;
				}
				bean.getIsSelects().set(viewHolder.getPosition(), !isSelect);
				checkBox.setChecked(!isSelect);
				if (mListener != null) { 
					mListener.onSelect(all);
				}

			}
		});
		
	}

	/**
	 * 设置删除按钮的监听
	 * @param viewHolder
	 * @param t
	 */
	private void setDeleteListener(final ViewHolder viewHolder, final ProCartListBean t) {
		ImageView imageBt = viewHolder.getView(R.id.id_shopping_cart_iv_delete);
		imageBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (mOnDeleteListener!=null) {
					mOnDeleteListener.onDelete(position, t.item);
				}
//				String delteUrl = StringUtils.getDeleteFromCartUrl(BaseUrl.EHSY_DELETE_PRO_FROM_CART_URL,
//						"58e767f9fb72c44e68922d7033cca5e5", t.item);
//				
//				MyAlertDialog alertDialog = new MyAlertDialog(mActivity,viewHolder.getPosition(),t.item);
//				
//				alertDialog.showDialogOne("确定删除该商品吗？", "提示", -1);
			}
		});
		
	}

	/**
	 * 加载图片
	 * @param viewHolder
	 */
	private void loadImage(ViewHolder viewHolder,ProCartListBean t) {
		ImageView imageView = viewHolder.getView(R.id.iv_goods);
		ImageLoader.getInstance().displayImage(BaseUrl.EHSY_BASE_URL + t.thumb,imageView, baseOptions);
		
	}

	/**
	 * 设置加减按钮的监听
	 * @param viewHolder
	 * @param t
	 */
//	private void setAddAndSubListener(final ViewHolder viewHolder,final ProCartListBean t) {
//		final int position = viewHolder.getPosition();
//		
//		TextView addTv = viewHolder.getView(R.id.tv_add_price);
//		TextView subTv = viewHolder.getView(R.id.tv_sub_price);
//		
//		//加号按钮的监听
//		addTv.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				if (LoadContent.isNetworkAvailable(context)) {
//					
//					addOrSubLogic(1, viewHolder, t, position);
//					setUpdate();
//					
//				} else {
//					Toast.makeText(context, "网络问题", Toast.LENGTH_SHORT).show();
//				}
//				
//			}
//		});
//		
//		
//		//减号按钮的监听
//		subTv.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				if (numbers.get(position) <= 1) {
//					viewHolder.getView(R.id.tv_sub_price).setClickable(false);
//				} else {
//					
//					if (LoadContent.isNetworkAvailable(context)) {
//						
//						addOrSubLogic(0, viewHolder, t, position);
//						setUpdate();
//						
//					} else {
//						Toast.makeText(context, "网络问题",Toast.LENGTH_SHORT).show();
//					}
//
//				}
//				
//			}
//		});
//
//
//	}

	protected void setUpdate() {
		isClick = true;
		secondTime = System.currentTimeMillis();
	}

	/**
	 * 加减逻辑
	 * 
	 * @param type
	 *            1是加，其他数字都是减
	 * @param viewHolder
	 * @param t
	 * @param pos
	 */
	public void addOrSubLogic(int type, final ViewHolder viewHolder,
			final ProCartListBean t, final int pos) {
		int num = numbers.get(pos);
		isNumbersChanged(pos);
		// 点击加号的时候
		if (type == ADD) {
			++num;
			if (bean.getIsSelects().get(pos)) {

				if (mListener != null) {
					all += Float.valueOf(t.price);
				}
			}
			viewHolder.getView(R.id.tv_sub_price).setClickable(true);
			// 点击减号的时候
		} else {
			--num;
			if (bean.getIsSelects().get(pos)) {

				if (mListener != null) {
					all -= Float.valueOf(t.price);
					
				}
			}
		}
		mListener.onSelect(all);
		numbers.set(pos, num);
		String subTotal = decimalFormat.format((Float.parseFloat(t.price) * numbers.get(pos)));
		viewHolder.setText(R.id.tv_pro_num, String.valueOf(numbers.get(pos)));
		viewHolder.setText(R.id.tv_item_price, "小计: " + subTotal);
		bean.getSubTotals().set(pos, subTotal);
	}

	/**
	 * 设置number值已经改变
	 * 
	 * @param num2
	 * @param pos
	 */
	private void isNumbersChanged(int pos) {
		isNumbersChanged.set(pos, true);
	}

	/**
	 * list删除选项
	 */
	public void removeItem(int pos) {
		list.remove(pos);
		if (bean.getIsSelects().get(pos)) {
			all -= Float.valueOf(bean.getSubTotals().get(pos));
			selectCount--;
		}
		bean.getSubTotals().remove(pos);
		bean.getIsSelects().remove(pos);
		numbers.remove(pos);
		if (mListener != null) {
			mListener.onSelect(all);
		}

		notifyDataSetChanged();
	}

	/**
	 * 创建对外方法选中所有商品
	 */
	public void selectAllPro() {
		selectCount = list.size();
		System.out.println(selectCount + "  selectCount");
		for (int i = 0; i < list.size(); i++) {
			if (bean.getIsSelects().get(i) == false) {
				bean.getIsSelects().set(i, true);
				all += (Float.valueOf(bean.getSubTotals().get(i)));

			}

		}

		notifyDataSetChanged();
	}

	/**
	 * 创建对外方法取消=选中所有商品
	 */
	public void unSelectAllPro() {
		selectCount = 0;
		for (int i = 0; i < list.size(); i++) {
			bean.getIsSelects().set(i, false);
		}
		all = 0.00f;
		notifyDataSetChanged();
	}

	/**
	 * 获取选中商品的数量
	 * 
	 * @return
	 */
	public int getSelectCount() {
		return selectCount;
	}

	/**
	 * 获取所有商品的价格
	 * 
	 * @param count
	 * @return
	 */
	public float getAllPrice(int count) {
		List<String> subTotals = bean.getSubTotals();
		float allPrice = 0.0f;
		if (count != 0) {
			for (int i = 0; i < subTotals.size(); i++) {
				allPrice += (Float.valueOf(subTotals.get(i)));
			}
		}
		return allPrice;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			mTime = (Long) msg.obj;

			if (mTime - secondTime > 1000 && isClick) {
				for (int i = 0; i < isNumbersChanged.size(); i++) {
					if (isNumbersChanged.get(i)) {
						//LoadContent.UpdateItem(mActivity, numbers.get(i),list.get(i).item);
						isNumbersChanged.set(i, false);
					}
				}

				isClick = false;
			}

		};
	};
	private long mTime = 0;

	/**
	 * 创建一个线程时刻判断当点击停止时候所获得的时间和线程里的时间做比较，若后者和前者的差值大于1秒，则向副服务器提交值，跟新购物车
	 * 
	 * @author Administrator
	 * 
	 */
	class MyThread extends Thread {

		@Override
		public void run() {
			while (true) {
				try {
					mTime = System.currentTimeMillis();
					Thread.sleep(1);
					Message msg = Message.obtain();
					msg.obj = mTime;
					mHandler.sendMessage(msg);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
