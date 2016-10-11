package com.marco.marplex.schoolbook.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marco.marplex.schoolbook.ArgumentActivity;
import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.models.Argument;

import java.util.ArrayList;

/**
 * Created by marco on 30/06/16.
 */

public class ArgumentsAdapter extends RecyclerView.Adapter<ArgumentsAdapter.ViewHolder>{

    private ArrayList<Argument> mList;
    private Context context;
    private Activity activity;

    public ArgumentsAdapter(ArrayList<Argument> mList, Context context, Activity activity) {
        this.mList = mList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.model_argument, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Argument argument = mList.get(position);

        holder.subject.setText(argument.subject);
        holder.icon.setImageResource(argument.iconID);

        Bitmap bitmap = ((BitmapDrawable)holder.icon.getDrawable()).getBitmap();
        Palette p = Palette.from(bitmap).generate();

        // Asynchronous
        final int[] color = new int[1];
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                color[0] = p.getVibrantColor(ContextCompat.getColor(context, R.color.colorPrimary));
                holder.card.setCardBackgroundColor(color[0]);
                holder.subject.setTextColor(Color.WHITE);
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ArgumentActivity.class);
                i.putExtra("argument", argument.subject);
                i.putExtra("icon", argument.iconID);
                i.putExtra("color", color[0]);
                i.putExtra("textColor", holder.subject.getCurrentTextColor());

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Pair<View, String> p1 = Pair.create(v, "argumentCard");
                    Pair<View, String> p2 = Pair.create((View)holder.subject, "argumentSubject");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(activity, p1, p2);

                    context.startActivity(i, options.toBundle());

                }else context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView subject;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.img_model_argument);
            subject = (TextView) itemView.findViewById(R.id.txt_model_argument);
            card = (CardView) itemView.findViewById(R.id.card_model_argument);
        }
    }

}
