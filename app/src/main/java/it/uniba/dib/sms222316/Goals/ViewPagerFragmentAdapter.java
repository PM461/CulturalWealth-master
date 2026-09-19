package it.uniba.dib.sms222316.Goals;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private final String[] titles;
    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity, String[] titles) {
        super(fragmentActivity);
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentAchievements();
            case 1:
                return new FragmentMissions();
        }
        return new FragmentAchievements();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
