package combox.example.loonj.combox;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;


public class AfComboBox
{
    final String TAG = "AfComboBox";

    // PopupWindow Jack 更新了16.30
	// 张三更新了 3月15日
    View contentView;
    PopupWindow popupWindow;

    // ListView
    LayoutInflater layoutInflater;
    MyListAdapter listAdapter;
    ArrayList<Option> listData = new ArrayList();


    public AfComboBox()
    {
    }


    // 添加下拉列表选项
    public void addOption(Option option)
    {
        listData.add(option);
    }
    public void addOptions(Option[] options)
    {
        for(Option opt: options)
        {
            listData.add (opt);
        }
    }

    // context, 所在Activity
    // anchor, 点中的View
    // xOff, yOff, 偏移量
    public void show(Context context, View anchor, int xOff, int yOff)
    {
        // 创建View
        layoutInflater = LayoutInflater.from(context);
        contentView = layoutInflater.inflate(R.layout.af_combobox, null);

        // 初始化窗口里的内容
        initContentView();

        // 创建PopupWindow, 宽度恒定，高度自适应
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(anchor, xOff, yOff);
    }

    // 初始化
    private void initContentView()
    {
        // 初始化ListView
        listAdapter = new MyListAdapter(); //通过listAdapter，af_combobox 和 af_combobox_item 建立包含关系
        ListView listView = (ListView) contentView.findViewById(R.id.id_listview);
        listView.setAdapter(listAdapter); // 设置适配器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Option it = (Option) listAdapter.getItem(position);
                onItemClicked(view, it);
            }
        });
    }

    // 回调
    public interface OnItemClickedListener
    {
        public void OnItemClickedListener(Option option);
    }
    public OnItemClickedListener listener;


    // 点击菜单项
    private void onItemClicked(View view, Option it)
    {
        // 关闭窗口
        popupWindow.dismiss();

        // 回调
        if(listener != null)
        {
            listener.OnItemClickedListener(it);
        }
    }


    ///////////// 每一条记录的数据 /////////
    public static class Option
    {
        public String name;     // 名字
        public String value;    // 值
        public Drawable icon;   // 图标
        public Option(){}
        public Option(String name, String value, Drawable icon)
        {
            this.name = name;
            this.value = value;
            this.icon = icon;
        }
    }

    //////////// 适配器 //////////////////
    private class MyListAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return listData.size();
        }

        @Override
        public Object getItem(int position)
        {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // 创建控件
            if (convertView == null)
            {
                // 使用外部类里的 layoutInflater;
                convertView = layoutInflater
                        .inflate(R.layout.af_combobox_item, parent, false);
            }

            // 获取/显示数据
            Option it = (Option) getItem(position);
            TextView textView = (TextView) convertView.findViewById(R.id.id_combobox_item_title);
            textView.setText(it.name);
            if(it.icon !=null)
            {
                // TextView左侧显示图标
                textView.setCompoundDrawables(it.icon, null, null, null);
            }
            return convertView;
        }
    }
}

