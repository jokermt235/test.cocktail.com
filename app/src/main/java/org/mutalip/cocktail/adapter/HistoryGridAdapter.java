package org.mutalip.cocktail.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mutalip.cocktail.R;
import org.mutalip.cocktail.model.DrinkRepo;
import org.mutalip.cocktail.model.IDrink;

public class HistoryGridAdapter extends RecyclerView.Adapter<HistoryGridAdapter.ViewHolder> {
    private JSONArray items;
    private Context context;
    private static String TAG = "HistoryGridAdapter";
    public HistoryGridAdapter(Context context , JSONArray items){
        this.items = items;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view , this);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            JSONObject item = items.getJSONObject(position);
            Log.d(TAG, item.toString());
            try {
                holder.imageDrink.setImageBitmap(BitmapFactory.decodeFile(item.getString("strDrinkThumb")));
                holder.strDrink.setText(item.getString("idDrink"));
            }catch (Exception e){
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "Item count "  + String.valueOf(items.length()));
        return this.items.length();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , IDrink {
        public ImageView imageDrink;
        public Button removeButton;
        public TextView strDrink;
        public HistoryGridAdapter adapter;
        public ViewHolder(View view, HistoryGridAdapter adapter) {
            super(view);
            this.adapter = adapter;
            imageDrink = view.findViewById(R.id.historyImage);
            removeButton = view.findViewById(R.id.historyDelete);
            removeButton.setOnClickListener(this);
            strDrink     = view.findViewById(R.id.historyIdDrink);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.historyDelete: {
                    new DrinkRepo(this).delete((String) strDrink.getText());
                }
            }
        }

        @Override
        public void getData(JSONArray data) {
            items = data;
            adapter.notifyDataSetChanged();
        }

        @Override
        public void failure(String message) {

        }
    }
}
