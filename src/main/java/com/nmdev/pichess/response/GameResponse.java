package com.nmdev.pichess.response;
import com.nmdev.pichess.model.Game;

import lombok.Data;

@Data
public class GameResponse {
	
    private String message;
    private String id;
    private String fen;
    private String userColor; 
    private boolean status;
    
    /**
     * GameResponse constructor
     * @param game
     */
    public GameResponse(Game game) {
    	setId(game.getId());
    	setFen(game.getFen());
    	setUserColor(game.getWhiteUser() != null ? "white" : "black");
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isStatus() {
        return status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
	public String getFen() {
		return fen;
	}
	
	public void setFen(String fen) {
		this.fen = fen;
	}
	
	public String getUserColor() {
		return userColor;
	}
	
	public void setUserColor(String userColor) {
		this.userColor = userColor;
	}
	
}