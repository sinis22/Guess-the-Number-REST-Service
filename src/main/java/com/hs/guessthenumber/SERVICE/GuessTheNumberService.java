package com.hs.guessthenumber.SERVICE;


import com.hs.guessthenumber.ENTITY.Game;
import com.hs.guessthenumber.ENTITY.Rounds;
import java.util.List;


public interface GuessTheNumberService {

    int createAndAddGame();

    Rounds submitGuess(String guess, Game game);

    List<Game> getAllGames();

    Game getGame(int gameId);

    List<Rounds> getRounds(Game game);

}
