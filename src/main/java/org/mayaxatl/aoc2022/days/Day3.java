package org.mayaxatl.aoc2022.days;

import org.apache.commons.lang3.tuple.Pair;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
@Profile("done")
public class Day3 implements Day {

  private static final Logger LOG = LoggerFactory.getLogger(Day3.class);

  private final List<String> input;

  public Day3(@Value("classpath:day03.txt") Resource rucksacks) throws IOException {
    input = Files.readAllLines(rucksacks.getFile().toPath()).stream().toList();
  }

  public static <T> Stream<List<T>> batches(List<T> source, int length) {
    if (length <= 0) {
      throw new IllegalArgumentException();
    }
    if (source.isEmpty()) {
      return Stream.empty();
    }
    int size = source.size();
    int fullChunks = (size - 1) / length;
    return IntStream.range(0, fullChunks + 1).mapToObj(
        n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
  }

  @Override
  public void part1() {
    int sum = input.stream().map(this::divideEvenly).map(this::overlappingChar).mapToInt(this::priority).sum();
    LOG.info("part 1: {}", sum);
  }

  @Override
  public void part2() {
    int sum = batches(input, 3).map(list -> Triple.of(list.get(0), list.get(1), list.get(2)))
        .map(this::overlappingChar)
        .mapToInt(this::priority).sum();

    LOG.info("part 2: {}", sum);
  }

  @Override
  public int getNr() {
    return 3;
  }

  private Pair<String, String> divideEvenly(String s) {
    return Pair.of(s.substring(0, s.length() / 2), s.substring(s.length() / 2));
  }

  private char overlappingChar(Pair<String, String> input) {
    return (char) input.getLeft().chars()
        .filter(i -> input.getRight().chars().boxed().toList().contains(i))
        .findAny().orElseThrow();
  }

  private char overlappingChar(Triple<String, String, String> input) {
    return overlappingChar(Pair.of(input.getLeft(), overlappingChars(input.getMiddle(), input.getRight())));
  }

  private String overlappingChars(String s1, String s2) {
    return s1.chars()
        .filter(i -> s2.chars().boxed().toList().contains(i))
        .mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining());
  }

  private int priority(char c) {
    if (Character.isUpperCase(c)) {
      return c - 38;
    } else {
      return c - 96;
    }
  }
}
