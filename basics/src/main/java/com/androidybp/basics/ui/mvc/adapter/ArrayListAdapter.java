/*
 * Copyright (C) 2009 Teleca Poland Sp. z o.o. <android@teleca.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androidybp.basics.ui.mvc.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义一个抽象的适配器类，以ArrayList作为数据。
 * 像getView（）涉及具体实现的方法由子类去实现。
 * 
 * @param <T>
 */
public abstract class ArrayListAdapter<T> extends BaseAdapter{
	
	public List<T> mList;
	protected Context mContext;
	private LayoutInflater inflater;
	protected ListView lvListView;
	protected DecimalFormat decimalFormat1;//两位小数限制

	public ArrayListAdapter(Context context){
		this.mContext = context;
		decimalFormat1 = new DecimalFormat("##,##0.00");
	}

	public ArrayListAdapter(Activity context){
		this.mContext = context;
		inflater=context.getLayoutInflater();
		decimalFormat1 = new DecimalFormat("##,##0.00");
	}

	@Override
	public int getCount() {
		if(getArrayCount() != 0){
			return getArrayCount();
		}else{
			if(mList != null)
				return mList.size();
			else
				return 0;
		}

	}

	@Override
	public Object getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	abstract public View getView(int position, View convertView, ViewGroup parent);

	/**
	 *
	 * @param list 向集合里添加内容
	 */
	public void addList(List<T> list){
		if (list == null || list.size() < 1)
			return;
		if(mList == null)
			mList = new ArrayList<>();
		this.mList.addAll(list);
		refreshList();
		notifyDataSetChanged();
	}
	public void clearList(){
		if(mList != null){
			mList.clear();
		}else {
			mList = new ArrayList<>();
		}
		refreshList();
		notifyDataSetChanged();
	}
	/**
	 * 更新适配器数据后，更新列表   List集合形式
	 */
	public void setList(List<T> list){
		if(list == null){
			if(mList == null){
				this.mList = new ArrayList<>();
			}else{
				mList.clear();
			}

		}else{
			this.mList = list;
		}
		refreshList();
		notifyDataSetChanged();
	}

	/**
	 * 更新数据
	 * @param list
     */
	public void setmListDate(List<T> list){
		if(list == null){
			this.mList = new ArrayList<>();
		}else{
			this.mList = list;
		}
		refreshList();
		notifyDataSetChanged();
	}

	/**
	 * 替换集合  前提是传进来的集合不等于null 长度大于1
	 * @param list 新的数据
	 */
	public void replaceList(List<T> list){
		if(list != null && list.size() > 0){
			this.mList = list;
			refreshList();
			notifyDataSetChanged();
		}

	}

	/**
	 * 备用方法   在当前展示数据 发生变动的时候  调用这个方法
	 */
	public void refreshList(){

	}
	
	public List<T> getList(){
		return mList;
	}

	/**
	 * @param list 对象的数组形式
	 */
	public void setList(T[] list){
		ArrayList<T> arrayList = new ArrayList<T>(list.length);  
		for (T t : list) {  
			arrayList.add(t);  
		}  
		setList(arrayList);
	}
	//获取适配器对应的List组件 
	public ListView getListView(){
		return lvListView;
	}
	//设置适配器对应的List组件
	public void setListView(ListView listView){
		lvListView = listView;
	}
	//返回布局视图
	public View getConvertView(int res_id){
		return inflater.inflate(res_id, null);
	}

	/**此方法用于指定getCount的返回值*/
	protected int getArrayCount(){return 0;}

	public void deleteItemData(int requestCode) {
		if(mList != null && requestCode < mList.size() && requestCode >= 0){
			mList.remove(requestCode);
			this.notifyDataSetChanged();
		}

	}

	/**
	 * 释放对象中的引用
	 */
	public void clearData(){
		if(mList != null)
			mList.clear();
		mList = null;
		mContext = null;
		inflater = null;
		lvListView = null;
		decimalFormat1 = null;
	}
}
