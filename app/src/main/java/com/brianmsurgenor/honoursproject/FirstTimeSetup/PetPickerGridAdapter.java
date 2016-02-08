package com.brianmsurgenor.honoursproject.FirstTimeSetup;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian on 09/12/2015.
 */
public class PetPickerGridAdapter extends RecyclerView.Adapter<PetPickerGridAdapter.ViewHolder> {

    private List<Integer> mItems;
    private List<LinearLayout> backgroundList;
    private Context mContext;

    public PetPickerGridAdapter(ContentResolver contentResolver, Context context) {
        super();
        mItems = new ArrayList<>();
        backgroundList = new ArrayList<>();
        mContext = context;

        //Add items to mItems
        mItems.add(R.drawable.frog); // graphics would be named pet1/pet2 etc in future
        mItems.add(R.drawable.ic_trophy_winner);
        mItems.add(R.drawable.ic_trophy_loser);
        mItems.add(R.drawable.pets10);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picker_adapter, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final int id = mItems.get(i);
        viewHolder.imgThumbnail.setImageResource(id);
        backgroundList.add(viewHolder.petBackground);

        viewHolder.petHolder.setOnClickListener(new View.OnClickListener() {
            boolean selected = false;

            @Override
            public void onClick(View v) {

                if(!selected){
                    selected = true;
                    unselectAll();
                    viewHolder.petBackground.setBackgroundColor(Color.YELLOW);

                    Toast.makeText(mContext.getApplicationContext(), id + "Pet celebration graphic to be " +
                            "played", Toast.LENGTH_SHORT).show();

                    SetupPetActivity.petSelected("" + id);
                } else {
                    selected = false;
                    SetupPetActivity.petSelected(null);
                    viewHolder.petBackground.setBackgroundColor(mContext.getResources().
                            getColor(R.color.primaryBackground));
                }

            }
        });
    }

    private void unselectAll() {
        for (LinearLayout l: backgroundList) {
            l.setBackgroundColor(mContext.getResources().getColor(R.color.primaryBackground));
        }
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public CardView petHolder;
        public LinearLayout petBackground;

        public ViewHolder(View itemView) {
            super(itemView);
            petHolder = (CardView) itemView.findViewById(R.id.item_CardView);
            petBackground = (LinearLayout) itemView.findViewById(R.id.linearItem);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.itemImage);
        }
    }
}
