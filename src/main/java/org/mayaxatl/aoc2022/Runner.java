package org.mayaxatl.aoc2022;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Runner {
  private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

  private final List<Day> days;

  @Autowired
  public Runner(List<Day> days) {
    this.days = days;
  }

  public void runAll() {
    days.forEach(day -> {
      day.part1();
      day.part2();
    });
  }

  public void run(int dayNr) {
    days.stream().filter(day -> day.getNr() == dayNr) //
        .findFirst().ifPresentOrElse(day -> {
              day.part1();
              day.part2();
            },
            () -> LOG.error("Day {} not found", dayNr)
        );
  }
}
