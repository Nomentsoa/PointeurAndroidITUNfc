package mg.itu.lazanomentsoa.pointagenfcitu.commun;

import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import mg.itu.lazanomentsoa.pointagenfcitu.viewmodels.PointageViewModel;

public abstract class AbsctractBaseActivity extends AppCompatActivity {
    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";
    public NfcAdapter nfcAdapter;
    public PendingIntent pendingIntent;
    public IntentFilter writeTagFilters[];
    public boolean writeMode;
    public Tag myTag;
    public Context context;


    protected LoadingDialogFragment loadingDialogFragment;
    protected FragmentManager fragmentManager;

    public PointageViewModel pointageViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = this.getSupportFragmentManager();

        // recuperation du view model pointageViewModel
        pointageViewModel = ViewModelProviders.of(this).get(PointageViewModel.class);
        pointageViewModel.init();

    }


    public void showLoading(boolean cancelable) {
        loadingDialogFragment = new LoadingDialogFragment();
        loadingDialogFragment.show(fragmentManager, Utils.createUniqueTag());
        loadingDialogFragment.setCancelable(cancelable);
    }

    public void dismissLoading() {
        loadingDialogFragment.dismiss();
    }

    /**
     * activier l'ecriture
     */
    public void WriteModeOn() {
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    /**
     * desactiver l'ecriture
     */
    public void WriteModeOff() {
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

    /**
     * C'est pour la récupération du numero de serie de la carte
     * @param inarray
     * @return
     */
    public String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
