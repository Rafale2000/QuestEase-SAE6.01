package Service;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.common.util.Log;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import android.view.View;
import com.example.questease.Model.BDD.MotCryptex;

public class PrixJusteAdapter extends RecyclerView.Adapter<PrixJusteAdapter.LobbyViewHolder> {

    private List<MotCryptex> motCryptexList;

    public PrixJusteAdapter(List<MotCryptex> motCryptexList) {
        this.motCryptexList = motCryptexList;
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
        MotCryptex motCryptex = motCryptexList.get(position);

        // Log data being bound to the view holder
        Log.e("onBindViewHolder", "Mot: " + motCryptex.getMot() + ", Diff: " + motCryptex.getDiff());

        holder.lobbyName.setText(motCryptex.getMot());
        holder.lobbyPlayer.setText(motCryptex.getDiff());
    }

    @Override
    public int getItemCount() {
        return motCryptexList.size();
    }

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
