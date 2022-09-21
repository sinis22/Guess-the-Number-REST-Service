package com.hs.guessthenumber.DAO;

import com.hs.guessthenumber.ENTITY.Game;
import java.util.List;

public interface GameDao {

    int addGame(Game game);

    List<Game> getAllGames();

    Game getGame(int gameId);

}
