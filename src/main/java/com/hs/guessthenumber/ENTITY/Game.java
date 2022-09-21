package com.hs.guessthenumber.ENTITY;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Game {

    private int gameId;
    private String answer;
    private String status;
}
