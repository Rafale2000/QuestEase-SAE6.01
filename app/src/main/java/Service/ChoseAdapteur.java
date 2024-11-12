package Service;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



import android.view.View;

import com.example.questease.Model.BDD.ChoseATrouverPrixJuste;

public class ChoseAdapteur extends RecyclerView.Adapter<ChoseAdapteur.LobbyViewHolder> {


    private List<ChoseATrouverPrixJuste> choseList;

    public ChoseAdapteur(List<ChoseATrouverPrixJuste> choseList) {
        this.choseList = choseList;
    }

    @NonNull
    @Override
    public LobbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new LobbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LobbyViewHolder holder, int position) {
        ChoseATrouverPrixJuste chose = choseList.get(position);

        // Log data being bound to the view holder
        Log.d("ziz","zizi");
        Log.d("onBindViewHolder", "Mot: " + chose.getNom() + ", Diff: " + chose.getValeur());

        holder.lobbyName.setText(chose.getValeur());
        holder.lobbyPlayer.setText(chose.getNom());
    }

    @Override
    public int getItemCount() {
        return choseList.size();
    }
    //TODO Modifier ici + comprendre comment ça marche
    public static class LobbyViewHolder extends RecyclerView.ViewHolder {
        TextView lobbyName;
        TextView lobbyPlayer;

        public LobbyViewHolder(@NonNull View itemView) {
            super(itemView);
            lobbyName = itemView.findViewById(android.R.id.text1);
            lobbyPlayer = itemView.findViewById(android.R.id.text2);
        }
    }
}