package org.mayaxatl.aoc2022.days;

import org.mayaxatl.aoc2022.Day;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("done")
public class Day2 implements Day {

  private static final Logger LOG = LoggerFactory.getLogger(Day2.class);

  private final List<List<String>> input;

  public Day2(@Value("classpath:day02.txt") Resource script) throws IOException {
    input = Files.readAllLines(script.getFile().toPath()).stream()
        .map(x -> Arrays.stream(x.split(" ")).toList()).toList();
  }

  @Override
  public void part1() {
    int score = input.stream().mapToInt(list -> score(list.get(0), list.get(1))).sum();
    LOG.info("part 1: {}", score);
  }

  @Override
  public void part2() {
    int score = input.stream().mapToInt(list -> score2(list.get(0), list.get(1))).sum();
    LOG.info("part 2: {}", score);
  }

  private int score(String opponent, String you) {
    switch (opponent) {
      case "A" -> {
        switch (you) {
          case "X" -> {
            return 1 + 3;
          }
          case "Y" -> {
            return 2 + 6;
          }
          case "Z" -> {
            return 3;
          }
        }
      }
      case "B" -> {
        switch (you) {
          case "X" -> {
            return 1;
          }
          case "Y" -> {
            return 2 + 3;
          }
          case "Z" -> {
            return 3 + 6;
          }
        }

      }
      case "C" -> {
        switch (you) {
          case "X" -> {
            return 1 + 6;
          }
          case "Y" -> {
            return 2;
          }
          case "Z" -> {
            return 3 + 3;
          }
        }
      }
    }
    throw new IllegalStateException();
  }

  private int score2(String opponent, String outcome) {
    switch (opponent) {
      case "A" -> {
        switch (outcome) {
          case "X" -> {
            return score(opponent, "Z");
          }
          case "Y" -> {
            return score(opponent, "X");
          }
          case "Z" -> {
            return score(opponent, "Y");
          }
        }
      }
      case "B" -> {
        switch (outcome) {
          case "X" -> {
            return score(opponent, "X");
          }
          case "Y" -> {
            return score(opponent, "Y");
          }
          case "Z" -> {
            return score(opponent, "Z");
          }
        }
      }
      case "C" -> {
        switch (outcome) {
          case "X" -> {
            return score(opponent, "Y");
          }
          case "Y" -> {
            return score(opponent, "Z");
          }
          case "Z" -> {
            return score(opponent, "X");
          }
        }
      }
    }
    throw new IllegalStateException();
  }

  @Override
  public int getNr() {
    return 2;
  }
}
