package com.example.nerita_hendra.i_fans;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    public static AccountFragment newInstance(){
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.action_account);
        if (currentFragment instanceof AccountFragment) {
            System.out.println("ASDASDASDSADASDS"+currentFragment);
        }
        FloatingActionButton mSharedFAB = ((HomeActivity) getActivity()).getFabBtn();
        mSharedFAB.show();
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

}
