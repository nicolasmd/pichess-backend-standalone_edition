package com.nmdev.pichess.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "game")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Game {
	@Id
    private String id;
	private String fen;
	private boolean active;	
	private String result;
	private String whiteUser;
	private String blackUser;
	private Integer whiteElo;
	private Integer blackElo;
	
	
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getWhiteUser() {
		return whiteUser;
	}
	public void setWhiteUser(String whiteUser) {
		this.whiteUser = whiteUser;
	}
	public String getBlackUser() {
		return blackUser;
	}
	public void setBlackUser(String blackUser) {
		this.blackUser = blackUser;
	}
	public Integer getWhiteElo() {
		return whiteElo;
	}
	public void setWhiteElo(Integer whiteElo) {
		this.whiteElo = whiteElo;
	}
	public Integer getBlackElo() {
		return blackElo;
	}
	public void setBlackElo(Integer blackElo) {
		this.blackElo = blackElo;
	}
	
}
