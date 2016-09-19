package com.cyborg.englishhelperr;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cyborg.englishhelper.R;


public class TestMenuFragment extends Fragment {

    private Button randomBut;
    private FloatBut floatBut;
    private Fragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_test_menu, container, false);


        randomBut = (Button) v.findViewById(R.id.randomButton);
        randomBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fragment = new TestFragment();
                    floatBut = (FloatBut) getActivity();
                    floatBut.trans(fragment);
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
            }
        });

        try {
            floatBut = (FloatBut) getActivity();
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        if (floatBut != null){
            floatBut.hideBut();
        }

        return v;
    }

    @Override
    public void onAttach(final Activity activity) {

        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
