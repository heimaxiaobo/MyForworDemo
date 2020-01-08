package com.bobo.news;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import androidx.annotation.NonNull;

public class DemoAdapter extends BaseQuickAdapter<NewInfoBean, BaseViewHolder> {


    public DemoAdapter() {
        super(R.layout.item_list_new);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewInfoBean item) {
        helper.setText(R.id.tvTitle, item.getTitle()).setText(R.id.tvAuthorName, item.getAuthor_name());
        ImageView ivPic = helper.getView(R.id.ivPic);
        Glide.with(mContext).load(item.getThumbnail_pic_s()).into(ivPic);
    }
}