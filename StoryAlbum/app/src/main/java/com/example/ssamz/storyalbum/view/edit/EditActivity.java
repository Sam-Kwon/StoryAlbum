package com.example.ssamz.storyalbum.view.edit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.ssamz.storyalbum.R;
import com.example.ssamz.storyalbum.adapter.ImageListAdapter;
import com.example.ssamz.storyalbum.adapter.ImageListAdapterContractor;
import com.example.ssamz.storyalbum.data.StoryData;
import com.example.ssamz.storyalbum.view.RecycleImageView;
import com.example.ssamz.storyalbum.view.list.StoryListActivity;

public class EditActivity extends AppCompatActivity implements EditContractor.View{
    private final static String TAG = EditActivity.class.getSimpleName();

    public final static String MODE_EDIT = "MODE_EDIT";
    boolean mEditMode = true;

    private EditContractor.Presenter mPresenter;
    private EditContractor.Model mModel;

    private ImageListAdapterContractor.Presenter mAdapterPresenter;
    private ImageListAdapterContractor.Model mAdapterModel;

    CreateFragment mCreateFragment;
    EditFragment mEditFragment;

    RecyclerView mListView;
    ImageListAdapter mAdapter;

    StoryData mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);

        mCreateFragment = new CreateFragment();
        mEditFragment = new EditFragment();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mListView = (RecyclerView) findViewById(R.id.list_image);
        mListView.setLayoutManager(manager);

        mPresenter = new EditPresenter();
        mModel = new EditModel();

        mPresenter.setView(this);
        mPresenter.setModel(mModel);

        mAdapterPresenter = (EditPresenter)mPresenter;
        mAdapterPresenter.setModel((EditModel)mModel);

        getSupportFragmentManager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mData = mPresenter.getData();

        Intent intent = getIntent();
        mEditMode = intent.getBooleanExtra(MODE_EDIT, true);
        setToolbarTitle(mEditMode);

        mAdapter = new ImageListAdapter(this, mData.getUrls(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file = null;
                if (v instanceof RecycleImageView) {
                    file = ((RecycleImageView) v).getPath();
                    showPopup(file);
                }
            }
        });
        mAdapter.setPresenter((EditPresenter)mPresenter);
        mListView.setAdapter(mAdapter);

        Fragment selected = null;
        if (mEditMode == true) {
            selected = mEditFragment;
        }else {
            selected = mCreateFragment;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected).commit();

        TextView title = (TextView)findViewById(R.id.text_album_size);
        String text = mData.getImageSize() + getResources().getString(R.string.text_story_size);
        title.setText(text);
    }

    private void showPopup(String file) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_image, null, false);

        final PopupWindow window = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView image = (ImageView)layout.findViewById(R.id.image_full);

        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOutsideTouchable(true);

        mAdapterPresenter.setImage(this, file, image);

        window.showAtLocation(layout, Gravity.CENTER, 0, 0);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (mEditMode == true) {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_create, menu);
        }

        EditText editTitle = (EditText) findViewById(R.id.text_title);
        editTitle.setText(mData.getTitle());
        EditText editMemo = (EditText) findViewById(R.id.text_memo);
        editMemo.setText(mData.getMemo());

        if (mEditMode == true) {
            TextView textDate = (TextView) findViewById(R.id.text_date);
            textDate.setText(mPresenter.convertDate(mPresenter.getData().getDate(), true) + "에 수정됨");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit: {
                EditText editTitle = (EditText) findViewById(R.id.text_title);
                String title = null;
                if (editTitle != null) {
                    title = editTitle.getText().toString();
                }

                EditText editMemo = (EditText) findViewById(R.id.text_memo);
                String memo = null;
                if (editMemo != null) {
                    memo = editMemo.getText().toString();
                }

                Intent intent = new Intent(this, StoryListActivity.class);
                mPresenter.updateData(title, memo);
                startActivity(intent);
            }
                return true;

            case R.id.action_create: {
                EditText editTitle = (EditText) findViewById(R.id.text_title);
                String title = null;
                if (editTitle != null) {
                    title = editTitle.getText().toString();
                }

                EditText editMemo = (EditText) findViewById(R.id.text_memo);
                String memo = null;
                if (editMemo != null) {
                    memo = editMemo.getText().toString();
                }

                Intent intent = new Intent(this, StoryListActivity.class);
                mModel.updateData(title, memo);
                startActivity(intent);
            }
                return true;

            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void setToolbarTitle(boolean editMode) {
        if (editMode == true) {
            if (mData != null)
                getSupportActionBar().setTitle(mData.getTitle());
        }
        else {
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_create));
        }
    }
}
