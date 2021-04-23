package mg.itu.lazanomentsoa.pointagenfcitu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import mg.itu.lazanomentsoa.pointagenfcitu.R;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Employe;
import mg.itu.lazanomentsoa.pointagenfcitu.models.Tache;

public class CustomAdapterSpinnerEmploye extends BaseAdapter {
    private List<Employe> employeList;

    public CustomAdapterSpinnerEmploye(List<Employe> employes){
        this.employeList = employes;
    }
    @Override
    public int getCount() {
        if(this.employeList == null)
            return 0;
        return this.employeList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.employeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Employe employe =(Employe) getItem(position);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.item_spinner_employe, parent, false);
        TextView tvNomEmpoloye = (TextView)rowView.findViewById(R.id.tv_employe_name);
        TextView tvPosteEmploye = (TextView)rowView.findViewById(R.id.tv_employe_post);
        tvNomEmpoloye.setText(employe.getNom());
        tvPosteEmploye.setText("Poste: " + employe.getPoste());
        return rowView;
    }
}
