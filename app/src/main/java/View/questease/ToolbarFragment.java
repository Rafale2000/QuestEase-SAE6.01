package View.questease;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;

import com.example.questease.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToolbarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToolbarFragment extends Fragment {

    // Nom du paramètre à passer
    private static final String ARG_TITLE = "title";

    // Variable pour stocker le paramètre du titre
    private String mTitle;

    public ToolbarFragment() {
        // Required empty public constructor
    }

    /**
     * Utiliser cette méthode pour créer une nouvelle instance de
     * ToolbarFragment avec un paramètre titre.
     *
     * @param title Le titre à afficher dans la Toolbar.
     * @return Une nouvelle instance de ToolbarFragment.
     */
    public static ToolbarFragment newInstance(String title) {
        ToolbarFragment fragment = new ToolbarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);  // Récupère le titre passé
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate le layout pour ce fragment
        View view = inflater.inflate(R.layout.fragment_toolbar, container, false);

        // Accéder à la Toolbar et modifier le titre avec le paramètre passé
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (toolbar != null && mTitle != null) {
            toolbar.setTitle(mTitle);  // Utiliser le titre passé en paramètre
        }

        return view;
    }

// permet de retourner au menu principale ou le lobby de recherche
// en appuyant sur la fléche
    public void onBackButtonClicked(View view) {
        Intent intent;
        // si c'est dans le salon de la partie, retourner dans le salon des lobby
        if (getActivity() instanceof SplashActivity){
            intent = new Intent(requireActivity(), Searchlobby.class);
        }
        // sinon on revient au menu principale
        else {
            intent = new Intent(requireActivity(), MainActivity.class);
        }
        startActivity(intent);
        requireActivity().finish();
    }
}
