package org.mayaxatl.aoc2022;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Aoc2022Application implements CommandLineRunner {

  private final Runner runner;

  @Autowired
  public Aoc2022Application(Runner runner) {
    this.runner = runner;
  }

  @Override
  public void run(String... args) {
    if (args.length == 1) {
      runner.run(Integer.parseInt(args[0]));
    } else {
      runner.runAll();
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(Aoc2022Application.class, args);
  }

}
