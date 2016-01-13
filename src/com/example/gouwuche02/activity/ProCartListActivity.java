package com.example.gouwuche02.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.gouwuche02.R;
import com.example.gouwuche02.adapter.ProCartListAdapter;
import com.example.gouwuche02.bean.ProCartListBean;
import com.example.gouwuche02.config.BaseUrl;
import com.example.gouwuche02.controller.ProCartListController;
import com.example.gouwuche02.dialog.MyDialog;
import com.example.gouwuche02.http.BaseHttpLoad;
import com.example.gouwuche02.http.HttpDeleteProFromCart;
import com.example.gouwuche02.http.HttpDeleteProFromCart.onDeleteFromCartListener;
import com.example.gouwuche02.http.HttpProCartList;
import com.example.gouwuche02.http.HttpProCartList.onProCartListListener;
import com.example.gouwuche02.http.HttpUpdateProFromCart;
import com.example.gouwuche02.http.HttpUpdateProFromCart.onUpdateFromCartListener;
import com.example.gouwuche02.utils.StringUtils;
import com.example.gouwuche02.view.ProCartListView;



public class ProCartListActivity extends Activity {
	private ProCartListView mProCartListView;
	private ProCartListController mProCartListController;
	private final String INFO = "加载中";
	private ProCartListAdapter adapter;
	List<ProCartListBean> result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_cart_list);
        mProCartListView = (ProCartListView) findViewById(R.id.proCartListView);
        mProCartListView.init();
        mProCartListController = new ProCartListController(mProCartListView, this);
    }

    
    public void setProCartListAdapter(){
    	
		final MyDialog dialog = new MyDialog(this, INFO);
		dialog.createDialog();
		String realUrl = StringUtils.getProCarListUrl(BaseUrl.EHSY_ADD_PROCART_LIST_URL,"58e767f9fb72c44e68922d7033cca5e5");
		HttpProCartList httpProCartList = new HttpProCartList(realUrl);
		httpProCartList.start();
		httpProCartList.setOnProCartListListener(new onProCartListListener() {
			
			@Override
			public void loadSuccess(List<ProCartListBean> result) {
				Toast.makeText(ProCartListActivity.this,"yes", 1).show();
				        adapter = new ProCartListAdapter(ProCartListActivity.this, result, R.layout.item_pro_cart_list);
				        mProCartListView.setProCartListAdapter(adapter);
				        dialog.dismiss();
				
			}
			
			@Override
			public void loadFailed(String failed) {
					dialog.dismiss();
			}
		});
		
	}
    
    
    public void deleteProFromCart(final int position,String item) {
    	final MyDialog dialog = new MyDialog(this, INFO);
		dialog.createDialog();
		String delteUrl = StringUtils.getDeleteFromCartUrl(BaseUrl.EHSY_DELETE_PRO_FROM_CART_URL,
				"58e767f9fb72c44e68922d7033cca5e5",item); 
		BaseHttpLoad mContent = new HttpDeleteProFromCart(delteUrl);
		mContent.start();
		((HttpDeleteProFromCart) mContent)
				.setOnDeleteFromCartListener(new onDeleteFromCartListener() {

					@Override
					public void deleteSuccess(String success) {
						adapter.removeItem(position);
						dialog.dismiss();
						Toast.makeText(ProCartListActivity.this, success, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void deleteFailed(String failed) {
						dialog.dismiss();
						Toast.makeText(ProCartListActivity.this, failed, Toast.LENGTH_SHORT).show();
					}
				});
	}
    
	public void UpdateItem(int number,String item){
		BaseHttpLoad mContent = new HttpUpdateProFromCart(
				StringUtils.getUpdateFromCartUrl(BaseUrl.EHSY_UPDATE_PRO_FROM_CART_URL, 
						"58e767f9fb72c44e68922d7033cca5e5", String.valueOf(number),item));
					
		mContent.start();
		((HttpUpdateProFromCart)mContent).setOnUpdateFromCartListener(new onUpdateFromCartListener() {
			
			@Override
			public void UpdateSuccess(String success) {
				System.out.println(success+"UpdateSuccess>>>>>>>");
			}
			
			@Override
			public void UpdateFailed(String failed) {
				Toast.makeText(ProCartListActivity.this, failed, Toast.LENGTH_SHORT).show();
			}
		});
	}

  
}
