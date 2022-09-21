package com.hs.guessthenumber.CONTROLLER;


import java.util.List;

import com.hs.guessthenumber.ENTITY.Game;
import com.hs.guessthenumber.ENTITY.Rounds;
import com.hs.guessthenumber.SERVICE.GuessTheNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/Gameapi")
public class GuessTheNumberController {

    private final GuessTheNumberService service;

    @Autowired
    public GuessTheNumberController(GuessTheNumberService service){
        this.service = service;
    }

    //Starts game, generates an answer, sets status to "In Progress"
    @PostMapping("/begin")
    public ResponseEntity<Integer> beginGame(){
        return new ResponseEntity<>(service.createAndAddGame(), HttpStatus.CREATED);
    }

    //Makes a guess by passing guess and gameId in as JSON
    //Calculates results of guess and marks game finished if correct
    @PostMapping("/guess")
    public ResponseEntity<Rounds> makeGuess(@RequestBody Rounds guess){
        Game game = service.getGame(guess.getGameId()); // INPUT A 4 DIGIT NUMBER
        Rounds round = service.submitGuess(guess.getGuess(), game);
        return ResponseEntity.ok(round);
    }
// Example JSON guess entry
//    {
//        "gameId": 8,
//            "guess": 9364
//
//    }


    //Gets a list of all Games, covering answers for in-progress games
    @GetMapping("/game")
    public List<Game> getAllGames(){
        List<Game> games = service.getAllGames();
        games.forEach(this::hideInProgressAnswers);
        return games;
    }

    //Gets a specific game based on its ID
    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable int gameId){
        Game game = service.getGame(gameId);
        hideInProgressAnswers(game);
        return ResponseEntity.ok(game);
    }

    //Gets a list of rounds for the specified game, sorted by time
    @GetMapping("/rounds/{gameId}")
    public ResponseEntity<List<Rounds>> getAllRoundsByGameId(
            @PathVariable int gameId){

        Game game = service.getGame(gameId);
        List<Rounds> rounds = service.getRounds(game);
        return ResponseEntity.ok(rounds);

    }

    //Sets game's answer to "Hidden" if status is "In Progress"
    private void hideInProgressAnswers(Game game){
        if (game.getStatus().equals("In Progress")){
            game.setAnswer("Hidden");
        }
    }

}
