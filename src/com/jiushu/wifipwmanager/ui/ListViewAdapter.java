package com.jiushu.wifipwmanager.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.jiushu.wifipwmanager.R;
import com.jiushu.wifipwmanager.data.WiFiNetWork;
import com.jiushu.wifipwmanager.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 13/11/15.
 */
public class ListViewAdapter extends BaseAdapter implements Filterable {
    private Context activityContext;

    private List<WiFiNetWork> sourceDatas, copySourceDatas;

    private MyFilter mFilter;

    public ListViewAdapter(List<WiFiNetWork> dataSource, Context context) {
        // TODO Auto-generated constructor stub

        this.sourceDatas = dataSource;
        copySourceDatas = dataSource;
        this.activityContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.sourceDatas.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        WiFiNetWork data = sourceDatas.get(position);

        ViewHolder holder;
        if (null == convertView) {
            //LayoutInflater inflater = ((Activity) activityContext).getLayoutInflater();
            //convertView = inflater.inflate(R.layout.listview_item, null);
            convertView = LayoutInflater.from(activityContext).inflate(R.layout.listview_item, null);

            holder = new ViewHolder();
            holder.ssidTV = (TextView) convertView.findViewById(R.id.main_listViewItem_SSID);
            holder.passWordTV = (TextView) convertView.findViewById(R.id.main_listViewItem_PassWord);
            holder.commentTV = (TextView) convertView.findViewById(R.id.main_listViewItem_Comment);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String ssid = data.getSsid();
        if (data.isChineseSsid())
            ssid = StringUtil.hexToStringUTF8(ssid);
        holder.ssidTV.setText(ssid);
        holder.passWordTV.setText("密码：" + data.getPsk());
        if (!StringUtil.isEmpty(data.getComment())) {
            holder.commentTV.setText("备注：" + data.getComment());
            holder.commentTV.setVisibility(View.VISIBLE);

        } else {
            holder.commentTV.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void updataView(ListView listView, int posi, String valStr) {
        if (listView == null) return;

        WiFiNetWork tempNetWork = sourceDatas.get(posi);
        tempNetWork.setComment(valStr);
        sourceDatas.set(posi, tempNetWork);

        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
            View view = listView.getChildAt(posi - visibleFirstPosi);
            ViewHolder holder = (ViewHolder) view.getTag();

            holder.commentTV.setText(valStr);
        }
    }

    @Override
    public Filter getFilter() {
        if (null == mFilter) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }

    private static class ViewHolder {
        private TextView ssidTV;
        private TextView passWordTV;
        private TextView commentTV;
    }


    // 自定义Filter类
    class MyFilter extends Filter {

        // 该方法在子线程中执行
        // 自定义过滤规则
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            List<WiFiNetWork> filterResultDatas = new ArrayList<>();
            String filterString = constraint.toString().trim()
                    .toLowerCase();

            // 如果搜索框内容为空，就恢复原始数据
            if (StringUtil.isEmpty(filterString)) {
                filterResultDatas = copySourceDatas;
            } else {
                // 过滤出新数据
                for (WiFiNetWork wiFiNetWork : copySourceDatas) {
                    if (-1 != wiFiNetWork.getSsid().toLowerCase().indexOf(filterString)) {
                        filterResultDatas.add(wiFiNetWork);
                    }
                }
            }

            results.values = filterResultDatas;
            results.count = filterResultDatas.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            sourceDatas = (List<WiFiNetWork>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}
