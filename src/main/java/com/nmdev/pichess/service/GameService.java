package com.nmdev.pichess.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nmdev.pichess.model.Game;
import com.nmdev.pichess.model.User;
import com.nmdev.pichess.repository.GameRepository;

@Service
public class GameService {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private Stockfish stockfish;
	
	private static final Integer DEFAULT_ELO = 1200;
	private static final String INITIAL_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	
	
	/**
	 * New game creation
	 * @param elo
	 * @param color
	 * @param user
	 * @return Game
	 */
	public Game createNewGame(String elo, String color, User user) {
		Game createdGame = new Game();
		createdGame.setActive(true);
		switch (getColor(color)) {
			case "white": 
				createdGame.setBlackElo(Integer.valueOf(elo));
				createdGame.setWhiteElo(user.getElo());
				createdGame.setBlackUser(null);
				createdGame.setWhiteUser(user.getId());
				break;
			case "black": 
				createdGame.setWhiteElo(Integer.valueOf(elo));
				createdGame.setBlackElo(user.getElo());
				createdGame.setWhiteUser(null);
				createdGame.setBlackUser(user.getId());
				break;
		}	
		createdGame.setFen(INITIAL_FEN);
		Game savedGame = gameRepository.save(createdGame);
		gameRepository.save(savedGame);
		return savedGame;
	}
	
	/**
	 * Plays computer move and saves new position
	 * @param game
	 * @param fen
	 * @return
	 */
	public String play(Game game, String fen) {
		Integer elo = getComputerElo(game);
		stockfish.setStockfishElo(elo);
		String bestMove = stockfish.getBestMove(fen, elo);
		String newFen = stockfish.getNewFen(fen, bestMove);
		game.setFen(newFen);
		gameRepository.save(game);
		return bestMove;
	}
	
	/**
	 * Get color (white|black), randomize if not specified
	 * @param color
	 * @return
	 */
	private String getColor(String color) {
		if (color != "white" && color != "black") {
			Random randomNumbers = new Random();
			return randomNumbers.nextBoolean() ? "white" : "black";
		}
		return color;
	}
	
	/**
	 * Get computer elo (or default elo)
	 * @param game
	 * @return
	 */
	private Integer getComputerElo(Game game) {
		if (game.getWhiteUser() == null) {
			return game.getWhiteElo();
		}
		if (game.getBlackUser() == null) {
			return game.getBlackElo();
		}
		return DEFAULT_ELO;
	}

}
