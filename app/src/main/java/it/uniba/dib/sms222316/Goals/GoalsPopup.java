package it.uniba.dib.sms222316.Goals;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import it.uniba.dib.sms222316.R;

public class GoalsPopup extends Dialog {

    private final String[] titles = new String[]{getContext().getString(R.string.achievements), getContext().getString(R.string.missions)};
    ViewPager2 pager;
    TabLayout tabLayout;
    ViewPagerFragmentAdapter adapter;
    public GoalsPopup(Context context) {
        super(context);
        context.setTheme(R.style.CustomDialogStyle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.goals_popup);

        pager = findViewById(R.id.viewpager);
        pager.setUserInputEnabled(false);
        pager.setClipToOutline(true);
        tabLayout = findViewById(R.id.tabLayout);

        adapter = new ViewPagerFragmentAdapter((FragmentActivity) context, titles);
        pager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, pager, ((tab, position) -> tab.setText(titles[position]))).attach();
    }
}