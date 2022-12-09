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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Component
@Profile("done")
public class Day8 implements Day {

  private static final Logger LOG = LoggerFactory.getLogger(Day8.class);

  private final List<List<Tree>> grid;

  public Day8(@Value("classpath:day08.txt") Resource treehouse) throws IOException {
    List<String> input = Files.readAllLines(treehouse.getFile().toPath());
    grid = new ArrayList<>();
    input.forEach(line -> grid.add(line.chars()
        .mapToObj(c -> new Tree((char) c))
        .toList()));

    IntStream.range(0, grid.size()).forEach(x ->
        IntStream.range(0, grid.get(x).size()).forEach(y
            -> grid.get(x).get(y).setCoordinate(new Point(x, y))));
  }

  @Override
  public void part1() {
    long treesVisible = grid.stream().flatMap(List::stream).filter(Tree::isVisible).count();
    LOG.info("part 1: {}", treesVisible);
  }

  @Override
  public void part2() {
    long highestScenicScore = grid.stream().flatMap(List::stream).mapToInt(Tree::scenicScore).max().orElse(-1);
    LOG.info("part 2: {}", highestScenicScore);
  }

  @Override
  public int getNr() {
    return 8;
  }

  private enum Direction {
    NORTH, EAST, SOUTH, WEST
  }

  private record Point(int x, int y) {
  }

  class Tree {
    private final int height;
    private Point coordinate;

    Tree(char height) {
      this.height = Integer.parseInt(String.valueOf(height));
    }

    public boolean isVisible() {
      return Arrays.stream(Direction.values()).anyMatch(this::isVisibleFrom);
    }

    private boolean isVisibleFrom(Direction direction) {
      return getTreesTo(direction).stream().allMatch(tree -> height > tree.height);
    }

    public int scenicScore() {
      return Arrays.stream(Direction.values())
          .mapToInt(this::scenicScoreForDirection)
          .reduce(1, (a, b) -> a * b);
    }

    public int scenicScoreForDirection(Direction direction) {
      List<Tree> trees = getTreesTo(direction);
      int score = 0;
      for (Tree tree : trees) {
        score++;
        if (tree.height >= height) {
          break;
        }
      }
      return score;
    }

    private List<Tree> getTreesTo(Direction direction) {
      return switch (direction) {
        case NORTH ->
            IntStream.range(0, coordinate.x).boxed().sorted(Collections.reverseOrder()).map(i -> grid.get(i).get(coordinate.y)).toList();
        case EAST ->
            IntStream.range(coordinate.y + 1, grid.get(coordinate.x).size()).mapToObj(i -> grid.get(coordinate.x).get(i)).toList();
        case SOUTH ->
            IntStream.range(coordinate.x + 1, grid.size()).mapToObj(i -> grid.get(i).get(coordinate.y)).toList();
        case WEST ->
            IntStream.range(0, coordinate.y).boxed().sorted(Collections.reverseOrder()).map(i -> grid.get(coordinate.x).get(i)).toList();
      };
    }

    @Override
    public String toString() {
      return "%d".formatted(height);
    }

    public void setCoordinate(Point coordinate) {
      this.coordinate = coordinate;
    }
  }
}