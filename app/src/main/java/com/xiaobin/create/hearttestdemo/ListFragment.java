package com.xiaobin.create.hearttestdemo;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class ListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private Cursor mCursor = null;
    private HeartCursorAdapter adapter;
    private ListView mListView;    //The fragment's ListView

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCursor = getActivity().getContentResolver().query(Uri.parse("content://heartContentProvider/heart"),null,null,null,null);
        if(mCursor!=null)
        getActivity().startManagingCursor(mCursor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        //mCursor = getActivity().getContentResolver().query(Uri.parse("content://heartContentProvider/heart"),null,null,null,null);
        Log.d("liao","--mCursor--"+mCursor+"----");
        mListView = (ListView) view.findViewById(R.id.list_view);
        if(mCursor!=null) {
            adapter = new HeartCursorAdapter(getActivity(), mCursor, true);
            mListView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mCursor!=null) {
            getActivity().stopManagingCursor(mCursor);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
