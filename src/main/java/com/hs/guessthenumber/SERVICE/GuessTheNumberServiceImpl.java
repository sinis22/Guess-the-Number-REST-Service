package com.hs.guessthenumber.SERVICE;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.hs.guessthenumber.DAO.GameDao;
import com.hs.guessthenumber.DAO.RoundDao;
import com.hs.guessthenumber.ENTITY.Game;
import com.hs.guessthenumber.ENTITY.Rounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GuessTheNumberServiceImpl implements GuessTheNumberService {

    private final GameDao gameDao;
    private final RoundDao roundDao;

    @Autowired
    public GuessTheNumberServiceImpl(GameDao gameDao, RoundDao roundDao){
        this.gameDao = gameDao;
        this.roundDao = roundDao;
    }

    @Override
    public int createAndAddGame() {
        Game game = new Game();
        game.setStatus("In Progress");
        game.setAnswer(generateAnswer());
        return gameDao.addGame(game);
    }

    @Override
    public Rounds submitGuess(String guess, Game game) {

        // Evaluate guess
        String guessResult = generateGuessResult(guess, game.getAnswer());

        // Create GameRound object
        Rounds round = new Rounds();
        round.setGameId(game.getGameId());
        round.setGuess(guess);
        round.setGuessResult(guessResult);
        round.setGuessTime(LocalTime.now().withNano(0));

        // Add GuessRound to DB and update Game if correct guess
        if (guessResult.equals("e:4:p:0")){
            return roundDao.addWinningRound(round);
        }
        else{
            return roundDao.addLosingRound(round);
        }
    }

    @Override
    public List<Game> getAllGames() {
        return gameDao.getAllGames();
    }

    @Override
    public Game getGame(int gameId) {
        return gameDao.getGame(gameId);
    }

    @Override
    public List<Rounds> getRounds(Game game) {
        return roundDao.getRounds(game);
    }


    //Generates an answer with 4 unique random digits
    private String generateAnswer() {
        List<Integer> availableNums = new ArrayList<>();
        for (int i=0; i<10; i++){
            availableNums.add(i);
        }
        String answer = "";
        Random rand = new Random();
        while (answer.length() < 4){
            answer += availableNums.remove(rand.nextInt(availableNums.size()));
        }
        return answer;
    }


    //Compares guess to answer, returning a result indicating number of exact
    //matches and number of partial matches in format "e:#:p:#"
    private String generateGuessResult(String guess, String answer) {

        int exactMatches = 0;
        int partialMatches = 0;

        for (int i=0; i<answer.length(); i++){
            if (guess.charAt(i) == answer.charAt(i)){
                exactMatches += 1;
            }
            else if (answer.contains(String.valueOf(guess.charAt(i)))){
                partialMatches += 1;
            }
        }

        return "e:" + exactMatches + ":p:" + partialMatches;
    }

}
