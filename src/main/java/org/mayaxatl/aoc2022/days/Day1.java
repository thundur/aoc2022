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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

@Component
@Profile("done")
public class Day1 implements Day {

  private static final Logger LOG = LoggerFactory.getLogger(Day1.class);

  private final List<String> input;

  public Day1(@Value("classpath:day01.txt") Resource rations) throws IOException {
    input = Files.readAllLines(rations.getFile().toPath());
  }

  @Override
  public void part1() {
    OptionalInt max = rationizeInput().stream()
        .map(cals -> cals.stream().mapToInt(Integer::valueOf).sum())
        .mapToInt(Integer::valueOf)
        .max();
    LOG.info("part 1: {}", max.orElseThrow());
  }

  @Override
  public void part2() {
    int sum = rationizeInput().stream()
        .map(cals -> cals.stream().mapToInt(Integer::valueOf).sum())
        .sorted(Collections.reverseOrder())
        .mapToInt(Integer::valueOf)
        .limit(3).sum();
    LOG.info("part 2: {}", sum);
  }

  private List<List<Integer>> rationizeInput() {
    List<List<Integer>> rationsPerElf = new ArrayList<>();
    List<Integer> calories = new ArrayList<>();
    for (String line : input) {
      if (line.isBlank()) {
        rationsPerElf.add(calories);
        calories = new ArrayList<>();
      } else {
        calories.add(Integer.parseInt(line));
      }
    }
    return rationsPerElf;
  }

  @Override
  public int getNr() {
    return 1;
  }
}
