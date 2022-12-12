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
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Profile("done")
public class Day9 implements Day {

  private static final Logger LOG = LoggerFactory.getLogger(Day9.class);

  private final List<Instruction> instructions;

  public Day9(@Value("classpath:day09.txt") Resource knots) throws IOException {
    instructions = Files.readAllLines(knots.getFile().toPath()).stream().map(Instruction::parse).toList();
  }

  @Override
  public void part1() {
    Point head = new Point(0, 0);
    Point tail = new Point(0, 0);
    List<Point> tailLocations = new ArrayList<>();

    for (Instruction instruction : instructions) {
      for (var i = 0; i < instruction.steps; i++) {
        head = head.move(instruction.direction);
        tail = followHead(head, tail);
        tailLocations.add(tail);
      }
    }

    LOG.info("part 1: {}", tailLocations.stream().distinct().count());
  }

  @Override
  public void part2() {
    Point head = new Point(0, 0);
    List<Point> tails = new ArrayList<>();
    for (var i = 0; i < 9; i++) {
      tails.add(new Point(0, 0));
    }
    List<Point> tailLocations = new ArrayList<>();

    for (Instruction instruction : instructions) {
      for (var i = 0; i < instruction.steps; i++) {
        head = head.move(instruction.direction);
        tails.set(0, followHead(head, tails.get(0)));
        for (var j = 1; j < 9; j++) {
          tails.set(j, followHead(tails.get(j - 1), tails.get(j)));
        }
        tailLocations.add(tails.get(8));
      }
    }

    LOG.info("part 2: {}", tailLocations.stream().distinct().count());
  }

  @Override
  public int getNr() {
    return 9;
  }

  private Point followHead(Point head, Point tail) {
    int dx = head.x - tail.x;
    int dy = head.y - tail.y;

    if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
      return tail;
    }

    if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
      return tail.addXY(sign(dx), sign(dy));
    }

    throw new IllegalStateException();
  }

  private int sign(int i) {
    return Integer.compare(i, 0);
  }

  private enum Direction {
    U, R, D, L;

    int dx() {
      return switch (this) {
        case U, D -> 0;
        case R -> 1;
        case L -> -1;
      };
    }

    int dy() {
      return switch (this) {
        case R, L -> 0;
        case U -> 1;
        case D -> -1;
      };

    }
  }

  private record Instruction(Direction direction, int steps) {
    private static final Pattern PATTERN = Pattern.compile("([URDL]) ([0-9]+)$");

    static Instruction parse(String s) {
      Matcher m = PATTERN.matcher(s);
      if (m.matches()) {
        MatchResult result = m.toMatchResult();
        return new Instruction(Direction.valueOf(result.group(1)), Integer.parseInt(result.group(2)));
      }
      throw new IllegalArgumentException();

    }
  }

  private record Point(int x, int y) {
    Point move(Direction direction) {
      return new Point(x + direction.dx(), y + direction.dy());
    }

    Point addXY(int dx, int dy) {
      return new Point(x + dx, y + dy);
    }
  }
}