package combox.example.loonj.combox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    // 学生列表ListView
    MyListAdapter listAdapter;
    ArrayList<MyListItem> listData = new ArrayList();

    // 所有过滤条件
    AfComboBox.Option[] optionsA = new AfComboBox.Option[3];
    AfComboBox.Option[] optionsB = new AfComboBox.Option[4];

    // 当前过滤条件2020年3月14日，在哪里见过你
    AfComboBox.Option filter1, filter2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 过滤条件
        optionsA[0] = new AfComboBox.Option("性别:不限", "all", null);
        optionsA[1] = new AfComboBox.Option("男", "male", null);
        optionsA[2] = new AfComboBox.Option("女", "female", null);

        optionsB[0] = new AfComboBox.Option("年龄:不限", "all", null);
        optionsB[1] = new AfComboBox.Option("<18岁", "lt18", null);
        optionsB[2] = new AfComboBox.Option("18-20岁", "18-20", null);
        optionsB[3] = new AfComboBox.Option(">20岁", "gt20", null);

        // 默认过滤条件
        filter1 = optionsA[0];
        filter2 = optionsB[0];

        initListView();

        showFilters();
    }

    private void initListView()
    {
        // 初始化ListView
        listAdapter = new MyListAdapter();
        ListView listView = (ListView) findViewById(R.id.id_listview);
        listView.setAdapter(listAdapter); // 设置适配器

        initListData();
    }
    // 演示数据
    private void initListData()
    {
        listData.add(new MyListItem("赵",true, 17));
        listData.add(new MyListItem("钱",true, 24));
        listData.add(new MyListItem("孙",false, 22));
        listData.add(new MyListItem("李",true, 18));
        listData.add(new MyListItem("周",false, 19));
        listData.add(new MyListItem("吴",true, 19));
        listData.add(new MyListItem("郑",true, 21));
        listData.add(new MyListItem("王",true, 18));
    }

    // 显示当前过滤条件
    private void showFilters()
    {
        ((TextView)findViewById(R.id.id_filter1)).setText(filter1.name);
        ((TextView)findViewById(R.id.id_filter2)).setText(filter2.name);
        listAdapter.doFilter();
    }

    // 点击第1个查询条件
    public void changeFilter1(View view)
    {
        AfComboBox cbx = new AfComboBox();
        cbx.addOptions(optionsA);
        cbx.show(this, view, 0, 0);

        cbx.listener = new AfComboBox.OnItemClickedListener()
        {
            @Override
            public void OnItemClickedListener(AfComboBox.Option option)
            {
                filter1 = option;
                showFilters();
            }
        };
    }

    // 点击第2个查询条件
    public void changeFilter2(View view)
    {
        AfComboBox cbx = new AfComboBox();
        cbx.addOptions(optionsB);
        cbx.show(this, view, 0, 0);

        cbx.listener = new AfComboBox.OnItemClickedListener()
        {
            @Override
            public void OnItemClickedListener(AfComboBox.Option option)
            {
                filter2 = option;
                showFilters();
            }
        };
    }



    ///////////// 每一条记录的数据 /////////
    private static class MyListItem
    {
        public String name;
        public boolean sex;
        public int age;
        public MyListItem(){}
        public MyListItem(String name, boolean sex, int age)
        {
            this.name = name;
            this.sex = sex;
            this.age = age;
        }
    }

    //////////// 适配器 //////////////////
    private class MyListAdapter extends BaseAdapter
    {
        ArrayList<MyListItem> listCopy = new ArrayList();

        public MyListAdapter()
        {
        }

        // 条件过滤
        public void doFilter()
        {
            listCopy.clear();

            // 遍历listData里的所有数据项，查找符合条件的项，加到listCopy
            for(MyListItem it : listData)
            {
                // 过滤条件1：按性别
                if(filter1.value.equals("male"))
                {
                    if(it.sex != true) continue;
                }
                else if(filter1.value.equals("female"))
                {
                    if(it.sex == true) continue;
                }

                // 过滤条件2：按年龄
                if(filter2.value.equals("lt18"))
                {
                    if(it.age >= 18) continue;
                }
                else if(filter2.value.equals("18-20"))
                {
                    if(it.age<18 || it.age>20) continue;
                }
                else if(filter2.value.equals("gt20"))
                {
                    if(it.age <=20) continue;
                }

                // 符合条件, 则放到listCopy里
                listCopy.add(it);
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount()
        {
            return listCopy.size();
        }

        @Override
        public Object getItem(int position)
        {
            return listCopy.get(position);
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
                convertView = getLayoutInflater()
                        .inflate(R.layout.list_item_student, parent, false);
            }

            // 获取/显示数据
            MyListItem it = (MyListItem) getItem(position);
            ((TextView)convertView.findViewById(R.id.id_item_name)).setText(it.name);
            ((TextView)convertView.findViewById(R.id.id_item_sex)).setText(it.sex?"男":"女");
            ((TextView)convertView.findViewById(R.id.id_item_age)).setText(it.age + "岁");

            return convertView;
        }
    }

}