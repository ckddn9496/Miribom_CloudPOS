package com.example.cloudpos.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cloudpos.R;
import com.example.cloudpos.adapters.MenuListViewAdapter;
import com.example.cloudpos.child_fragments.Cfragment_menu_add;
import com.example.cloudpos.child_fragments.Cfragment_menu_edit;
import com.example.cloudpos.child_fragments.Cfragment_menu_verify;
import com.example.cloudpos.connections.DeleteMenuTask;
import com.example.cloudpos.data.MenuItem;
import com.example.cloudpos.data.MenuList;

import java.util.ArrayList;

/*메뉴 관리 Fragment*/
//implemented by Yang Insu


//메뉴 등록-편집-삭제 기능 여기서 관리
//이 Fragment 안에서 조회/편집/추가를 할 수 있도록 또 내부 Fragment를 만들었음
//이중으로 Fragment가 쌓이는 것을 관리하도록 setChildFragment 함수 구현함

public class FragmentMenu extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, Cfragment_menu_add.AddUpdater {
    private final static String TAG = "FragmentMenu>>";
    ListView listview;
    MenuListViewAdapter adapter;
    ArrayList<MenuItem> menuItems = MenuList.getInstance().menuItemArrayList;

    /*뷰 위젯 Reference*/
    EditText editTextFilter; //TODO: VER 1.1 filter로 검색 가능하게 업그레이드
    Button menu_add_btn;
    Button menu_del_btn;
    Button menu_edit_btn;


    public interface Transmitter {
        void transmit(Bundle bundle);
    }

    static Transmitter transmitter;

    public void setTransmitter(Transmitter transmitter) {
        this.transmitter = transmitter;
    }

    public static FragmentMenu newInstance() {
        return new FragmentMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view1 = inflater.inflate(R.layout.menu_fragment, container, false);
        listview = (ListView) view1.findViewById(R.id.menu_listview);

        adapter = new MenuListViewAdapter();
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(this); //아이템 클릭할때 이벤트 처리

        //뷰 컴포넌트 연결
        menu_add_btn = (Button) view1.findViewById(R.id.menu_add_btn);
        menu_add_btn.setOnClickListener(this);
        menu_del_btn = (Button) view1.findViewById(R.id.menu_del_btn);
        menu_del_btn.setOnClickListener(this);
        menu_edit_btn = (Button) view1.findViewById(R.id.menu_edit_btn);
        menu_edit_btn.setOnClickListener(this);


        return view1;
    }


    /*각 버튼을 누를 때 발생하는 이벤트 처리*/
    @Override
    public void onClick(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.menu_edit_btn: //수정 누르면 여기서 처리
                fragment = Cfragment_menu_edit.newInstance();
                setChildFragment(fragment);
                break;
            case R.id.menu_add_btn: //추가 버튼
                fragment = Cfragment_menu_add.newInstance();
                setChildFragment(fragment);
                break;
            case R.id.menu_del_btn: //메뉴 삭제 시

                //TODO for Yang: 삭제 2단계 확인하는 Dialog 생성하기

                Toast.makeText(getContext(), "삭제버튼 누름", Toast.LENGTH_SHORT).show();
                SparseBooleanArray checkedItems = listview.getCheckedItemPositions();

                Log.d(TAG, "onClick: " + checkedItems.toString());

                int count = adapter.getCount();
                for (int i = count - 1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        DeleteMenuTask deleteMenuTask = new DeleteMenuTask(adapter, i, getContext());
                        deleteMenuTask.execute();
//                        adapter.delItem(i);
                    }
                }
                listview.clearChoices();
                adapter.notifyDataSetChanged();

        }

    }

    /*메뉴 리스트 아이템들을 누를 때 발생하는 이벤트 처리 모듈*/
    //이 안에서 또 Cfragment_menu_verify 자녀 fragment를 생성함

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String menuNo = ((MenuItem) adapter.getItem(position)).getMenuNo();
        String menuCode = ((MenuItem) adapter.getItem(position)).getMenuCode();
        final String menuName = ((MenuItem) adapter.getItem(position)).getMenuName();
        String menuImageUrl = ((MenuItem) adapter.getItem(position)).getImageUrl();
        String menuPrice = ((MenuItem) adapter.getItem(position)).getMenuPrice();

        Bundle bundle = new Bundle(); //번들 안에 상품에 대한 정보를 넣고
        bundle.putString("menuNo", menuNo);
        bundle.putString("menuCode", menuCode);
        bundle.putString("menuName", menuName);
        bundle.putString("menuPrice", menuPrice);
        bundle.putString("menuImageUrl", menuImageUrl);
        bundle.putInt("index", position);


        Cfragment_menu_verify fragment = Cfragment_menu_verify.newInstance();
        fragment.transmit(bundle); //정보가 담긴 번들을 자녀 fragment로 넘겨준다 (transmit 함수는 자녀 fragment에서 implment함)
        setChildFragment(fragment);
    }


    //Fragment 스택을 관리하는 함수
    /*isAdded함수로 menuFragment안에 Fragment가 있는 지 확인*/
    //버튼 Transaction으로 다른 fragment가 생길때마다 스택에 쌓이게 한다
    private void setChildFragment(Fragment childFragment) {
        FragmentTransaction childFt = getChildFragmentManager().beginTransaction();
        if (!childFragment.isAdded()) {
            childFt.replace(R.id.menu_parent_fragment_container, childFragment);
            childFt.addToBackStack(null);
            childFt.commit();
        }
    }

    @Override
    public void update(MenuItem item) {
        int count;
        count = adapter.getCount();
        menuItems.add(item);
//        adapter.addItem(item.getMenuNo(),item.getMenuCode(),item.getMenuName(),item.getMenuPrice());
        adapter.notifyDataSetChanged();
    }


}
