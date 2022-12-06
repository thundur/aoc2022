package org.mayaxatl.aoc2022.days;

import org.mayaxatl.aoc2022.Day;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

@Component
public class Day6 implements Day {

  private static final Logger LOG = LoggerFactory.getLogger(Day6.class);

  private final String input;

  public Day6(@Value("classpath:day06.txt") Resource communication) throws IOException {
    input = Files.readAllLines(communication.getFile().toPath()).get(0);
  }

  @Override
  public void part1() {
    LOG.info("part 1: {}", findDistinctCharsOfLength(4));
  }

  @Override
  public void part2() {
    LOG.info("part 2: {}", findDistinctCharsOfLength(14));
  }

  @Override
  public int getNr() {
    return 6;
  }

  private int findDistinctCharsOfLength(int length) {
    for (int i = 0; i < input.length(); i++) {
      String code = input.substring(i, i + length);
      if (code.chars().distinct().toArray().length == length) {
        return i + length;
      }
    }
    throw new IllegalStateException();
  }
}
