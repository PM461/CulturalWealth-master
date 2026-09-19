package it.uniba.dib.sms222316;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import it.uniba.dib.sms222316.Gameplay.Game;
import it.uniba.dib.sms222316.Gameplay.Player;

public class TurnDialog extends DialogFragment {
    private Game game;
    private TextView playerNameTextView;
    private TextView playerScoreTextView;

    public TurnDialog(Game game) {
        this.game = game;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_game, container, false);

        playerNameTextView = view.findViewById(R.id.name1);
        playerScoreTextView = view.findViewById(R.id.money1);

        updateUI();

        return view;
    }

    private void updateUI() {
        Player currentPlayer = game.getCurrentPlayer();
        playerNameTextView.setText(currentPlayer.getName());
        playerScoreTextView.setText(String.valueOf(currentPlayer.getScore()));
    }
}

