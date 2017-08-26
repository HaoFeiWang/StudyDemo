package com.whf.decorationitem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private ArrayList<Person> personList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        initData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(new RecyclerViewAdapter(personList,this));

//        mRecyclerView.addItemDecoration(new SimpleDecoration(this));
//        mRecyclerView.addItemDecoration(new LabelDecoration());

//        mRecyclerView.addItemDecoration(new SectionDecoration(new DecorationCallback() {
//            @Override
//            public long getGroupId(int position) {
//                //将名字的首字母变为大写，转换成long类型，作为GroupID
//                return Character.toUpperCase(personList.get(position).getName().charAt(0));
//            }
//
//            @Override
//            public String getGroupFirstLine(int position) {
//                //将名字的首字母变为大写，作为当前组的名字
//                return personList.get(position).getName().substring(0, 1).toUpperCase();
//            }
//        }));

        mRecyclerView.addItemDecoration(new StickyHeadDecoration(new DecorationCallback() {
            @Override
            public long getGroupId(int position) {
                return Character.toUpperCase(personList.get(position).getName().charAt(0));
            }

            @Override
            public String getGroupFirstLine(int position) {
                return personList.get(position).getName().substring(0, 1).toUpperCase();
            }
        }));
    }

    public void initData(){
        Person person;
        person = new Person("AAA","18409201457");
        personList.add(person);
        person = new Person("ABC","18409201457");
        personList.add(person);
        person = new Person("ADE","18409201457");
        personList.add(person);
        person = new Person("AFG","18409201457");
        personList.add(person);
        person = new Person("BBB","15792012457");
        personList.add(person);
        person = new Person("BCD","15792012457");
        personList.add(person);
        person = new Person("BEF","15792012457");
        personList.add(person);
        person = new Person("BBQ","15792012457");
        personList.add(person);
        person = new Person("BBB","15792012457");
        personList.add(person);
        person = new Person("CCC","13409201457");
        personList.add(person);
        person = new Person("CDE","13409201457");
        personList.add(person);
        person = new Person("CFG","13409201457");
        personList.add(person);
        person = new Person("CHQ","13409201457");
        personList.add(person);
        person = new Person("CEE","13409201457");
        personList.add(person);
        person = new Person("DDD","18407121457");
        personList.add(person);
        person = new Person("GZZ","15609234567");
        personList.add(person);
        person = new Person("GHQ","18550966457");
        personList.add(person);
        person = new Person("GGG","125661101457");
        personList.add(person);
        person = new Person("GTF","125661101457");
        personList.add(person);
        person = new Person("HHH","198516498145");
        personList.add(person);
        person = new Person("HHH","198516498145");
        personList.add(person);
        person = new Person("HQA","184091654641");
        personList.add(person);
        person = new Person("HMN","184091654641");
        personList.add(person);
        person = new Person("HHH","198516498145");
        personList.add(person);
        person = new Person("HHH","198516498145");
        personList.add(person);
        person = new Person("HQA","184091654641");
        personList.add(person);
        person = new Person("HMN","184091654641");
        personList.add(person);
        person = new Person("HHH","198516498145");
        personList.add(person);
        person = new Person("HHH","198516498145");
        personList.add(person);
        person = new Person("HQA","184091654641");
        personList.add(person);
        person = new Person("HMN","184091654641");
        personList.add(person);
    }

}
