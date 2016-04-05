package com.brianmsurgenor.honoursproject.FirstTimeSetup;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brianmsurgenor.honoursproject.R;
import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.BounceAnimation;

import java.util.ArrayList;

/**
 * Adapter to be used by the recyclerView in order to fill the view with the pet graphics
 */
public class PetPickerAdapter extends RecyclerView.Adapter<PetPickerAdapter.ViewHolder> {

    private ArrayList<Integer> petGraphics; //holds pet graphics
    private ArrayList<LinearLayout> backgroundList; //used to loop through the pet layouts
    private Context mContext; // used to get the resources in order to change pet background colour

    public PetPickerAdapter(Context context) {
        super();
        petGraphics = new ArrayList<>();
        backgroundList = new ArrayList<>();
        mContext = context;

        //Add the pet graphics to be used by the onBindViewHolder to fill the layouts
        petGraphics.add(R.drawable.frog);
        petGraphics.add(R.drawable.puppy);
        petGraphics.add(R.drawable.cat);
        petGraphics.add(R.drawable.turtle);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picker_adapter, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final int id = petGraphics.get(i);
        viewHolder.imgThumbnail.setImageResource(id);
        backgroundList.add(viewHolder.petBackground);

        //on click listener to mark the pet as selected and pass this info back to the setupActivity
        viewHolder.petHolder.setOnClickListener(new View.OnClickListener() {
            boolean selected = false;

            @Override
            public void onClick(View v) {

                /*
                 * If the item hasn't been selected then set it to selected and change colour
                 * and pass pet graphic back to the activity, otherwise reset colour and pass 0 back
                 * to cancel any previous selections
                 */
                if(!selected){
                    selected = true;
                    unselectAll();
                    viewHolder.petBackground.setBackgroundColor(Color.YELLOW);

                    new BounceAnimation(viewHolder.imgThumbnail)
                            .setNumOfBounces(3)
                            .setDuration(Animation.DURATION_SHORT)
                            .animate();

                    SetupUserActivity.petSelected(id);
                } else {
                    selected = false;
                    SetupUserActivity.petSelected(0);
                    viewHolder.petBackground.setBackgroundColor(mContext.getResources().
                            getColor(R.color.primaryBackground));
                }

            }
        });
    }

    /**
     * Loops through each pet background to change the colour to primary on a pet being deselected
     */
    private void unselectAll() {
        for (LinearLayout l: backgroundList) {
            l.setBackgroundColor(mContext.getResources().getColor(R.color.primaryBackground));
        }
    }

    @Override
    public int getItemCount() {
        return petGraphics.size();
    }

    //Viewholder used to define the view in which the pet graphic is held
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
