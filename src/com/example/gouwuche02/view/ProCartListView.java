package com.example.gouwuche02.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gouwuche02.R;
import com.example.gouwuche02.adapter.ProCartListAdapter;

public class ProCartListView extends LinearLayout {
	private Context mContext;
	private ListView mListView;
	private ImageView imageAllSelect;
	private TextView alreadySelectTv;
	private TextView allPriceTv;
	private TextView goOrderTv;
	
	public ProCartListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	public void init(){
		mListView = (ListView) findViewById(R.id.lv);
		imageAllSelect = (ImageView)findViewById(R.id.iv_unselect);
		alreadySelectTv = (TextView) findViewById(R.id.tv_select_count);
		allPriceTv = (TextView) findViewById(R.id.tv_all_price);
		goOrderTv = (TextView) findViewById(R.id.tv_go_order);
		
	}
	
	public void setListener(){
		
	}
	
	public void setProCartListAdapter(ProCartListAdapter adapter){
		mListView.setAdapter(adapter);
	}
	
	public void setListeners(OnClickListener onClickListener){
			imageAllSelect.setOnClickListener(onClickListener);
			goOrderTv.setOnClickListener(onClickListener);
	}
	
	public void setAlreadySelectText(String text){
		   alreadySelectTv.setText(text);
	}
	
	public void setAllPriceText(String text){
		   allPriceTv.setText(text);
	}
	
	public void createCartListDialog(){
		
	}
	
//	public void createCartListDialog(String title,String message,int position){
//		 AlertDialog.Builder builder = new Builder(mContext);
//		 builder.setMessage(message);
//		 builder.setTitle(title);
//		 builder.setIcon(position);
//		 builder.setPositiveButton("确定", new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		 builder.setPositiveButton("", new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				context.deleteProFromCart(position,item);
//				dialog.dismiss();
//			}
//		});
//		 builder.setNegativeButton("取消", new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				
//			}
//		});
//		 
//		 builder.create().show();
//	}
	
	
	

}
