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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Day11 implements Day {

  private static final Logger LOG = LoggerFactory.getLogger(Day11.class);

  private final String myInput;

  public Day11(@Value("classpath:day11.txt") Resource input) throws IOException {
    myInput = Files.readString(input.getFile().toPath());
  }

  private List<Monkey> initialize(String s) {
    return Arrays.stream(s.split("\n\n"))
        .map(Monkey::parse)
        .toList();
  }

  @Override
  public void part1() {
    List<Monkey> monkeys = initialize(myInput);
    for (var i = 0; i < 20; i++) {
      for (Monkey monkey : monkeys) {
        monkey.inspectItems(monkeys);
      }
    }
    LOG.info("part 1: {}", monkeyBusiness(monkeys));
  }

  @Override
  public void part2() {
    List<Monkey> monkeys = initialize(myInput);
    int manageableLevel = monkeys.stream().mapToInt(m -> m.testDivisibleBy).reduce(1, (a, b) -> a * b);
    for (var round = 1; round <= 10000; round++) {
      for (Monkey monkey : monkeys) {
        monkey.inspectItems(monkeys, manageableLevel);
      }
    }
    LOG.info("part 2: {}", monkeyBusiness(monkeys));
  }

  private long monkeyBusiness(List<Monkey> monkeys) {
    return monkeys.stream()
        .mapToLong(Monkey::getItemsInspected)
        .boxed()
        .sorted(Collections.reverseOrder())
        .limit(2)
        .reduce(1L, (a, b) -> a * b);
  }

  @Override
  public int getNr() {
    return 11;
  }

  private static class Monkey {
    private static final Pattern PATTERN = Pattern.compile("Monkey([0-9]):Startingitems:([0-9,]+)Operation:new=(old[+*](old|[0-9]+))Test:divisibleby([0-9]+)Iftrue:throwtomonkey([0-9])Iffalse:throwtomonkey([0-9])", Pattern.CASE_INSENSITIVE);
    final List<Item> items;
    final Operation operation;
    final int testDivisibleBy;
    final int toMonkeyIfTrue;
    final int toMonkeyIfFalse;
    long itemsInspected = 0;

    private Monkey(List<Item> items, Operation operation, int testDivisibleBy, int toMonkeyIfTrue, int toMonkeyIfFalse) {
      this.items = new ArrayList<>(items);
      this.operation = operation;
      this.testDivisibleBy = testDivisibleBy;
      this.toMonkeyIfTrue = toMonkeyIfTrue;
      this.toMonkeyIfFalse = toMonkeyIfFalse;
    }

    static Monkey parse(String line) {
      line = line.trim().replace("\n", "").replace(" ", "");
      Matcher m = PATTERN.matcher(line);
      if (m.matches()) {
        List<Item> items = Arrays.stream(m.group(2).split(","))
            .mapToInt(Integer::parseInt)
            .mapToObj(Item::of)
            .toList();
        Operation operation = Operation.parse(m.group(3));
        int testDivisibleBy = Integer.parseInt(m.group(5));
        int toMonkeyIfTrue = Integer.parseInt(m.group(6));
        int toMonkeyIfFalse = Integer.parseInt(m.group(7));
        return new Monkey(items, operation, testDivisibleBy, toMonkeyIfTrue, toMonkeyIfFalse);
      }
      throw new IllegalArgumentException(line);
    }

    void inspectItems(List<Monkey> monkeys) {
      inspectItems(monkeys, null);
    }

    void inspectItems(List<Monkey> monkeys, Integer manageableLevel) {
      for (Item item : items) {
        item.apply(operation);
        if (manageableLevel == null) {
          item.loseInterest();
        } else {
          item.manageWorryLevel(manageableLevel);
        }
        if (item.worryLevel % testDivisibleBy == 0) {
          monkeys.get(toMonkeyIfTrue).receiveItem(item);
        } else {
          monkeys.get(toMonkeyIfFalse).receiveItem(item);
        }
        itemsInspected++;
      }
      items.clear();
    }

    void receiveItem(Item item) {
      items.add(item);
    }

    long getItemsInspected() {
      return itemsInspected;
    }
  }

  private static class Item {
    long worryLevel;

    public Item(long worryLevel) {
      this.worryLevel = worryLevel;
    }

    static Item of(long worryLevel) {
      return new Item(worryLevel);
    }

    public String toString() {
      return "%d".formatted(worryLevel);
    }

    public void apply(Operation operation) {
      worryLevel = operation.apply(worryLevel);
    }

    public void loseInterest() {
      worryLevel = worryLevel / 3;
    }

    public void manageWorryLevel(int divider) {
      worryLevel = worryLevel % divider;
    }
  }

  private record Operation(char symbol, Long amount) {

    private static final Pattern PATTERN = Pattern.compile("^old([+*])(old|[0-9]+)$");

    static Operation parse(String s) {
      Matcher m = PATTERN.matcher(s);
      if (m.matches()) {
        return new Operation(m.group(1).charAt(0), parseLong(m.group(2)));
      }
      throw new IllegalArgumentException(s);
    }

    static Long parseLong(String s) {
      if (s.equals("old")) {
        return null;
      }
      return Long.parseLong(s);
    }

    public long apply(long input) {
      long myAmount = Optional.ofNullable(amount).orElse(input);
      return switch (symbol) {
        case '+' -> input + myAmount;
        case '*' -> input * myAmount;
        default -> 0;
      };
    }
  }

}