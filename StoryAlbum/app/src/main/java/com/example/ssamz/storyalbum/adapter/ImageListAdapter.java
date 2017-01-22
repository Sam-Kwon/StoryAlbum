package com.example.ssamz.storyalbum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ssamz.storyalbum.R;
import com.example.ssamz.storyalbum.view.RecycleImageView;

import java.util.ArrayList;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> implements ImageListAdapterContractor.View{
    static final String TAG = ImageListAdapter.class.getSimpleName();

    Context mContext;
    ArrayList<String> mUrls;
    ImageListAdapterContractor.Presenter mPresenter;

    View.OnClickListener mCallback;

    public ImageListAdapter(Context context, ArrayList<String> urls, View.OnClickListener callback) {
        mContext = context;
        mUrls = urls;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View cardView = inflater.inflate(R.layout.item_image_child, null, false);
        ViewHolder holder = new ViewHolder(cardView);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageView imageThumb = (ImageView) holder.imageThumb;
        mPresenter.setImage(mContext, mUrls.get(position), imageThumb);

        if (imageThumb instanceof RecycleImageView) {
            ((RecycleImageView)imageThumb).setPath(mUrls.get(position));
        }

        holder.imageThumb.setOnClickListener(mCallback);
    }

    @Override
    public int getItemCount() {
        return mUrls.size();
    }

    @Override
    public void setPresenter(ImageListAdapterContractor.Presenter presenter) {
        mPresenter = presenter;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageThumb;

        public ViewHolder(View itemView) {
            super(itemView);
            imageThumb = (ImageView) itemView.findViewById(R.id.image_thumb);
        }
    }
}
