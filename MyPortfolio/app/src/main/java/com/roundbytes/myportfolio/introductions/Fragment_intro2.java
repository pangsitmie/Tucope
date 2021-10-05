package com.roundbytes.myportfolio.introductions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.roundbytes.myportfolio.R;


public class Fragment_intro2 extends Fragment {
    ImageView nextBtn;
    ImageView previousBtn;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_intro2, container , false);

        nextBtn = v.findViewById(R.id.nextBtn);
        previousBtn = v.findViewById(R.id.previousBtn);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_intro3());
                fragmentTransaction.commit();
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_intro1());
                fragmentTransaction.commit();
            }
        });
        return v;
    }




}
