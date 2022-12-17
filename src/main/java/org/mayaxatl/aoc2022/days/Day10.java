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
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
@Profile("done")
public class Day10 implements Day {

  private static final Logger LOG = LoggerFactory.getLogger(Day10.class);

  private final List<Instruction> instructions;

  public Day10(@Value("classpath:day10.txt") Resource code) throws IOException {
    instructions = Files.readAllLines(code.getFile().toPath())
        .stream()
        .map(Instruction::parse)
        .flatMap(this::addNoopForAddX)
        .toList();
  }

  @Override
  public void part1() {
    int registerX = 1;
    int sumOfSignalStrengths = 0;
    int cycle = 0;
    for (Instruction instruction : instructions) {
      cycle++;
      if ((cycle + 20) % 40 == 0) {
        sumOfSignalStrengths += cycle * registerX;
      }
      if (instruction.operation == Operation.addx) {
        registerX += instruction.number;
      }
    }
    LOG.info("part 1: {}", sumOfSignalStrengths);
  }

  @Override
  public void part2() {
    int registerX = 1;
    StringBuilder result = new StringBuilder("\n");
    int cycle = 0;
    for (Instruction instruction : instructions) {
      cycle++;
      int pixelPosition = cycle - 1;
      if (List.of(registerX, registerX - 1, registerX + 1).contains(pixelPosition % 40)) {
        result.append('#');
      } else {
        result.append('.');
      }
      if (instruction.operation == Operation.addx) {
        registerX += instruction.number;
      }
      if (cycle % 40 == 0) {
        result.append('\n');
      }
    }
    LOG.info("part 2: {}", result);
  }

  @Override
  public int getNr() {
    return 10;
  }

  private Stream<Instruction> addNoopForAddX(Instruction i) {
    if (i.operation == Operation.noop) {
      return Stream.of(i);
    }
    return Stream.of(Instruction.parse("noop"), i);
  }

  private enum Operation {
    noop, addx
  }

  private record Instruction(Operation operation, int number) {

    private final static Pattern PATTERN = Pattern.compile("^(noop|addx)( (-?[0-9]+))?$");

    static Instruction parse(String s) {
      Matcher m = PATTERN.matcher(s);
      if (m.matches()) {
        MatchResult result = m.toMatchResult();
        if (result.group(1).equals("noop")) {
          return new Instruction(Operation.noop, 0);
        }
        return new Instruction(Operation.addx, Integer.parseInt(result.group(3)));
      }
      throw new IllegalArgumentException(s);
    }
  }

}