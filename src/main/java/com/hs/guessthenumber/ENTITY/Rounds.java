package com.hs.guessthenumber.ENTITY;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode
public class Rounds {

    private int roundId;
    private int gameId;
    private String guess;
    private LocalTime guessTime;
    private String guessResult;

}
