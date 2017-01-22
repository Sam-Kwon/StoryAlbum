package com.example.ssamz.storyalbum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ssamz.storyalbum.R;
import com.example.ssamz.storyalbum.data.StoryData;

import java.util.ArrayList;

public class StoryListAdapter extends BaseExpandableListAdapter implements StoryListAdapterContractor.View {
    static final String TAG = StoryListAdapter.class.getSimpleName();

    Context mContext;

    ArrayList<ArrayList<StoryData>> mData;

    StoryListAdapterContractor.Presenter mPresenter;

    public StoryListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mData == null)
            return 0;
        return mData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (mData == null)
            return null;
        return mData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (mData == null)
            return 0;
        return mData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        HeaderHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);

            holder = new HeaderHolder();
            holder.text = (TextView) convertView.findViewById(R.id.story_header);
            holder.indicator = (ImageView) convertView.findViewById(R.id.story_indicator);
            convertView.setTag(holder);
        } else {
            holder = (HeaderHolder) convertView.getTag();
        }

        if (mData.get(groupPosition).size() != 0){
            StoryData data  = (StoryData) mData.get(groupPosition).get(0);
            holder.text.setText(mPresenter.convertDate(data.getDate(), false) + "(" + mData.get(groupPosition).size() + ")");
        }

        if (isExpanded) {
            holder.indicator.setImageResource(android.R.drawable.arrow_up_float);
        } else {
            holder.indicator.setImageResource(android.R.drawable.arrow_down_float);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildrenHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_child, null);

            holder = new ChildrenHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.story_image);
            holder.title = (TextView) convertView.findViewById(R.id.story_title);
            holder.date = (TextView) convertView.findViewById(R.id.story_date);

            convertView.setTag(holder);
        } else {
            holder = (ChildrenHolder) convertView.getTag();
        }

        StoryData data = (StoryData) mData.get(groupPosition).get(childPosition);
        mPresenter.setImage(mContext, data.getPath(), data.getUrls().get(0), holder.image);
        holder.title.setText(data.getTitle());
        holder.date.setText(mPresenter.convertDate(data.getDate(), true));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    @Override
    public void setPresenter(StoryListAdapterContractor.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void updateDate(ArrayList<ArrayList<StoryData>> list) {

    }

    public void setData(ArrayList<ArrayList<StoryData>> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void deleteChild(int headerPos, int childPos) {
        mData.get(headerPos).remove(childPos);
        mPresenter.deleteChild(headerPos, childPos);
    }

    private static class HeaderHolder {
        ImageView indicator;
        TextView text;
    }

    private static class ChildrenHolder {
        ImageView image;
        TextView title;
        TextView date;
    }
}
