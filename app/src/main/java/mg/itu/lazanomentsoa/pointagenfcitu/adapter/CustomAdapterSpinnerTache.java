package mg.itu.lazanomentsoa.pointagenfcitu.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import mg.itu.lazanomentsoa.pointagenfcitu.R;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Tache;

public class CustomAdapterSpinnerTache extends BaseAdapter {
    private List<Tache> listTache;

    public CustomAdapterSpinnerTache(List<Tache> taches){
        this.listTache = taches;
    }
    @Override
    public int getCount() {
        if(this.listTache == null)
            return 0;
        return this.listTache.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listTache.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tache tache =(Tache) getItem(position);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.item_spinner_tache, parent, false);
        TextView nomTache = (TextView)rowView.findViewById(R.id.tv_tache_name);
        nomTache.setText(tache.getNom());
        return rowView;
    }
}
