package bd.edu.seu.messengerapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import bd.edu.seu.messengerapp.fragment.CallsFragment;
import bd.edu.seu.messengerapp.fragment.ChatFragment;
import bd.edu.seu.messengerapp.fragment.StatusFragment;

public class FragmentAdapter extends FragmentPagerAdapter {


    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 1: return new StatusFragment() ;
           case 2: return new CallsFragment() ;
           case 0:
           default: return new ChatFragment();

       }
    }

    @Override
    public int getCount() {
//        number of fragment
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;
        if (position==0){
            title = "Chat";
        }else if (position == 1){
            title = "Status";
        }else if(position == 2){
            title = "Calls";
        }

        return title;
    }
}
