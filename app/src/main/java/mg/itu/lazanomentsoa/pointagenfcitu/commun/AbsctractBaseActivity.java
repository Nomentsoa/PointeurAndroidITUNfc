package mg.itu.lazanomentsoa.pointagenfcitu.commun;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public abstract class AbsctractBaseActivity extends AppCompatActivity {
    protected LoadingDialogFragment loadingDialogFragment;
    protected FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = this.getSupportFragmentManager();
    }


    public void showLoading(boolean cancelable) {
        loadingDialogFragment = new LoadingDialogFragment();
        loadingDialogFragment.show(fragmentManager, Utils.createUniqueTag());
        loadingDialogFragment.setCancelable(cancelable);
    }

    public void dismissLoading() {
        loadingDialogFragment.dismiss();
    }
}
