package com.hs.guessthenumber.DAO;



import com.hs.guessthenumber.ENTITY.Game;
import com.hs.guessthenumber.ENTITY.Rounds;

import java.util.List;

public interface RoundDao {

    Rounds addLosingRound(Rounds round);

    Rounds addWinningRound(Rounds round);

    List<Rounds> getRounds(Game game);

}
