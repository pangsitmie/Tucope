package com.roundbytes.myportfolio.introductions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.activity.WelcomeActivity;


public class Fragment_intro3 extends Fragment {
    Button nextBtn;
    ImageView previousBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_intro3, container , false);

        nextBtn = v.findViewById(R.id.nextBtn);
        previousBtn = v.findViewById(R.id.previousBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                getContext().startActivity(intent);
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_intro2());
                fragmentTransaction.commit();
            }
        });
        return v;
    }




}
