package org.mayaxatl.aoc2022.days;

import org.mayaxatl.aoc2022.Day;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Day7 implements Day {

  private static final Logger LOG = LoggerFactory.getLogger(Day7.class);

  private static final Pattern CD = Pattern.compile("^\\$ cd (.+)$");
  private static final Pattern FILE = Pattern.compile("^([0-9]+) (.+)$");
  private static final Pattern DIR = Pattern.compile("^dir (.+)$");

  private static final int CAPACITY = 70_000_000;
  private static final int FREE_SPACE_NEEDED = 30_000_000;

  private final Directory root = new Directory(null);

  public Day7(@Value("classpath:day07.txt") Resource terminal) throws IOException {
    readTerminal(Files.readAllLines(terminal.getFile().toPath()));
  }

  @Override
  public void part1() {
    LOG.info("part 1: {}", root.sizeIfLessThan1K());
  }

  @Override
  public void part2() {
    int spaceToClear = FREE_SPACE_NEEDED - (CAPACITY - root.size());

    Directory candidate = root;

    for (Directory directory : root.offspring()) {
      int wouldFree = directory.size();
      if (wouldFree >= spaceToClear && wouldFree < candidate.size()) {
        candidate = directory;
      }
    }

    LOG.info("part 2: {}", candidate.size());
  }

  @Override
  public int getNr() {
    return 7;
  }

  private void readTerminal(List<String> input) {
    Directory context = root;
    for (String line : input) {
      Matcher cdMatcher = CD.matcher(line);
      Matcher fileMatcher = FILE.matcher(line);
      Matcher dirMatcher = DIR.matcher(line);

      if (cdMatcher.matches()) {
        MatchResult result = cdMatcher.toMatchResult();
        String toDir = result.group(1);
        if (toDir.equals("/")) {
          context = root;
        } else if (toDir.equals("..")) {
          context = context.parent;
        } else {
          context = context.directories.get(toDir);
        }
      } else if (fileMatcher.matches()) {
        MatchResult result = fileMatcher.toMatchResult();
        int size = Integer.parseInt(result.group(1));
        String name = result.group(2);
        context.files.put(name, new File(size));
      } else if (dirMatcher.matches()) {
        MatchResult result = dirMatcher.toMatchResult();
        String name = result.group(1);
        context.directories.put(name, new Directory(context));
      }
    }
  }

  private record File(int size) {
  }

  private static class Directory {
    Directory parent;
    Map<String, File> files = new HashMap<>();
    Map<String, Directory> directories = new HashMap<>();

    Directory(Directory parent) {
      this.parent = parent;
    }

    int size() {
      return files.values().stream().mapToInt(File::size).sum() +
          directories.values().stream().mapToInt(Directory::size).sum();
    }

    int sizeIfLessThan1K() {
      return (size() <= 100_000L ? size() : 0)
          + directories.values().stream().mapToInt(Directory::sizeIfLessThan1K).sum();
    }

    List<Directory> offspring() {
      List<Directory> offspring = new ArrayList<>(directories.values().stream()
          .map(Directory::offspring)
          .flatMap(Collection::stream)
          .toList());
      offspring.addAll(directories.values());
      return offspring;
    }
  }
}