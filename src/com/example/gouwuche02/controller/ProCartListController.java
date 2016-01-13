package com.example.gouwuche02.controller;

import com.example.gouwuche02.activity.ProCartListActivity;

import com.example.gouwuche02.view.ProCartListView;

public class ProCartListController {
	
	private ProCartListView mView;
	private ProCartListActivity mContext;
	
	public ProCartListController(ProCartListView mView,ProCartListActivity mContext) {
		this.mView = mView;
		this.mContext = mContext;
		initController();
	}

	private void initController() {
		mContext.setProCartListAdapter();
	}
	
	

}
