package org.mayaxatl.aoc2022.days;

import org.apache.commons.lang3.tuple.Triple;
import org.mayaxatl.aoc2022.Day;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Profile("done")
public class Day5 implements Day {

  private static final Logger LOG = LoggerFactory.getLogger(Day5.class);
  private static final Pattern pattern = Pattern.compile("^move (\\d+) from (\\d+) to (\\d+)$");
  private final List<Triple<Integer, Integer, Integer>> commands;
  private final List<String> input;
  private List<Deque<String>> crates = new ArrayList<>();

  public Day5(@Value("classpath:day05.txt") Resource myCrates) throws IOException {
    input = Files.readAllLines(myCrates.getFile().toPath()).stream().toList();
    commands = input.stream().filter(line -> line.startsWith("move")).map(this::toCommand).toList();
  }

  @Override
  public void part1() {
    loadCrates();
    commands.forEach(c -> {
      for (int i = 0; i < c.getLeft(); i++) {
        String crate = crates.get(c.getMiddle()).pop();
        crates.get(c.getRight()).push(crate);
      }
    });
    LOG.info("part 1: {}", crates.stream().skip(1).map(Deque::pop).collect(Collectors.joining()));
  }

  @Override
  public void part2() {
    loadCrates();
    commands.forEach(c -> {
      Deque<String> cs = new ArrayDeque<>();
      for (int i = 0; i < c.getLeft(); i++) {
        cs.addFirst(crates.get(c.getMiddle()).pop());
      }
      cs.forEach(crate -> crates.get(c.getRight()).push(crate));
    });
    LOG.info("part 2: {}", crates.stream().skip(1).map(Deque::pop).collect(Collectors.joining()));
  }

  @Override
  public int getNr() {
    return 5;
  }

  public Triple<Integer, Integer, Integer> toCommand(String line) {
    Matcher matcher = pattern.matcher(line);
    if (matcher.matches()) {
      MatchResult result = matcher.toMatchResult();
      return Triple.of(Integer.parseInt(result.group(1)),
          Integer.parseInt(result.group(2)),
          Integer.parseInt(result.group(3)));
    }
    throw new IllegalArgumentException();
  }

  private void loadCrates() {
    crates = new ArrayList<>();
    IntStream.range(0, 10).forEach(i -> crates.add(new ArrayDeque<>()));

    input.stream().filter((line -> line.startsWith("[")))
        .forEach(line -> {
          try {
            for (int i = 0; i < 10; i++) {
              char crate = line.charAt((4 * i) + 1);
              if (Character.isUpperCase(crate)) {
                crates.get(i + 1).addLast(String.valueOf(crate));
              }
            }
          } catch (RuntimeException ex) {
            // ignore
          }
        });

  }

}
