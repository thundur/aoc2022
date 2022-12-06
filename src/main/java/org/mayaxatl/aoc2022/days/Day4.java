package org.mayaxatl.aoc2022.days;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.mayaxatl.aoc2022.Day;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Component
@Profile("done")
public class Day4 implements Day {

  private static final Logger LOG = LoggerFactory.getLogger(Day4.class);

  private final List<Pair<Range<Integer>, Range<Integer>>> input;

  public Day4(@Value("classpath:day04.txt") Resource ranges) throws IOException {
    input = Files.readAllLines(ranges.getFile().toPath()).stream()
        .map(s -> s.split(","))
        .map(a -> Pair.of(a[0], a[1]))
        .map(p -> Pair.of(range(p.getLeft()), range(p.getRight())))
        .toList();
  }

  @Override
  public void part1() {
    long result = input.stream()
        .filter(p -> p.getLeft().containsRange(p.getRight()) || p.getRight().containsRange(p.getLeft()))
        .count();
    LOG.info("part 1: {}", result);
  }

  @Override
  public void part2() {
    long result = input.stream()
        .filter(p -> p.getLeft().isOverlappedBy(p.getRight()))
        .count();
    LOG.info("part 2: {}", result);
  }

  @Override
  public int getNr() {
    return 4;
  }

  private Range<Integer> range(String s) {
    String[] parts = s.split("-");
    return Range.between(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
  }

}
