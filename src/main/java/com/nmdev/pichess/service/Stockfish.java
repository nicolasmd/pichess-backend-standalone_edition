package com.nmdev.pichess.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.springframework.stereotype.Service;

@Service
public class Stockfish {
	
	private Process engineProcess;
	private BufferedReader processReader;
	private OutputStreamWriter processWriter;

	private static final String PATH = "/home/nm/Apps/stockfish/stockfish";

	/**
	 * Starts Stockfish engine as a process and initializes it
	 * 
	 * @param None
	 * @return True on success. False otherwise
	 */
	public boolean startEngine() {
		try {
			engineProcess = Runtime.getRuntime().exec(PATH);
			processReader = new BufferedReader(new InputStreamReader(
					engineProcess.getInputStream()));
			processWriter = new OutputStreamWriter(
					engineProcess.getOutputStream());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Takes in any valid UCI command and executes it
	 * 
	 * @param command
	 */
	public void sendCommand(String command) {
		try {
			processWriter.write(command + "\n");
			processWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is generally called right after 'sendCommand' for getting the raw
	 * output from Stockfish
	 * 
	 * @param waitTime
	 *            Time in milliseconds for which the function waits before
	 *            reading the output. Useful when a long running command is
	 *            executed
	 * @return Raw output from Stockfish
	 */
	public String getOutput(int waitTime) {
		StringBuffer buffer = new StringBuffer();
		try {
			Thread.sleep(waitTime);
			sendCommand("isready");
			while (true) {
				String text = processReader.readLine();
				if (text.equals("readyok"))
					break;
				else
					buffer.append(text + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("BUFFER : ");
		System.out.println(buffer.toString());
		return buffer.toString();
	}

	/**
	 * Returns the best move for a given position after
	 * calculating for 'waitTime' ms
	 * 
	 * @param fen
	 *            Position string
	 * @param waitTime
	 *            in milliseconds
	 * @return Best Move in PGN format
	 */
	public String getBestMove(String fen, int elo) {
		int waitTime = elo / 100;
		int depth = Math.round(elo / 400);
		System.out.println(fen);
		sendCommand("position fen " + fen);
		sendCommand("go depth " + depth + " movetime " + waitTime);
		return getOutput(waitTime + 50).split("bestmove ")[1].split(" ")[0].trim();
	}
	
	/**
	 * Get new Fen position after last move
	 * 
	 * @param fen
	 * @param move
	 * @return fen
	 */
	public String getNewFen(String fen, String move) {
		sendCommand("position fen " + fen + " moves " + move);
		sendCommand("d");
		return getOutput(0).split("Fen: ")[1];
	}

	/**
	 * Set UCI Stockfish Elo
	 * Below 1320 it is compensated by calculation time and depth
	 * 
	 * @param elo
	 */
	public void setStockfishElo(int elo) {
		int UCIElo = Math.max(1320, elo);
		sendCommand("setoption name UCI_LimitStrength value true");
		sendCommand("setoption name UCI_Elo value " + UCIElo);
	}
	
	/**
	 * Stops Stockfish and cleans up before closing it
	 */
	public void stopEngine() {
		try {
			sendCommand("quit");
			processReader.close();
			processWriter.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Get a list of all legal moves from the given position
	 * 
	 * @param fen
	 *            Position string
	 * @return String of moves
	 */
	public String getLegalMoves(String fen) {
		sendCommand("position fen " + fen);
		sendCommand("d");
		return getOutput(0).split("Legal moves: ")[1];
	}

	/**
	 * Draws the current state of the chess board
	 * 
	 * @param fen
	 *            Position string
	 */
	public void drawBoard(String fen) {
		sendCommand("position fen " + fen);
		sendCommand("d");

		String[] rows = getOutput(0).split("\n");

		for (int i = 1; i < 18; i++) {
			System.out.println(rows[i]);
		}
	}

	/**
	 * Get the evaluation score of a given board position
	 * @param fen Position string
	 * @param waitTime in milliseconds
	 * @return evalScore
	 */
	public float getEvalScore(String fen, int waitTime) {
		sendCommand("position fen " + fen);
		sendCommand("go movetime " + waitTime);

		float evalScore = 0.0f;
		String[] dump = getOutput(waitTime + 20).split("\n");
		for (int i = dump.length - 1; i >= 0; i--) {
			if (dump[i].startsWith("info depth ")) {
				try {
				evalScore = Float.parseFloat(dump[i].split("score cp ")[1]
						.split(" nodes")[0]);
				} catch(Exception e) {
					evalScore = Float.parseFloat(dump[i].split("score cp ")[1]
							.split(" upperbound nodes")[0]);
				}
			}
		}
		return evalScore/100;
	}
}
