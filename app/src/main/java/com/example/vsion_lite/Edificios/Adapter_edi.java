package com.example.vsion_lite.Edificios;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vsion_lite.R;
import com.example.vsion_lite.Teacher.InfTeacher;

import java.util.ArrayList;
import java.util.List;

import static com.example.vsion_lite.Teacher.Valores.*;

public class Adapter_edi extends RecyclerView.Adapter<Adapter_edi.PersonViewHolder> implements Filterable {

    private Context  mContext;
    private List<InfTeacher> teacher;
    private List<InfTeacher> teacherFull;

    private OnItemClickListener mListener;


    public class PersonViewHolder extends RecyclerView.ViewHolder {
        public InfTeacher iTeacher;
        Context contex;
        CardView cv;
        ImageView personPhoto;
        TextView tname, temail, id, dispon, label;

        public PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            tname = (TextView) itemView.findViewById(R.id.tname);
            temail = (TextView) itemView.findViewById(R.id.temail);
            // location = (TextView) itemView.findViewById(R.id.location);
            personPhoto = (ImageView) itemView.findViewById(R.id.person_photo);
            id = (TextView) itemView.findViewById(R.id.id);
            dispon = (TextView) itemView.findViewById(R.id.dispon);
            label = (TextView) itemView.findViewById(R.id.contact);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public Adapter_edi(Context context, List<InfTeacher> lista){
        mContext = context;
        this.teacher = lista;
        this.teacherFull = new ArrayList<>(lista);
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card_teacher, viewGroup, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PersonViewHolder personViewHolder, final int i) {
        final InfTeacher currentItem = teacher.get(i);
        personViewHolder.iTeacher = currentItem;

        String name = currentItem.getName();
        String email = currentItem.getEmail();
        final String id = currentItem.getId();
        int imG = currentItem.getImg();
        String dispon = currentItem.getDispon();

        personViewHolder.tname.setText(name);
        personViewHolder.temail.setText(email);
        String idd = id;
        personViewHolder.id.setText(idd);
        personViewHolder.personPhoto.setImageResource(imG);
        personViewHolder.label.setText("Ubicación");
//        personViewHolder.location.setText(dispon);
        //Picasso.with(mContext).load(imG).fit().centerInside().into(personViewHolder.personPhoto);

        personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int position = i;
                    if (position != RecyclerView.NO_POSITION){
                        // mListener.onItemClick(position);

                         Toast.makeText(v.getContext(),id,Toast.LENGTH_SHORT).show();
/*
                        Intent detail = new Intent(v.getContext(), Information.class);
                        detail.putExtra(JSON_SONG, gson.toJson(personViewHolder.iTeacher));
                        v.getContext().startActivity(detail);//Inicia la actividad

*/
                    }
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return teacher.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<InfTeacher> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(teacherFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (InfTeacher item : teacherFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            teacher.clear();
            teacher.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



}
