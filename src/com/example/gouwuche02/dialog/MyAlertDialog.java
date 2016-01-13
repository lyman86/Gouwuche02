package com.example.gouwuche02.dialog;

import com.example.gouwuche02.activity.ProCartListActivity;
import com.example.gouwuche02.adapter.ProCartListAdapter;
import com.example.gouwuche02.bean.ProCartListBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;


public class MyAlertDialog {
	
	private ProCartListActivity context;
	private int position;
	private String item;
	public MyAlertDialog(ProCartListActivity context,int position,String item) {
		this.context = context;
		this.position = position;
		this.item = item;
	}

	public void showDialogOne(String message,String title,int icon){
		 AlertDialog.Builder builder = new Builder(context);
		 builder.setMessage(message);
		 builder.setTitle(title);
		 builder.setIcon(icon);
		 builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.deleteProFromCart(position,item);
				dialog.dismiss();
			}
		});
		 builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		 
		 builder.create().show();
	}
	

}
