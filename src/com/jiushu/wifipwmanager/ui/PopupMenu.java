package com.jiushu.wifipwmanager.ui;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jiushu.wifipwmanager.R;
import com.jiushu.wifipwmanager.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 19/11/15.
 */
public class PopupMenu {
    private Context mContext;
    private LayoutInflater mInflater;
    private WindowManager mWindowManager;

    private PopupWindow mPopupWindow;
    private View mContentView;
    private ListView mItemsView;
    private TextView mHeaderTitleView;
    private OnItemSelectedListener mListener;

    private List<PopupMenuItem> mItems;
    private int mWidth = 300;
    private float mScale;

    public PopupMenu(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mScale = metrics.scaledDensity;

        mItems = new ArrayList<>();

        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        setContentView(mInflater.inflate(R.layout.popup_menu, null));
    }

    /**
     * Sets the popup's content.
     *
     * @param contentView
     */
    private void setContentView(View contentView) {
        mContentView = contentView;
        mItemsView = (ListView) contentView.findViewById(R.id.items);
        mHeaderTitleView = (TextView) contentView.findViewById(R.id.header_title);

        mPopupWindow.setContentView(contentView);
    }

    /**
     * Add menu item.
     *
     * @param itemId
     * @param titleRes
     * @return item
     */
    public PopupMenuItem add(int itemId, int titleRes) {
        PopupMenuItem item = new PopupMenuItem();
        item.setItemId(itemId);
        item.setTitle(mContext.getString(titleRes));
        mItems.add(item);

        return item;
    }

    /**
     * Show popup menu.
     */
    public void show() {
        show(null);
    }

    /**
     * Show popup menu.
     *
     * @param anchor
     */
    public void show(View anchor) {

        if (mItems.size() == 0) {
            throw new IllegalStateException("PopupMenu#add was not called with a menu item to display.");
        }

        preShow();

        PopupMenuItemAdapter adapter = new PopupMenuItemAdapter(mContext, mItems);
        mItemsView.setAdapter(adapter);
        mItemsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if (mListener != null) {
                    mListener.onItemSelected(mItems.get(position));
                }
                mPopupWindow.dismiss();
            }
        });

        if (anchor == null) {
            View parent = ((Activity) mContext).getWindow().getDecorView();
            mPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            return;
        }

        int xPos, yPos;

        mContentView.setLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT));

        mContentView.measure(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        int mPopupWindowHeight = DensityUtil.dip2px(mContext, (mItems.size()+1) * 40);

        // Set x-coordinate to display the popup menu
        xPos = (screenWidth - mPopupWindow.getWidth()) / 2;
        yPos = (screenHeight - mPopupWindowHeight) / 2;

        mPopupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
    }

    private void preShow() {
        int width = (int) (mWidth * mScale);
        mPopupWindow.setWidth(width);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.panel_background));
    }

    /**
     * Dismiss the popup menu.
     */
    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * Sets the popup menu header's title.
     *
     * @param title
     */
    public void setHeaderTitle(CharSequence title) {
        mHeaderTitleView.setText(title);
        mHeaderTitleView.setVisibility(View.VISIBLE);
        mHeaderTitleView.requestFocus();
    }

    /**
     * Change the popup's width.
     *
     * @param width
     */
    public void setWidth(int width) {
        mWidth = width;
    }

    /**
     * Register a callback to be invoked when an item in this PopupMenu has
     * been selected.
     *
     * @param listener
     */
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when
     * an item in this PopupMenu has been selected.
     */
    public interface OnItemSelectedListener {
        public void onItemSelected(PopupMenuItem item);
    }

    static class ViewHolder {
        ImageView icon;
        TextView title;
    }

    private class PopupMenuItemAdapter extends ArrayAdapter<PopupMenuItem> {

        public PopupMenuItemAdapter(Context context, List<PopupMenuItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.menu_list_item, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.title = (TextView) convertView.findViewById(R.id.setcomment_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PopupMenuItem item = getItem(position);
            if (item.getIcon() != null) {
                holder.icon.setImageDrawable(item.getIcon());
                holder.icon.setVisibility(View.VISIBLE);
            } else {
                holder.icon.setVisibility(View.GONE);
            }
            holder.title.setText(item.getTitle());

            return convertView;
        }
    }
}
