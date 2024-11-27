package com.nmdev.pichess.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.nmdev.pichess.SecurityConfig.JwtProvider;
import com.nmdev.pichess.model.Game;
import com.nmdev.pichess.model.User;
import com.nmdev.pichess.repository.GameRepository;
import com.nmdev.pichess.repository.UserRepository;
import com.nmdev.pichess.response.ApiResponse;
import com.nmdev.pichess.response.GameResponse;
import com.nmdev.pichess.service.GameService;
import com.nmdev.pichess.service.Stockfish;

@RestController
@RequestMapping("/game")
public class GameController {
	
	@Autowired
	private Stockfish stockfish;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private GameService gameService; 

	@PostMapping("/create")
    public ResponseEntity<GameResponse> createGameHandler(@RequestBody Map<String,String> map, Principal principal) throws Exception {
		String elo = map.get("elo");
		String color = map.get("color");
		User user = userRepository.findByEmail(principal.getName());
		
		boolean userHasActiveGame = hasActiveGame(user);
		if (userHasActiveGame) {
			throw new Exception("User has already an active game");
		}
		
		Game newGame = gameService.createNewGame(elo, color, user);
		GameResponse apiResponse = new GameResponse(newGame);
         return new ResponseEntity<GameResponse>(apiResponse, HttpStatus.OK);
    }
	
	
	@PostMapping("/play")
    public ResponseEntity<ApiResponse> playMoveHandler(@RequestBody Game gameRequest, Principal principal) throws Exception {
		// TODO check last move / fen consistency
		String fen = gameRequest.getFen();
		String gameId = gameRequest.getId();
		User user = userRepository.findByEmail(principal.getName());
		Game activeGame = gameRepository.getActiveGame(user.getId());
		if (!activeGame.getId().equals(gameId)) {
			throw new Exception("Game id error : " + activeGame.getId() + " / " + gameId);
		}
		String bestMove = gameService.play(activeGame, fen);
        ApiResponse apiResponse = new ApiResponse("bestMove", false);
        apiResponse.setMessage(bestMove);
        apiResponse.setStatus(true);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }
	
	
	private boolean hasActiveGame(User user) {
		Game hasActiveGame = gameRepository.getActiveGame(user.getId());
        return hasActiveGame != null;
	}
}
