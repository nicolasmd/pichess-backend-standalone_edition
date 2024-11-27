package com.nmdev.pichess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nmdev.pichess.service.Stockfish;

import jakarta.annotation.PreDestroy;

@SpringBootApplication
public class StartApplication implements CommandLineRunner {
	
	@Autowired
	private Stockfish stockfish;
	
	@Override
    public void run(String...args) throws Exception {
		System.out.println("Start Stockfish");
		stockfish.startEngine();
		stockfish.sendCommand("uci");
    }
	
	@PreDestroy
	private void shutdown() {
		System.out.println("Stopfish");
		stockfish.stopEngine();
	}

}
