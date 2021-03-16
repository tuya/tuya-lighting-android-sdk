package com.example.light;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import com.tuya.smart.sdk.centralcontrol.api.ITuyaLightDevice;

public class LightWidget {
    private Context mContext;


    public View render(Context context) {
        mContext = context;
        View rootView =
                LayoutInflater.from(context).inflate(R.layout.light_view_func, null, false);
        initView(rootView);
        return rootView;

    }

    private void initView(View rootView) {
        rootView.findViewById(R.id.tvNewHome).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(mContext, LightDetailActivity.class);
            mContext.startActivity(intent);
        });

    }

}
