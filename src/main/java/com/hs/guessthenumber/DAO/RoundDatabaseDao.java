package com.hs.guessthenumber.DAO;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.hs.guessthenumber.ENTITY.Game;
import com.hs.guessthenumber.ENTITY.Rounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Profile("database")
public class RoundDatabaseDao implements RoundDao {

    private final JdbcTemplate jdbc;

    @Autowired
    public RoundDatabaseDao(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }


    //Adds a round to the database while leaving game status as in-progress
    @Override
    @Transactional
    public Rounds addLosingRound(Rounds round) {
        return addRound(round);
    }


    //Adds a round to the database and updates game status to finished in one
    @Override
    @Transactional
    public Rounds addWinningRound(Rounds round){
        final String UPDATE_GAME_TO_FINISHED = "UPDATE game " +
                "SET status = ? " +
                "WHERE gameId = ?;";
        final String FINISHED_STATUS = "Finished";
        jdbc.update(UPDATE_GAME_TO_FINISHED,
                FINISHED_STATUS,
                round.getGameId());
        return addRound(round);
    }

    //Returns a list of all rounds in DB for a given game
    @Override
    public List<Rounds> getRounds(Game game) {
        final String GET_GAME_ROUNDS = "SELECT roundId, gameId, guess, guessTime, guessResult "
                + "FROM gameround "
                + "WHERE gameId = ? "
                + "ORDER BY guessTime;";
        return jdbc.query(GET_GAME_ROUNDS, new RoundMapper(), game.getGameId());
    }

    private static final class RoundMapper implements RowMapper<Rounds>{

        @Override
        public Rounds mapRow(ResultSet rs, int index) throws SQLException{
            Rounds round = new Rounds();
            round.setGameId(rs.getInt("gameId"));
            round.setRoundId(rs.getInt("roundId"));
            round.setGuess(rs.getString("guess"));
            round.setGuessResult(rs.getString("guessResult"));
            round.setGuessTime(rs.getTime("guessTime").toLocalTime());
            return round;
        }
    }


    //Adds a round to the database after getting the next valid round ID for the game.
    private Rounds addRound(Rounds round){
        final String GET_NEXT_GAME_ROUND_ID = "SELECT IFNULL(MAX(roundId)+1, 1) " +
                "FROM gameround " +
                "WHERE gameId = ?;";
        int roundId = jdbc.queryForObject(GET_NEXT_GAME_ROUND_ID, Integer.class, round.getGameId());
        round.setRoundId(roundId);

        final String INSERT_ROUND = "INSERT INTO gameround(roundId, gameId, guess, guessTime, guessResult) "
                + "VALUES (?, ?, ?, ?, ?);";

        jdbc.update(INSERT_ROUND,
                round.getRoundId(),
                round.getGameId(),
                round.getGuess(),
                round.getGuessTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                round.getGuessResult());

        return round;
    }
}
